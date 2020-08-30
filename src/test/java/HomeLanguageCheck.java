import io.qameta.allure.Step;
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
import spec.HashMapPropertyFile;

import java.util.*;
import java.util.stream.Stream;

@TestInstance(Lifecycle.PER_CLASS)
public class HomeLanguageCheck {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    PhpTravels phpTravels;

    @BeforeAll
    void initAll() {
        phpTravels = new PhpTravels()
                .setUrl(HashMapPropertyFile.load("url").get("home").toString())
                .link();
        checkTitleStep(phpTravels.getCurrentUrl(), phpTravels.getTitle(),
                HashMapPropertyFile.load("url").get("homeControl").toString());
    }

    @AfterAll
    void tearDownAll() {
        phpTravels.close();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Смена языка на главной странице")
    void checkLanguage(String alias, String control) {
        phpTravels.swithLanguage(alias);
        String findString = "//a[contains(@href, 'https://www.phptravels.net/supplier-register/')]";
        String result = phpTravels.getDriver()
                .findElement(By.xpath(findString)).getText();
        logger.info("Контрольное слово : " + result);
        checkLanguageStep(phpTravels.getLanguage(), alias, control, result);
    }

    static Stream<Arguments> checkLanguage() {
        ArrayList<Arguments> aList = new ArrayList<Arguments>();
        HashMapPropertyFile.load("languages").forEach((k, v) ->aList.add(Arguments.of(k,v)));
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