package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.firebase.enums.UserState;
import com.smuraha.currency_rates.service.UserService;
import com.smuraha.currency_rates.service.enums.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class CommandScriptServiceCancel implements CommandScriptService {

    private final UserService userService;

    @Override
    public SendMessage launchScript(Update update) {
        User user = userService.findOrSaveUserFromUpdate(update);
        user.setUserState(UserState.BASIC_STATE);
        userService.updateUser(user);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("""
                            Статус успешно сброшен!
                            """);
        return sendMessage;
    }

    @Override
    public SendMessage processCallback(Update update) {
        return null;
    }

    @Override
    public String getCommandId() {
        return "5";
    }

    @Override
    public Commands getScriptCommand() {
        return Commands.CANCEL;
    }
}
