import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhpTravels {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private HashMap<String, String> params;
    private String url;
    private WebDriver driver;
    String webDriverUrl = "http://localhost:4444/wd/hub";
    String browserName;
    WebDriverWait wait;

    public PhpTravels() {
        logger.info("Выбор и запуск браузера");
        try {
            int randomDriver = new Random().nextInt(3);
            if (randomDriver == 0) {
                driver = new RemoteWebDriver(new URL(webDriverUrl), DesiredCapabilities.chrome());
                browserName = "chrome";
            } else if (randomDriver == 1) {
                driver = new RemoteWebDriver(new URL(webDriverUrl), DesiredCapabilities.firefox());
                browserName = "firefox";
            } else if (randomDriver == 2) {
                driver = new RemoteWebDriver(new URL(webDriverUrl), DesiredCapabilities.internetExplorer());
                browserName = "internetExplorer";
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        }
        driver.manage().window().maximize();
        logger.info(browserName);
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

    public List<String> getAllLinks() {
        List<String> links = new ArrayList<String>();

        for (WebElement link : driver.findElements(By.tagName("a"))) {
            url = link.getAttribute("href");

            links.add(url);

        }
        return links;
    }

    public PhpTravels swithLanguage(String alias) {
        logger.info(">> " + alias);
        driver.findElement(By.id("dropdownLangauge")).click();
        driver.findElement(By.id(alias)).click();

        String findString = "//a[contains(@id, 'dropdownLangauge')]";
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath(findString)));
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
        driver.quit();
        logger.info(browserName + " закрыт");
        return this;
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