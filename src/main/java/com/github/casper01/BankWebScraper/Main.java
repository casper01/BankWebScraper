package com.github.casper01.BankWebScraper;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        MbankWebScraper mbankWebScraper = new MbankWebScraper(MbankCredentials.LOGIN, MbankCredentials.PASSWORD);
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
