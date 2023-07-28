package com.smuraha.currency_rates.firebase.entity;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.enums.Currencies;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {

    private Currencies currency;

    private BigDecimal rateBuy;

    private BigDecimal rateSell;

    private Timestamp lastUpdate;

}
