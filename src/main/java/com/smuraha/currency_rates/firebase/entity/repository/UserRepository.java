package com.smuraha.currency_rates.firebase.entity.repository;

import com.smuraha.currency_rates.firebase.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserRepository {
    Optional<User> findUserById(Long userId);
    User save(User user);
    List<User> findAll();
}
