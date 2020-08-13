package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;

class MbankInitprepareResponseParser extends MbankJsonResponseParser {
    private static final String TRANSACTION_ID_KEY = "TranId";
    private static final String ERROR_MESSAGE = "Incorrect initprepare response";

    MbankInitprepareResponseParser(Connection.Response response) {
        super(response);
    }

    String getTransactionId() {
        if (!getJSON().has(TRANSACTION_ID_KEY)) {
            throw new WebScraperException(ERROR_MESSAGE);
        }
        return getJSON().get(TRANSACTION_ID_KEY).toString();
    }
}
