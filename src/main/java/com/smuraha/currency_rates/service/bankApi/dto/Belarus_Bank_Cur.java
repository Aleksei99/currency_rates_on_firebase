package com.smuraha.currency_rates.service.bankApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Belarus_Bank_Cur {
    @JsonProperty("USD_in")
    private String uSD_in;
    @JsonProperty("USD_out")
    private String uSD_out;
    @JsonProperty("EUR_in")
    private String eUR_in;
    @JsonProperty("EUR_out")
    private String eUR_out;
    @JsonProperty("RUB_in")
    private String rUB_in;
    @JsonProperty("RUB_out")
    private String rUB_out;
    @JsonProperty("GBP_in")
    private String gBP_in;
    @JsonProperty("GBP_out")
    private String gBP_out;
    @JsonProperty("CAD_in")
    private String cAD_in;
    @JsonProperty("CAD_out")
    private String cAD_out;
    @JsonProperty("PLN_in")
    private String pLN_in;
    @JsonProperty("PLN_out")
    private String pLN_out;
    @JsonProperty("SEK_in")
    private String sEK_in;
    @JsonProperty("SEK_out")
    private String sEK_out;
    @JsonProperty("CHF_in")
    private String cHF_in;
    @JsonProperty("CHF_out")
    private String cHF_out;
    @JsonProperty("USD_EUR_in")
    private String uSD_EUR_in;
    @JsonProperty("USD_EUR_out")
    private String uSD_EUR_out;
    @JsonProperty("USD_RUB_in")
    private String uSD_RUB_in;
    @JsonProperty("USD_RUB_out")
    private String uSD_RUB_out;
    @JsonProperty("RUB_EUR_in")
    private String rUB_EUR_in;
    @JsonProperty("RUB_EUR_out")
    private String rUB_EUR_out;
    @JsonProperty("JPY_in")
    private String jPY_in;
    @JsonProperty("JPY_out")
    private String jPY_out;
    @JsonProperty("CNY_in")
    private String cNY_in;
    @JsonProperty("CNY_out")
    private String cNY_out;
    @JsonProperty("CNY_EUR_in")
    private String cNY_EUR_in;
    @JsonProperty("CNY_EUR_out")
    private String cNY_EUR_out;
    @JsonProperty("CNY_USD_in")
    private String cNY_USD_in;
    @JsonProperty("CNY_USD_out")
    private String cNY_USD_out;
    @JsonProperty("CNY_RUB_in")
    private String cNY_RUB_in;
    @JsonProperty("CNY_RUB_out")
    private String cNY_RUB_out;
    @JsonProperty("CZK_in")
    private String cZK_in;
    @JsonProperty("CZK_out")
    private String cZK_out;
    @JsonProperty("NOK_in")
    private String nOK_in;
    @JsonProperty("NOK_out")
    private String nOK_out;
    private String filial_id;
    private String sap_id;
    private String info_worktime;
    private String street_type;
    private String street;
    private String filials_text;
    private String home_number;
    private String name;
    private String name_type;
}
