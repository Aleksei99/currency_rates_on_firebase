package com.smuraha.currency_rates.service.bankApi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface IBank {
    String getBankId();
    String getBankName();
    void updateBankData();

    default <T> List<T> extractRawDataFromBankApi_JSON(String url, RestTemplate restTemplate, ParameterizedTypeReference<List<T>> bankDtoBean){
        ResponseEntity<List<T>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                bankDtoBean);
        return response.getBody();
    }
}
