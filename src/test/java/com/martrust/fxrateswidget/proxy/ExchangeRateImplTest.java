package com.martrust.fxrateswidget.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martrust.fxrateswidget.model.ExchangeRate;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class ExchangeRateImplTest {

    private static final String BASE_CURRENCY = "PHP";
    private static final Date DATE = new Date();
    private static final Map<String, Double> rates = new HashMap<>();

    private ExchangeRateImpl exchangeRateImpl;
    private ExchangeRate mockExchangeRate;
    static MockWebServer mockExchangeRatesApi;

    @BeforeAll
    static void setUp() throws IOException {
        mockExchangeRatesApi = new MockWebServer();
        mockExchangeRatesApi.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockExchangeRatesApi.shutdown();
    }

    @BeforeEach
    void init() {
        String baseUrl = String.format("http://localhost:%s",
                mockExchangeRatesApi.getPort());
        exchangeRateImpl = new ExchangeRateImpl(baseUrl);

        mockExchangeRate = new ExchangeRate();
        mockExchangeRate.setSuccess(true);
        mockExchangeRate.setTimeStamp(1L);
        mockExchangeRate.setBaseCurrency(BASE_CURRENCY);
        mockExchangeRate.setDate(DATE);
        mockExchangeRate.setRates(rates);

    }

    @Test
    void testFetchExchangeRates() throws InterruptedException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        mockExchangeRatesApi.enqueue(
                new MockResponse()
                        .setBody(objectMapper.writeValueAsString(mockExchangeRate))
                        .addHeader("Content-Type", "application/json")
        );

        Mono<ExchangeRate> exchangeRateMono = exchangeRateImpl.fetchExchangeRates();
        final ExchangeRate exchangeRate = exchangeRateMono.block();
        assertNotNull(exchangeRate);

        RecordedRequest recordedRequest = mockExchangeRatesApi.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/latest?access_key", recordedRequest.getPath());

    }

    @Test
    void testGetExchangeRate() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        mockExchangeRatesApi.enqueue(
                new MockResponse()
                        .setBody(objectMapper.writeValueAsString(mockExchangeRate))
                        .addHeader("Content-Type", "application/json")
        );

        final ExchangeRate exchangeRate = exchangeRateImpl.getExchangeRate();

        assertNotNull(exchangeRate);
        assertEquals(true, exchangeRate.getSuccess());
        assertEquals(1L, exchangeRate.getTimeStamp());
        assertEquals(BASE_CURRENCY, exchangeRate.getBaseCurrency());
        assertEquals(DATE, exchangeRate.getDate());
        assertEquals(rates, exchangeRate.getRates());
    }

}