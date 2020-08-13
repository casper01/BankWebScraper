package com.github.casper01.BankWebScraper;

import org.jsoup.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MbankSetupDataResponseParserTest {
    private MbankSetupDataResponseParser mbankSetupDataResponseParser;

    @Test
    void getAntiForgeryTokenExistingTokenReturnsToken() {
        String body = "{\"localization\":{\"selectedLanguage\":\"pl\",\"defaultLanguage\":\"pl\",\"languages\":[\"pl\"],\"veneziaCultureUrlPrefix\":\"pl\"},\"antiForgeryToken\":\"NWXk2T/zQPFEU2m6s7t41DUrXzez+KpzKL2JgeFSexBg5eb8SzK5Q2wWsu1k7cCY6Im4nAsXg3Q6RUArWrOvz9L0lm86PArWQyqbe90X3xWGIiQAZHuRIslz0v3pE4pqonhpI4R/sL124VuYM+AA7SXc39mAoGYgIaxM54dLHKMy112I7FLpTVqHe6/1Qxy5326Fks/GZL2zw3cI7UNu3jW6+nUB0O+Ii77L5eDw3Zd4UAhrJc5g++OFd/Bt\",\"configuration\":{\"sessionTimer\":{\"timeLeftToShowAlert\":60,\"sessionTimeout\":300}},\"profile\":{\"name\":\"\",\"type\":0,\"isPb\":false},\"flags\":{\"AllowTranslations\":false,\"AllowDemo\":false},\"veneziaCultureUrlPrefix\":null,\"tracker\":{\"trackerKey\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"cdnAddress\":\"//cdn.skp.mbank.pl/sdk/synerise-javascript-sdk-no-m-cm-wss.min.js\"},\"entity\":\"2020\",\"customer\":{\"consents\":{\"PR2\":true,\"REK\":true,\"PR1\":true},\"name\":\"XYZ XYZ\"}}";
        Connection.Response response = new ConnectionResponseMock(body);
        mbankSetupDataResponseParser = new MbankSetupDataResponseParser(response);
        String expected = "NWXk2T/zQPFEU2m6s7t41DUrXzez+KpzKL2JgeFSexBg5eb8SzK5Q2wWsu1k7cCY6Im4nAsXg3Q6RUArWrOvz9L0lm86PArWQyqbe90X3xWGIiQAZHuRIslz0v3pE4pqonhpI4R/sL124VuYM+AA7SXc39mAoGYgIaxM54dLHKMy112I7FLpTVqHe6/1Qxy5326Fks/GZL2zw3cI7UNu3jW6+nUB0O+Ii77L5eDw3Zd4UAhrJc5g++OFd/Bt";
        String actual = mbankSetupDataResponseParser.getAntiForgeryToken();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAntiForgeryTokenNotExistingTokenThrowsException() {
        String body = "{\"localization\":{\"selectedLanguage\":\"pl\",\"defaultLanguage\":\"pl\",\"languages\":[\"pl\"],\"veneziaCultureUrlPrefix\":\"pl\"},\"token\":\"NWXk2T/zQPFEU2m6s7t41DUrXzez+KpzKL2JgeFSexBg5eb8SzK5Q2wWsu1k7cCY6Im4nAsXg3Q6RUArWrOvz9L0lm86PArWQyqbe90X3xWGIiQAZHuRIslz0v3pE4pqonhpI4R/sL124VuYM+AA7SXc39mAoGYgIaxM54dLHKMy112I7FLpTVqHe6/1Qxy5326Fks/GZL2zw3cI7UNu3jW6+nUB0O+Ii77L5eDw3Zd4UAhrJc5g++OFd/Bt\",\"configuration\":{\"sessionTimer\":{\"timeLeftToShowAlert\":60,\"sessionTimeout\":300}},\"profile\":{\"name\":\"\",\"type\":0,\"isPb\":false},\"flags\":{\"AllowTranslations\":false,\"AllowDemo\":false},\"veneziaCultureUrlPrefix\":null,\"tracker\":{\"trackerKey\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\",\"cdnAddress\":\"//cdn.skp.mbank.pl/sdk/synerise-javascript-sdk-no-m-cm-wss.min.js\"},\"entity\":\"2020\",\"customer\":{\"consents\":{\"PR2\":true,\"REK\":true,\"PR1\":true},\"name\":\"XYZ XYZ\"}}";
        Connection.Response response = new ConnectionResponseMock(body);
        mbankSetupDataResponseParser = new MbankSetupDataResponseParser(response);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser.getAntiForgeryToken());
    }

    @Test
    void constructorEmptyResponseThrowsException() {
        String body = "";
        Connection.Response response = new ConnectionResponseMock(body);
        Assertions.assertThrows(WebScraperException.class, () -> mbankSetupDataResponseParser = new MbankSetupDataResponseParser(response));
    }
}
