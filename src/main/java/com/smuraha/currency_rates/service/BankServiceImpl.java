package com.smuraha.currency_rates.service;

import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.dto.CurrencyRatesWithTotalPageSize;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    public Optional<CurrencyRatesWithTotalPageSize> getCurrencyRatesWithTotalPageSizeByBankId(int page, int pageSize, List<CurrencyRate> rates) {
        if (rates.isEmpty()){
            return Optional.empty();
        }
        int size = rates.size();
        int totalPageSize = size%pageSize==0?size/pageSize:(size/pageSize+1);
        List<CurrencyRate> currencyRates = rates.subList(page * pageSize, Math.min(page * pageSize + pageSize, size));
        return Optional.of(
                new CurrencyRatesWithTotalPageSize(
                        totalPageSize,
                        currencyRates
                )
        );
    }

    @SneakyThrows
    @Override
    public List<CurrencyRate> getCurrencyRatesByBankId(String bankId) {
        return bankRepository.getBankById(bankId).getRates();
    }

}
