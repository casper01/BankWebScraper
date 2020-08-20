package com.github.casper01.BankWebScraper;

import com.github.casper01.BankWebScraper.exceptions.CredentialsException;

import java.io.IOException;
import java.util.Properties;

public class CredentialsLoader {
    private static final String CREDENTIALS_FILE_PATH = "credentials.properties";
    private static final String LOGING_KEY = "login";
    private static final String PASSWORD_KEY = "password";
    private final Properties properties;

    public CredentialsLoader() {
        properties = new Properties();
        try {
            properties.load(CredentialsLoader.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH));
        } catch (IOException e) {
            throw new CredentialsException();
        }
    }

    public String getLogin() {
        return properties.getProperty(LOGING_KEY);
    }

    public String getPassword() {
        return properties.getProperty(PASSWORD_KEY);
    }
}
