package com.github.casper01.BankWebScraper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class MbankAccountManager {
    private static String ACCOUNT_GROUPS_URL = "https://online.mbank.pl/pl/Accounts/Accounts/AccountsGroups";
    private Map<String, String> cookies = new HashMap<>();

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

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
