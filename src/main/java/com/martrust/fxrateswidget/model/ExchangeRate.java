package com.martrust.fxrateswidget.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

public class ExchangeRate {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("timestamp")
    private Long timeStamp;

    @JsonProperty("base")
    private String baseCurrency; //Free Tier Only supports EUR

    @JsonProperty("date")
    private Date date;

    @JsonProperty("rates")
    private Map<String, Double> rates;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
