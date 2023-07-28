package com.smuraha.currency_rates.firebase.repository;

import com.smuraha.currency_rates.firebase.BPP.FirebaseCollection;
import com.smuraha.currency_rates.firebase.BPP.FirebaseRepo;
import com.smuraha.currency_rates.firebase.entity.RawData;

@FirebaseCollection(name = "RawData")
public class RawDataRepository extends FirebaseRepo {

    public void add(RawData rawData){
        collection.add(rawData);
    }
}
