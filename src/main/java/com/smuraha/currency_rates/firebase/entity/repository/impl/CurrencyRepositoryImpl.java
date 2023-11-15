package com.smuraha.currency_rates.firebase.entity.repository.impl;

import com.google.cloud.firestore.DocumentReference;
import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.entity.Currency;
import com.smuraha.currency_rates.firebase.entity.repository.CurrencyRepository;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

@FirebaseCollection(name = "Currency")
public class CurrencyRepositoryImpl extends FirebaseRepo implements CurrencyRepository {
    @Override
    public void save(Currency currency) {
        collection.document(currency.getCur_ID()+"").set(currency);
    }

    @Override
    @SneakyThrows
    public List<Currency> findAll() {
        Iterable<DocumentReference> documents = collection.listDocuments();
        List<Currency> currencies = new ArrayList<>();
        for (DocumentReference documentReference : documents) {
            currencies.add(documentReference.get().get().toObject(Currency.class));
        }
        return currencies;
    }
}
