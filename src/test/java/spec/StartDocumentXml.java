package spec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

/**
 * Класс Начало документа <?xml version="1.0" encoding="UTF-8"?>
 */
class StartDocumentXml extends NodeXml {

    public StartDocumentXml(XMLEventReader eventReader) throws XMLStreamException {
        super(eventReader);
    }

    @Override
    public Object getValue() throws XMLStreamException {
        NodeXml nodeXml = create(this);
        value = nodeXml.getValue();
        if (nodeXml.isFilterMatched == false) {
            if (nodeNameFilter != null && attributesFilter != null) value = null;
        }
        return value;
    }

}