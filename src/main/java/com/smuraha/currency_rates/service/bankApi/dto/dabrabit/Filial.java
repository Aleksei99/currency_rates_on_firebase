package com.smuraha.currency_rates.service.bankApi.dto.dabrabit;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;

@ToString
public class Filial {
    private String city;
    private String address;
    private int id;
    private Rates rates;
    private String name;
    private String text;

    @XmlElement
    public String getCity() {
        return city;
    }

    @XmlElement
    public String getAddress() {
        return address;
    }

    @XmlElement
    public int getId() {
        return id;
    }
    @XmlElement
    public Rates getRates() {
        return rates;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public String getText() {
        return text;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Filial(String city, String address, int id, Rates rates, String name, String text) {
        this.city = city;
        this.address = address;
        this.id = id;
        this.rates = rates;
        this.name = name;
        this.text = text;
    }

    public Filial() {
    }
}
