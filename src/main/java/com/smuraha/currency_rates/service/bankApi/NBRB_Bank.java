package com.smuraha.currency_rates.service.bankApi;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.Currency;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import com.smuraha.currency_rates.firebase.entity.repository.CurrencyRepository;
import com.smuraha.currency_rates.service.bankApi.dto.NBRB_Bank_Cur;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NBRB_Bank implements IBank, IChart_NBRB {

    private final static String BANK_ID = "1";
    private final static String BANK_NAME = "Национальный банк";
    private final static String BANK_UPDATE_URL = "https://api.nbrb.by/exrates/rates?periodicity=0";

    private final RestTemplate restTemplate;
    private final BankRepository bankRepository;
    private final CurrencyRepository currencyRepository;

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
        ParameterizedTypeReference<List<NBRB_Bank_Cur>> bankDtoBean =
                new ParameterizedTypeReference<>() {
                };
        List<NBRB_Bank_Cur> rawDataObjects = extractRawDataFromBankApi_JSON(BANK_UPDATE_URL, restTemplate, bankDtoBean);
        Bank bank = convertRawDataToBankWithCurrencies(rawDataObjects);
        bankRepository.save(bank);
    }

    @Override
    public List<NBRB_Bank_Cur> getRateStatByCurrency(String curId) {
        ParameterizedTypeReference<List<NBRB_Bank_Cur>> bankDtoBean =
                new ParameterizedTypeReference<>() {
                };
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusDays(360);
        String getUrl = GET_RATES_FOR_PERIOD + curId + "?startDate="+start+"&endDate="+now;
        return extractRawDataFromBankApi_JSON(getUrl, restTemplate, bankDtoBean);
    }

    @Override
    @Cacheable("currencies")
    public List<Currency> getAllNBRBCurrencies() {
        return currencyRepository.findAll();
    }

    private Bank convertRawDataToBankWithCurrencies(List<NBRB_Bank_Cur> rawDataObjects) {
        Bank.BankBuilder bankBuilder = Bank.builder().id(BANK_ID).bankName(BANK_NAME);
        List<CurrencyRate> currencyRates = rawDataObjects.stream().map(bank -> new CurrencyRate(
                bank.getCur_Abbreviation(),
                bank.getCur_Scale(),
                bank.getCur_OfficialRate(),
                null,
                null,
                Timestamp.parseTimestamp(bank.getDate())
        )).collect(Collectors.toList());
        return bankBuilder.rates(currencyRates).build();
    }

}
