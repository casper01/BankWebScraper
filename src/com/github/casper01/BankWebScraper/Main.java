package com.github.casper01.BankWebScraper;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        MbankWebScraper mbankWebScraper = new MbankWebScraper(MbankCredentials.LOGIN, MbankCredentials.PASSWORD);
        try {
            mbankWebScraper.signIn();
        } catch (IOException e) {
            System.out.println("Could not sign in");
            e.printStackTrace();
        }
        System.out.println("> Signed in!");
        try {
            mbankWebScraper.authorize();
        } catch (IOException e) {
            System.out.println("Could not authorize");
            e.printStackTrace();
        }
        System.out.println("> authorized!");
    }
}
