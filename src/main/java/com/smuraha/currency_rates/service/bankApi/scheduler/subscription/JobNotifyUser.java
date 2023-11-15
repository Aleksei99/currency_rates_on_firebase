package com.smuraha.currency_rates.service.bankApi.scheduler.subscription;

import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.Subscription;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import com.smuraha.currency_rates.service.processor.MessageBroker;
import com.smuraha.currency_rates.service.util.TelegramUI;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;

@Service
@NoArgsConstructor
public class JobNotifyUser implements Job {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) {
        Subscription subscription = (Subscription) context.getMergedJobDataMap().get("subscription");
        BankRepository bankRepo = (BankRepository) context.getMergedJobDataMap().get("bankRepo");
        TelegramUI telegramUI = (TelegramUI) context.getMergedJobDataMap().get("telegramUI");
        MessageBroker producer = (MessageBroker) context.getMergedJobDataMap().get("producer");

        String bankId = subscription.getBankId();
        String currency = subscription.getCurrency();
        Bank bank = bankRepo.getBankById(bankId);
        CurrencyRate currencyRate = bank.getRates().stream().filter(rate -> rate.getCurrency().equals(currency)).findFirst()
                .orElse(CurrencyRate.builder()
                        .rateOfficial(BigDecimal.ZERO)
                        .currency("UNDEFINED")
                        .scale(0)
                        .build());
        String bankFormedRatesAnswer = telegramUI.getFormedRate(currencyRate);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setChatId(subscription.getTelegramUserId());
        sendMessage.setText(bankFormedRatesAnswer);
        producer.process(sendMessage);
    }

}
