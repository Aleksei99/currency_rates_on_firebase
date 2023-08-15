package com.smuraha.currency_rates.firebase.entity.repository.impl;


import com.google.api.Page;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@FirebaseCollection(name = "Bank")
public class BankRepositoryImpl extends FirebaseRepo implements BankRepository {

    @Override
    public void save(@NonNull Bank bank){
        collection.document(bank.getId()).set(bank);
    }

    public Optional<CurrencyRate> getCurrencyRatesByBankId(int page, int pageSize, String id) throws ExecutionException, InterruptedException {
        DocumentReference document = collection.document(id);
        ApiFuture<DocumentSnapshot> apiFuture = document.get();
        DocumentSnapshot snapshot = apiFuture.get();
        Bank bank = snapshot.toObject(Bank.class);
        if (bank==null){
            return Optional.empty();
        }
        List<CurrencyRate> rates = bank.getRates();
        rates.subList(page*pageSize)
    }
}
