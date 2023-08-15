package com.smuraha.currency_rates.service.customExceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MyJsonProcessingException extends JsonProcessingException {
    public MyJsonProcessingException(String msg) {
        super(msg);
    }
}
