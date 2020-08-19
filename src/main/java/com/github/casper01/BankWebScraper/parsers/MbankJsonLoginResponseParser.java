package com.github.casper01.BankWebScraper.parsers;

import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import org.jsoup.Connection;


public class MbankJsonLoginResponseParser extends MbankJsonResponseParser {
    private static final String SUCCESSFUL_KEY = "successful";
    private static final String ERROR_MESSAGE_KEY = "errorMessageBody";

    public MbankJsonLoginResponseParser(Connection.Response response) {
        super(response);
    }

    public boolean isSuccessful() {
        if (!getJSON().has(SUCCESSFUL_KEY)) {
            throw new WebScraperException();
        }
        return (boolean) getJSON().get(SUCCESSFUL_KEY);
    }

    public boolean hasErrorMessage() {
        return getJSON().has(ERROR_MESSAGE_KEY);
    }

    public String getErrorMessage() {
        if (!hasErrorMessage()) {
            throw new WebScraperException("Error message does not exist");
        }
        return getJSON().get(ERROR_MESSAGE_KEY).toString();
    }
}
