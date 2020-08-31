import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

public class LinkToHome {

    @Epic(value = "Link")
    @Feature(value = "Доступность старницы")
    @Story(value = "Домашняя")
    @Test
    @DisplayName("Доступность домашней страницы")
    void linkToHome() {
        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(ParametersXml.getUrl("home"))
                .link();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, ParametersXml.getTitle("home"));
    }
}
