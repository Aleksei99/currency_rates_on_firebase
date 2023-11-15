package com.smuraha.currency_rates.firebase.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Currency {
    @JsonAlias(value = "Cur_ID")
    private int Cur_ID;
    @JsonAlias(value = "Cur_Abbreviation")
    private String Cur_Abbreviation;
}
