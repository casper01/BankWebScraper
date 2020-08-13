package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;

import java.io.InvalidObjectException;

class MbankInitprepareResponseParser extends MbankJsonResponseParser {
    private static final String TRANSACTION_ID_KEY = "TranId";
    private static final String ERROR_MESSAGE = "Incorrect initprepare response";

    MbankInitprepareResponseParser(Connection.Response response) throws InvalidObjectException {
        super(response);
    }

    String getTransactionId() throws InvalidObjectException {
        if (!getJSON().has(TRANSACTION_ID_KEY)) {
            throw new InvalidObjectException(ERROR_MESSAGE);
        }
        return getJSON().get(TRANSACTION_ID_KEY).toString();
    }
}
