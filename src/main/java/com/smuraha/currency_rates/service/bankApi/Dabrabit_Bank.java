package com.smuraha.currency_rates.service.bankApi;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.entity.Bank;
import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import com.smuraha.currency_rates.service.bankApi.dto.dabrabit.Filial;
import com.smuraha.currency_rates.service.bankApi.dto.dabrabit.Root;
import com.smuraha.currency_rates.service.bankApi.dto.dabrabit.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class Dabrabit_Bank implements IBank {

    private final static String BANK_ID = "3";
    private final static String BANK_NAME = "Банк Дабрабыт";
    private final static String BANK_UPDATE_URL = "https://bankdabrabyt.by/export_courses.php";

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
        Root root = extractRootRawDataFromBankApi_XML();
        if (root == null)
            return;
        Bank bank = convertRawDataToBankWithCurrencies(root);
        if(bank!=null) {
            bankRepository.save(bank);
        }
    }

    private Bank convertRawDataToBankWithCurrencies(Root root) {
        Filial mainFilial = root.getFilials().getFilial().get(0);
        List<Value> ratesValue = mainFilial.getRates().getValue();
        Bank.BankBuilder bankBuilder = Bank.builder().id(BANK_ID).bankName(BANK_NAME);
        List<CurrencyRate> currencyRates = ratesValue.stream()
                .filter(value -> Double.parseDouble(value.getBuy()) > 0 && Double.parseDouble(value.getSale()) > 0)
                .map(value -> new CurrencyRate(
                        value.getIso(),
                        1,
                        null,
                        BigDecimal.valueOf(Double.parseDouble(value.getBuy())),
                        BigDecimal.valueOf(Double.parseDouble(value.getSale())),
                        Timestamp.of(java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay().plusHours(3)))
                )).collect(Collectors.toList());
        return bankBuilder.rates(currencyRates).build();
    }

    private Root extractRootRawDataFromBankApi_XML() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Root) jaxbUnmarshaller.unmarshal(new URL(BANK_UPDATE_URL));
        } catch (Exception e) {
            log.error("Ошибка парсинга данных из апи Дабрабыт банка", e);
        }
        return null;
    }
}
