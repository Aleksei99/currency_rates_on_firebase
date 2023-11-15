package com.smuraha.currency_rates.firebase.entity.repository;

import com.smuraha.currency_rates.firebase.entity.Currency;

import java.util.List;

public interface CurrencyRepository {
    void save(Currency currency);
    List<Currency> findAll();
}
