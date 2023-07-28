package com.smuraha.currency_rates.firebase.repository;

import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.entity.Bank;

@FirebaseCollection(name = "Bank")
public class BankRepository extends FirebaseRepo {

    public void save(Bank bank){
        collection.document(bank.getId()).set(bank);
    }
}
