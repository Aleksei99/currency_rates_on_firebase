package com.smuraha.currency_rates.service.processor;

import com.smuraha.currency_rates.firebase.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ProcessorService {
    void processUserInput(Update update, User user);

    void processCommand(Update update);

    void processCallback(Update update);

    void processCancel(Update update, User user);
}
