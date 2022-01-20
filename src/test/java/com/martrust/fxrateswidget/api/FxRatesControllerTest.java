package com.martrust.fxrateswidget.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martrust.fxrateswidget.proxy.ExchangeRateImpl;
import com.martrust.fxrateswidget.service.ExchangeRatesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FxRatesController.class)
class FxRatesControllerTest {

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ExchangeRatesService exchangeRatesService;

    private static final String BASE_URL = "http://test-base-url";

    private static final String UNKNOWN_CURRENCY = "unknowncurrency";
    private static final String BASE_CURRENCY = "PHP";
    private static final String TARGET_CURRENCY = "USD";
    private static final Double AMOUNT = 1D;
    private static final Double RESULT = 1D;

    @Test
    void testGetRateOfferedOK() throws Exception {

        Mockito.when(exchangeRatesService.getRateOffered(BASE_CURRENCY, TARGET_CURRENCY)).thenReturn(RESULT);

        mockMvc.perform(
            get("/exchange?base=" + BASE_CURRENCY + "&target=" + TARGET_CURRENCY)
                .contentType("application/json"))
                .andExpect(status()
                .isOk()
        );

    }

    @Test
    void testBuyCurrencyOK() throws Exception {

        Mockito.when(exchangeRatesService.buyCurrency(AMOUNT, BASE_CURRENCY, TARGET_CURRENCY)).thenReturn(RESULT);

        mockMvc.perform(
                get("/exchange/buy?amount=" + AMOUNT + "&base=" + BASE_CURRENCY + "&buying_currency=" + TARGET_CURRENCY)
                        .contentType("application/json"))
                .andExpect(status()
                        .isOk()
                );
    }

    @Test
    void testSellCurrencyOK() throws Exception {

        Mockito.when(exchangeRatesService.sellCurrency(AMOUNT, BASE_CURRENCY, TARGET_CURRENCY)).thenReturn(RESULT);

        mockMvc.perform(
                get("/exchange/sell?amount=" + AMOUNT + "&selling_currency=" + BASE_CURRENCY + "&target=" + TARGET_CURRENCY)
                        .contentType("application/json"))
                .andExpect(status()
                        .isOk()
                );
    }
}