package com.smuraha.currency_rates.firebase.entity.dto;

import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRatesWithTotalPageSize {

    private int totalPageSize;
    private List<CurrencyRate> rates;

}
