package com.smuraha.currency_rates.service.bankApi;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import com.smuraha.currency_rates.service.bankApi.dto.Belarus_Bank_Cur;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Belarus_Bank implements IBank{

    private final static String BANK_ID="2";
    private final static String BANK_NAME="Беларусбанк";
    private final static String BANK_UPDATE_URL="https://belarusbank.by/api/kursExchange";

    private final RestTemplate restTemplate;
    private final BankRepository bankRepository;

    @Override
    public String getBankId() {
        return BANK_ID;
    }

    @Override
    public String getBankName() {
        return BANK_NAME;
    }

    @Override
    public void updateBankData() {
        ParameterizedTypeReference<List<Belarus_Bank_Cur>> bankDtoBean =
                new ParameterizedTypeReference<>() {};
        List<Belarus_Bank_Cur> rawDataObjects = extractRawDataFromBankApi_JSON(BANK_UPDATE_URL,restTemplate,bankDtoBean);
        Bank bank = convertRawDataToBankWithCurrencies(rawDataObjects.get(0));
        if(bank!=null) {
            bankRepository.save(bank);
        }
    }

    private Bank convertRawDataToBankWithCurrencies(Belarus_Bank_Cur bank) {
        Bank.BankBuilder bankBuilder = Bank.builder().id(BANK_ID).bankName(BANK_NAME);
        List<CurrencyRate> currencyRates = List.of(
                CurrencyRate.builder()
                        .currency("USD")
                        .lastUpdate(Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay().plusHours(3))))
                        .rateBuy(BigDecimal.valueOf(Double.parseDouble(bank.getUSD_in())))
                        .rateSell(BigDecimal.valueOf(Double.parseDouble(bank.getUSD_out())))
                        .rateOfficial(null)
                        .scale(1)
                        .build(),
                CurrencyRate.builder()
                        .currency("EUR")
                        .lastUpdate(Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay().plusHours(3))))
                        .rateBuy(BigDecimal.valueOf(Double.parseDouble(bank.getEUR_in())))
                        .rateSell(BigDecimal.valueOf(Double.parseDouble(bank.getEUR_out())))
                        .rateOfficial(null)
                        .scale(1)
                        .build(),
                CurrencyRate.builder()
                        .currency("RUB")
                        .lastUpdate(Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay().plusHours(3))))
                        .rateBuy(BigDecimal.valueOf(Double.parseDouble(bank.getRUB_in())))
                        .rateSell(BigDecimal.valueOf(Double.parseDouble(bank.getRUB_out())))
                        .rateOfficial(null)
                        .scale(100)
                        .build(),
                CurrencyRate.builder()
                        .currency("PLN")
                        .lastUpdate(Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay().plusHours(3))))
                        .rateBuy(BigDecimal.valueOf(Double.parseDouble(bank.getPLN_in())))
                        .rateSell(BigDecimal.valueOf(Double.parseDouble(bank.getPLN_out())))
                        .rateOfficial(null)
                        .scale(10)
                        .build()
        );
        return bankBuilder.rates(currencyRates).build();
    }
}
