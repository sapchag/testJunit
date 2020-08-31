import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

public class LoginToAdmin {

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
