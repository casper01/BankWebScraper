package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MbankGetScaAuthorizationDataResponseParserTest {
    private MbankGetScaAuthorizationDataResponseParser mbankSetupDataResponseParser;

    @Test
    void getScaAuthorizationId_ExistingScaAuthorizationId_ReturnsToken() {
        String body = "{\"TrustedDeviceAddingAllowed\":false,\"ScaAuthorizationId\":\"7FMvaX7DDB8PnRixxxxxxxxxxxxxxxxxxxxxxxxxxxx1aHvcq24Qf3tS4gAEbDUO\"}";
        Connection.Response response = new ConnectionResponseMock(body);
        mbankSetupDataResponseParser = new MbankGetScaAuthorizationDataResponseParser(response);
        String expected = "7FMvaX7DDB8PnRixxxxxxxxxxxxxxxxxxxxxxxxxxxx1aHvcq24Qf3tS4gAEbDUO";
        String actual = mbankSetupDataResponseParser.getScaAuthorizationId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getScaAuthorizationId_NotExistingScaAuthorizationId_ThrowsException() {
        String body = "{\"TrustedDeviceAddingAllowed\":false,\"DifferentThanScaAuthorizationId\":\"7FMvaX7DDB8PnRixxxxxxxxxxxxxxxxxxxxxxxxxxxx1aHvcq24Qf3tS4gAEbDUO\"}";
        Connection.Response response = new ConnectionResponseMock(body);
        mbankSetupDataResponseParser = new MbankGetScaAuthorizationDataResponseParser(response);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser.getScaAuthorizationId());
    }

    @Test
    void constructor_EmptyResponse_ThrowsException() {
        String body = "";
        Connection.Response response = new ConnectionResponseMock(body);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser = new MbankGetScaAuthorizationDataResponseParser(response));
    }
}
