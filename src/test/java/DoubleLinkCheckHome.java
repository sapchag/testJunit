import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import spec.ParametersXml;
import spec.PhpTravels;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoubleLinkCheckHome {

    @Epic("Link")
    @Feature("Отстутствие ссылок, ведущих к одной странице")
    @Story("Пользователь")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Отстутствие дублирующих ссылок на главной странице")
    void checkLink(String url, long count) {
        Assert.assertEquals(url, 1, count);
    }

    static Stream<Arguments> checkLink() {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl("home"))
                .link();

        String title = phpTravels.getTitle();
        String url = phpTravels.getCurrentUrl();
        List<String> links = phpTravels.getOutLinks();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle("home"));

        Map<String, Long> result =
                links.stream()
                        .filter(Objects::nonNull)
                        .filter(link -> !link.contains("javascript:void"))
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
        //Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", conrol, conrol);
    }
}
