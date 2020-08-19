package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MbankJsonLoginResponseParserTest {
    private MbankJsonLoginResponseParser mbankJsonLoginResponseParser;

    @Test
    void constructorEmptyResponseThrowsException() {
        String body = "";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        Assertions.assertThrows(WebScraperException.class, () -> mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response));
    }

    @Test
    void isSuccessfulExistingStatusSuccessfulReturnsTrue() {
        String body = "{\"tabId\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"betaTestingApproval\":false,\"isUserAccountBlocked\":false,\"redirectUrl\":\"/authorization\",\"allAprovalsSaved\":false,\"implicitTestingApproval\":false,\"errorMessageBody\":\"\",\"errorMessageTitle\":null,\"button\":false,\"regulationsApproval\":false,\"isActivationPackageBlocked\":false,\"behaviour2Enabled\":false,\"sessionKeyForUW\":null,\"successful\":true}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        boolean status = mbankJsonLoginResponseParser.isSuccessful();
        Assertions.assertTrue(status);
    }

    @Test
    void isSuccessfulExistingStatusNotSuccessfulReturnsFalse() {
        String body = "{\"tabId\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"betaTestingApproval\":false,\"isUserAccountBlocked\":false,\"redirectUrl\":\"/authorization\",\"allAprovalsSaved\":false,\"implicitTestingApproval\":false,\"errorMessageBody\":\"\",\"errorMessageTitle\":null,\"button\":false,\"regulationsApproval\":false,\"isActivationPackageBlocked\":false,\"behaviour2Enabled\":false,\"sessionKeyForUW\":null,\"successful\":false}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        boolean status = mbankJsonLoginResponseParser.isSuccessful();
        Assertions.assertFalse(status);
    }

    @Test
    void isSuccessful_NotExistingStatus_ThrowsException() {
        String body = "{\"tabId\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"betaTestingApproval\":false,\"isUserAccountBlocked\":false,\"redirectUrl\":\"/authorization\",\"allAprovalsSaved\":false,\"implicitTestingApproval\":false,\"errorMessageBody\":\"\",\"errorMessageTitle\":null,\"button\":false,\"regulationsApproval\":false,\"isActivationPackageBlocked\":false,\"behaviour2Enabled\":false,\"sessionKeyForUW\":null}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        Assertions.assertThrows(WebScraperException.class, () -> mbankJsonLoginResponseParser.isSuccessful());
    }

    @Test
    void hasErrorMessage_MessageExists_ReturnsTrue() {
        String body = "{\"tabId\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"betaTestingApproval\":false,\"isUserAccountBlocked\":false,\"redirectUrl\":\"/authorization\",\"allAprovalsSaved\":false,\"implicitTestingApproval\":false,\"errorMessageBody\":\"Error message\",\"errorMessageTitle\":null,\"button\":false,\"regulationsApproval\":false,\"isActivationPackageBlocked\":false,\"behaviour2Enabled\":false,\"sessionKeyForUW\":null,\"successful\":false}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        boolean hasErrorMessage = mbankJsonLoginResponseParser.hasErrorMessage();
        Assertions.assertTrue(hasErrorMessage);
    }

    @Test
    void hasErrorMessage_MessageNotExists_ReturnsFalse() {
        String body = "{\"tabId\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"betaTestingApproval\":false,\"isUserAccountBlocked\":false,\"redirectUrl\":\"/authorization\",\"allAprovalsSaved\":false,\"implicitTestingApproval\":false,\"errorMessageTitle\":null,\"button\":false,\"regulationsApproval\":false,\"isActivationPackageBlocked\":false,\"behaviour2Enabled\":false,\"sessionKeyForUW\":null,\"successful\":false}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        boolean hasErrorMessage = mbankJsonLoginResponseParser.hasErrorMessage();
        Assertions.assertFalse(hasErrorMessage);
    }

    @Test
    void getErrorMessage_MessageExists_ReturnsMessage() {
        String body = "{\"tabId\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"betaTestingApproval\":false,\"isUserAccountBlocked\":false,\"redirectUrl\":\"/authorization\",\"allAprovalsSaved\":false,\"implicitTestingApproval\":false,\"errorMessageBody\":\"Custom error message\",\"errorMessageTitle\":null,\"button\":false,\"regulationsApproval\":false,\"isActivationPackageBlocked\":false,\"behaviour2Enabled\":false,\"sessionKeyForUW\":null,\"successful\":false}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        String expected = "Custom error message";
        String actual = mbankJsonLoginResponseParser.getErrorMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getErrorMessage_MessageNotExists_ThrowsException() {
        String body = "{\"tabId\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"betaTestingApproval\":false,\"isUserAccountBlocked\":false,\"redirectUrl\":\"/authorization\",\"allAprovalsSaved\":false,\"implicitTestingApproval\":false,\"errorMessageTitle\":null,\"button\":false,\"regulationsApproval\":false,\"isActivationPackageBlocked\":false,\"behaviour2Enabled\":false,\"sessionKeyForUW\":null,\"successful\":false}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankJsonLoginResponseParser = new MbankJsonLoginResponseParser(response);
        Assertions.assertThrows(WebScraperException.class, () -> mbankJsonLoginResponseParser.getErrorMessage());
    }
}
