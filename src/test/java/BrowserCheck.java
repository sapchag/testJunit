import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import spec.ParametersXml;
import spec.PhpTravels;
import static spec.PhpTravels.BROWSER;
import static spec.PhpTravels.BROWSER.*;

@Epic("Browser")
@Feature("Работа браузера")
@Execution(ExecutionMode.CONCURRENT)
public class BrowserCheck {

    @Issue("19")
    @Story("CHROME")
    @Test
    @DisplayName("Работоспособность браузера CHROME")
    void openChrome() {
        Assert.assertEquals(openBrowser(CHROME), ParametersXml.getTitle("home"));
    }

    @Issue("20")
    @Story("FIREFOX")
    @Test
    @DisplayName("Работоспособность браузера FIREFOX")
    void openFirefox() {
        Assert.assertEquals(openBrowser(FIREFOX), ParametersXml.getTitle("home"));
    }

    @Issue("21")
    @Story("IE")
    @Test
    @DisplayName("Работоспособность браузера InternetExplorer")
    void openIE() {
        Assert.assertEquals(openBrowser(IE), ParametersXml.getTitle("home"));
    }

    @Issue("22")
    @Story("OPERA")
    @Test
    @DisplayName("Работоспособность браузера OPERA")
    void openOpera() {
        Assert.assertEquals(openBrowser(OPERA), ParametersXml.getTitle("home"));
    }

    @Issue("23")
    @Story("EDGE")
    @Test
    @DisplayName("Работоспособность браузера EDGE")
    void openEdge() {
        Assert.assertEquals(openBrowser(EDGE), ParametersXml.getTitle("home"));
    }


    static String openBrowser(BROWSER browser) {
        PhpTravels phpTravels = new PhpTravels(browser)
                .setUrl(ParametersXml.getUrl("home"))
                .link();
        Allure.addAttachment("Траффик", "text/plain", phpTravels.getProxyLogs());
        String title = phpTravels.getTitle();
        phpTravels.close();
        return title;
    }
}
