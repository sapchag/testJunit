import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstTest {

    private String webDriverUrl = "http://localhost:4444/wd/hub";
    private WebDriver driver = new RemoteWebDriver(new URL(webDriverUrl), DesiredCapabilities.firefox());

    public FirstTest() throws MalformedURLException {
    }

    @Test
    @DisplayName("Link to home")
    void linkToHome() {

        PhpTravels phpTravels = new PhpTravels(driver)
                .setUrl("https://www.phptravels.net/home")
                .link();
        assertEquals(phpTravels.getTitle(), "PHPTRAVELS | Travel Technology Partner");
        phpTravels.close();
    }

    @Test
    @DisplayName("Link to admin")
    void linkToAdmin() {

        PhpTravels phpTravels = new PhpTravels(driver)
                .setUrl("https://www.phptravels.net/admin")
                .link();
        assertEquals(phpTravels.getTitle(), "Administator Login");
        phpTravels.close();
    }

    @Test
    @DisplayName("userLogIn")
    void userLogin() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", "user@phptravels.com");
        hashMap.put("password", "demouser");

        PhpTravels phpTravels = new PhpTravels(driver)
                .setUrl("https://www.phptravels.net/login")
                .setParams(hashMap)
                .login();
        assertEquals(phpTravels.getTitle(), "My Account");
        phpTravels.close();
    }

    @Test
    @DisplayName("adminLogIn")
    void adminLogIn() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", "admin@phptravels.com");
        hashMap.put("password", "demoadmin");

        PhpTravels phpTravels = new PhpTravels(driver)
                .setUrl("https://www.phptravels.net/admin")
                .setParams(hashMap)
                .login();
        assertEquals(phpTravels.getTitle(), "Dashboard");
        phpTravels.close();
    }
}
