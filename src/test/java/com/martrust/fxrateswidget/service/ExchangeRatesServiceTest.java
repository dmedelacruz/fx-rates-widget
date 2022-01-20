package com.martrust.fxrateswidget.service;

import com.martrust.fxrateswidget.exceptions.CurrencyException;
import com.martrust.fxrateswidget.model.ExchangeRate;
import com.martrust.fxrateswidget.proxy.ExchangeRateImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ExchangeRatesServiceTest {

    private static final String BASE_URL = "http://test-base-url";

    private static final String UNKNOWN_CURRENCY = "unknowncurrency";
    private static final String BASE_CURRENCY = "PHP";
    private static final String TARGET_CURRENCY = "USD";
    private static final Double BASE_CURRENCY_VALUE = 1.0;
    private static final Double TARGET_CURRENCY_VALUE = 1.0;

    private static final ExchangeRate EXCHANGE_RATE = new ExchangeRate();

    @TestConfiguration
    static class Configuration {

        @Bean
        public ExchangeRateImpl exchangeRateImpl() {
            return new ExchangeRateImpl(BASE_URL);
        }

        @Bean
        public ExchangeRatesService exchangeRatesService() {
            return new ExchangeRatesService(exchangeRateImpl());
        }

    }

    @BeforeEach
    void init() {
        Map<String, Double> rates = new HashMap<>();
        rates.put(BASE_CURRENCY, BASE_CURRENCY_VALUE);
        rates.put(TARGET_CURRENCY, TARGET_CURRENCY_VALUE);
        EXCHANGE_RATE.setRates(rates);

        Mockito.when(exchangeRateImpl.getExchangeRate()).thenReturn(EXCHANGE_RATE);
    }

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    @MockBean
    private ExchangeRateImpl exchangeRateImpl;

    @Test
    void testGetRateOffered() throws CurrencyException {

        assertThrows(IllegalArgumentException.class, () -> {
            exchangeRatesService.getRateOffered(null, TARGET_CURRENCY);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            exchangeRatesService.getRateOffered("", TARGET_CURRENCY);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            exchangeRatesService.getRateOffered(BASE_CURRENCY, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            exchangeRatesService.getRateOffered(BASE_CURRENCY, "");
        });

        assertThrows(CurrencyException.class, () -> {
            exchangeRatesService.getRateOffered(UNKNOWN_CURRENCY, TARGET_CURRENCY);
        });

        assertThrows(CurrencyException.class, () -> {
            exchangeRatesService.getRateOffered(BASE_CURRENCY, UNKNOWN_CURRENCY);
        });

        Double rate = exchangeRatesService.getRateOffered(BASE_CURRENCY, TARGET_CURRENCY);
        assertNotNull(rate);
        assertEquals((TARGET_CURRENCY_VALUE / BASE_CURRENCY_VALUE), rate);

    }

    @Test
    void testBuyCurrency() throws CurrencyException {

        assertThrows(IllegalArgumentException.class, () -> {
            exchangeRatesService.buyCurrency(null, BASE_CURRENCY, TARGET_CURRENCY);
        });

        final Double amount = 100.0;
        Double res = exchangeRatesService.buyCurrency(amount, BASE_CURRENCY, TARGET_CURRENCY);
        assertNotNull(res);
        assertNotEquals(0, res);
        assertEquals((amount * (1 / (TARGET_CURRENCY_VALUE / BASE_CURRENCY_VALUE))), res);
    }

    @Test
    void testSellCurrency() throws CurrencyException {

        assertThrows(IllegalArgumentException.class, () -> {
            exchangeRatesService.sellCurrency(null, BASE_CURRENCY, TARGET_CURRENCY);
        });

        final Double amount = 100.0;
        Double res = exchangeRatesService.sellCurrency(amount, BASE_CURRENCY, TARGET_CURRENCY);
        assertNotNull(res);
        assertNotEquals(0, res);
        assertEquals((amount * (TARGET_CURRENCY_VALUE / BASE_CURRENCY_VALUE)), res);
    }

}