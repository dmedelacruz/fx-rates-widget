package com.martrust.fxrateswidget.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CurrencyException extends Exception{

    public CurrencyException(String message) {
        super(message);
    }

}
