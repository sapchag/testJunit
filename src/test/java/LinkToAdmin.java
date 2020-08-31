import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spec.ParametersXml;

public class LinkToAdmin {

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

}
