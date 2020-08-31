import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

public class LoginToUser {

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
