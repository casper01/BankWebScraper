package com.github.casper01.BankWebScraper;

import java.io.Console;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        String login = getLogin("Login: ");
        String password = getPassword("Password: ");
        MbankWebScraper mbankWebScraper = new MbankWebScraper(login, password);
        mbankWebScraper.signIn();
        System.out.println("> Signed in!");
        mbankWebScraper.authorize();
        System.out.println("> Authorized!");
        Collection<BankAccount> bankAccounts = mbankWebScraper.getBankAccounts();
        System.out.println("> Accounts downloaded:");
        for (BankAccount bankAccount : bankAccounts) {
            System.out.println(bankAccount.getName() + ": " + bankAccount.getBalance());
        }
    }

    private static String getLogin(String prompt){
        Console console = System.console();
        String login = console.readLine(prompt);
        return login;
    }

    private static String getPassword(String prompt) {
        Console console = System.console();
        char[] password = console.readPassword("Password: ");
        return String.valueOf(password);
    }
}
