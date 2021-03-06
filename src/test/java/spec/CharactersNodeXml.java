package spec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;

/**
 * Класс содержимого узла XML
 */
class CharactersNodeXml extends NodeXml {
    public CharactersNodeXml(NodeXml parent) throws XMLStreamException {
        super(parent);
        Characters characters = event.asCharacters();
        if (!characters.getData().trim().isEmpty()) {
            value = characters.getData().trim();
        } else value = null;
    }
};
