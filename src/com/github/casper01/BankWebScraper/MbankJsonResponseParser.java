package com.github.casper01.BankWebScraper;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;

import java.io.InvalidObjectException;

abstract class MbankJsonResponseParser {
    JSONObject response;

    MbankJsonResponseParser(Connection.Response response) throws InvalidObjectException {
        try {
            this.response = new JSONObject(response.body());
        } catch (JSONException ex) {
            throw new InvalidObjectException("Request is not correct json");
        }
    }

    protected JSONObject getJSON() {
        return response;
    }
}
