package com.smuraha.currency_rates.service.bankApi.scheduler.subscription;

import com.smuraha.currency_rates.firebase.entity.Subscription;
import com.smuraha.currency_rates.firebase.entity.repository.BankRepository;
import com.smuraha.currency_rates.service.processor.MessageBroker;
import com.smuraha.currency_rates.service.util.TelegramUI;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchedulerManagerImpl implements SchedulerManager {

    private final Scheduler scheduler;

    private final BankRepository bankRepo;
    private final MessageBroker producer;
    private final TelegramUI telegramUI;


    @Override
    @SneakyThrows
    public void startSubscriptionJob(Subscription subscription) {
        if(subscription.isActive()) {
            Map<Object, Object> map = new HashMap<>();
            map.put("subscription", subscription);
            map.put("bankRepo", bankRepo);
            map.put("telegramUI", telegramUI);
            map.put("producer", producer);
            String uniqueJobId = subscription.getBankId() + "" + subscription.getCurrency();
            JobDetail jobDetail = JobBuilder.newJob(JobNotifyUser.class)
                    .usingJobData(new JobDataMap(map))
                    .withIdentity(uniqueJobId).build();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(uniqueJobId)
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(
                            subscription.getTimeNotify().toSqlTimestamp().getHours(),
                            subscription.getTimeNotify().toSqlTimestamp().getMinutes())
                    ).build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        }
    }

    @Override
    public void stopSubscriptionJob(Subscription subscription) throws SchedulerException {
        String uniqueJobId = subscription.getBankId() + "" + subscription.getCurrency();
        scheduler.deleteJob(JobKey.jobKey(uniqueJobId));
    }
}
