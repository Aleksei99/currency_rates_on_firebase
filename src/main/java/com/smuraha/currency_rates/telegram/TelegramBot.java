package com.smuraha.currency_rates.telegram;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.config.TelegramConfig;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.enums.Currencies;
import com.smuraha.currency_rates.firebase.repository.BankRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramConfig config;
    private final UpdateController updateController;
    private final BankRepository bankRepository;


    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    public TelegramBot(TelegramConfig config, UpdateController updateController, BankRepository bankRepository) {
        super(config.getToken());
        this.config = config;
        this.updateController = updateController;
        this.bankRepository = bankRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        //Firestore firestore = FirestoreClient.getFirestore();
        //ApiFuture<DocumentReference> future = firestore.collection("RawData").add(update);

        //firestore.collection("Bank").add(test);
        //updateController.processUpdate(update);
    }

    @PostConstruct
    public void test(){
        Bank test = Bank.builder().bankName("Test2")
                .id("3")
                .rates(List.of(
                        CurrencyRate.builder()
                                .lastUpdate(Timestamp.now())
                                .rateBuy(new BigDecimal("2.25"))
                                .rateSell(new BigDecimal("2.35"))
                                .currency(Currencies.USD).build()
                )).build();
        bankRepository.save(test);
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    public void sendMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Error occurred, during sending message", e);
            }
        }
    }

    public void deleteMessage(DeleteMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Error occurred, during deleting message", e);
            }
        }
    }

    public void editMessage(EditMessageText message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Error occurred, during editing message", e);
            }
        }
    }
}
