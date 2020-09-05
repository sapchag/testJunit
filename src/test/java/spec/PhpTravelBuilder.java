package spec;

public class PhpTravelBuilder {

    PhpTravelBuilder() {
    }

    public static PhpTravels createLoginedPhpTravelsPage(String pageType) {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl(pageType))
                .setParams(ParametersXml.getPageParameters(pageType))
                .login();
        return phpTravels;
    }

    public static PhpTravels createPhpTravelsPage(String pageType) {
        PhpTravels phpTravels = new PhpTravels()
                .setUrl(ParametersXml.getUrl(pageType))
                .link();
        return phpTravels;
    }

}
