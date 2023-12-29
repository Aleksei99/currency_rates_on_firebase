package com.smuraha.currency_rates.service.afterAppStart;

import com.smuraha.currency_rates.firebase.entity.Subscription;
import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.firebase.entity.repository.UserRepository;
import com.smuraha.currency_rates.service.bankApi.scheduler.subscription.SchedulerManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class SubscriptionAutoLauncher {

    private final UserRepository userRepository;
    private final SchedulerManager schedulerManager;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        List<User> users = userRepository.findAll();
        List<Subscription> subscriptions = new ArrayList<>();
        if (!users.isEmpty()) {
            subscriptions = users.stream().flatMap(user -> {
                if (user.getSubscriptions() != null && !user.getSubscriptions().isEmpty()) {
                    return user.getSubscriptions().stream();
                }
                return Stream.empty();
            }).collect(Collectors.toList());
        }
        if (!subscriptions.isEmpty()) {
            subscriptions.forEach(schedulerManager::startSubscriptionJob);
        }
        System.out.println("hello world, I have just started up");
    }
}
