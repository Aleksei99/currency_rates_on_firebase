package com.smuraha.currency_rates.service;

import com.smuraha.currency_rates.firebase.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {
    User findOrSaveUserFromUpdate(Update update);
    void updateUser(User user);
    void removeUnActiveSubscriptionsByUserId(Long userId);
    User findUserById(Long id);
}
