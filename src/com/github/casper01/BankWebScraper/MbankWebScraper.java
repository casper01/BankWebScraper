package com.github.casper01.BankWebScraper;

import java.io.IOException;
import java.util.Collection;

public class MbankWebScraper {
    private static final String LOGIN_ERROR_MESSAGE = "Unable to log in";
    private static final String AUTHORIZE_ERROR_MESSAGE = "Unable to authorize";
    private static final String GET_BANK_ACCOUNTS_ERROR_MESSAGE = "Unable to get bank accounts";
    private final MbankSignInManager mbankSignInManager;
    private final MbankAuthorizer mbankAuthorizer;
    private final MbankAccountManager mbankAccountManager;

    MbankWebScraper(String login, String password) {
        mbankSignInManager = new MbankSignInManager(login, password);
        mbankAuthorizer = new MbankAuthorizer();
        mbankAccountManager = new MbankAccountManager();
    }

    public void signIn() {
        try {
            mbankSignInManager.signIn();
        } catch (IOException ex) {
            throw new WebScraperException(LOGIN_ERROR_MESSAGE);
        }
    }

    public void authorize() {
        System.out.println("Please authorize signing in by your phone");
        mbankAuthorizer.setCookies(mbankSignInManager.getCookies());
        try {
            mbankAuthorizer.authorizeByPhone();
        } catch (IOException ex) {
            throw new WebScraperException(AUTHORIZE_ERROR_MESSAGE);
        }
    }

    public Collection<BankAccount> getBankAccounts() {
        mbankAccountManager.setCookies(mbankAuthorizer.getCookies());
        try {
            return mbankAccountManager.getBankAccounts();
        } catch (IOException ex) {
            throw new WebScraperException(GET_BANK_ACCOUNTS_ERROR_MESSAGE);
        }
    }
}
