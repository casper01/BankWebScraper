package com.github.casper01.BankWebScraper.parsers;

import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import org.jsoup.Connection;


public class MbankGetScaAuthorizationDataResponseParser extends MbankJsonResponseParser {
    private static final String SCA_AUTHORIZATION_ID_KEY = "ScaAuthorizationId";
    private static final String ERROR_MESSAGE = "Incorrect GetScaAuthorizationData response";

    public MbankGetScaAuthorizationDataResponseParser(Connection.Response response) {
        super(response);
    }

    public String getScaAuthorizationId() {
        if (!getJSON().has(SCA_AUTHORIZATION_ID_KEY)) {
            throw new WebScraperException(ERROR_MESSAGE);
        }
        return getJSON().get(SCA_AUTHORIZATION_ID_KEY).toString();
    }
}
