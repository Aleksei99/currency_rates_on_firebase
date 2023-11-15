package com.smuraha.currency_rates.service.processor;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ProcessorService {
    void processCustomActionSetSubscriptionNotificationTime(Update update);

    void processCommand(Update update);

    void processCallback(Update update);
}
