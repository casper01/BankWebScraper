package com.github.casper01.BankWebScraper.apiManagers;

import com.github.casper01.BankWebScraper.BankAccount;
import com.github.casper01.BankWebScraper.CredentialsLoader;
import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class MbankAccountManagerTest {
    private MbankAccountManager mbankAccountManager;

    @Test
    void getBankAccountsNotSignedInThrowsException() {
        mbankAccountManager = new MbankAccountManager();
        Assertions.assertThrows(WebScraperException.class, () -> mbankAccountManager.getBankAccounts());
    }

    @Test
    void getBankAccountsNotAuthorizedThrowsException() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        MbankSignInManager mbankSignInManager = new MbankSignInManager(login, password);
        mbankSignInManager.signIn();
        mbankAccountManager = new MbankAccountManager();
        mbankAccountManager.setCookies(mbankSignInManager.getCookies());
        mbankAccountManager = new MbankAccountManager();
        Assertions.assertThrows(WebScraperException.class, () -> mbankAccountManager.getBankAccounts());
    }

    @Test
    void getBankAccountsSuccessReturnsNotEmptyAccountList() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        MbankSignInManager mbankSignInManager = new MbankSignInManager(login, password);
        mbankSignInManager.signIn();
        MbankAuthorizer mbankAuthorizer = new MbankAuthorizer();
        mbankAuthorizer.setCookies(mbankSignInManager.getCookies());
        mbankAuthorizer.authorizeByPhone();
        mbankAccountManager = new MbankAccountManager();
        mbankAccountManager.setCookies(mbankAuthorizer.getCookies());
        Collection<BankAccount> bankAccounts = mbankAccountManager.getBankAccounts();
        Assertions.assertFalse(bankAccounts.isEmpty());
    }
}