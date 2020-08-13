package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

class ConnectionResponseMock implements Connection.Response {
    private final String responseBody;

    ConnectionResponseMock(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String body() {
        return responseBody;
    }

    @Override
    public int statusCode() {
        return 0;
    }

    @Override
    public String statusMessage() {
        return null;
    }

    @Override
    public String charset() {
        return null;
    }

    @Override
    public Connection.Response charset(String s) {
        return null;
    }

    @Override
    public String contentType() {
        return null;
    }

    @Override
    public Document parse() throws IOException {
        return null;
    }

    @Override
    public byte[] bodyAsBytes() {
        return new byte[0];
    }

    @Override
    public Connection.Response bufferUp() {
        return null;
    }

    @Override
    public BufferedInputStream bodyStream() {
        return null;
    }

    @Override
    public URL url() {
        return null;
    }

    @Override
    public Connection.Response url(URL url) {
        return null;
    }

    @Override
    public Connection.Method method() {
        return null;
    }

    @Override
    public Connection.Response method(Connection.Method method) {
        return null;
    }

    @Override
    public String header(String s) {
        return null;
    }

    @Override
    public List<String> headers(String s) {
        return null;
    }

    @Override
    public Connection.Response header(String s, String s1) {
        return null;
    }

    @Override
    public Connection.Response addHeader(String s, String s1) {
        return null;
    }

    @Override
    public boolean hasHeader(String s) {
        return false;
    }

    @Override
    public boolean hasHeaderWithValue(String s, String s1) {
        return false;
    }

    @Override
    public Connection.Response removeHeader(String s) {
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return null;
    }

    @Override
    public Map<String, List<String>> multiHeaders() {
        return null;
    }

    @Override
    public String cookie(String s) {
        return null;
    }

    @Override
    public Connection.Response cookie(String s, String s1) {
        return null;
    }

    @Override
    public boolean hasCookie(String s) {
        return false;
    }

    @Override
    public Connection.Response removeCookie(String s) {
        return null;
    }

    @Override
    public Map<String, String> cookies() {
        return null;
    }
}
