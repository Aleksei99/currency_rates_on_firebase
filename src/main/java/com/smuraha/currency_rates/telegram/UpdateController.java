package com.smuraha.currency_rates.telegram;

import com.smuraha.currency_rates.firebase.entity.RawData;
import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.firebase.enums.UserState;
import com.smuraha.currency_rates.service.RawDataService;
import com.smuraha.currency_rates.service.UserService;
import com.smuraha.currency_rates.service.enums.Commands;
import com.smuraha.currency_rates.service.processor.ProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateController {

    private TelegramBot telegramBot;
    private ProcessorService processorService;
    private final MessageGenerator messageGenerator;
    private final RawDataService rawDataService;
    private final UserService userService;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void registerProcessor(ProcessorService processorService) {
        this.processorService = processorService;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }
        if (update.hasMessage()) {
            distributeMessageByType(update);
        } else if (update.hasCallbackQuery()) {
            processorService.processCallback(update);
        } else {
            log.error("Unsupported message type is received: " + update);
        }
    }

    private void distributeMessageByType(Update update) {
        Message message = update.getMessage();
        rawDataService.add(new RawData(update));
        User user = userService.findOrSaveUserFromUpdate(update);
        if (message.hasText()) {
            String text = message.getText();
            if (text.equals(Commands.CANCEL.getCommand())) {
                processorService.processCommand(update);
            } else if (user.getUserState().equals(UserState.WAIT_FOR_TIME_PICK) && user.getWaitForApproveSubscriptionBC()!=null) {
                processorService.processCustomActionSetSubscriptionNotificationTime(update);
            } else if (message.isCommand()) {
                processorService.processCommand(update);
            } else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Выполните предыдущий запрос или для отмены предыдущей операции введите /cancel");
                sendMessage.setChatId(message.getChatId());
                setView(sendMessage);
            }
        } else {
            setUnsupportedMessageType(message);
        }
    }

    private void setUnsupportedMessageType(Message message) {
        if (message.getChat().getType().equals("private")) {
            SendMessage sendMessage = messageGenerator.generateSendMessageWithText(message, "Бот не поддерживает отправку файлов!");
            setView(sendMessage);
        }
    }

    public <T extends Serializable> void processMessage(PartialBotApiMethod<T> message) {
        if (message instanceof SendMessage) {
            setView((SendMessage) message);
        } else if (message instanceof DeleteMessage) {
            delete((DeleteMessage) message);
        } else if (message instanceof EditMessageText) {
            edit((EditMessageText) message);
        }
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendMessage(sendMessage);
    }

    private void delete(DeleteMessage deleteMessage) {
        telegramBot.deleteMessage(deleteMessage);
    }

    private void edit(EditMessageText editMessageText) {
        telegramBot.editMessage(editMessageText);
    }
}
