package com.martrust.fxrateswidget.proxy;

import com.martrust.fxrateswidget.model.ExchangeRate;
import reactor.core.publisher.Mono;

public interface ExchangeRateProxy {

    Mono<ExchangeRate> fetchExchangeRates();

}
