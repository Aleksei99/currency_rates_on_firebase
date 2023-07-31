package com.smuraha.currency_rates.firebase.repository;

import com.smuraha.currency_rates.firebase.entity.Bank;

public interface BankRepository {
    void save(Bank bank);
}
