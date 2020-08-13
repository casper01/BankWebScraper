package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class MbankAccountManager {
    private static final String  ACCOUNT_GROUPS_URL = "https://online.mbank.pl/pl/Accounts/Accounts/AccountsGroups";
    private Map<String, String> cookies = new HashMap<>();

    /**
     * Overwrite cookies used in requests.
     * @param cookies new cookies that will be used in future requests.
     */
    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    /**
     * Get information about all accounts of the user pointed by cookies.
     * Including accounts in PLN and in foreign currency.
     * @return Collection of bank accounts.
     * @throws IOException
     */
    Collection<BankAccount> getBankAccounts() throws IOException {
        Connection.Response response = Jsoup.connect(ACCOUNT_GROUPS_URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .cookies(cookies)
                .execute();
        MbankAccountsGroupsResponseParser mbankAccountsGroupsResponseParser = new MbankAccountsGroupsResponseParser(response);
        return mbankAccountsGroupsResponseParser.getAllAccounts();
    }
}
