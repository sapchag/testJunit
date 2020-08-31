package spec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class ParametersXml {

    static final String path = "src/main/resources/PhpTravelConfig.xml";
    static final Logger logger = LoggerFactory.getLogger(ParametersXml.class);

    public static HashMap getNodeValues(String nodeName, String... attributeParams) {

        HashMap<String, String> hMap = new HashMap<String, String>();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader =
                    factory.createXMLEventReader(new FileReader(path));
            StartElement startElement;
            String mapKeyValue = null;
            Attribute attribute;
            boolean nodeIsFound = false;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                HashMap<String, String> attributesMap = new HashMap<String, String>();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElement = event.asStartElement();
                        String elementName = startElement.getName().getLocalPart();
                        if (nodeIsFound) {
                            mapKeyValue = elementName;
                        }
                        if (elementName.equalsIgnoreCase(nodeName)) {
                            if (attributeParams.length == 0) {
                                nodeIsFound = true;
                            } else {
                                Iterator<Attribute> attributes = startElement.getAttributes();
                                while (attributes.hasNext()) {
                                    attribute = attributes.next();
                                    attributesMap.put(attribute.getName().getLocalPart(), attribute.getValue());
                                }
                                if (attributesMap.get("name").contains(attributeParams[0])) {
                                    if (attributeParams.length < 2) {
                                        nodeIsFound = true;
                                    } else {
                                        hMap.put(attributeParams[1], attributesMap.get(attributeParams[1]));
                                        return hMap;
                                    }
                                }
                                attributesMap.clear();
                            }
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if (!characters.getData().trim().isEmpty() && nodeIsFound) {
                            hMap.put(mapKeyValue, characters.getData());
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (event.asEndElement().getName().getLocalPart() == nodeName) {
                            nodeIsFound = false;
                        }
                }
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return hMap;
    }

    public static HashMap getPageParameters(String pageName) {
        return getNodeValues("page", pageName);
    }

    public static String getTitle(String pageName) {
        return getNodeValues("page", pageName, "title")
                .get("title").toString();
    }

    public static String getUrl(String pageName) {
        return getNodeValues("page", pageName, "url")
                .get("url").toString();
    }
}