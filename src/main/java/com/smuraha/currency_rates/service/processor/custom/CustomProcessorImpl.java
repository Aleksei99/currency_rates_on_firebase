package com.smuraha.currency_rates.service.processor.custom;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.entity.Subscription;
import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.firebase.entity.repository.UserRepository;
import com.smuraha.currency_rates.service.bankApi.IBank;
import com.smuraha.currency_rates.service.bankApi.scheduler.subscription.SchedulerManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomProcessorImpl implements CustomProcessor {

    private final SchedulerManager schedulerManager;
    private final UserRepository userRepository;
    private final List<IBank> banks;

    @Override
    public SendMessage processSetSubscriptionNotificationTime(Update update) {
        Long id = update.getMessage().getFrom().getId();
        User user = userRepository.findUserById(id).orElseThrow(RuntimeException::new);
        String subscriptionBC = user.getWaitForApproveSubscriptionBC();
        String answer = "";
        String text = update.getMessage().getText();
        try {
            String[] split = text.split(" ");
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            if (hour < 0 || hour > 24 || minute < 0 || minute > 59) {
                throw new NumberFormatException("must be hour>=0 || hour<=24 and minute >=0 and <=59");
            }
            String bankId = subscriptionBC.substring(0, 1);
            String currency = subscriptionBC.substring(1);
            Subscription subscription = user.activateSubscription(bankId, currency);
            subscription.setTimeNotify(Timestamp.of(java.sql.Timestamp.valueOf(LocalDateTime.of(2023, 12, 12, hour, minute))));
            userRepository.save(user);
            schedulerManager.startSubscriptionJob(subscription);
            answer = String.format("""
                        Ваша подписка на валюту %s банка %s
                        успешно оформлена.
                        Установлено время оповещения на %s
                        """, currency, IBank.getBankNameByBankId(banks, bankId), hour + " " + minute);
        } catch (NumberFormatException e) {
            answer = """
                    Неверный формат ввода!
                    Введите время оповещения в формате h m, например: 9 10
                    """;
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(answer);
        return sendMessage;
    }
}
