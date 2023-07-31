package com.smuraha.currency_rates.firebase.repository.impl;

import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.entity.RawData;
import com.smuraha.currency_rates.firebase.repository.RawDataRepository;

@FirebaseCollection(name = "RawData")
public class RawDataRepositoryImpl extends FirebaseRepo implements RawDataRepository {

    @Override
    public void add(RawData rawData){
        collection.add(rawData);
    }
}
