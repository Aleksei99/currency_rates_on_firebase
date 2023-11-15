package com.smuraha.currency_rates;

import com.smuraha.currency_rates.firebase.entity.Currency;
import com.smuraha.currency_rates.firebase.entity.repository.impl.CurrencyRepositoryImpl;
import com.smuraha.currency_rates.service.bankApi.IBank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CurrencyRatesApplicationTests {

    @Qualifier("NBRB_Bank")
    @Autowired
    private IBank iBank;

    @Autowired
    private CurrencyRepositoryImpl currencyRepository;

    @Test
    void contextLoads() {
        iBank.updateBankData();
    }

    @Test
    void testSave(){
        List<Currency> all = currencyRepository.findAll();
        System.out.println(all);
    }

}
