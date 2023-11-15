package com.smuraha.currency_rates.service;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.firebase.entity.repository.UserRepository;
import com.smuraha.currency_rates.firebase.enums.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepo;

    @Override
    public User findOrSaveUserFromUpdate(Update update) {
        org.telegram.telegrambots.meta.api.objects.User from = update.getMessage().getFrom();
        User user = userRepo.findUserById(from.getId()).orElse(
                User.builder()
                        .firstLoginDate(Timestamp.now())
                        .firstName(from.getFirstName())
                        .lastName(from.getLastName())
                        .telegramUserId(from.getId())
                        .username(from.getUserName())
                        .userState(UserState.BASIC_STATE)
                        .build()
        );
        user.setLastActionDate(Timestamp.now());
        return userRepo.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepo.save(user);
    }

    @Override
    public void removeUnActiveSubscriptionsByUserId(Long userId) {
        User user = userRepo.findUserById(userId).orElseThrow(RuntimeException::new);
        user.removeUnActiveSubscriptions();
        updateUser(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepo.findUserById(id).orElseThrow(RuntimeException::new);
    }
}
