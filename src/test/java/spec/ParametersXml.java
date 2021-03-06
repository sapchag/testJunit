package spec;

import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ParametersXml {

    static final String path = "src/main/resources/PhpTravelConfig.xml";

    /**
     * Возвращает  содежимое узла в виде дерева из HashMap<String,Object>
     * @param nodeName
     * @param attributeParams
     * @return
     */
    @Nullable
    public static HashMap<String, Object> getXmlToMap(String nodeName, String... attributeParams) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader =
                    factory.createXMLEventReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            StartDocumentXml startDocumentXml = new StartDocumentXml(eventReader);
            if (attributeParams.length > 0) {
                int i = 0;
                HashMap<String, String> hMap = new HashMap<>();
                while (i < attributeParams.length) {
                    hMap.put(attributeParams[i], attributeParams[i + 1]);
                    i += 2;
                }
                startDocumentXml.setAttributesFilter(hMap);
            }
            return (HashMap<String, Object>) startDocumentXml
                    .setNodeNameFilter(nodeName)
                    .getValue();

        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Конвертирeвет HashMap<String,List<String>> в HashMap<String,(String)List<String>[0]>
     *
     * @param object
     * @return
     */
    public static HashMap<String, String> objectToStringHashMap(Object object) {
        HashMap<String, String> map = new HashMap<String, String>();
        ((HashMap<String, ArrayList<String>>) object).forEach((k, v) -> map.put(k, v.get(0)));
        return map;
    }

    /**
     * Возвращает содежимое узла в виде HashMap<String, String>
     *
     * @param
     * @return
     */
    public static HashMap<String, String> getNodeValues(String nodeName, String... attributeParams) {
        ArrayList<Object> list = (ArrayList<Object>) getXmlToMap(nodeName, attributeParams).get(nodeName);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        for (Object it : list) {
            if (it != null) resultMap.putAll(objectToStringHashMap(it));
        }
        return resultMap;
    }

    /**
     * Возвращает содежимое аттрибутов узла в виде HashMap<String, String>
     *
     * @param
     * @return
     */
    public static HashMap<String, String> getNodeAttributes(String nodeName, String... attributeParams) {
        return (HashMap<String, String>) getXmlToMap(nodeName, attributeParams).get("Attributes");
    }

    /**
     * Возвращает аттрибуты страницы указанной в параметре
     *
     * @param pageName
     * @return
     */
    public static HashMap<String, String> getPageParameters(String pageName) {
        return getNodeValues("page", "name", pageName);
    }

    /**
     * Возвращает заголовок страницы указанной в параметре
     *
     * @param pageName
     * @return
     */

    public static String getTitle(String pageName) {
        return getNodeAttributes("page", "name", pageName)
                .get("title");
    }

    /**
     * Возвращает url страницы указанной в параметре
     *
     * @param pageName
     * @return
     */
    public static String getUrl(String pageName) {
        return getNodeAttributes("page", "name", pageName)
                .get("url");
    }
}