package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

class MbankAccountsGroupsResponseParserTest {
    private MbankAccountsGroupsResponseParser mbankSetupDataResponseParser;

    @Test
    void getAllAccountsOneAccountInfoReturnsOneElementCollection() {
        String body = "{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0.01,\"currency\":\"PLN\"},\"accountsGroups\":[{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0.01,\"currency\":\"PLN\"},\"header\":\"Personal\",\"accounts\":[{\"primaryAction\":{\"name\":null,\"url\":\"/pl/Accounts/Accounts/GetTransferDomestic\"},\"balance\":0.01,\"name\":\"mKonto Intensive\",\"currency\":\"PLN\",\"customName\":\"\",\"id\":\"xxxxxxxxxxxxxxxxxxxxxxxIDxxxxxxxxxxxxxxxxxxxxxxxxx\",\"accountNumber\":\"xx xxxx xxxx xxxx xxxx xxxx xxxx\"}]},{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0,\"currency\":\"PLN\"},\"header\":\"Vat\",\"accounts\":[]},{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0,\"currency\":\"PLN\"},\"header\":\"Foreigns\",\"accounts\":[]},{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0,\"currency\":\"PLN\"},\"header\":\"Authorities\",\"accounts\":[]},{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0,\"currency\":\"PLN\"},\"header\":\"Others\",\"accounts\":[]}]}";
        Connection.Response response = new ConnectionResponseMock(body);
        mbankSetupDataResponseParser = new MbankAccountsGroupsResponseParser(response);
        Collection<BankAccount> expected = new LinkedList<>();
        expected.add(new BankAccount("mKonto Intensive", 0.01));
        Collection<BankAccount> actual = mbankSetupDataResponseParser.getAllAccounts();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllAccountsZeroAccountInfosReturnsEmptyCollection() {
        String body = "{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0.00,\"currency\":\"PLN\"},\"accountsGroups\":[{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0.01,\"currency\":\"PLN\"},\"header\":\"Personal\",\"accounts\":[]},{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0,\"currency\":\"PLN\"},\"header\":\"Vat\",\"accounts\":[]},{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0,\"currency\":\"PLN\"},\"header\":\"Foreigns\",\"accounts\":[]},{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0,\"currency\":\"PLN\"},\"header\":\"Authorities\",\"accounts\":[]},{\"summary\":{\"isRoundedToOneCurrency\":false,\"balance\":0,\"currency\":\"PLN\"},\"header\":\"Others\",\"accounts\":[]}]}";
        Connection.Response response = new ConnectionResponseMock(body);
        mbankSetupDataResponseParser = new MbankAccountsGroupsResponseParser(response);
        Collection<BankAccount> bankAccounts = mbankSetupDataResponseParser.getAllAccounts();
        Assertions.assertTrue(bankAccounts.isEmpty());
    }

    @Test
    void constructorEmptyResponseThrowsException() {
        String body = "";
        Connection.Response response = new ConnectionResponseMock(body);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser = new MbankAccountsGroupsResponseParser(response));
    }
}
