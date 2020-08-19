package com.github.casper01.BankWebScraper.exceptions;

public class WebScraperException extends RuntimeException {
    public WebScraperException() {
        super();
    }

    public WebScraperException(String message) {
        super(message);
    }
}
