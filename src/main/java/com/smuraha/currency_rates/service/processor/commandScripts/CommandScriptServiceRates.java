package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.dto.CurrencyRatesWithTotalPageSize;
import com.smuraha.currency_rates.service.BankService;
import com.smuraha.currency_rates.service.bankApi.IBank;
import com.smuraha.currency_rates.service.enums.CallBackParams;
import com.smuraha.currency_rates.service.enums.Commands;
import com.smuraha.currency_rates.service.util.CustomCallBack;
import com.smuraha.currency_rates.service.util.JsonMapper;
import com.smuraha.currency_rates.service.util.TelegramUI;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smuraha.currency_rates.service.enums.CallBackParams.B;
import static com.smuraha.currency_rates.service.enums.CallBackParams.P;

@Service
@RequiredArgsConstructor
public class CommandScriptServiceRates implements CommandScriptService {

    private final List<IBank> banks;
    private final JsonMapper jsonMapper;
    private final TelegramUI telegramUI;
    private final BankService bankService;

    private final static int PAGE_SIZE = 5;

    @Override
    public SendMessage launchScript(Update update) {
        String text = "Выберите банк из списка: ";
        Long chatId = update.getMessage().getChatId();

        List<List<InlineKeyboardButton>> banks_KB = new ArrayList<>();

        Map<CallBackParams, String> params = new HashMap<>();
        for (IBank bank : banks) {
            List<InlineKeyboardButton> row_banks_KB = new ArrayList<>();
            InlineKeyboardButton cell_bank_KB = new InlineKeyboardButton();
            cell_bank_KB.setText(bank.getBankName());
            params.put(B, bank.getBankId());
            cell_bank_KB.setCallbackData(jsonMapper.writeCustomCallBackAsString(
                    new CustomCallBack(getCommandId(), 1, params)
            ));
            row_banks_KB.add(cell_bank_KB);
            banks_KB.add(row_banks_KB);
        }
        return telegramUI.getMessageWithButtons(banks_KB, text, chatId);
    }

    @SneakyThrows
    @Override
    public SendMessage processCallback(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        CustomCallBack callBack = jsonMapper.readCustomCallBack(update.getCallbackQuery().getData());
        Map<CallBackParams, String> params = callBack.getPrms();
        switch (callBack.getOrder()) {
            case 1 -> {
                List<List<InlineKeyboardButton>> cur_KB = new ArrayList<>();
                int page = params.containsKey(P) ? Integer.parseInt(params.get(P)) : 0;
                String bankId = params.get(B);
                List<CurrencyRate> currencyRates = bankService.getCurrencyRatesByBankId(bankId);
                CurrencyRatesWithTotalPageSize ratesWithSize = bankService.getCurrencyRatesWithTotalPageSizeByBankId(page, PAGE_SIZE, currencyRates).get();
                int totalPages = ratesWithSize.getTotalPageSize();
                List<CurrencyRate> rates = ratesWithSize.getRates();
                StringBuilder builder = new StringBuilder();
                builder.append(IBank.getBankNameByBankId(banks,bankId)).append("\n");
                for (CurrencyRate rate:rates){
                    builder.append(telegramUI.getFormedRate(rate));
                }
                List<InlineKeyboardButton> pager = telegramUI.getCustomPager(callBack, page, totalPages);
                InlineKeyboardButton back_KB = new InlineKeyboardButton();
                back_KB.setText("Назад");
                back_KB.setCallbackData(jsonMapper.writeCustomCallBackAsString(
                        new CustomCallBack(getCommandId(), 2, params)
                ));
                pager.add(0,back_KB);
                cur_KB.add(pager);
                return telegramUI.getMessageWithButtons(cur_KB, builder.toString(), chatId);
            }
            case 2 -> {
                update.setMessage(update.getCallbackQuery().getMessage());
                return launchScript(update);
            }
        }
        return null;
    }

    @Override
    public Commands getScriptCommand() {
        return Commands.RATES;
    }

    @Override
    public String getCommandId() {
        return "1";
    }
}
