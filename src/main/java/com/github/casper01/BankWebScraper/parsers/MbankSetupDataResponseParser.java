package com.github.casper01.BankWebScraper.parsers;

import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import org.jsoup.Connection;


public class MbankSetupDataResponseParser extends MbankJsonResponseParser {
    private static final String ANTI_FORGERY_TOKEN_KEY = "antiForgeryToken";
    private static final String ERROR_MESSAGE = "Incorrect setup/data response";

    public MbankSetupDataResponseParser(Connection.Response response) {
        super(response);
    }

    public String getAntiForgeryToken() {
        if (!getJSON().has(ANTI_FORGERY_TOKEN_KEY)) {
            throw new WebScraperException(ERROR_MESSAGE);
        }
        return getJSON().get(ANTI_FORGERY_TOKEN_KEY).toString();
    }
}
