package com.github.casper01.BankWebScraper.apiManagers;

public abstract class MbankApiManager {
    private static final String BASE_URL = "https://online.mbank.pl";

    static String getBaseUrl() {
        return BASE_URL;
    }
}
