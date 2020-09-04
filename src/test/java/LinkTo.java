import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import spec.ParametersXml;

@Execution(ExecutionMode.CONCURRENT)
public class LinkTo {

    @Epic("Link")
    @Feature("Доступность старницы")
    @Story("Администратор")
    @Test
    @DisplayName("Доступность старницы администратора")
    void linkToAdmin() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(ParametersXml.getUrl("admin"))
                .link();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, ParametersXml.getTitle("nologinadmin"));
    }

    @Epic("Link")
    @Feature("Доступность старницы")
    @Story("Домашняя")
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

    @Test
    @Epic("Link")
    @Feature("Доступность старницы")
    @Story("Пользователь")
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
