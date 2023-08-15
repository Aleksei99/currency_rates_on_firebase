package com.smuraha.currency_rates.service.bankApi;

import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Dabrabit_BankTest {

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private Dabrabit_Bank dabrabit_bank;

    @Test
    public void testGetBankId() {
        Dabrabit_Bank bank = new Dabrabit_Bank(null);
        assertEquals("3", bank.getBankId());
    }

    @Test
    public void testUpdateBankData() throws MalformedURLException, JAXBException {
        dabrabit_bank.updateBankData();
        verify(bankRepository).save(any());
    }
}