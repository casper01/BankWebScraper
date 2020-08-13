package com.github.casper01.BankWebScraper;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Double.compare(that.balance, balance) == 0 &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, balance);
    }
}
