package spec;

import javax.xml.stream.XMLStreamException;

/**
 * Класс завершающего узла XML
 */
class EndNodeXml extends NodeXml {
    public EndNodeXml(NodeXml parent) throws XMLStreamException {
        super(parent);
    }
};
