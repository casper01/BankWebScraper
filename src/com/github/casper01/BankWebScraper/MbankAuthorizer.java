package com.github.casper01.BankWebScraper;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;

class MbankAuthorizer {
    private static final String SCA_AUTHORIZATION_URL_DATA_URL = "https://online.mbank.pl/pl/Sca/GetScaAuthorizationData";
    private static final String VERIFICATION_URL = "https://online.mbank.pl/pl/setup/data";
    private static final String INIT_AUTHORIZATION_URL = "https://online.mbank.pl/api/auth/initprepare";
    private static final String AUTHORIZATION_STATUS_URL = "https://online.mbank.pl/api/auth/status";
    private static final int STATUS_CHECK_INTERVAL_MS = 1000;
    private static final int STATUS_CHECK_MAX_ITERATIONS = 300;
    private Map<String, String> cookies = new HashMap<>();

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    void authorizeByPhone() throws IOException {
        String authorizationId = getScaAuthorizationId();
        String verificationToken = getRequestVerificationToken();
        String transactionId = initTransaction(authorizationId, verificationToken);
        waitForTransactionAuthorization(transactionId);
    }

    private void waitForTransactionAuthorization(String transactionId) throws IOException {
        JSONObject responseJson;
        int it = 0;
        do {
            try {
                Thread.sleep(STATUS_CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Connection.Response response = Jsoup.connect(AUTHORIZATION_STATUS_URL)
                    .timeout(10 * 1000)
                    .ignoreContentType(true)
                    .followRedirects(true)
                    .cookies(cookies)
                    .data("TranId", transactionId)
                    .method(Connection.Method.POST)
                    .execute();
            responseJson = new JSONObject(response.body());
            it++;
            System.out.println("Status: " + responseJson.get("Status") + " (" + it + ")");
        } while (!responseJson.get("Status").equals("Authorized") && it < STATUS_CHECK_MAX_ITERATIONS);
    }

    private String initTransaction(String authorizationId, String verificationToken) throws IOException {
        String body = "{\"Url\":\"sca/authorization/disposable\",\"Method\":\"POST\",\"Data\":{\"ScaAuthorizationId\":\"" + authorizationId + "\"}}";

        Connection.Response response = Jsoup.connect(INIT_AUTHORIZATION_URL)
                .header("x-request-verification-token", verificationToken)
                .header("content-type", "application/json; charset=utf-8")
                .ignoreContentType(true)
                .cookies(cookies)
                .requestBody(body)
                .method(Connection.Method.POST)
                .execute();
        JSONObject ans = new JSONObject(response.body());
        System.out.println("TODO: init trans json: " + ans.toString());
        return ans.get("TranId").toString();
    }

    private String getScaAuthorizationId() throws IOException {
        Connection.Response response = Jsoup.connect(SCA_AUTHORIZATION_URL_DATA_URL)
                .ignoreContentType(true)
                .cookies(cookies)
                .method(Connection.Method.POST)
                .execute();

        JSONObject responseJson;
        String authorizationId;
        try {
            responseJson = new JSONObject(response.body());
            authorizationId = responseJson.get("ScaAuthorizationId").toString();
        } catch (JSONException ex) {
            throw new InvalidObjectException("Invalid data returned from server");
        }
        // TODO: String pewnie do stalej
        // TODO: teksty wyjatkow do stalej...?
        return authorizationId;
    }

    private String getRequestVerificationToken() throws IOException {
        Connection.Response response = Jsoup.connect(VERIFICATION_URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .cookies(cookies)
                .execute();

        JSONObject responseJson;
        String verificationToken;
        try {
            responseJson = new JSONObject(response.body());
            verificationToken = responseJson.get("antiForgeryToken").toString();
        } catch (JSONException ex) {
            throw new InvalidObjectException("Invalid data returned from server");
        }
        return verificationToken;
    }
}
