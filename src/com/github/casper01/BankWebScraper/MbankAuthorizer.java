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
    private static final String SCA_AUTHORIZATION_DATA_URL = "https://online.mbank.pl/pl/Sca/GetScaAuthorizationData";
    private static final String VERIFICATION_URL = "https://online.mbank.pl/pl/setup/data";
    private static final String INIT_AUTHORIZATION_URL = "https://online.mbank.pl/api/auth/initprepare";
    private static final String START_FINALIZING_AUTHORIZATON_URL = "https://online.mbank.pl/api/auth/execute";
    private static final String FINALIZE_AUTHORIZATION_URL = "https://online.mbank.pl/pl/Sca/FinalizeAuthorization";
    private static final String AUTHORIZATION_STATUS_URL = "https://online.mbank.pl/api/auth/status";
    private static final int STATUS_CHECK_INTERVAL_MS = 1000;
    private static final int STATUS_CHECK_MAX_ITERATIONS = 300;
    private Map<String, String> cookies = new HashMap<>();


    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    void authorizeByPhone() throws IOException {
        String authorizationId = getScaAuthorizationId();
        String verificationToken = getRequestVerificationToken();
        String transactionId = initTransaction(authorizationId, verificationToken);
        waitForTransactionAuthorization(transactionId);
        startFinalizingTransaction(verificationToken);
        finalizeAuthorization(authorizationId, verificationToken);
    }

    private void startFinalizingTransaction(String token) throws IOException {
        Connection.Response conn = Jsoup.connect(START_FINALIZING_AUTHORIZATON_URL)
                .header("x-request-verification-token", token)
                .header("content-type", "application/json")
                .requestBody("{}")
                .ignoreContentType(true)
                .cookies(cookies)
                .method(Connection.Method.POST)
                .execute();
    }

    private void waitForTransactionAuthorization(String transactionId) throws IOException {
        JSONObject responseJson;
        int iteration = 0;
        do {
            try {
                Thread.sleep(STATUS_CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Connection.Response response = Jsoup.connect(AUTHORIZATION_STATUS_URL)
                    .ignoreContentType(true)
                    .cookies(cookies)
                    .data("TranId", transactionId)
                    .method(Connection.Method.POST)
                    .execute();
            responseJson = new JSONObject(response.body());
            iteration++;
            System.out.println("Status: " + responseJson.get("Status") + " (" + iteration + ")");
        } while (iteration < STATUS_CHECK_MAX_ITERATIONS && (responseJson.get("Status").equals("Prepared") || responseJson.get("Status").equals("PreAuthorized")));
        if (!responseJson.get("Status").equals("Authorized")) {
            throw new InvalidObjectException("Could not authenticate");
        }
    }

    private void finalizeAuthorization(String authorizationId, String token) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("scaAuthorizationId", authorizationId);

        Connection.Response response = Jsoup.connect(FINALIZE_AUTHORIZATION_URL)
                .header("content-type", "application/json")
                .header("x-request-verification-token", token)
                .requestBody(requestBody.toString())
                .ignoreContentType(true)
                .cookies(cookies)
                .method(Connection.Method.POST)
                .execute();
        cookies = response.cookies();
    }

    private String initTransaction(String authorizationId, String verificationToken) throws IOException {
        String requestBody = getInitTransactionBody(authorizationId);

        Connection.Response response = Jsoup.connect(INIT_AUTHORIZATION_URL)
                .header("x-request-verification-token", verificationToken)
                .header("content-type", "application/json")
                .ignoreContentType(true)
                .cookies(cookies)
                .requestBody(requestBody)
                .method(Connection.Method.POST)
                .execute();
        JSONObject responseJson;
        String transactionId;
        try {
            responseJson = new JSONObject(response.body());
            transactionId = responseJson.get("TranId").toString();
        } catch (JSONException ex) {
            throw new InvalidObjectException("Invalid data returned from server");
        }
        System.out.println("TODO: init trans json: " + responseJson.toString());
        return transactionId;
    }

    private String getInitTransactionBody(String authorizationId) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("Url", "sca/authorization/disposable");
        requestBody.put("Method", "POST");

        JSONObject dataBody = new JSONObject();
        dataBody.put("ScaAuthorizationId", authorizationId);
        requestBody.put("Data", dataBody);

        return requestBody.toString();
    }

    private String getScaAuthorizationId() throws IOException {
        Connection.Response response = Jsoup.connect(SCA_AUTHORIZATION_DATA_URL)
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
