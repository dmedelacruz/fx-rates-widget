package com.martrust.fxrateswidget.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
public class ExchangeRateException extends Exception {

    public ExchangeRateException(String errorMessage) {
        super(errorMessage);
    }

}
