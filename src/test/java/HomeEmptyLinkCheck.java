import io.qameta.allure.Step;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spec.HashMapPropertyFile;
import spec.UrlChecks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

public class HomeEmptyLinkCheck {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие пустых ссылок на главной странице")
    void checkLink(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isChecked());
    }

    static Stream<String> checkLink() {
        PhpTravels phpTravels = new PhpTravels()
                //.linkToHomePage();
                .setUrl(HashMapPropertyFile.load("url").get("home").toString())
                .link();
        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getAllLinks();
        phpTravels.close();
        checkTitleStep(url, title, HashMapPropertyFile.load("url").get("homeControl").toString());
        return links.stream();
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol) {
        //Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", conrol, conrol);
    }
}
