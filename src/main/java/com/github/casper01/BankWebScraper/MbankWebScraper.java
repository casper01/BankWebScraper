package com.github.casper01.BankWebScraper;

import com.github.casper01.BankWebScraper.apiManagers.MbankAccountManager;
import com.github.casper01.BankWebScraper.apiManagers.MbankAuthorizer;
import com.github.casper01.BankWebScraper.apiManagers.MbankSignInManager;

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

    public boolean isSignedIn() {
        return mbankSignInManager.isSignedIn();
    }

    public void authorize() {
        System.out.println("Please authorize signing in by your phone");
        mbankAuthorizer.setCookies(mbankSignInManager.getCookies());
        mbankAuthorizer.authorizeByPhone();
    }

    public boolean isAuthorized() {
        return mbankAuthorizer.isAuthorized();
    }

    public Collection<BankAccount> getBankAccounts() {
        mbankAccountManager.setCookies(mbankAuthorizer.getCookies());
        return mbankAccountManager.getBankAccounts();
    }
}
