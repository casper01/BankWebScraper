package com.github.casper01.BankWebScraper.apiManagers;

import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import com.github.casper01.BankWebScraper.parsers.MbankGetScaAuthorizationDataResponseParser;
import com.github.casper01.BankWebScraper.parsers.MbankInitprepareResponseParser;
import com.github.casper01.BankWebScraper.parsers.MbankSetupDataResponseParser;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MbankAuthorizer extends MbankApiManager {
    private static final String SCA_AUTHORIZATION_DATA_URL = getBaseUrl() + "/pl/Sca/GetScaAuthorizationData";
    private static final String VERIFICATION_URL = getBaseUrl() + "/pl/setup/data";
    private static final String INIT_AUTHORIZATION_URL = getBaseUrl() + "/api/auth/initprepare";
    private static final String START_FINALIZING_AUTHORIZATON_URL = getBaseUrl() + "/api/auth/execute";
    private static final String FINALIZE_AUTHORIZATION_URL = getBaseUrl() + "/pl/Sca/FinalizeAuthorization";
    private static final String AUTHORIZATION_STATUS_URL = getBaseUrl() + "/api/auth/status";
    private static final String AUTHORIZE_ERROR_MESSAGE = "Unable to authorize";
    private static final int STATUS_CHECK_INTERVAL_MS = 1000;
    private static final int STATUS_CHECK_MAX_ITERATIONS = 300;
    private Map<String, String> cookies = new HashMap<>();
    private boolean isAuthorized = false;


    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void authorizeByPhone() {
        String authorizationId = getScaAuthorizationId();
        String verificationToken = getRequestVerificationToken();
        String transactionId = initTransaction(authorizationId, verificationToken);
        waitForTransactionAuthorization(transactionId);
        startFinalizingTransaction(verificationToken);
        finalizeAuthorization(authorizationId, verificationToken);
        isAuthorized = true;
    }

    private String getScaAuthorizationId() {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(SCA_AUTHORIZATION_DATA_URL)
                    .ignoreContentType(true)
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e) {
            throw new WebScraperException(AUTHORIZE_ERROR_MESSAGE);
        }
        MbankGetScaAuthorizationDataResponseParser mbankGetScaAuthorizationDataResponseParser = new MbankGetScaAuthorizationDataResponseParser(response);
        return mbankGetScaAuthorizationDataResponseParser.getScaAuthorizationId();
    }

    private String getRequestVerificationToken() {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(VERIFICATION_URL)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .cookies(cookies)
                    .execute();
        } catch (IOException e) {
            throw new WebScraperException(AUTHORIZE_ERROR_MESSAGE);
        }
        MbankSetupDataResponseParser mbankSetupDataResponseParser = new MbankSetupDataResponseParser(response);
        return mbankSetupDataResponseParser.getAntiForgeryToken();
    }

    private String initTransaction(String authorizationId, String verificationToken) {
        String requestBody = getInitTransactionBody(authorizationId);
        Connection.Response response = null;
        try {
            response = Jsoup.connect(INIT_AUTHORIZATION_URL)
                    .header("x-request-verification-token", verificationToken)
                    .header("content-type", "application/json")
                    .ignoreContentType(true)
                    .cookies(cookies)
                    .requestBody(requestBody)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e) {
            throw new WebScraperException(AUTHORIZE_ERROR_MESSAGE);
        }
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

    private void waitForTransactionAuthorization(String transactionId) {
        JSONObject responseJson;
        int iteration = 0;
        String transactionStatus;
        do {
            try {
                Thread.sleep(STATUS_CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Connection.Response response = null;
            try {
                response = Jsoup.connect(AUTHORIZATION_STATUS_URL)
                        .ignoreContentType(true)
                        .cookies(cookies)
                        .data("TranId", transactionId)
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                throw new WebScraperException(AUTHORIZE_ERROR_MESSAGE);
            }
            responseJson = new JSONObject(response.body());
            transactionStatus = responseJson.get("Status").toString();
            iteration++;
        } while (iteration < STATUS_CHECK_MAX_ITERATIONS && (transactionStatus.equals("Prepared") || transactionStatus.equals("PreAuthorized")));
        if (!transactionStatus.equals("Authorized")) {
            throw new WebScraperException("Could not authenticate");
        }
    }

    private void startFinalizingTransaction(String token) {
        try {
            Connection.Response conn = Jsoup.connect(START_FINALIZING_AUTHORIZATON_URL)
                    .header("x-request-verification-token", token)
                    .header("content-type", "application/json")
                    .requestBody("{}")
                    .ignoreContentType(true)
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e) {
            throw new WebScraperException(AUTHORIZE_ERROR_MESSAGE);
        }
    }

    private void finalizeAuthorization(String authorizationId, String token) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("scaAuthorizationId", authorizationId);
        Connection.Response response = null;
        try {
            response = Jsoup.connect(FINALIZE_AUTHORIZATION_URL)
                    .header("content-type", "application/json")
                    .header("x-request-verification-token", token)
                    .requestBody(requestBody.toString())
                    .ignoreContentType(true)
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e) {
            throw new WebScraperException(AUTHORIZE_ERROR_MESSAGE);
        }
        cookies = response.cookies();
    }
}
