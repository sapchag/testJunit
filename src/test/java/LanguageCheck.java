import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spec.ParametersXml;
import spec.PhpTravelBuilder;
import spec.PhpTravels;

import java.util.ArrayList;
import java.util.stream.Stream;

@Epic("Language")
@Feature("Смена языка на главной странице")
public class LanguageCheck {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Issue("8")
    @Story("Пользователь")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Смена языка на странице пользователя")
    void checkLanguageUser(String alias, String control) {
        checkLanguage("user", alias, control);
    }

    static Stream<Arguments> checkLanguageUser() {
        ArrayList<Arguments> aList = new ArrayList<Arguments>();
        ParametersXml.getNodeValues("languages").forEach((k, v) -> aList.add(Arguments.of(k, v)));
        return aList.stream();
    }

    @Issue("9")
    @Story("Домашняя")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Смена языка на главной странице")
    void checkLanguageHome(String alias, String control) {
        checkLanguage("home", alias, control);
    }

    void checkLanguage(String pageType, String alias, String control) {

        PhpTravels phpTravels = PhpTravelBuilder.createLoginedPhpTravelsPage(pageType);
        String url = phpTravels.getCurrentUrl();
        String title = phpTravels.getTitle();
        String loginLog = phpTravels.getProxyLogs();
        phpTravels.swithLanguage(alias);
        String findString = "//a[contains(@href, 'https://www.phptravels.net/supplier-register/')]";
        String result = phpTravels.getDriver()
                .findElement(By.xpath(findString)).getText();
        logger.info("Контрольное слово : " + result);
        String language = phpTravels.getLanguage();
        String languageLog = phpTravels.getProxyLogs();
        phpTravels.close();
        checkTitleStep(url, title, ParametersXml.getTitle(pageType), loginLog);
        checkLanguageStep(language, alias, result, control, languageLog);

    }

    static Stream<Arguments> checkLanguageHome() {
        ArrayList<Arguments> aList = new ArrayList<Arguments>();
        ParametersXml.getNodeValues("languages").forEach((k, v) -> aList.add(Arguments.of(k, v)));
        return aList.stream();
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol, String log) {
        Allure.addAttachment("Траффик", "text/plain", log);
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
    }

    @Step("Проверка смены языка {language}")
    @DisplayName("Проверка смены языка {language}")
    static void checkLanguageStep(String language, String alias, String origin, String conrol, String log) {
        Allure.addAttachment("Траффик", "text/plain", log);
        Assert.assertEquals("Контрольная фраза для " + language + " не соответсвует значению на странице",
                origin, conrol);
    }

}