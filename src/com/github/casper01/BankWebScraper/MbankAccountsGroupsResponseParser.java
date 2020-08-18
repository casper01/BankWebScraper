package com.github.casper01.BankWebScraper;


import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;

import java.util.Collection;
import java.util.LinkedList;

class MbankAccountsGroupsResponseParser extends MbankJsonResponseParser {
    private static final String ACCOUNTS_GROUP_KEY = "accountsGroups";
    private static final String ACCOUNTS_GROUP_ELEMENT_KEY = "accounts";
    private static final String ACCOUNT_NAME_KEY = "name";
    private static final String ACCOUNT_BALANCE_KEY = "balance";
    private static final String ERROR_MESSAGE = "Incorrect AccountsGroups response";

    MbankAccountsGroupsResponseParser(Connection.Response response) {
        super(response);
    }

    Collection<BankAccount> getAllAccounts() {
        if (!getJSON().has(ACCOUNTS_GROUP_KEY)) {
            throw new WebScraperException(ERROR_MESSAGE);
        }
        JSONArray accountsGroups = (JSONArray) getJSON().get(ACCOUNTS_GROUP_KEY);
        Collection<BankAccount> bankAccounts = new LinkedList<>();
        for (Object accountGroup : accountsGroups) {
            JSONObject accountGroupJson = (JSONObject) accountGroup;

            if (!accountGroupJson.has(ACCOUNTS_GROUP_ELEMENT_KEY)) {
                throw new WebScraperException(ERROR_MESSAGE);
            }
            JSONArray accountList = (JSONArray) accountGroupJson.get(ACCOUNTS_GROUP_ELEMENT_KEY);
            for (Object account : accountList) {
                JSONObject accountJson = (JSONObject) account;

                if (!accountJson.has(ACCOUNT_NAME_KEY) || !accountJson.has(ACCOUNT_BALANCE_KEY)) {
                    throw new WebScraperException(ERROR_MESSAGE);
                }
                String name = accountJson.get(ACCOUNT_NAME_KEY).toString();
                String balance = accountJson.get(ACCOUNT_BALANCE_KEY).toString();
                bankAccounts.add(new BankAccount(name, Double.parseDouble(balance)));
            }
        }
        return bankAccounts;
    }
}
