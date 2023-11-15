package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.service.bankApi.IChart_NBRB;
import com.smuraha.currency_rates.firebase.entity.Currency;
import com.smuraha.currency_rates.service.bankApi.dto.NBRB_Bank_Cur;
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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.smuraha.currency_rates.service.enums.CallBackParams.C;
import static com.smuraha.currency_rates.service.enums.CallBackParams.P;

@Service
@RequiredArgsConstructor
public class CommandScriptServiceRatesStat implements CommandScriptService {

    private final JsonMapper jsonMapper;
    private final TelegramUI telegramUI;
    private final IChart_NBRB iChart_nbrb;

    private final static int PAGE_SIZE = 5;

    @Override
    public SendMessage launchScript(Update update) {
        return getCurrencyListByPage(update.getMessage().getChatId(), 0);
    }

    private SendMessage getCurrencyListByPage(Long chatId, int pageNum) {
        String text = "Выберите валюту из списка: ";

        List<List<InlineKeyboardButton>> currencies_KB = new ArrayList<>();

        Map<CallBackParams, String> params = new HashMap<>();
        List<Currency> allNBRBCurrencies = iChart_nbrb.getAllNBRBCurrencies();
        int size = allNBRBCurrencies.size();
        for (Currency currency : allNBRBCurrencies.subList(pageNum * PAGE_SIZE, Math.min((pageNum * PAGE_SIZE + PAGE_SIZE), size))) {
            List<InlineKeyboardButton> row_currencies_KB = new ArrayList<>();
            InlineKeyboardButton cell_currency_KB = new InlineKeyboardButton();
            cell_currency_KB.setText(currency.getCur_Abbreviation());
            params.put(C, currency.getCur_ID() + "");
            cell_currency_KB.setCallbackData(jsonMapper.writeCustomCallBackAsString(
                    new CustomCallBack(getCommandId(), 1, params)
            ));
            row_currencies_KB.add(cell_currency_KB);
            currencies_KB.add(row_currencies_KB);
        }
        int totalPageSize = size % PAGE_SIZE == 0 ? size / PAGE_SIZE : (size / PAGE_SIZE + 1);
        List<InlineKeyboardButton> pager = telegramUI.getCustomPager(new CustomCallBack(getCommandId(), 0, new HashMap<>()), pageNum, totalPageSize);

        currencies_KB.add(pager);
        return telegramUI.getMessageWithButtons(currencies_KB, text, chatId);
    }

    @SneakyThrows
    @Override
    public SendMessage processCallback(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        CustomCallBack callBack = jsonMapper.readCustomCallBack(update.getCallbackQuery().getData());
        Map<CallBackParams, String> params = callBack.getPrms();
        switch (callBack.getOrder()) {
            case 0 -> {
                return getCurrencyListByPage(chatId, Integer.parseInt(params.get(P)));
            }
            case 1 -> {
                String cur_id = params.getOrDefault(C, "431");
                List<NBRB_Bank_Cur> rateStatByCurrency = iChart_nbrb.getRateStatByCurrency(cur_id);
                Map<LocalDate, BigDecimal> map = new TreeMap<>(LocalDate::compareTo);
                rateStatByCurrency.forEach(cur -> map.put(LocalDate.parse(cur.getDate().substring(0,10)),cur.getCur_OfficialRate()));
                telegramUI.drawChart(map,chatId+"");
            }
        }
        return null;
    }

    @Override
    public String getCommandId() {
        return "3";
    }

    @Override
    public Commands getScriptCommand() {
        return Commands.RATES_STAT;
    }
}
