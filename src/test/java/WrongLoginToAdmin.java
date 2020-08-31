import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

import java.util.HashMap;

public class WrongLoginToAdmin {

    @Epic(value = "Login")
    @Epic(value = "Link")
    @Feature(value = "Попытка входа на страницу")
    @Story(value = "Администратор")
    @Test
    @DisplayName("Попытка входа на старницу администратора с неправильным паролем")
    void wrongLoginToAdmin() {

        HashMap<String, String> hMap = ParametersXml.getPageParameters("admin");
        String errorPassword = hMap.get("password") + "error";
        hMap.remove("password");
        hMap.put("password", errorPassword);

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(ParametersXml.getUrl("admin"))
                .setParams(hMap)
                .login();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, ParametersXml.getTitle("nologinadmin"));
    }
}