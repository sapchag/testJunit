import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;
import spec.PhpTravels;

import java.util.HashMap;

@Epic("Login")
public class LoginTo {

    @Issue("6")
    @Feature("Вход на страницу")
    @Story("Администратор")
    @Test
    @DisplayName("Вход на страницу администратора")
    void logInToAdmin() {
        logInTo("admin", true);
    }

    @Issue("5")
    @Feature("Вход на страницу")
    @Story("Пользователь")
    @Test
    @DisplayName("Вход на старницу пользователя")
    void logInToUser() {
        logInTo("user", true);
    }

    @Issue("7")
    @Feature("Попытка входа на страницу")
    @Story("Администратор")
    @Test
    @DisplayName("Попытка входа на старницу администратора с неправильным паролем")
    void logInToWrongAdmin() {
        logInTo("admin", false);
    }

    @Issue("4")
    @Feature("Попытка входа на страницу")
    @Story("Пользователь")
    @Test
    @DisplayName("Попытка входа на старницу пользователя с неправильным паролем")
    void logInToWrongUser() {
        logInTo("user", false);
    }

    void logInTo(String pageType, boolean login) {

        HashMap<String, String> hMap = ParametersXml.getPageParameters(pageType);
        String errorPassword = hMap.get("password") + "error";
        if (!login) {
            hMap.remove("password");
            hMap.put("password", errorPassword);
        }
        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(ParametersXml.getUrl(pageType))
                .setParams(hMap)
                .login();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, ParametersXml.getTitle(login ? pageType : "nologin" + pageType));
    }
}
