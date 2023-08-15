package com.smuraha.currency_rates.service.util;

import com.smuraha.currency_rates.service.enums.CallBackParams;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TelegramUI {
    SendMessage getMessageWithButtons(List<List<InlineKeyboardButton>> buttons, String text);

    // String getBankFormedRates(Bank bank);

    // List<List<InlineKeyboardButton>> getCurrencyButtons(List<Currencies> currencies,CallBackKeys cbs) throws JsonProcessingException;

    List<InlineKeyboardButton> getCustomPager(CustomCallBack customCallBack, int page, int totalPages);

    void drawChart(Map<LocalDate, List<BigDecimal>> data, String chatId) throws IOException;
}
