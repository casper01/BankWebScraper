package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
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

    /**
     * Send sign in request to the mBank server with credentials passed in constructor
     * @throws IOException
     */
    void signIn() throws IOException {
        Connection.Response response = Jsoup.connect(LOGIN_URL)
                .ignoreContentType(true)
                .data("UserName", login)
                .data("Password", password)
                .method(Connection.Method.POST)
                .execute();
        cookies = response.cookies();
        MbankJsonLoginResponseParser mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        if (!mbankJsonLoginResponseParser.isSuccessful()) {
            if (mbankJsonLoginResponseParser.hasErrorMessage())
                throw new LoginException(mbankJsonLoginResponseParser.getErrorMessage());
            else {
                throw new LoginException("Could not sign in");
            }
        }
    }
}