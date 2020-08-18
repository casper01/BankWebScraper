package com.github.casper01.BankWebScraper;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
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
        String transactionStatus;
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
            transactionStatus = responseJson.get("Status").toString();
            iteration++;
        } while (iteration < STATUS_CHECK_MAX_ITERATIONS && (transactionStatus.equals("Prepared") || transactionStatus.equals("PreAuthorized")));
        if (!transactionStatus.equals("Authorized")) {
            throw new WebScraperException("Could not authenticate");
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
        MbankInitprepareResponseParser mbankInitprepareResponseParser = new MbankInitprepareResponseParser(response);
        return mbankInitprepareResponseParser.getTransactionId();
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
        MbankGetScaAuthorizationDataResponseParser mbankGetScaAuthorizationDataResponseParser = new MbankGetScaAuthorizationDataResponseParser(response);
        return mbankGetScaAuthorizationDataResponseParser.getScaAuthorizationId();
    }

    private String getRequestVerificationToken() throws IOException {
        Connection.Response response = Jsoup.connect(VERIFICATION_URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .cookies(cookies)
                .execute();
        MbankSetupDataResponseParser mbankSetupDataResponseParser = new MbankSetupDataResponseParser(response);
        return mbankSetupDataResponseParser.getAntiForgeryToken();
    }
}
