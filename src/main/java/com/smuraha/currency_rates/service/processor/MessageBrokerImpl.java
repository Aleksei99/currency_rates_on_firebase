package com.smuraha.currency_rates.service.processor;

import com.smuraha.currency_rates.telegram.UpdateController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class MessageBrokerImpl implements MessageBroker {

    private final UpdateController updateController;

    @Override
    public <T extends Serializable> void process(PartialBotApiMethod<T> message) {
        updateController.processMessage(message);
    }
}
