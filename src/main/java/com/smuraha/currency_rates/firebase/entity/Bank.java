package com.smuraha.currency_rates.firebase.entity;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bank {

    @NonNull
    private String id;

    private String bankName;

    private List<CurrencyRate> rates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return id.equals(bank.id) && Objects.equals(bankName, bank.bankName) && Objects.equals(rates, bank.rates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bankName, rates);
    }
}
