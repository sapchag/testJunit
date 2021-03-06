package spec;

import javax.xml.stream.XMLStreamException;

/**
 * Класс последнего узла документа XML
 */
class EndDocumentNodeXml extends NodeXml {
    public EndDocumentNodeXml(NodeXml parent) throws XMLStreamException {
        super(parent);
    }
};