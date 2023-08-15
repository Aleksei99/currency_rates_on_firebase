package com.smuraha.currency_rates.service.bankApi;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import com.smuraha.currency_rates.service.bankApi.dto.NBRB_Bank_Cur;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NBRB_Bank implements IBank{

    private final static String BANK_ID="1";
    private final static String BANK_NAME="Национальный банк";
    private final static String BANK_UPDATE_URL="https://api.nbrb.by/exrates/rates?periodicity=0";

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
        ParameterizedTypeReference<List<NBRB_Bank_Cur>> bankDtoBean =
                new ParameterizedTypeReference<>() {};
        List<NBRB_Bank_Cur> rawDataObjects = extractRawDataFromBankApi_JSON(BANK_UPDATE_URL,restTemplate,bankDtoBean);
        Bank bank = convertRawDataToBankWithCurrencies(rawDataObjects);
        bankRepository.save(bank);
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
