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

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    Collection<BankAccount> getBankAccounts() throws IOException {
        Connection.Response response = Jsoup.connect(ACCOUNT_GROUPS_URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .cookies(cookies)
                .execute();
        return getAccountFromResponse(response.body());
    }

    private Collection<BankAccount> getAccountFromResponse(String response) throws InvalidObjectException {
        Collection<BankAccount> bankAccounts = new ArrayList<>();
        JSONArray accountsGroups;
        try {
            JSONObject responseJson = new JSONObject(response);
            accountsGroups = (JSONArray) responseJson.get("accountsGroups");
        } catch (JSONException ex) {
            throw new InvalidObjectException("Invalid data returned from server");
        }

        for (Object accountCurrency : accountsGroups) {
            JSONObject accountCurrencyJson = (JSONObject) accountCurrency;
            JSONArray accountList = (JSONArray) accountCurrencyJson.get("accounts");
            for (Object account : accountList) {
                JSONObject accountJson = (JSONObject) account;
                String name = accountJson.get("name").toString();
                String balance = accountJson.get("balance").toString();
                bankAccounts.add(new BankAccount(name, Double.parseDouble(balance)));
            }
        }
        return bankAccounts;
    }
}
