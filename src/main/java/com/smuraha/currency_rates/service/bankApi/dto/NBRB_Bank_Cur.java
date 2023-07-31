package com.smuraha.currency_rates.service.bankApi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonKey;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NBRB_Bank_Cur {
    @JsonAlias(value = "Cur_ID")
    private int Cur_ID;
    @JsonAlias(value = "Date")
    private String Date;
    @JsonAlias(value = "Cur_Abbreviation")
    private String Cur_Abbreviation;
    @JsonAlias(value = "Cur_Scale")
    private int Cur_Scale;
    @JsonAlias(value = "Cur_Name")
    private String Cur_Name;
    @JsonAlias(value = "Cur_OfficialRate")
    private BigDecimal Cur_OfficialRate;
}
