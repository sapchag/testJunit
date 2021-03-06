package spec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;


import java.util.HashMap;

import static javax.xml.stream.XMLStreamConstants.*;

/**
 * Абстрактный класс парсера XML
 */
abstract class NodeXml {
    public XMLEventReader eventReader;
    public XMLEvent event;
    public Object value;
    public int nodeType;
    NodeXml parent;
    protected boolean isFilterMatched;
    protected HashMap<String, String> attributesFilter;
    protected String nodeNameFilter;

    private void init(XMLEventReader eventReader) throws XMLStreamException {
        this.eventReader = eventReader;
        event = eventReader.nextEvent();
        nodeType = event.getEventType();
        //Оставил для отладки и наглядности
        //System.out.println(event.getEventType() + " " + event.toString().trim() + " " + this.getClass().getName());
    }

    public NodeXml(XMLEventReader eventReader) throws XMLStreamException {
        init(eventReader);
    }

    public NodeXml(NodeXml parent) throws XMLStreamException {
        init(parent.eventReader);
        this.parent = parent;
        attributesFilter = parent.attributesFilter;
        nodeNameFilter = parent.nodeNameFilter;
    }

    /**
     * Билдер для класса узлов
     *
     * @param parent - узел владелец
     * @return
     * @throws XMLStreamException
     */
    public NodeXml create(NodeXml parent) throws XMLStreamException {
        XMLEvent event = parent.eventReader.peek();
        if (event == null) return null;
        int nodeType = event.getEventType();
        if (nodeType == END_DOCUMENT) return new EndDocumentNodeXml(this);
        else if (nodeType == CHARACTERS) return new CharactersNodeXml(this);
        else if (nodeType == START_ELEMENT) return new StartNodeXml(this);
        else if (nodeType == END_ELEMENT) return new EndNodeXml(this);
        return null;
    }

    public Object getValue() throws XMLStreamException {
        return value;
    }

    public NodeXml setNodeNameFilter(String nodeName) {
        nodeNameFilter = nodeName;
        return this;
    }

    public NodeXml setAttributesFilter(HashMap<String, String> attributesFilter) {
        this.attributesFilter = attributesFilter;
        return this;
    }

}