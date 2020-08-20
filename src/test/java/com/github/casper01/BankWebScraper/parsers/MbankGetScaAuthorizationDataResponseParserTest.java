package com.github.casper01.BankWebScraper.parsers;

import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import org.jsoup.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MbankGetScaAuthorizationDataResponseParserTest {
    private MbankGetScaAuthorizationDataResponseParser mbankSetupDataResponseParser;

    @Test
    void getScaAuthorizationIdExistingScaAuthorizationIdReturnsToken() {
        String body = "{\"TrustedDeviceAddingAllowed\":false,\"ScaAuthorizationId\":\"7FMvaX7DDB8PnRixxxxxxxxxxxxxxxxxxxxxxxxxxxx1aHvcq24Qf3tS4gAEbDUO\"}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankSetupDataResponseParser = new MbankGetScaAuthorizationDataResponseParser(response);
        String expected = "7FMvaX7DDB8PnRixxxxxxxxxxxxxxxxxxxxxxxxxxxx1aHvcq24Qf3tS4gAEbDUO";
        String actual = mbankSetupDataResponseParser.getScaAuthorizationId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getScaAuthorizationIdNotExistingScaAuthorizationIdThrowsException() {
        String body = "{\"TrustedDeviceAddingAllowed\":false,\"DifferentThanScaAuthorizationId\":\"7FMvaX7DDB8PnRixxxxxxxxxxxxxxxxxxxxxxxxxxxx1aHvcq24Qf3tS4gAEbDUO\"}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankSetupDataResponseParser = new MbankGetScaAuthorizationDataResponseParser(response);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser.getScaAuthorizationId());
    }

    @Test
    void constructorEmptyResponseThrowsException() {
        String body = "";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser = new MbankGetScaAuthorizationDataResponseParser(response));
    }
}
