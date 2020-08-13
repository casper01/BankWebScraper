package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;


class MbankJsonLoginResponseParser extends MbankJsonResponseParser {
    private static final String SUCCESSFUL_KEY = "successful";
    private static final String ERROR_MESSAGE_KEY = "errorMessageBody";

    MbankJsonLoginResponseParser(Connection.Response response) {
        super(response);
    }

    boolean isSuccessful() {
        if (!getJSON().has(SUCCESSFUL_KEY)) {
            throw new WebScraperException();
        }
        return (boolean) getJSON().get(SUCCESSFUL_KEY);
    }

    boolean hasErrorMessage() {
        return getJSON().has(ERROR_MESSAGE_KEY);
    }

    String getErrorMessage() {
        if (!hasErrorMessage()) {
            throw new WebScraperException("Error message does not exist");
        }
        return getJSON().get(ERROR_MESSAGE_KEY).toString();
    }
}
