package com.smuraha.currency_rates.firebase.entity;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class Bank {

    ///TODO https://medium.com/@claudiorauso/local-testing-spring-gcp-firestore-57f2ffc49c1e

    @NonNull
    @DocumentId
    private String id;

    private String bankName;

    @ToString.Exclude
    private List<CurrencyRate> rates;
}
