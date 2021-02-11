import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import spec.ParametersXml;
import spec.PhpTravels;
import spec.UrlChecks;

import java.util.List;
import java.util.stream.Stream;

@Epic("Изображения")
@Feature("Отсутвие битых изображений на странице")
public class TestBrokenImageLinkCheck {

    @Issue("18")
    @Story("Администратор")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие битых изображений на странице администратора")
    void checkLinkAdmin(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Allure.addAttachment("Траффик", "text/plain", urlChecks.getProxyLogs());
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isCheckOk());
    }

    @Issue("17")
    @Story("Пользователь")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие битых изображений на странице пользователя")
    void checkLinkUser(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Allure.addAttachment("Траффик", "text/plain", urlChecks.getProxyLogs());
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isCheckOk());
    }

    @Issue("16")
    @Story("Домашняя")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие битых изображений на домашней странице")
    void checkLinkHome(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Allure.addAttachment("Траффик", "text/plain", urlChecks.getProxyLogs());
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isCheckOk());
    }

    static Stream<String> checkLink(String pageType) {
        PhpTravels phpTravels = PhpTravels.createLoginedPhpTravelsPage(pageType);

        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getImageLinks();
        String log = phpTravels.getProxyLogs();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle(pageType), log);
        return links.stream();
    }

    static Stream<String> checkLinkAdmin() {
        return checkLink("admin");
    }

    static Stream<String> checkLinkUser() {
        return checkLink("user");
    }

    static Stream<String> checkLinkHome() {
        return checkLink("home");
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol, String log) {
        Allure.addAttachment("Траффик", "text/plain", log);
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
    }
}
