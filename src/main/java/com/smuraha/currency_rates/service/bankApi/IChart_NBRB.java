package com.smuraha.currency_rates.service.bankApi;

import com.smuraha.currency_rates.firebase.entity.Currency;
import com.smuraha.currency_rates.service.bankApi.dto.NBRB_Bank_Cur;

import java.util.List;

public interface IChart_NBRB {
    String GET_RATES_FOR_PERIOD = "https://api.nbrb.by/exrates/rates/dynamics/";
    List<NBRB_Bank_Cur> getRateStatByCurrency(String curId);
    List<Currency> getAllNBRBCurrencies();
}
