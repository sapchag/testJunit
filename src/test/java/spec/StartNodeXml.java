package spec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static javax.xml.stream.XMLStreamConstants.*;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Класс начало узла XML
 */
class StartNodeXml extends NodeXml {
    HashMap<String, Object> map = new HashMap<>();
    ArrayList<Object> list = new ArrayList<>();
    StartElement startElement = event.asStartElement();

    public StartNodeXml(NodeXml parent) throws XMLStreamException {
        super(parent);
        value = map;
        HashMap<String, String> attributes = getAttributes();
        NodeXml nodeXml;
        while (eventReader.hasNext()) {
            nodeXml = create(this);
            if (nodeXml.nodeType == CHARACTERS) if (nodeXml.getValue() != null) list.add(nodeXml.getValue());
            if (nodeXml.nodeType == END_DOCUMENT || nodeXml.nodeType == END_ELEMENT) break;
            if (nodeXml.nodeType == START_ELEMENT) {
                if (nodeXml.isFilterMatched) {
                    isFilterMatched = true;
                    value = nodeXml.getValue();
                    break;
                }
                list.add(nodeXml.getValue());
            }
        }
        if (attributes != null) {
            list.add(map.put("Attributes", attributes));
        }
        if (list.size() > 0) {
            map.put(startElement.getName().toString(), list);
        }
        if (isMatchFilter()) isFilterMatched = true;

    }

    boolean isMatchFilter() {

        if (nodeNameFilter != startElement.getName().toString()) return false;
        if (attributesFilter == null) return true;
        HashMap<String, String> attributes = getAttributes();
        if (attributes == null) return false;
        for (Map.Entry entry : attributesFilter.entrySet()) {

            if (!attributes.containsKey(entry.getKey().toString())) return false;
            if (!attributes.get(entry.getKey().toString()).contentEquals(entry.getValue().toString())) return false;
        }
        return true;
    }

    /**
     * Пребаразует аттрибуты узлов в HashMap
     *
     * @return
     */
    public HashMap<String, String> getAttributes() {
        Iterator<Attribute> attributes = startElement.getAttributes();
        if (!attributes.hasNext()) return null;
        HashMap<String, String> attributesMap = new HashMap<String, String>();
        Attribute attribute;
        while (attributes.hasNext()) {
            attribute = attributes.next();
            attributesMap.put(attribute.getName().getLocalPart(), attribute.getValue());
        }
        return attributesMap;
    }
};