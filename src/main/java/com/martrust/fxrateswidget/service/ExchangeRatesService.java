package com.martrust.fxrateswidget.service;

import com.martrust.fxrateswidget.exceptions.CurrencyException;
import com.martrust.fxrateswidget.model.ExchangeRate;
import com.martrust.fxrateswidget.proxy.ExchangeRateImpl;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRatesService {

    private final ExchangeRateImpl exchangeRateImpl;

    public ExchangeRatesService(ExchangeRateImpl exchangeRateImpl) {
        this.exchangeRateImpl = exchangeRateImpl;
    }

    /*
     * exchangeratesapi.io only allows EUR as base currency
     * on free tier subscription. Additional logic is required
     * to get the rateOffered.
     */
    public Double getRateOffered(String baseCurrency, String targetCurrency) throws CurrencyException {

        //Null Checking
        if(baseCurrency == null || baseCurrency.isBlank()) {
            throw new IllegalArgumentException("Missing base currency");
        }

        if(targetCurrency == null || targetCurrency.isBlank()) {
            throw new IllegalArgumentException("Missing target currency");
        }

        //Check Currency if supported
        final Currency bC = getCurrency(baseCurrency);
        final Currency tC = getCurrency(targetCurrency);

        final ExchangeRate exchangeRates = exchangeRateImpl.getExchangeRate();

        final Double baseCurrencyValue = exchangeRates.getRates().get(bC.name());
        final Double targetCurrencyValue = exchangeRates.getRates().get(tC.name());

        return targetCurrencyValue / baseCurrencyValue;
    }

    public Double buyCurrency(Double amount, String baseCurrency, String targetCurrency) throws CurrencyException {

        //Check amount
        if(amount == null || amount.isNaN()) {
            throw new IllegalArgumentException("Incorrect Amount");
        }

        Double cf = getRateOffered(targetCurrency, baseCurrency);
        return amount * (1/cf);
    }

    public Double sellCurrency(Double amount, String baseCurrency, String targetCurrency) throws CurrencyException {

        //Check amount
        if(amount == null || amount.isNaN()) {
            throw new IllegalArgumentException("Incorrect Amount");
        }

        Double cf = getRateOffered(baseCurrency, targetCurrency);
        return amount * cf;
    }

    /*
     * Utility Method that will verify if currency is supported
     */
    private Currency getCurrency(String currency) throws CurrencyException {

        try {
            return Currency.valueOf(currency);
        } catch (Exception e) {
            throw new CurrencyException("Unknown Currency: " + currency);
        }

    }

}
