package com.martrust.fxrateswidget.api;

import com.martrust.fxrateswidget.exceptions.CurrencyException;
import com.martrust.fxrateswidget.service.ExchangeRatesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class FxRatesController {

    private final ExchangeRatesService exchangeRatesService;

    public FxRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping
    @Operation(summary = "Get Rate Offered")
    public Double getRateOffered(
            @RequestParam("base") String baseCurrency,
            @RequestParam("target") String targetCurrency
    ) throws CurrencyException {
        return exchangeRatesService.getRateOffered(baseCurrency.toUpperCase(), targetCurrency.toUpperCase());
    }

    @GetMapping("/buy")
    @Operation(summary = "Buy Currency")
    public Double buyCurrency(
            @RequestParam("amount") Double amount,
            @RequestParam("base") String baseCurrency,
            @RequestParam("buying_currency") String targetCurrency
    ) throws CurrencyException {
        return exchangeRatesService.buyCurrency(amount, baseCurrency.toUpperCase(), targetCurrency.toUpperCase());
    }

    @GetMapping("/sell")
    @Operation(summary = "Sell Currency")
    public Double sellCurrency(
            @RequestParam("amount") Double amount,
            @RequestParam("selling_currency") String baseCurrency,
            @RequestParam("target") String targetCurrency
    ) throws CurrencyException {
        return exchangeRatesService.sellCurrency(amount, baseCurrency.toUpperCase(), targetCurrency.toUpperCase());
    }

}
