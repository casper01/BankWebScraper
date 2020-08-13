package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;

import java.io.InvalidObjectException;

public class MbankJsonLoginResponseParser extends MbankJsonResponseParser {
    private static final String SUCCESSFUL_KEY = "successful";
    private static final String ERROR_MESSAGE_KEY = "errorMessageBody";

    MbankJsonLoginResponseParser(Connection.Response response) throws InvalidObjectException {
        super(response);
    }

    boolean isSuccessful() {
        return (boolean) getJSON().get(SUCCESSFUL_KEY);
    }

    boolean hasErrorMessage() {
        return getJSON().has(ERROR_MESSAGE_KEY);
    }

    String getErrorMessage() {
        if (!hasErrorMessage()) {
            throw new UnsupportedOperationException("Error mesage does not exist");
        }
        return getJSON().get(ERROR_MESSAGE_KEY).toString();
    }
}
