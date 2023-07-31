package com.smuraha.currency_rates.config;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomBeansConfig {

    @Bean
    public Firestore firestore(){
        return FirestoreClient.getFirestore();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
