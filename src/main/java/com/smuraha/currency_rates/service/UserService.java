package com.smuraha.currency_rates.service;

import com.smuraha.currency_rates.firebase.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {
    public User findOrSaveUser(Update update);
}
