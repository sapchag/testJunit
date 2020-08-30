import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhpTravels {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private HashMap<String, String> adminParamHashMap = new HashMap<>();
    private HashMap<String, String> userParamHashMap = new HashMap<>();
    private HashMap<String, String> languages = new HashMap<>();

    //    private String homeUrl = "https://www.phptravels.net/home";
    private String homeUrl = "https://www.phptravels.org/";
    private String userUrl = "https://www.phptravels.net/login";
    private String adminUrl = "https://www.phptravels.net/admin";

    private HashMap params;
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
        logger.info(browserName);
        wait = new WebDriverWait(driver, 10);

        adminParamHashMap.put("email", "admin@phptravels.com");
        adminParamHashMap.put("password", "demoadmin");
        userParamHashMap.put("username", "user@phptravels.com");
        userParamHashMap.put("password", "demouser");
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

    public PhpTravels linkToHomePage() {
        setUrl(homeUrl);
        link();
        return this;
    }

    public PhpTravels login() {
        link();

        params.forEach((k, v) -> driver.findElement(By.name(k.toString())).sendKeys(v.toString()));
        driver.findElement(By.xpath("//form/button")).click();

        wait.until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//*[contains(text(),'Logout')]")));

        logger.info("-->" + driver.getTitle());
        return this;
    }

    public PhpTravels loginByAdmin() {
        setUrl(adminUrl);
        setParams(adminParamHashMap);
        login();
        return this;
    }

    public PhpTravels loginByUser() {
        setUrl(userUrl);
        setParams(userParamHashMap);
        login();
        return this;
    }

    public List<String> getAllLinks() {
        List<String> links = new ArrayList<String>();

        for (WebElement link : driver.findElements(By.tagName("a"))) {
            url = link.getAttribute("href");

//            if (isValidUrl(url)) {
//                if (url.contains("mailto:")) {
//                    continue;
//                }
                links.add(url);
//            }
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


    public boolean checkLink(WebElement link) {

        String url = "";
        HttpURLConnection huc = null;
        int respCode = 200;

        url = link.getAttribute("href");
        String textAndUrl = "id: " + link.getAttribute("id") + " class: " + link.getAttribute("class") +
                " text: " + link.getText() + " URL: " + url;
        logger.info(textAndUrl);
        try {

            huc = (HttpURLConnection) (new URL(url).openConnection());
            huc.setRequestMethod("HEAD");
            huc.connect();
            respCode = huc.getResponseCode();
            if (respCode >= 400) {
                System.out.println(" - URL не отвечает");
                return false;
            }
            System.out.println();
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
            return false;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

//    public boolean checkAllLinks() {
//
//        boolean checkResult = true;
//        for (WebElement link : getAllLinks()) {
//            //checkResult = checkResult && checkLink(link);
//            checkLink(link);
//        }
//        return checkResult;
//    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            if (e.getMessage() == "unknown protocol: javascript" | e.getMessage() == "null") {
                return false;
            }
        } catch (URISyntaxException e) {
        }
        return true;
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