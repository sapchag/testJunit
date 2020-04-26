import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstTest {

    WebDriver driver = new FirefoxDriver();

    @Test
    @DisplayName("Link to site")
    void linkToSite() {
        driver.get("https://www.phptravels.net/home");
        System.out.println(driver.getTitle().toString());
        assertEquals(driver.getTitle(), "PHPTRAVELS | Travel Technology Partner");
    }

    @Test
    @DisplayName("LogIn")
    void Login() {
        driver.get("https://www.phptravels.net/login");
        System.out.println(driver.getTitle().toString());
        assertEquals(driver.getTitle(), "Login");
        driver.findElement(By.name("username")).sendKeys("user@phptravels.com");
        driver.findElement(By.name("password")).sendKeys("demouser");
        driver.findElement(By.xpath("//form/button")).click();
        System.out.println(driver.getTitle().toString());
        assertEquals(driver.getTitle(), "Login");
    }
}
