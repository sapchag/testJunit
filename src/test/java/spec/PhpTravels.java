package spec;

import io.qameta.allure.Allure;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс для работы с сайтом
 */
public class PhpTravels {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private HashMap<String, String> params;
    private String url;
    private WebDriver driver;
    String webDriverUrl = ParametersXml.getNodeValues("webdriver").get("url");
    BROWSER browser;
    WebDriverWait wait;
    BrowserMobProxyServer proxy;

    public enum BROWSER {
        CHROME, FIREFOX, OPERA, IE, EDGE
    }

    public PhpTravels() {
        ArrayList<BROWSER> browsers = new ArrayList<>();
        ParametersXml.getNodeValues("browsers").forEach((k, v) -> browsers.add(BROWSER.valueOf(k)));
        Collections.shuffle(browsers);
        browser = browsers.get(0);
        startProxyAndDriver();
    }

    public PhpTravels(BROWSER browser) {
        this.browser = browser;
        startProxyAndDriver();
    }

    /**
     * Запускает прокси и браузер
     * @return
     */
    public PhpTravels startProxyAndDriver() {
        proxy = new BrowserMobProxyServer();
        proxy.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        proxy.enableHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        proxy.start(0);

        createWebDriver();
        assert driver != null : "Драйвер " + browser.toString() + " не создан";

        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        proxy.newHar();
        wait = new WebDriverWait(driver, 10);
        return this;
    }

    /**
     * Создает веб драйвер
     */
    void createWebDriver(){
        DesiredCapabilities capability = createBrowserCapabilities();
        logger.info("Запуск браузера " + browser.toString());
        try {
            driver = new RemoteWebDriver(new URL(webDriverUrl), capability);
        } catch (MalformedURLException | UnreachableBrowserException | SessionNotCreatedException e) {
            logger.error(e.getMessage());
            Allure.addAttachment("Ошибка", "text/plain", e.getMessage());
            e.getCause();
        }
    }

    /**
     * Создает Capabilities в зависимости от выбранного типа браузера
     * @return
     */
    DesiredCapabilities createBrowserCapabilities() {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        DesiredCapabilities capability = new DesiredCapabilities();
        switch (browser) {
            case CHROME:
                capability = DesiredCapabilities.chrome();
                break;
            case FIREFOX:
                capability = DesiredCapabilities.firefox();
                break;
            case OPERA:
                capability = DesiredCapabilities.operaBlink();
                break;
            case IE:
                capability = DesiredCapabilities.internetExplorer();
                break;
            case EDGE:
                capability = DesiredCapabilities.edge();
        }
        capability.setCapability(CapabilityType.PROXY, seleniumProxy);
        capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capability.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, false);
        return capability;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public PhpTravels link() {
        try {
            driver.get(url);
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
            Allure.addAttachment("Ошибка", "text/plain", e.getMessage());
            e.getCause();
        }
        logger.info(url);
        logger.info(" >" + driver.getTitle());
        return this;
    }

    public PhpTravels login() {
        link();

        if (params.isEmpty()) return this;
        params.forEach((k, v) -> driver.findElement(By.name(k)).sendKeys(v));
        driver.findElement(By.xpath("//form/button")).click();
        try {
            wait.until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath("//*[contains(text(),'Logout')]")));
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
        }

        logger.info("-->" + driver.getTitle());
        return this;
    }

    public List<String> getImageLinks() {
        List<String> links = new ArrayList<>();

        for (WebElement link : driver.findElements(By.tagName("img"))) {
            url = link.getAttribute("src");
            links.add(url);
        }
        return links;
    }

    public List<String> getAllLinks() {
        List<String> links = new ArrayList<>();

        for (WebElement link : driver.findElements(By.tagName("a"))) {
            url = link.getAttribute("href");
            links.add(url);
        }
        return links.stream()
                .filter(Objects::nonNull)
                .filter(link -> !link.contains("javascript:"))
                .collect(Collectors.toList());
    }

    public PhpTravels swithLanguage(String alias) {
        logger.info(">> " + alias);
        try {
            driver.findElement(By.id("dropdownLangauge")).click();
            driver.findElement(By.id(alias)).click();
            String findString = "//a[contains(@id, 'dropdownLangauge')]";
            wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath(findString)));
        } catch (ElementNotInteractableException | TimeoutException e) {
            logger.error(e.getMessage());
        }
        logger.info(getLanguage());
        return this;
    }

    public String getLanguage() {
        return driver
                .findElement(By.xpath("//a[contains(@id, 'dropdownLangauge')]")).getText();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public PhpTravels close() {
        proxy.endHar();
        proxy.abort();
        driver.quit();

        logger.info(browser.toString() + " закрыт");
        return this;
    }

    public String getProxyLogs() {
        StringBuilder logs = new StringBuilder();
        for (HarEntry entry : proxy.getHar().getLog().getEntries()) {
            logs.append(entry.getRequest().getUrl())
                    .append(" ")
                    .append(entry.getResponse().getStatus())
                    .append(" ")
                    .append(System.lineSeparator());
        }
        proxy.endHar();
        proxy.newHar();
        return logs.toString();
    }

    public PhpTravels setParams(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    public PhpTravels setUrl(String url) {
        this.url = url;
        return this;
    }

    public static PhpTravels createLoginedPhpTravelsPage(String pageType) {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl(pageType))
                .setParams(ParametersXml.getPageParameters(pageType))
                .login();
        return phpTravels;
    }

    public static PhpTravels createPhpTravelsPage(String pageType) {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl(pageType))
                .link();
        return phpTravels;
    }
}