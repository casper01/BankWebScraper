package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class MbankAccountManager {
    private static final String  ACCOUNT_GROUPS_URL = "https://online.mbank.pl/pl/Accounts/Accounts/AccountsGroups";
    private static final String GET_BANK_ACCOUNTS_ERROR_MESSAGE = "Unable to get bank accounts";
    private Map<String, String> cookies = new HashMap<>();

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    Collection<BankAccount> getBankAccounts() {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(ACCOUNT_GROUPS_URL)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .cookies(cookies)
                    .execute();
        } catch (IOException e) {
            throw new WebScraperException(GET_BANK_ACCOUNTS_ERROR_MESSAGE);
        }
        MbankAccountsGroupsResponseParser mbankAccountsGroupsResponseParser = new MbankAccountsGroupsResponseParser(response);
        return mbankAccountsGroupsResponseParser.getAllAccounts();
    }
}
