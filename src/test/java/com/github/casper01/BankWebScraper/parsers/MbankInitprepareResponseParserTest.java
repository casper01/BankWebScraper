package com.github.casper01.BankWebScraper.parsers;

import com.github.casper01.BankWebScraper.exceptions.WebScraperException;
import org.jsoup.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MbankInitprepareResponseParserTest {
    private MbankInitprepareResponseParser mbankSetupDataResponseParser;

    @Test
    void getTransactionIdExistingTransactionIdReturnsToken() {
        String body = "{\"Status\":\"Prepared\",\"AuthData\":null,\"Message\":\"\",\"OperationDate\":\"2020-08-13T15:42:51.88\",\"OperationNumber\":8,\"Data\":null,\"TranId\":\"85r3g7fg-e3a6-xxxx-9211-fd6898ff36f7\",\"MultiDevice\":false,\"ListNumber\":null,\"StatusCheckInterval\":1000,\"TanNumber\":0,\"ErrorCode\":\"\",\"AuthMode\":\"NAM\",\"Pending\":false,\"DeviceName\":\"XXX_XXX_0\"}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankSetupDataResponseParser = new MbankInitprepareResponseParser(response);
        String expected = "85r3g7fg-e3a6-xxxx-9211-fd6898ff36f7";
        String actual = mbankSetupDataResponseParser.getTransactionId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getTransactionIdNotExistingTransactionIdThrowsException() {
        String body = "{\"Status\":\"Prepared\",\"AuthData\":null,\"Message\":\"\",\"OperationDate\":\"2020-08-13T15:42:51.88\",\"OperationNumber\":8,\"Data\":null,\"MultiDevice\":false,\"ListNumber\":null,\"StatusCheckInterval\":1000,\"TanNumber\":0,\"ErrorCode\":\"\",\"AuthMode\":\"NAM\",\"Pending\":false,\"DeviceName\":\"XXX_XXX_0\"}";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        mbankSetupDataResponseParser = new MbankInitprepareResponseParser(response);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser.getTransactionId());
    }

    @Test
    void constructorEmptyResponseThrowsException() {
        String body = "";
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(body);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser = new MbankInitprepareResponseParser(response));
    }

}
