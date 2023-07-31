package com.smuraha.currency_rates.firebase.entity;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.enums.Currencies;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CurrencyRate {

    private String currency;

    private int scale;

    private BigDecimal rateOfficial;

    private BigDecimal rateBuy;

    private BigDecimal rateSell;

    private Timestamp lastUpdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyRate that = (CurrencyRate) o;
        return scale == that.scale && Objects.equals(currency, that.currency)
                && Objects.equals(rateOfficial, that.rateOfficial)
                && Objects.equals(rateBuy, that.rateBuy)
                && Objects.equals(rateSell, that.rateSell)
                && Objects.equals(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, scale, rateOfficial, rateBuy, rateSell, lastUpdate);
    }
}
