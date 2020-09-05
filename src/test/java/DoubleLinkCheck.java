import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import spec.ParametersXml;
import spec.PhpTravelBuilder;
import spec.PhpTravels;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Epic("Ссылки")
@Feature("Отстутствие ссылок, ведущих к одной странице")
public class DoubleLinkCheck {

    @Issue("15")
    @Story("Администратор")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отстутствие дублирующих ссылок на странице администратора")
    void checkLinkAdmin(String url, long count) {
        Assert.assertEquals(url, 1, count);
    }

    @Issue("14")
    @Story("Пользователь")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отстутствие дублирующих ссылок на главной странице")
    void checkLinkUser(String url, long count) {
        Assert.assertEquals(url, 1, count);
    }

    @Issue("13")
    @Story("Домашняя")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отстутствие дублирующих ссылок на странице пользователя")
    void checkLinkHome(String url, long count) {
        Assert.assertEquals(url, 1, count);
    }

    static Stream<Arguments> checkLinkAdmin() {
        return checkLink("admin");
    }

    static Stream<Arguments> checkLinkUser() {
        return checkLink("user");
    }

    static Stream<Arguments> checkLinkHome() {
        return checkLink("home");
    }

    static Stream<Arguments> checkLink(String pageType) {
        PhpTravels phpTravels = PhpTravelBuilder.createLoginedPhpTravelsPage(pageType);

        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getAllLinks();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle(pageType));

        Map<String, Long> result =
                links.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Function.identity(), Collectors.counting()
                                )
                        );

        return result.entrySet().stream().map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol) {
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
    }
}
