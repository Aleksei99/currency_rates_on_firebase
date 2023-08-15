package com.smuraha.currency_rates.service.bankApi.dto.dabrabit;

import lombok.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


@ToString
public class Value {
    private String iso;
    private String code;
    private String buy;
    private String sale;

    @XmlAttribute
    public String getIso() {
        return iso;
    }
    @XmlAttribute
    public String getCode() {
        return code;
    }
    @XmlAttribute
    public String getBuy() {
        return buy;
    }
    @XmlAttribute
    public String getSale() {
        return sale;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public Value() {
    }

    public Value(String iso, String code, String buy, String sale) {
        this.iso = iso;
        this.code = code;
        this.buy = buy;
        this.sale = sale;
    }
}
