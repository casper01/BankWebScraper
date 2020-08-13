package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;

import java.io.InvalidObjectException;

public class MbankSetupDataResponseParser extends MbankJsonResponseParser {
    private static final String ANTI_FORGERY_TOKEN_KEY = "antiForgeryToken";
    private static final String ERROR_MESSAGE = "Incorrect setup/data response";

    MbankSetupDataResponseParser(Connection.Response response) throws InvalidObjectException {
        super(response);
    }

    String getAntiForgeryToken() throws InvalidObjectException {
        if (!getJSON().has(ANTI_FORGERY_TOKEN_KEY)) {
            throw new InvalidObjectException(ERROR_MESSAGE);
        }
        return getJSON().get(ANTI_FORGERY_TOKEN_KEY).toString();
    }
}
