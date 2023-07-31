package com.smuraha.currency_rates.firebase.repository.impl;


import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.repository.BankRepository;
import lombok.NonNull;


@FirebaseCollection(name = "Bank")
public class BankRepositoryImpl extends FirebaseRepo implements BankRepository {

    @Override
    public void save(@NonNull Bank bank){
        collection.document(bank.getId()).set(bank);
    }
}
