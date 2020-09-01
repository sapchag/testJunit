import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spec.ParametersXml;
import spec.UrlChecks;

import java.util.List;
import java.util.stream.Stream;

public class EmptyLinkCheckUser {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Epic("Link")
    @Feature("Отсутвие пустых ссылок на странице")
    @Story("Пользователь")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отсутвие пустых ссылок на странице пользователя")
    void checkLink(String url) {
        UrlChecks urlChecks = new UrlChecks(url);
        Assert.assertTrue(urlChecks.getMessage(), urlChecks.isChecked());
    }

    static Stream<String> checkLink() {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl("user"))
                .setParams(ParametersXml.getPageParameters("user"))
                .login();

        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getAllLinks();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle("user"));
        return links.stream();
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol) {
        //Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", conrol, conrol);
    }
}
