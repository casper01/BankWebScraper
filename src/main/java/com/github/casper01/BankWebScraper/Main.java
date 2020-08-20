package com.github.casper01.BankWebScraper;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        CredentialsLoader credentialsLoader = new CredentialsLoader();
        MbankWebScraper mbankWebScraper = new MbankWebScraper(credentialsLoader.getLogin(), credentialsLoader.getPassword());
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
}
