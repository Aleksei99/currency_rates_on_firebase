package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.service.enums.Commands;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CommandScriptServiceHelp implements CommandScriptService {
    @Override
    public SendMessage launchScript(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("""
                            👋  Данный бот по вашему запросу предоставит актуальный курс валют
                            ▶  Для того чтобы получить курс  💰  нажмите /rates
                            ▶  Для получения оповещения  ✓✉  о изменении курса той или иной валюты
                            нажмите /subscribe
                            ▶  Для отключения оповещения  ✕✉  нажмите /unsubscribe
                            ▶  Для просмотра статистики  📈  по курсу нажмите /rates_stat
                            """);
        return sendMessage;
    }

    @Override
    public SendMessage processCallback(Update update) {
        return null;
    }

    @Override
    public String getCommandId() {
        return "0";
    }

    @Override
    public Commands getScriptCommand() {
        return Commands.HELP;
    }
}
