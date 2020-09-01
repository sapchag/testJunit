import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

import java.util.HashMap;

public class WrongLoginToUser {

    @Epic("Login")
    @Feature("Попытка входа на страницу")
    @Story("Пользователь")
    @Test
    @DisplayName("Попытка входа на старницу пользователя с неправильным паролем")
    void wrongLoginToUser() {

        HashMap<String, String> hMap = ParametersXml.getPageParameters("user");
        String errorPassword = hMap.get("password") + "error";
        hMap.remove("password");
        hMap.put("password", errorPassword);

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(ParametersXml.getUrl("user"))
                .setParams(hMap)
                .login();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, ParametersXml.getTitle("nologinuser"));
    }
}
