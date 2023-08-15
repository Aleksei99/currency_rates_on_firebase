package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.service.bankApi.IBank;
import com.smuraha.currency_rates.service.enums.CallBackParams;
import com.smuraha.currency_rates.service.enums.Commands;
import com.smuraha.currency_rates.service.util.CustomCallBack;
import com.smuraha.currency_rates.service.util.JsonMapper;
import com.smuraha.currency_rates.service.util.TelegramUI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smuraha.currency_rates.service.enums.CallBackParams.B;

@Service
@RequiredArgsConstructor
public class CommandScriptServiceRates implements CommandScriptService {

    private final List<IBank> banks;
    private final JsonMapper jsonMapper;
    private final TelegramUI telegramUI;

    @Override
    public SendMessage launchScript(Update update) {
        String text = "Выберите банк из списка: ";
        Long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

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
        return telegramUI.getMessageWithButtons(banks_KB, text);
    }

    @Override
    public SendMessage processCallback(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long userTelegramId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        CustomCallBack callBack = jsonMapper.readCustomCallBack(update.getCallbackQuery().getData());
        Map<CallBackParams, String> params = callBack.getPrms();
        switch (callBack.getOrder()) {
            case 1 -> {
                s
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
