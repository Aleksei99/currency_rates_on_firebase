package com.smuraha.currency_rates.service.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.service.enums.Commands;
import com.smuraha.currency_rates.service.processor.commandScripts.CommandScriptService;
import com.smuraha.currency_rates.service.processor.commandScripts.CommandScriptServiceHelp;
import com.smuraha.currency_rates.service.util.CustomCallBack;
import com.smuraha.currency_rates.service.util.JsonMapper;
import com.smuraha.currency_rates.telegram.UpdateController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.smuraha.currency_rates.service.enums.Commands.RATES;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorServiceImpl implements ProcessorService {

    private final UpdateController updateController;
    private final List<CommandScriptService> commandScripts;
    private final JsonMapper jsonMapper;

    @Override
    public void processUserInput(Update update, User user) {

    }

    @Override
    public void processCommand(Update update) {
        Message message = update.getMessage();
        String userCommand = message.getText();
        Long chatId = message.getChatId();
        try {
            Commands command = Commands.getCommand(userCommand);
            switch (command) {
                case RATES -> {
                    SendMessage sendMessage = getCommandScriptServiceByCommand(RATES).launchScript(update);
                    updateController.processMessage(sendMessage);
                }
                case HELP, START -> {
                    sendTextAnswer("""
                            👋  Данный бот по вашему запросу предоставит актуальный курс валют
                            ▶  Для того чтобы получить курс  💰  нажмите /rates
                            ▶  Для получения оповещения  ✓✉  о изменении курса той или иной валюты
                            нажмите /subscribe
                            ▶  Для отключения оповещения  ✕✉  нажмите /unsubscribe
                            ▶  Для просмотра статистики  📈  по курсу нажмите /rates_stat
                            """, chatId);
                }
                case RATES_STAT -> {
                    String text = "Выберите валюту: ";
                    List<List<InlineKeyboardButton>> ratesStatCurrencyButtons;
                    try {
                        ratesStatCurrencyButtons = currencyButtons.stream()
                                .filter(o -> o instanceof CurrencyButtonsChart)
                                .findAny().get()
                                .getCurrencyButtons(update);
                    } catch (JsonProcessingException e) {
                        sendTextAnswer("Ошибка при выборе валюты!", chatId);
                        log.error("Ошибка при выборе валюты!", e);
                        return;
                    }
                    SendMessage sendMessage = telegramUI.getMessageWithButtons(ratesStatCurrencyButtons, text);
                    sendMessage.setChatId(chatId);
                    answerProducer.produce(sendMessage);
                }
                case SUBSCRIBE -> {
                    if (message.getChat().getType().equals("private")) {
                        String text = "Выберите валюту на которую хотите подписаться: ";
                        List<List<InlineKeyboardButton>> subscribeCurrencyButtons;
                        try {
                            subscribeCurrencyButtons = currencyButtons.stream()
                                    .filter(o -> o instanceof CurrencyButtonsSubscribe)
                                    .findAny().get()
                                    .getCurrencyButtons(update);
                        } catch (JsonProcessingException e) {
                            sendTextAnswer("Ошибка при выборе валюты!", chatId);
                            log.error("Ошибка при выборе валюты!", e);
                            return;
                        }
                        if (subscribeCurrencyButtons == null) {
                            sendTextAnswer("Вы уже подписаны на все валюты", chatId);
                            return;
                        }
                        SendMessage sendMessage = telegramUI.getMessageWithButtons(subscribeCurrencyButtons, text);
                        sendMessage.setChatId(chatId);
                        answerProducer.produce(sendMessage);
                    } else {
                        sendTextAnswer("Доступно только в приватном чате с ботом!", chatId);
                    }
                }
                case UNSUBSCRIBE -> {
                    if (message.getChat().getType().equals("private")) {
                        String text = "Выберите валюту от которой хотите отписаться: ";
                        List<List<InlineKeyboardButton>> unsubscribeCurrencyButtons;
                        try {
                            unsubscribeCurrencyButtons = currencyButtons.stream()
                                    .filter(o -> o instanceof CurrencyButtonsUnsubscribe)
                                    .findAny().get()
                                    .getCurrencyButtons(update);
                        } catch (JsonProcessingException e) {
                            sendTextAnswer("Ошибка при выборе валюты!", chatId);
                            log.error("Ошибка при выборе валюты!", e);
                            return;
                        }
                        if (unsubscribeCurrencyButtons == null) {
                            sendTextAnswer("Вы ещё не подписаны ни на одну из валют", chatId);
                            return;
                        }
                        SendMessage sendMessage = telegramUI.getMessageWithButtons(unsubscribeCurrencyButtons, text);
                        sendMessage.setChatId(chatId);
                        answerProducer.produce(sendMessage);
                    } else {
                        sendTextAnswer("Доступно только в приватном чате с ботом!", chatId);
                    }
                }
            }
        } catch (UnsupportedOperationException e) {
            log.error("Пользователь ввел не существующую команду");
            sendTextAnswer(e.getMessage(), chatId);
        }

    }

    private CommandScriptService getCommandScriptServiceByCommand(Commands command) {
        return commandScripts.stream()
                .filter(script -> script.getScriptCommand().equals(command))
                .findFirst()
                .orElse(new CommandScriptServiceHelp());
    }

    private CommandScriptService getCommandScriptServiceById(String id) {
        return commandScripts.stream()
                .filter(script -> script.getCommandId().equals(id))
                .findFirst()
                .orElse(new CommandScriptServiceHelp());
    }


    @Override
    public void processCallback(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        String queryData = callbackQuery.getData();
        if (!queryData.equals("IGNORE")) {
            CustomCallBack customCallBack = jsonMapper.readCustomCallBack(queryData);
            SendMessage sendMessage = getCommandScriptServiceById(customCallBack.getId()).processCallback(update);
            if (sendMessage == null) {
                return;
            }
            InlineKeyboardMarkup replyMarkup = (InlineKeyboardMarkup) sendMessage.getReplyMarkup();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
            editMessageText.setText(sendMessage.getText());
            editMessageText.setParseMode(ParseMode.HTML);
            editMessageText.setReplyMarkup(replyMarkup);
            updateController.processMessage(editMessageText);
        }
    }

    @Override
    public void processCancel(Update update, User user) {

    }

    private void sendTextAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        updateController.processMessage(sendMessage);
    }
}
