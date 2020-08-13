package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;


class MbankJsonLoginResponseParser extends MbankJsonResponseParser {
    private static final String SUCCESSFUL_KEY = "successful";
    private static final String ERROR_MESSAGE_KEY = "errorMessageBody";

    MbankJsonLoginResponseParser(Connection.Response response) {
        super(response);
    }

    /**
     * Verify if sign in operation returned response with success status
     * @return True if the operation was successful, otherwise false
     */
    boolean isSuccessful() {
        if (!getJSON().has(SUCCESSFUL_KEY)) {
            throw new WebScraperException();
        }
        return (boolean) getJSON().get(SUCCESSFUL_KEY);
    }

    /**
     * Check if sign in operation contains error message
     * @return True if the request contains error message, otherwise false
     */
    boolean hasErrorMessage() {
        return getJSON().has(ERROR_MESSAGE_KEY);
    }

    /**
     * Get error message from the request passed in constructor
     * @return Error message
     */
    String getErrorMessage() {
        if (!hasErrorMessage()) {
            throw new WebScraperException("Error message does not exist");
        }
        return getJSON().get(ERROR_MESSAGE_KEY).toString();
    }
}
