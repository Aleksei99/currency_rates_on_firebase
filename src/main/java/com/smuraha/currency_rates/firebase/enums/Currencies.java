package com.smuraha.currency_rates.firebase.enums;

import java.util.Arrays;

public enum Currencies {
    USD,
    EUR,
    RUB;

    public static boolean hasCurrency(Currencies name) {
        return Arrays.asList(Currencies.values()).contains(name);
    }
}
