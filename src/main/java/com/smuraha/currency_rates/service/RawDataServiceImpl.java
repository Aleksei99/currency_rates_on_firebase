package com.smuraha.currency_rates.service;

import com.smuraha.currency_rates.firebase.entity.RawData;
import com.smuraha.currency_rates.firebase.entity.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RawDataServiceImpl implements RawDataService{

    private final RawDataRepository rawDataRepository;

    @Override
    public void add(RawData rawData) {
        rawDataRepository.add(rawData);
    }
}
