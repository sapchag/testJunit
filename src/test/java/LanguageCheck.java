import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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

@TestInstance(Lifecycle.PER_CLASS)
public class LanguageCheck {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    PhpTravels phpTravelsHome;
    PhpTravels phpTravelsUser;

    @BeforeAll
    void initAll() {
        phpTravelsHome = PhpTravelBuilder.createPhpTravelsPage("home");
        checkTitleStep(phpTravelsHome.getCurrentUrl(), phpTravelsHome.getTitle(),
                ParametersXml.getTitle("home"));
        phpTravelsUser = PhpTravelBuilder.createLoginedPhpTravelsPage("user");
        checkTitleStep(phpTravelsUser.getCurrentUrl(), phpTravelsUser.getTitle(),
                ParametersXml.getTitle("user"));
    }

    @AfterAll
    void tearDownAll() {
        phpTravelsHome.close();
        phpTravelsUser.close();
    }

    @Epic("Language")
    @Feature("Смена языка на главной странице")
    @Story("Пользователь")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Смена языка на странице пользователя")
    void checkLanguageUser(String alias, String control) {
        phpTravelsUser.swithLanguage(alias);
        String findString = "//a[contains(@href, 'https://www.phptravels.net/supplier-register/')]";
        String result = phpTravelsUser.getDriver()
                .findElement(By.xpath(findString)).getText();
        logger.info("Контрольное слово : " + result);
        checkLanguageStep(phpTravelsUser.getLanguage(), alias, control, result);
    }

    static Stream<Arguments> checkLanguageUser() {
        ArrayList<Arguments> aList = new ArrayList<Arguments>();
        ParametersXml.getNodeValues("languages").forEach((k, v) -> aList.add(Arguments.of(k, v)));
        return aList.stream();
    }

    @Epic("Language")
    @Feature("Смена языка на главной странице")
    @Story("Домашняя")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Смена языка на главной странице")
    void checkLanguageHome(String alias, String control) {
        phpTravelsHome.swithLanguage(alias);
        String findString = "//a[contains(@href, 'https://www.phptravels.net/supplier-register/')]";
        String result = phpTravelsHome.getDriver()
                .findElement(By.xpath(findString)).getText();
        logger.info("Контрольное слово : " + result);
        checkLanguageStep(phpTravelsHome.getLanguage(), alias, control, result);
    }

    static Stream<Arguments> checkLanguageHome() {
        ArrayList<Arguments> aList = new ArrayList<Arguments>();
        ParametersXml.getNodeValues("languages").forEach((k, v) -> aList.add(Arguments.of(k, v)));
        return aList.stream();
    }

    @Step("Проверка заголовка страницы {url}")
    @DisplayName("Проверка заголовка страницы {url}")
    static void checkTitleStep(String url, String origin, String conrol) {
        Assert.assertEquals("Заголовк страницы " + url + " отличается от контрольного значения", origin, conrol);
    }

    @Step("Проверка смены языка {language}")
    @DisplayName("Проверка смены языка {language}")
    static void checkLanguageStep(String language, String alias, String origin, String conrol) {
        Assert.assertEquals("Контрольная фраза для " + language + " не соответсвует значению на странице",
                origin, conrol);
    }
}