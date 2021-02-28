package spec;

import com.google.gson.internal.$Gson$Preconditions;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

public class ParametersXml2 {

    static final String path = "src/main/resources/PhpTravelConfig.xml";

    public static void getXmlToMap() {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader =
                    factory.createXMLEventReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            //while (eventReader.hasNext()) {
                //XMLEvent event = eventReader.nextEvent();
                System.out.println(new StartDocumentXml(eventReader).getValue());
             //   eventReader.nextEvent();
            //}
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

class Simple{
    public static void main(String args[]){
        ParametersXml2.getXmlToMap();
    }
}