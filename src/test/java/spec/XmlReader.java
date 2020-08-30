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
import java.util.Properties;

public class XmlReader {

    static final String path = "src/main/resources/properties.xml";
    static final Logger logger = LoggerFactory.getLogger(XmlReader.class);

    public static HashMap getNode(String nodeName, String nameAttributeValue) {

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
                System.out.println(event.getEventType());

                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElement = event.asStartElement();
                        String elementName = startElement.getName().getLocalPart();
                        if (nodeIsFound) {
                            System.out.println(elementName);
                            mapKeyValue = elementName;
                        }
                        if (elementName.equalsIgnoreCase(nodeName)) {
                            if (nameAttributeValue == null) {
                                nodeIsFound = true;
                            } else {
                                System.out.println(nodeName);
                                Iterator<Attribute> attributes = startElement.getAttributes();
                                while (attributes.hasNext()) {
                                    attribute = attributes.next();
                                    System.out.println(attribute.getValue());
                                    if (attribute.getName().getLocalPart().equalsIgnoreCase("name") &&
                                            attribute.getValue().equalsIgnoreCase(nameAttributeValue)) {
                                        nodeIsFound = true;
                                        System.out.println("true");
                                    }
                                }
                            }
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if (!characters.getData().trim().isEmpty() && nodeIsFound) {
                            System.out.println(characters.getData());
                            hMap.put(mapKeyValue, characters.getData());
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (event.asEndElement().getName().getLocalPart() == nodeName) {
                            nodeIsFound = false;
                            System.out.println("false");
                        }
                }
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(hMap);
        return hMap;
    }
}



