package com.github.casper01.BankWebScraper;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;

class MbankSignInManager {
    private static final String LOGIN_URL = "https://online.mbank.pl/pl/LoginMain/Account/JsonLogin";
    private final String login;
    private final String password;
    private Map<String, String> cookies  = new HashMap<>();

    MbankSignInManager(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void signIn() throws IOException {
        Connection.Response response = Jsoup.connect(LOGIN_URL)
                .ignoreContentType(true)
                .data("UserName", login)
                .data("Password", password)
                .method(Connection.Method.POST)
                .execute();
        cookies = response.cookies();
        verifyResponse(new JSONObject(response.body()));
    }

    private void verifyResponse(JSONObject response) throws InvalidObjectException {
        System.out.println(response.toString());
        boolean status = (boolean) response.get("successful");
        if (!status) {
            String errorMsg = response.get("errorMessageBody").toString();
            throw new InvalidObjectException(errorMsg);
        }
    }
}
