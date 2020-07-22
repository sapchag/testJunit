import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class PhpTravels {
    private HashMap params;
    private String url;
    private WebDriver driver;

    public PhpTravels(WebDriver driver) {
        this.driver = driver;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public PhpTravels link() {
        driver.get(url);
        System.out.println(url);
        System.out.println(driver.getTitle());
        return this;
    }

    public PhpTravels login() {
        link();

        params.forEach((k,v) -> driver.findElement(By.name(k.toString())).sendKeys(v.toString()));
        driver.findElement(By.xpath("//form/button")).click();

        try {
            WebElement logoutElement =
                    (new WebDriverWait(driver, 5))
                            .until(ExpectedConditions
                                    .presenceOfElementLocated(By.xpath("//*[contains(text(),'Logout')]")));
        }
        catch (TimeoutException timeoutException){}

        System.out.println(driver.getTitle());
        return this;
    }

    public PhpTravels close() {
        driver.close();
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
