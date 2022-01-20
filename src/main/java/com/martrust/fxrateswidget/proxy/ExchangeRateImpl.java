package com.martrust.fxrateswidget.proxy;

import com.martrust.fxrateswidget.exceptions.ExchangeRateException;
import com.martrust.fxrateswidget.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExchangeRateImpl implements ExchangeRateProxy {

    @Value("${exchangerate.api.key}")
    private String accessKey;

    private final WebClient webClient;

    public ExchangeRateImpl(@Value("${exchangerate.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Mono<ExchangeRate> fetchExchangeRates() {
        return webClient.get()
                .uri(builder -> builder.path("/latest").queryParam("access_key", accessKey).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(new ExchangeRateException("Unauthorized"))
                )
                .onStatus(HttpStatus::is5xxServerError, response ->
                        Mono.error(new ExchangeRateException("Exchange Rate Server Error"))
                )
                .bodyToMono(ExchangeRate.class);
    }

    public ExchangeRate getExchangeRate() {
        final Mono<ExchangeRate> exchangeRatesMono = fetchExchangeRates();
        return exchangeRatesMono.block();
    }
}
