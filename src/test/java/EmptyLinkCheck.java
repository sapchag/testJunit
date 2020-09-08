import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import spec.ParametersXml;
import spec.PhpTravelBuilder;
import spec.PhpTravels;
import spec.UrlChecks;

import java.util.List;
import java.util.stream.Stream;

@Epic("Ссылки")
@Feature("Отсутствие неработающих ссылок на странице")
public class EmptyLinkCheck {

    @Issue("12")
    @Story("Администратор")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутствие неработающих ссылок на странице администратора")
    void checkLinkAdmin(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Allure.addAttachment("Траффик", "text/plain", urlChecks.getProxyLogs());
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isCheckOk());
    }

    @Issue("10")
    @Story("Домашняя")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутствие неработающих ссылок на главной странице")
    void checkLinkHome(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Allure.addAttachment("Траффик", "text/plain", urlChecks.getProxyLogs());
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isCheckOk());
    }

    @Issue("11")
    @Story("Пользователь")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутствие неработающих ссылок на странице пользователя")
    void checkLinkUser(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Allure.addAttachment("Траффик", "text/plain", urlChecks.getProxyLogs());
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isCheckOk());
    }

    static Stream<String> checkLinkAdmin() {
        return checkLink("admin");
    }

    static Stream<String> checkLinkHome() {
        return checkLink("home");
    }

    static Stream<String> checkLinkUser() {
        return checkLink("user");
    }

    static Stream<String> checkLink(String pageType) {
        PhpTravels phpTravels = PhpTravelBuilder.createLoginedPhpTravelsPage(pageType);

        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getAllLinks();
        String log = phpTravels.getProxyLogs();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle(pageType), log);
        return links.stream();
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol, String log) {
        Allure.addAttachment("Траффик", "text/plain", log);
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
    }
}
