import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

public class LoginToUser {

    @Epic("Login")
    @Feature("Вход на страницу")
    @Story("Пользователь")
    @Test
    @DisplayName("Вход на старницу пользователя")
    void userLogin() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(ParametersXml.getUrl("user"))
                .setParams(ParametersXml.getPageParameters("user"))
                .login();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, ParametersXml.getTitle("user"));
    }
}
