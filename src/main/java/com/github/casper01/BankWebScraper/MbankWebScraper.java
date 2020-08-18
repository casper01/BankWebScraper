package com.github.casper01.BankWebScraper;

import java.io.IOException;
import java.util.Collection;

public class MbankWebScraper {
    private final MbankSignInManager mbankSignInManager;
    private final MbankAuthorizer mbankAuthorizer;
    private final MbankAccountManager mbankAccountManager;

    MbankWebScraper(String login, String password) {
        mbankSignInManager = new MbankSignInManager(login, password);
        mbankAuthorizer = new MbankAuthorizer();
        mbankAccountManager = new MbankAccountManager();
    }

    public void signIn() {
        mbankSignInManager.signIn();
    }

    public void authorize() {
        System.out.println("Please authorize signing in by your phone");
        mbankAuthorizer.setCookies(mbankSignInManager.getCookies());
        mbankAuthorizer.authorizeByPhone();
    }

    public Collection<BankAccount> getBankAccounts() {
        mbankAccountManager.setCookies(mbankAuthorizer.getCookies());
        return mbankAccountManager.getBankAccounts();
    }
}
