package com.smuraha.currency_rates.firebase.entity.repository;

import com.smuraha.currency_rates.firebase.entity.Bank;

import java.util.concurrent.ExecutionException;

public interface BankRepository {
    void save(Bank bank);
    Bank getBankById(String bankId) throws InterruptedException, ExecutionException;
}
