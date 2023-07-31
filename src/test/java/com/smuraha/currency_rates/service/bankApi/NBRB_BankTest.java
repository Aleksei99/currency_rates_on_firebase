package com.smuraha.currency_rates.service.bankApi;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.repository.BankRepository;
import com.smuraha.currency_rates.service.bankApi.dto.NBRB_Bank_Cur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NBRB_BankTest {

    private static final String BANK_ID = "1";
    private static final String BANK_NAME = "Национальный банк";
    private static final String BANK_UPDATE_URL = "https://api.nbrb.by/exrates/rates?periodicity=0";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private NBRB_Bank bank;

    @Test
    void testGetBankId() {
        String bankId = bank.getBankId();
        assertEquals(BANK_ID, bankId);
    }

    @Test
    void testUpdateBankData() {
        // Mock the response from the external API
        List<NBRB_Bank_Cur> rawDataObjects = Arrays.asList(
                new NBRB_Bank_Cur(440,"2023-07-31T00:00:00","AUD", 1,"Австралийский доллар", BigDecimal.valueOf(2.0132)),
                new NBRB_Bank_Cur(431,"2023-07-31T00:00:00","USD", 1,"Доллар США", BigDecimal.valueOf(3.0258))
        );
        ResponseEntity<List<NBRB_Bank_Cur>> response = new ResponseEntity<>(rawDataObjects, HttpStatus.OK);
        when(restTemplate.exchange(eq(BANK_UPDATE_URL), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        // Call the method to be tested
        bank.updateBankData();

        // Verify that the bankRepository.save() method was called with the correct bank object
        Bank expectedBank = Bank.builder()
                .id(BANK_ID)
                .bankName(BANK_NAME)
                .rates(Arrays.asList(
                        new CurrencyRate("AUD", 1, BigDecimal.valueOf(2.0132), null, null, Timestamp.parseTimestamp("2023-07-31T00:00:00")),
                        new CurrencyRate("USD", 1, BigDecimal.valueOf(3.0258), null, null, Timestamp.parseTimestamp("2023-07-31T00:00:00"))
                ))
                .build();
        verify(bankRepository, times(1)).save(expectedBank);

        // Verify that restTemplate.exchange() was called with the correct parameters
        verify(restTemplate, times(1)).exchange(eq(BANK_UPDATE_URL), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class));
    }
}