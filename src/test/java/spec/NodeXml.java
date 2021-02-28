package spec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static javax.xml.stream.XMLStreamConstants.*;

abstract class NodeXml {
    XMLEventReader eventReader;
    XMLEvent event;
    Object value;
    NodeXml parentNode;
    NodeXml childrenNode;
    int nodeType;

    private void init(XMLEventReader eventReader) throws XMLStreamException {
        this.eventReader = eventReader;
        event = eventReader.nextEvent();
        nodeType = event.getEventType();
        System.out.println(event.getEventType() + " " + event.toString().trim() + " " + this.getClass().getName());
    }

    public NodeXml(XMLEventReader eventReader) throws XMLStreamException {
        init(eventReader);
    }

    public NodeXml(NodeXml parent) throws XMLStreamException {
        init(parent.eventReader);
        parent.childrenNode = this;
        this.parentNode = parent;
        //if (nodeType != END_DOCUMENT) create(this);
    }

    public NodeXml create(NodeXml parent) throws XMLStreamException {
        XMLEvent event = parent.eventReader.peek();
        if (event == null) return null;
        int nodeType = event.getEventType();
        //if (nodeType == XMLStreamConstants.START_DOCUMENT) return new StartDocumentXml(this);
        if (nodeType == END_DOCUMENT) return new EndDocumentNodeXml(this);
        else if (nodeType == CHARACTERS) return new CharactersNodeXml(this);
        else if (nodeType == START_ELEMENT) return new StartNodeXml(this);
        else if (nodeType == END_ELEMENT) return new EndNodeXml(this);
        //return new StartNodeXml(eventReader);
        return null;
    }

    public Object getValue() {
        return value;
    }

    public NodeXml getParentNode() {
        return parentNode;
    }

    public StartNodeXml getStartNode() throws XMLStreamException {
        //System.out.println("->getStartNode" + parentNode.nodeType);
        if (parentNode.nodeType == START_DOCUMENT) return null;
        if (parentNode.nodeType == START_ELEMENT) return (StartNodeXml) parentNode;
        return parentNode.getStartNode();
    }
}

class StartDocumentXml extends NodeXml {
    HashMap<String, Object> map = new HashMap<String, Object>();

    public StartDocumentXml(XMLEventReader eventReader) throws XMLStreamException {
        super(eventReader);
        value = map;
        map.put("Document", create(this).getValue());
    }
}

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
            if (nodeXml.nodeType == CHARACTERS) list.add(nodeXml.getValue());
            if (nodeXml.nodeType == END_DOCUMENT || nodeXml.nodeType == END_ELEMENT) {
                break;
            }
            if (nodeXml.nodeType == START_ELEMENT) list.add(nodeXml.getValue());
        }
        if (list.size() > 0) {
            map.put(startElement.getName().toString(), list);
        }
        if (attributes != null) {
            map.put("Attributes", attributes);
        }
    }

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

class CharactersNodeXml extends NodeXml {
    public CharactersNodeXml(NodeXml parent) throws XMLStreamException {
        super(parent);
        Characters characters = event.asCharacters();
        if (!characters.getData().trim().isEmpty()) {
            value = characters.getData().trim();
        } else value = null;
    }
};


class EndNodeXml extends NodeXml {
    public EndNodeXml(NodeXml parent) throws XMLStreamException {
        super(parent);
        NodeXml startNode = getStartNode();
        if (startNode != null) {
            create(startNode.getParentNode());
        }
    }
};

class EndDocumentNodeXml extends NodeXml {
    public EndDocumentNodeXml(NodeXml parent) throws XMLStreamException {
        //eventReader=parent.eventReader;
        super(parent);
        //while (eventReader.hasNext()) {
        //XMLEvent event = eventReader.nextEvent();
        //System.out.println(create(eventReader).getValue());
        //eventReader.nextEvent();
        //}
        //owner.
    }
};
