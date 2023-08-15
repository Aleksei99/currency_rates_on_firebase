package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.service.enums.CallBackParams;
import com.smuraha.currency_rates.service.util.CustomCallBack;
import com.smuraha.currency_rates.service.util.JsonMapper;
import com.smuraha.currency_rates.service.util.TelegramUI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smuraha.currency_rates.service.enums.CallBackParams.P;

@Service
@RequiredArgsConstructor
public class CallbackServiceRatesImpl implements CallbackServiceRates {

    private final TelegramUI telegramUI;
    private final JsonMapper jsonMapper;

    @Override
    public SendMessage getBankCurrenciesWithPager(Update update) {

        CustomCallBack callBack = jsonMapper.readCustomCallBack(update.getCallbackQuery().getData());
        Map<CallBackParams, String> params = callBack.getPrms();

        List<List<InlineKeyboardButton>> banks_KB = new ArrayList<>();
        int page = params.containsKey(P) ? Integer.parseInt(params.get(P)) : 0;
        Page<Bank> bankPages = bankRepo.getBanksByCur(currency, PageRequest.of(page, 5));
        int totalPages = bankPages.getTotalPages();

        List<Bank> allBanks = bankPages.toList();

        StringBuilder builder = new StringBuilder();
        for (Bank bank : allBanks) {
            builder.append(telegramUI.getBankFormedRates(bank));
        }
        List<InlineKeyboardButton> pager = telegramUI.getCustomPager(CAB, params, page, totalPages);
        banks_KB.add(pager);
        return telegramUI.getMessageWithButtons(banks_KB, builder.toString());
    }
}
