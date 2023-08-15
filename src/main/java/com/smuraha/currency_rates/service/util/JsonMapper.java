package com.smuraha.currency_rates.service.util;

public interface JsonMapper {
    String writeCustomCallBackAsString(CustomCallBack callBack) ;

    CustomCallBack readCustomCallBack(String callBack);
}
