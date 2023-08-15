package com.smuraha.currency_rates.firebase.entity.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.firebase.entity.repository.UserRepository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@FirebaseCollection(name = "User")
public class UserRepositoryImpl extends FirebaseRepo implements UserRepository {
    @Override
    public Optional<User> findUserById(Long userId) {
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
    public User save(User user) {
        collection.document(Long.toString(user.getTelegramUserId())).set(user);
        return user;
    }
}
