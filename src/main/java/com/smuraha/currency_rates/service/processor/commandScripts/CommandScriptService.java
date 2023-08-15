package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.service.enums.Commands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandScriptService {
    SendMessage launchScript(Update update);
    SendMessage processCallback(Update update);
    String getCommandId();
    Commands getScriptCommand();
}
