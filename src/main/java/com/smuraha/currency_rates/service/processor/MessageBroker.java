package com.smuraha.currency_rates.service.processor;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;

public interface MessageBroker {
    <T extends Serializable> void  process(PartialBotApiMethod<T> message);
}
