package com.smuraha.currency_rates.service.bankApi;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.repository.BankRepository;
import com.smuraha.currency_rates.service.bankApi.dto.Belarus_Bank_Cur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Belarus_BankTest {

    private final static String BANK_ID="2";
    private final static String BANK_NAME="Беларусбанк";
    private final static String BANK_UPDATE_URL="https://belarusbank.by/api/kursExchange";

    @InjectMocks
    private Belarus_Bank belarusBank;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private BankRepository bankRepository;

    @Test
    public void updateBankData_ShouldSaveBankWithCurrencyRates() {
        // Mock the response from the API
        List<Belarus_Bank_Cur> mockData = createMockData();
        ParameterizedTypeReference<List<Belarus_Bank_Cur>> bankDtoBean = new ParameterizedTypeReference<>() {};

        ResponseEntity<List<Belarus_Bank_Cur>> mockResponseEntity = ResponseEntity.ok(mockData);
        when(restTemplate.exchange(
                eq(BANK_UPDATE_URL),
                eq(HttpMethod.GET),
                eq(null),
                eq(bankDtoBean)
        )).thenReturn(mockResponseEntity);

        belarusBank.updateBankData();

        // Verify that the bank is saved with the correct currency rates
        Bank expectedBank = createExpectedBank();
        verify(bankRepository, times(1)).save(expectedBank);
    }

    private List<Belarus_Bank_Cur> createMockData() {
        return List.of(Belarus_Bank_Cur.builder()
                .uSD_in("1.234")
                .uSD_out("1.345")
                .eUR_in("2.567")
                .eUR_out("2.678")
                .rUB_in("2.8")
                .rUB_out("3.6")
                .pLN_in("7.2")
                .pLN_out("7.65")
                .build());
    }

    private Bank createExpectedBank() {
        // Create the expected Bank object with currency rates
        Bank.BankBuilder bankBuilder = Bank.builder()
                .id(BANK_ID)
                .bankName(BANK_NAME);

        List<CurrencyRate> currencyRates = List.of(
                CurrencyRate.builder()
                        .currency("USD")
                        .lastUpdate(Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay())))
                        .rateBuy(BigDecimal.valueOf(1.234))
                        .rateSell(BigDecimal.valueOf(1.345))
                        .rateOfficial(null)
                        .scale(1)
                        .build(),
                CurrencyRate.builder()
                        .currency("EUR")
                        .lastUpdate(Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay())))
                        .rateBuy(BigDecimal.valueOf(2.567))
                        .rateSell(BigDecimal.valueOf(2.678))
                        .rateOfficial(null)
                        .scale(1)
                        .build(),
                CurrencyRate.builder()
                        .currency("RUB")
                        .lastUpdate(Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay())))
                        .rateBuy(BigDecimal.valueOf(2.8))
                        .rateSell(BigDecimal.valueOf(3.6))
                        .rateOfficial(null)
                        .scale(100)
                        .build(),
                CurrencyRate.builder()
                        .currency("PLN")
                        .lastUpdate(Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay())))
                        .rateBuy(BigDecimal.valueOf(7.2))
                        .rateSell(BigDecimal.valueOf(7.65))
                        .rateOfficial(null)
                        .scale(10)
                        .build()
        );

        return bankBuilder.rates(currencyRates).build();
    }
}