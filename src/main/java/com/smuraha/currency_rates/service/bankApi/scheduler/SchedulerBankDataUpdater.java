package com.smuraha.currency_rates.service.bankApi.scheduler;

import com.smuraha.currency_rates.service.bankApi.IBank;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerBankDataUpdater {

    private final List<IBank> banks;

    @Scheduled(fixedRateString = "${updateBankData.fixedRate.in.hours}",timeUnit = TimeUnit.HOURS)
    public void updateBankData() {
        for (IBank bank:banks) {
            new Thread(bank::updateBankData).start();
        }
    }
}
