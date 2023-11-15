package com.smuraha.currency_rates.firebase.entity.repository.impl;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.concurrent.ExecutionException;

@FirebaseCollection(name = "Bank")
@Slf4j
public class BankRepositoryImpl extends FirebaseRepo implements BankRepository {

    @Override
    @CachePut(value = "bank",key = "#bank.id")
    public void save(@NonNull Bank bank){
        collection.document(bank.getId()).set(bank);
    }

    @Override
    @Cacheable(value = "bank",key = "#bankId")
    public Bank getBankById(String bankId) throws InterruptedException, ExecutionException {
        log.info("Слазил в репозиторий за банком");
        DocumentReference document = collection.document(bankId);
        ApiFuture<DocumentSnapshot> apiFuture = document.get();
        DocumentSnapshot snapshot = apiFuture.get();
        return snapshot.toObject(Bank.class);
    }
}
