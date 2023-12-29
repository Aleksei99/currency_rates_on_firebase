package com.smuraha.currency_rates.config;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Slf4j
class FileEncrypterDecrypter {

    private Key secretKey;
    private Cipher cipher;

    FileEncrypterDecrypter(Key secretKey, String cipher) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(cipher);
    }

    void encrypt(String content, String fileName) throws InvalidKeyException, IOException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();

        try (
                FileOutputStream fileOut = new FileOutputStream(fileName);
                CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)
        ) {
            fileOut.write(iv);
            cipherOut.write(content.getBytes());
        }

    }

    InputStream decrypt(URL url) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        InputStream inputStream = url.openStream();
        log.info("Reading from enc file...");
        byte[] fileIv = new byte[16];
        inputStream.read(fileIv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));
        return new CipherInputStream(inputStream, cipher);
    }
}
