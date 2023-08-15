package com.smuraha.currency_rates.service.processor.commandScripts;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackServiceRates {
    SendMessage getBankCurrenciesWithPager(Update update);
}
