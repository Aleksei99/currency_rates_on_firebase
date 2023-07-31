package com.smuraha.currency_rates.firebase.repository;

import com.smuraha.currency_rates.firebase.entity.RawData;

public interface RawDataRepository {
    void add(RawData rawData);
}
