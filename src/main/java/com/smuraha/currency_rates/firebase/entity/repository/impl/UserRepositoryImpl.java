package com.smuraha.currency_rates.firebase.entity.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.entity.Currency;
import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.firebase.entity.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@FirebaseCollection(name = "User")
public class UserRepositoryImpl extends FirebaseRepo implements UserRepository {
    @Override
    @Cacheable(value = "user", unless="#result == null")
    public Optional<User> findUserById(Long userId) {
        log.info("Слазил в базу за юзером с id="+userId);
        ApiFuture<DocumentSnapshot> apiFuture = collection.document(Long.toString(userId)).get();
        DocumentSnapshot documentSnapshot = null;
        try {
            documentSnapshot = apiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        User user = null;
        if (documentSnapshot != null) {
            user = documentSnapshot.toObject(User.class);
        }
        if (user != null) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    @CachePut(value = "user",key = "#user.telegramUserId")
    public User save(User user) {
        log.info("Обновил юзера с id="+user.getTelegramUserId());
        collection.document(Long.toString(user.getTelegramUserId())).set(user);
        return user;
    }

    @Override
    @SneakyThrows
    public List<User> findAll() {
        Iterable<DocumentReference> documents = collection.listDocuments();
        List<User> users = new ArrayList<>();
        for (DocumentReference documentReference : documents) {
            users.add(documentReference.get().get().toObject(User.class));
        }
        return users;
    }
}
