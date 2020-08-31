import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

public class LoginToAdmin {

    @Epic(value = "Login")
    @Feature(value = "Вход на страницу")
    @Story(value = "Администратор")
    @Test
    @DisplayName("Вход на страницу администратора")
    void adminLogIn() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(ParametersXml.getUrl("admin"))
                .setParams(ParametersXml.getPageParameters("admin"))
                .login();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, ParametersXml.getTitle("admin"));
    }

}
