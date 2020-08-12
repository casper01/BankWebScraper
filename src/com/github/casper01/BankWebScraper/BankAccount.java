package com.github.casper01.BankWebScraper;

public class BankAccount {
    private String name;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public BankAccount(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
}
