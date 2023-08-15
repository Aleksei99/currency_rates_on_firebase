package com.smuraha.currency_rates.service.bankApi.dto.dabrabit;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;


@ToString
public class Filials {
    private List<Filial> filial;

    public Filials(List<Filial> filial) {
        this.filial = filial;
    }

    @XmlElement
    public List<Filial> getFilial() {
        return filial;
    }

    public void setFilial(List<Filial> filial) {
        this.filial = filial;
    }

    public Filials() {
    }
}
