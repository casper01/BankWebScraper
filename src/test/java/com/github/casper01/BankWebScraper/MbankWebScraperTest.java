package com.github.casper01.BankWebScraper;

import com.github.casper01.BankWebScraper.exceptions.LoginException;
import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class MbankWebScraperTest {
    private MbankWebScraper mbankWebScraper;

    @Test
    void signInInvalidCredentialsThrowsException() {
        String login = "invalidLogin";
        String password = "invalidPassword";
        mbankWebScraper = new MbankWebScraper(login, password);
        Assertions.assertThrows(LoginException.class, () -> mbankWebScraper.signIn());
    }

    @Test
    void signInCorrectCredentialsIsSignedIn() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        mbankWebScraper = new MbankWebScraper(login, password);
        Assertions.assertFalse(mbankWebScraper.isSignedIn());
        mbankWebScraper.signIn();
        Assertions.assertTrue(mbankWebScraper.isSignedIn());
    }

    @Test
    void authorizeNotSignedInThrowsException() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        mbankWebScraper = new MbankWebScraper(login, password);
        Assertions.assertThrows(WebScraperException.class, () -> mbankWebScraper.authorize());
    }

    @Test
    void authorizeByPhoneSuccessIsAuthorized() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        mbankWebScraper = new MbankWebScraper(login, password);
        mbankWebScraper.signIn();
        Assertions.assertFalse(mbankWebScraper.isAuthorized());
        mbankWebScraper.authorize();
        Assertions.assertTrue(mbankWebScraper.isAuthorized());
    }

    @Test
    void getBankAccountsNotSignedInThrowsException() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        mbankWebScraper = new MbankWebScraper(login, password);
        mbankWebScraper.signIn();
        Assertions.assertThrows(WebScraperException.class, () -> mbankWebScraper.getBankAccounts());
    }

    @Test
    void getBankAccountsSuccessReturnsNotEmptyAccountList() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        mbankWebScraper = new MbankWebScraper(login, password);
        mbankWebScraper.signIn();
        mbankWebScraper.authorize();
        Collection<BankAccount> bankAccounts = mbankWebScraper.getBankAccounts();
        Assertions.assertFalse(bankAccounts.isEmpty());
    }
}