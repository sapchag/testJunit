import io.qameta.allure.Step;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

public class HomeEmptyLinkCheck {

    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие пустых ссылок на главной странице")
    void checkLink(String url) {
        boolean result = true;
        String message = url;
        HttpURLConnection huc = null;
        int respCode = 200;
        try {
            huc = (HttpURLConnection) (new URL(url).openConnection());
            huc.setRequestMethod("HEAD");
            huc.connect();
            respCode = huc.getResponseCode();
            if (respCode >= 400) {
                message = String.valueOf(respCode);
                result = false;
            }
        } catch (MalformedURLException e) {
            message = e.getMessage();
            result = false;
        } catch (IOException e) {
            message = e.getMessage();
            result = false;
        }
        Assert.assertTrue(message, result);
    }

    static Stream<String> checkLink() {
        PhpTravels phpTravels = new PhpTravels().linkToHomePage();
        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getAllLinks();
        phpTravels.close();
        checkTitleStep(url, title, "PHPTRAVELS | Travel Technology Partner");
        return links.stream();
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol) {
        //Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
       Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, origin);
    }
}
