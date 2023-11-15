package com.smuraha.currency_rates.service.processor.custom;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CustomProcessor {
    SendMessage processSetSubscriptionNotificationTime(Update update);
}
