package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.service.enums.Commands;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CommandScriptServiceHelp implements CommandScriptService {
    @Override
    public SendMessage launchScript(Update update) {
        return null;
    }

    @Override
    public Commands getScriptCommand() {
        return Commands.HELP;
    }
}
