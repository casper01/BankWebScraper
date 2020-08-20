package com.github.casper01.BankWebScraper.apiManagers;

import com.github.casper01.BankWebScraper.CredentialsLoader;
import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class MbankAuthorizerTest {
    MbankAuthorizer mbankAuthorizer;

    @Test
    void authorizeByPhoneNotSignedInThrowsException() {
        mbankAuthorizer = new MbankAuthorizer();
        Assertions.assertThrows(WebScraperException.class, () -> mbankAuthorizer.authorizeByPhone());
    }

    @Test
    void authorizeByPhoneSuccessIsAuthorized() {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        String login = credentialsLoader.getLogin();
        String password = credentialsLoader.getPassword();
        MbankSignInManager mbankSignInManager = new MbankSignInManager(login, password);
        mbankSignInManager.signIn();
        mbankAuthorizer = new MbankAuthorizer();
        mbankAuthorizer.setCookies(mbankSignInManager.getCookies());
        Assertions.assertFalse(mbankAuthorizer.isAuthorized());
        mbankAuthorizer.authorizeByPhone();
        Assertions.assertTrue(mbankAuthorizer.isAuthorized());
    }
}