package com.smuraha.currency_rates.firebase.entity;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.enums.Currencies;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription{

    private String bankId;

    private Currencies currency;

    private Timestamp timeNotify;
}
