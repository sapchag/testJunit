import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import spec.ParametersXml;
import spec.PhpTravelBuilder;
import spec.PhpTravels;

@Execution(ExecutionMode.CONCURRENT)
public class LinkTo {

    @Epic("Link")
    @Feature("Доступность старницы")
    @Story("Администратор")
    @Test
    @DisplayName("Доступность старницы администратора")
    void linkToAdmin() {
        Assert.assertEquals(linkTo("admin"), ParametersXml.getTitle("nologinadmin"));
    }

    @Epic("Link")
    @Feature("Доступность старницы")
    @Story("Домашняя")
    @Test
    @DisplayName("Доступность домашней страницы")
    void linkToHome() {
        Assert.assertEquals(linkTo("home"), ParametersXml.getTitle("home"));
    }

    @Test
    @Epic("Link")
    @Feature("Доступность старницы")
    @Story("Пользователь")
    @DisplayName("Доступность старницы пользователя")
    void linkToUser() {
        Assert.assertEquals(linkTo("user"), ParametersXml.getTitle("nologinuser"));
    }

    static String linkTo(String pageType) {
        PhpTravels phpTravels = PhpTravelBuilder.createPhpTravelsPage(pageType);
        String title = phpTravels.getTitle();
        phpTravels.close();
        return title;
    }
}
