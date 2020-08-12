package com.github.casper01.BankWebScraper;

import java.io.IOException;
import java.util.Collection;

public class MbankWebScraper {
    private MbankSignInManager mbankSignInManager;
    private MbankAuthorizer mbankAuthorizer;

    MbankWebScraper(String login, String password) {
        mbankSignInManager = new MbankSignInManager(login, password);
        mbankAuthorizer = new MbankAuthorizer();
    }

    public void signIn() throws IOException {
        mbankSignInManager.signIn();
    }

    public void authorize() throws IOException {
        mbankAuthorizer.setCookies(mbankSignInManager.getCookies());
        mbankAuthorizer.authorizeByPhone();
    }

    public Collection<BankAccount> getBankAccounts() {
        return null;
    }
}
