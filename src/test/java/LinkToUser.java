import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

import java.lang.reflect.Parameter;

public class LinkToUser {

    @Epic(value = "Link")
    @Feature(value = "Доступность старницы")
    @Story(value = "Пользователь")
    @Test
    @DisplayName("Доступность старницы пользователя")
    void linkToUser() {
        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(ParametersXml.getUrl("user"))
                .link();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, ParametersXml.getTitle("nologinuser"));
    }

}
