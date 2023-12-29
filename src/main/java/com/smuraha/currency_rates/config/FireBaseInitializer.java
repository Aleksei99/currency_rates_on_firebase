package com.smuraha.currency_rates.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class FireBaseInitializer {

    @PostConstruct
    public void init() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        Key secretKey =  new SecretKeySpec("alexalexalexalex".getBytes(), "AES");

        FileEncrypterDecrypter fileEncrypterDecrypter
                = new FileEncrypterDecrypter(secretKey, "AES/CBC/PKCS5Padding");
        InputStream serviceAccount = fileEncrypterDecrypter.decrypt(getClass().getClassLoader().getResource("firebase.enc"));
        log.info("Successfully read from enc! ");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }

}
