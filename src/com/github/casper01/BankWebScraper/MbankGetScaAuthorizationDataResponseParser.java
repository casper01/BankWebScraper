package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;

import java.io.InvalidObjectException;

class MbankGetScaAuthorizationDataResponseParser extends MbankJsonResponseParser {
    private static final String SCA_AUTHORIZATION_ID_KEY = "ScaAuthorizationId";
    private static final String ERROR_MESSAGE = "Incorrect GetScaAuthorizationData response";

    MbankGetScaAuthorizationDataResponseParser(Connection.Response response) throws InvalidObjectException {
        super(response);
    }

    String getScaAuthorizationId() throws InvalidObjectException {
        if (!getJSON().has(SCA_AUTHORIZATION_ID_KEY)) {
            throw new InvalidObjectException(ERROR_MESSAGE);
        }
        return getJSON().get(SCA_AUTHORIZATION_ID_KEY).toString();
    }
}
