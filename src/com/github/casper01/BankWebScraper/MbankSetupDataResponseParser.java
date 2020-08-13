package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;


class MbankSetupDataResponseParser extends MbankJsonResponseParser {
    private static final String ANTI_FORGERY_TOKEN_KEY = "antiForgeryToken";
    private static final String ERROR_MESSAGE = "Incorrect setup/data response";

    MbankSetupDataResponseParser(Connection.Response response) {
        super(response);
    }

    String getAntiForgeryToken() {
        if (!getJSON().has(ANTI_FORGERY_TOKEN_KEY)) {
            throw new WebScraperException(ERROR_MESSAGE);
        }
        return getJSON().get(ANTI_FORGERY_TOKEN_KEY).toString();
    }
}
