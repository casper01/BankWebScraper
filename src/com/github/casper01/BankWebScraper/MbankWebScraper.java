package com.github.casper01.BankWebScraper;

import java.io.IOException;
import java.util.Collection;

public class MbankWebScraper {
    private MbankSignInManager mbankSignInManager;
    private MbankAuthorizer mbankAuthorizer;
    private MbankAccountManager mbankAccountManager;

    MbankWebScraper(String login, String password) {
        mbankSignInManager = new MbankSignInManager(login, password);
        mbankAuthorizer = new MbankAuthorizer();
        mbankAccountManager = new MbankAccountManager();
    }

    public void signIn() throws IOException {
        mbankSignInManager.signIn();
    }

    public void authorize() throws IOException {
        mbankAuthorizer.setCookies(mbankSignInManager.getCookies());
        mbankAuthorizer.authorizeByPhone();
    }

    public Collection<BankAccount> getBankAccounts() throws IOException {
        mbankAccountManager.setCookies(mbankAuthorizer.getCookies());
        return mbankAccountManager.getBankAccounts();
    }
}
