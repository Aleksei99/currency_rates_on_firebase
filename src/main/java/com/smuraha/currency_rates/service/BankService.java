package com.smuraha.currency_rates.service;

import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.dto.CurrencyRatesWithTotalPageSize;

import java.util.List;
import java.util.Optional;

public interface BankService {

    Optional<CurrencyRatesWithTotalPageSize> getCurrencyRatesWithTotalPageSizeByBankId(int page, int pageSize, List<CurrencyRate> rates) ;

    List<CurrencyRate> getCurrencyRatesByBankId(String bankId);
}
