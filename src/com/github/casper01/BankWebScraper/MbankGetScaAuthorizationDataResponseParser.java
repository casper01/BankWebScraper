package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;


class MbankGetScaAuthorizationDataResponseParser extends MbankJsonResponseParser {
    private static final String SCA_AUTHORIZATION_ID_KEY = "ScaAuthorizationId";
    private static final String ERROR_MESSAGE = "Incorrect GetScaAuthorizationData response";

    MbankGetScaAuthorizationDataResponseParser(Connection.Response response) {
        super(response);
    }

    /**
     * Get Sca authorization id from the request passed in constructor
     * @return Sca authorization id
     */
    String getScaAuthorizationId() {
        if (!getJSON().has(SCA_AUTHORIZATION_ID_KEY)) {
            throw new WebScraperException(ERROR_MESSAGE);
        }
        return getJSON().get(SCA_AUTHORIZATION_ID_KEY).toString();
    }
}
