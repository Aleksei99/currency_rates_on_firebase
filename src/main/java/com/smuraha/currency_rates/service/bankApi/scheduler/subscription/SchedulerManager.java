package com.smuraha.currency_rates.service.bankApi.scheduler.subscription;

import com.smuraha.currency_rates.firebase.entity.Subscription;
import org.quartz.SchedulerException;

public interface SchedulerManager {

    void startSubscriptionJob(Subscription subscription);

    void stopSubscriptionJob(Subscription subscription) throws SchedulerException;

}
