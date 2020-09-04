import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import spec.ParametersXml;
import spec.UrlChecks;

import java.util.List;
import java.util.stream.Stream;

public class BrokenImageLinkCheck {

    @Epic("Link")
    @Feature("Отсутвие битых изображений на странице")
    @Story("Администратор")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие битых изображений на странице администратора")
    void checkLinkAdmin(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isChecked());
    }

    @Epic("Link")
    @Feature("Отсутвие битых изображений на странице")
    @Story("Пользователь")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие битых изображений на странице пользователя")
    void checkLinkUser(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isChecked());
    }

    @Epic("Link")
    @Feature("Отсутвие битых изображений на странице")
    @Story("Домашняя")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие битых изображений на домашней странице")
    void checkLinkHome(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isChecked());
    }

    static Stream<String> checkLinkAdmin() {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl("admin"))
                .setParams(ParametersXml.getPageParameters("admin"))
                .login();

        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getImageLinks();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle("admin"));
        return links.stream();
    }

    static Stream<String> checkLinkUser() {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl("user"))
                .setParams(ParametersXml.getPageParameters("user"))
                .login();

        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getImageLinks();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle("user"));
        return links.stream();
    }

    static Stream<String> checkLinkHome() {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl("home"))
                .link();

        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getImageLinks();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle("home"));
        return links.stream();
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol) {
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
    }
}
