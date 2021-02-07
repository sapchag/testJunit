import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import spec.ParametersXml;
import spec.PhpTravels;

import java.util.ArrayList;
import java.util.stream.Stream;

import static spec.PhpTravels.BROWSER;


@Epic("Browser")
@Feature("Работоспособность браузера")
public class TestBrowserCheck {

    @Issue("19")
    @Story("Браузер")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("Проверка работоспособности браузера {browser}")
    void openBrowser(String browser) {
        Assert.assertEquals(openAndGetTitleBrowser(browser), ParametersXml.getTitle("home"));
    }

    /**
     * Метод являющийся поставщиком данных для openBrowser
     *
     * @return поток из названий браузера
     */
    static Stream<String> openBrowser() {
        ArrayList<String> aList = new ArrayList<>();
        ParametersXml.getNodeValues("browsers").forEach((k, v) -> aList.add(k));
        return aList.stream();
    }

    /**
     * Запускает браузер указанный в параметре и возвращает строку заголовока
     *
     * @param browserName - имя браузера
     * @return заголовок страницы
     */
    static String openAndGetTitleBrowser(String browserName) {
        BROWSER browser = BROWSER.valueOf(browserName);
        PhpTravels phpTravels = new PhpTravels(browser)
                .setUrl(ParametersXml.getUrl("home"))
                .link();
        Allure.addAttachment("Траффик", "text/plain", phpTravels.getProxyLogs());
        String title = phpTravels.getTitle();
        phpTravels.close();
        return title;
    }
}
