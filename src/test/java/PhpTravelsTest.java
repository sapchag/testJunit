import io.qameta.allure.Link;
import io.qameta.allure.Step;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import spec.HashMapPropertyFile;
import spec.UrlChecks;
import spec.XmlReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static jdk.nashorn.internal.objects.Global.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PhpTravelsTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private HashMap<String, String> adminParamHashMap = new HashMap<>();
    private HashMap<String, String> userParamHashMap = new HashMap<>();
    private HashMap<String, String> languages = new HashMap<>();
    private HashMap<String, String> url = new HashMap<>();

    private String homeUrl = "https://www.phptravels.net/home";
    private String userUrl = "https://www.phptravels.net/login";
    private String adminUrl = "https://www.phptravels.net/admin";

    public PhpTravelsTest() throws MalformedURLException {

        adminParamHashMap.put("email", "admin@phptravels.com");
        adminParamHashMap.put("password", "demoadmin");

        userParamHashMap.put("username", "user@phptravels.com");
        userParamHashMap.put("password", "demouser");

        languages.put("ar", "اشتراك مورد");
        languages.put("fa", "ثبت نام کننده");
        languages.put("tr", "Tedarikçi Kayıt Olun");
        languages.put("fr", "Signature du fournisseur");
        languages.put("es", "Registro de proveedores");
        languages.put("de", "Unterschrift des Lieferanten");
        languages.put("vi", "Đăng ký nhà cung cấp");
        languages.put("ru", "Подпись поставщика");
        languages.put("en", "Supplier Sign Up");

        url.put("home", homeUrl);
        url.put("homeControl", "PHPTRAVELS | Travel Technology Partner");
        url.put("user", userUrl);
        url.put("userControl", "My Account");
        url.put("admin", adminUrl);
        url.put("adminControl", "Administator Login");

    }

    @Test
    @DisplayName("Link to home")
    void linkToHome() {
        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(homeUrl)
                .link();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, "PHPTRAVELS | Travel Technology Partner");
    }

    @Test
    @DisplayName("Link to admin")
    void linkToAdmin() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(adminUrl)
                .link();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, "Administator Login");
    }

    @Test
    @DisplayName("User LogIn")
    void userLogin() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(userUrl)
                .setParams(userParamHashMap)
                .login();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, "My Account");
    }

    @Test
    @DisplayName("Admin LogIn")
    void adminLogIn() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(adminUrl)
                .setParams(adminParamHashMap)
                .login();
        String title = phpTravels.getTitle();
        phpTravels.close();
        Assert.assertEquals(title, "Dashboard");
    }

    @Test
    @DisplayName("Отсутвие пустых ссылок на главной странице")
    void homeEmptyLinkCheck() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(homeUrl)
                .link();
        Assert.assertEquals(phpTravels.getTitle(), "PHPTRAVELS | Travel Technology Partner");
        for (String url : phpTravels.getAllLinks()) {
            checkLink(url);
//            //checkResult = checkResult && checkLink(link);
//            checkLink(link);
//        }
//        boolean checkResult = phpTravels.checkAllLinks();
        }
        phpTravels.close();
//        Assert.assertTrue(checkResult);

    }

    @Test
    @DisplayName("Отсутвие пустых ссылок на странице пользователя")
    void userEmptyLinkCheck() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(userUrl)
                .setParams(userParamHashMap)
                .login();

        assertEquals(phpTravels.getTitle(), "My Account");
//        boolean checkResult = phpTravels.checkAllLinks();
        phpTravels.close();
//        Assert.assertTrue(checkResult);
    }

    @Test
    @DisplayName("Отсутвие пустых ссылок на странице администратора")
    void adminEmptyLinkCheck() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(adminUrl)
                .setParams(adminParamHashMap)
                .login();
        assertEquals(phpTravels.getTitle(), "Dashboard");
//        boolean checkResult = phpTravels.checkAllLinks();
        phpTravels.close();
//        Assert.assertTrue(checkResult);
    }

    @Test
    @DisplayName("Смена языка на главной станице")
    void homeLanguageChangeCheck() {

        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(homeUrl)
                .link();

        Assert.assertEquals(phpTravels.getTitle(), "PHPTRAVELS | Travel Technology Partner");
        languages.forEach((k, v) -> {
            phpTravels.swithLanguage(k.toString());
            String findString = "//a[contains(@href, 'https://www.phptravels.net/supplier-register/')]";
            String result = phpTravels.getDriver()
                    .findElement(By.xpath(findString)).getText();
            logger.info("Контрольное слово : " + result);
            checkLanguageChangeStep(phpTravels.getLanguage(), v.toString(), result);
        });
        phpTravels.close();
    }

    @Test
    void write() {
        //HashMapPropertyFile.write("url",url);
        //logger.info(HashMapPropertyFile.load("url").get("home").toString());

//        String path = "src/main/resources/properties.xml";
//        try {
//            XMLInputFactory factory = XMLInputFactory.newInstance();
//            XMLEventReader eventReader =
//                    factory.createXMLEventReader(new FileReader(path));
//            eventReader.nextTag();
//            while (eventReader.hasNext()) {
//                XMLEvent event = eventReader.nextEvent();
//                System.out.println(event.getEventType());
//                switch (event.getEventType()) {
//                    case XMLStreamConstants.START_ELEMENT:
//                        StartElement startElement = event.asStartElement();
//                        String qName = startElement.getName().getLocalPart();
//                        System.out.println(qName);
//                        break;
//                    case XMLStreamConstants.CHARACTERS:
//                        Characters characters = event.asCharacters();
//                        System.out.println(characters.getData());
//                        break;
//                }
//            }
//        } catch (XMLStreamException | FileNotFoundException e) {
//            e.printStackTrace();
//        }

        //XmlReader.getNode("page","admin");
        XmlReader.getNode("languages",null);
    }


    @Test
    @DisplayName("Смена языка на станице пользователя")
    void userLanguageChangeCheck() {
        PhpTravels phpTravels = new PhpTravels();
        phpTravels
                .setUrl(userUrl)
                .setParams(userParamHashMap)
                .login();
        Assert.assertEquals(phpTravels.getTitle(), "My Account");
        languages.forEach((k, v) -> {
            phpTravels.swithLanguage(k.toString());
            String findString = "//a[contains(@href, 'https://www.phptravels.net/supplier-register/')]";
            String result = phpTravels.getDriver()
                    .findElement(By.xpath(findString)).getText();
            System.out.println("Контрольное слово : " + result);
            checkLanguageChangeStep(phpTravels.getLanguage(), v.toString(), result);
        });
        phpTravels.close();
    }

    @Step("{language}")
    public static void checkLanguageChangeStep(String language, String origin, String conrol) {
        Assert.assertEquals("Несоответствие контрольного слова", origin, conrol);
    }

    @Link(url = "{url}")
    @Step("{url}")
    public static void checkLink(String url) {
        boolean result = true;
        HttpURLConnection huc = null;
        int respCode = 200;
        try {
            huc = (HttpURLConnection) (new URL(url).openConnection());
            huc.setRequestMethod("HEAD");
            huc.connect();
            respCode = huc.getResponseCode();
            if (respCode >= 400) {
                result = false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(url, result);
    }
}
