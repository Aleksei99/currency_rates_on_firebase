package com.smuraha.currency_rates.service.bankApi.dto.dabrabit;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.MalformedURLException;
import java.net.URL;

@XmlRootElement
@Getter
@ToString
public class Root {

    private String time;

    private Filials filials;




    public void setTime(String time) {
        this.time = time;
    }




    public void setFilials(Filials filials) {
        this.filials = filials;
    }

    public Root(String time, Filials filials) {
        this.time = time;
        this.filials = filials;
    }

    public Root() {
    }


}
