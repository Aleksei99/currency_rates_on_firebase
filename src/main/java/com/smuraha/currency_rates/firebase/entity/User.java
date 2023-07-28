package com.smuraha.currency_rates.firebase.entity;

import com.google.cloud.Timestamp;
import com.smuraha.currency_rates.firebase.enums.UserState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long telegramUserId;

    private Timestamp firstLoginDate;

    private String firstName;

    private String lastName;

    private String username;

    private Timestamp lastActionDate;

    private List<Subscription> subscriptions;

    private UserState userState;

    public void addSubscription(Subscription subscription){
        this.subscriptions.add(subscription);
    }

}
