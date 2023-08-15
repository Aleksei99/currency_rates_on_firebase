package com.smuraha.currency_rates.service.bankApi.dto.dabrabit;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;


@ToString
public class Rates {
    private List<Value> value;

    public Rates() {
    }

    public Rates(List<Value> value) {
        this.value = value;
    }

    @XmlElement
    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }
}
