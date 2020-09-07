package spec;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhpTravels {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private HashMap<String, String> params;
    private String url;
    private WebDriver driver;
    String webDriverUrl = ParametersXml.getNodeValues("webdriver").get("url").toString();
    String browserName;
    WebDriverWait wait;
    BrowserMobProxyServer proxy;

    public PhpTravels() {
        logger.info("Выбор и запуск браузера");

        proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        proxy.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        proxy.enableHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        proxy.start(0);
        Proxy seleniumProxy = null;
        seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        DesiredCapabilities capability = new DesiredCapabilities();

        int browserNumber = new Random().nextInt(4);
        switch (browserNumber) {
            case 0:
                capability = DesiredCapabilities.chrome();
                browserName = "chrome";
                break;
            case 1:
                capability = DesiredCapabilities.firefox();
                browserName = "firefox";
                break;
            case 2:
                capability = DesiredCapabilities.internetExplorer();
                browserName = "internetExplorer";
                break;
            case 3:
                capability = DesiredCapabilities.operaBlink();
                browserName = "opera";
        }
        capability.setCapability(CapabilityType.PROXY, seleniumProxy);
        capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capability.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, false);
        try {

            driver = new RemoteWebDriver(new URL(webDriverUrl), capability);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
            e.getCause();
        }

        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        logger.info(browserName);
        proxy.newHar();
        wait = new WebDriverWait(driver, 10);

    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public PhpTravels link() {
        driver.get(url);
        logger.info(url);
        logger.info(" >" + driver.getTitle());
        return this;
    }

    public PhpTravels login() {
        link();

        if (params.isEmpty()) return this;
        params.forEach((k, v) -> driver.findElement(By.name(k.toString())).sendKeys(v.toString()));
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
        List<String> links = new ArrayList<String>();

        for (WebElement link : driver.findElements(By.tagName("img"))) {
            url = link.getAttribute("src");
            links.add(url);
        }
        return links;
    }

    public List<String> getAllLinks() {
        List<String> links = new ArrayList<String>();

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
        List<HarEntry> entries = proxy.getHar().getLog().getEntries();
        for (HarEntry entry : entries) {
            logger.info(entry.getStartedDateTime() + " URL " + entry.getRequest().getUrl());
            logger.info(entry.getStartedDateTime() + " Response Code " + entry.getResponse().getStatus());
        }
        proxy.endHar();
        proxy.abort();
        driver.quit();

        logger.info(browserName + " закрыт");
        return this;
    }

    public String getProxyLogs() {
        String logs = "";
        for (HarEntry entry : proxy.getHar().getLog().getEntries()) {
            logs = logs + entry.getStartedDateTime() + " " +
                    entry.getRequest().getUrl() + " " + entry.getResponse().getStatus() +
                    " " + System.lineSeparator();
        }
        proxy.endHar();
        proxy.newHar();
        return logs;
    }

    public PhpTravels setParams(HashMap params) {
        this.params = params;
        return this;
    }

    public PhpTravels setUrl(String url) {
        this.url = url;
        return this;
    }
}