package com.github.casper01.BankWebScraper.apiManagers;

import com.github.casper01.BankWebScraper.exceptions.LoginException;
import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import com.github.casper01.BankWebScraper.parsers.MbankJsonLoginResponseParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MbankSignInManager {
    private static final String LOGIN_URL = "https://online.mbank.pl/pl/LoginMain/Account/JsonLogin";
    private static final String LOGIN_ERROR_MESSAGE = "Unable to log in";
    private final String login;
    private final String password;
    private Map<String, String> cookies = new HashMap<>();
    private boolean isSignedIn;

    public MbankSignInManager(String login, String password) {
        this.login = login;
        this.password = password;
        this.isSignedIn = false;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void signIn() {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(LOGIN_URL)
                    .ignoreContentType(true)
                    .data("UserName", login)
                    .data("Password", password)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e) {
            throw new WebScraperException(LOGIN_ERROR_MESSAGE);
        }
        cookies = response.cookies();
        MbankJsonLoginResponseParser mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        if (!mbankJsonLoginResponseParser.isSuccessful()) {
            if (mbankJsonLoginResponseParser.hasErrorMessage())
                throw new LoginException(mbankJsonLoginResponseParser.getErrorMessage());
            else {
                throw new LoginException("Could not sign in");
            }
        }
        else {
            isSignedIn = true;
        }
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }
}