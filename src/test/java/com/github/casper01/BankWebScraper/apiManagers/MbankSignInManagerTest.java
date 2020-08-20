package com.github.casper01.BankWebScraper.apiManagers;

import com.github.casper01.BankWebScraper.CredentialsLoader;
import com.github.casper01.BankWebScraper.exceptions.LoginException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MbankSignInManagerTest {
    private MbankSignInManager mbankSignInManager;

    @Test
    void signInInvalidCredentialsThrowsException() {
        String login = "invalidLogin";
        String password = "admin1";
        mbankSignInManager = new MbankSignInManager(login, password);
        Assertions.assertThrows(LoginException.class, () -> mbankSignInManager.signIn());
    }

    @Test
    void signInCorrectCredentialsIsSignedIn() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        mbankSignInManager = new MbankSignInManager(login, password);
        Assertions.assertFalse(mbankSignInManager.isSignedIn());
        mbankSignInManager.signIn();
        Assertions.assertTrue(mbankSignInManager.isSignedIn());
    }
}