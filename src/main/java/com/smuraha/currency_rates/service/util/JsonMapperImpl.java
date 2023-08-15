package com.smuraha.currency_rates.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smuraha.currency_rates.service.customExceptions.MyJsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JsonMapperImpl implements JsonMapper {
    @Override
    public String writeCustomCallBackAsString(CustomCallBack callBack) {
        try {
            String value = new ObjectMapper().writeValueAsString(callBack);
            if (value.length() > 64)
                throw new MyJsonProcessingException("Длина callBack-a не может быть больше 64 байтов");
            return value;
        } catch (Exception e) {
            log.error("Ошибка преобразования ", e);
            return "IGNORE";
        }
    }

    @Override
    public CustomCallBack readCustomCallBack(String callBack) {
        try {
            return new ObjectMapper().readValue(callBack, CustomCallBack.class);
        } catch (JsonProcessingException e) {
            log.error("Ошибка чтения ", e);
            return null;
        }
    }
}
