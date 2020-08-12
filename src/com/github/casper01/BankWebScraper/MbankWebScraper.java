package com.github.casper01.BankWebScraper;

import java.io.IOException;
import java.util.Collection;

public class MbankWebScraper {
    private MbankSignInManager mbankSignInManager;

    MbankWebScraper(String login, String password) {
        mbankSignInManager = new MbankSignInManager(login, password);
    }

    public void signIn() throws IOException {
        mbankSignInManager.signIn();
    }

    public void authorize() {

    }

    public Collection<BankAccount> getBankAccounts() {
        return null;
    }
}
