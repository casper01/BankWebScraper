package com.github.casper01.BankWebScraper;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;

abstract class MbankJsonResponseParser {
    private final JSONObject response;

    MbankJsonResponseParser(Connection.Response response)  {
        try {
            this.response = new JSONObject(response.body());
        } catch (JSONException ex) {
            throw new WebScraperException("Request is not correct json");
        }
    }

    protected JSONObject getJSON() {
        return response;
    }
}
