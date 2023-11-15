package com.smuraha.currency_rates.firebase.entity;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.enums.UserState;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long telegramUserId;

    private Timestamp firstLoginDate;

    private String firstName;

    private String lastName;

    private String username;

    private Timestamp lastActionDate;

    private List<Subscription> subscriptions;

    private UserState userState;

    private String waitForApproveSubscriptionBC;

    public void addSubscription(Subscription subscription){
        this.subscriptions.add(subscription);
    }

    public Subscription activateSubscription(String bankId,String currency){
        Subscription subscription = this.subscriptions.stream()
                .filter(sub -> sub.getCurrency().equals(currency) &&
                        sub.getBankId().equals(bankId)).findFirst().orElseThrow(InternalError::new);
        subscription.setActive(true);
        this.userState=UserState.BASIC_STATE;
        this.waitForApproveSubscriptionBC=null;
        return subscription;
    }

    public Subscription deactivateSubscription(String bankId,String currency){
        Subscription subscription = this.subscriptions.stream()
                .filter(sub -> sub.getCurrency().equals(currency) &&
                        sub.getBankId().equals(bankId)).findFirst().orElseThrow(InternalError::new);
        this.subscriptions.remove(subscription);
        return subscription;
    }

    public void removeUnActiveSubscriptions(){
        this.subscriptions=this.subscriptions.stream()
                .filter(Subscription::isActive).collect(Collectors.toList());
        this.userState=UserState.BASIC_STATE;
    }

}
