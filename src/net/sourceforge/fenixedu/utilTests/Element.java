/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.utilTests;

import org.xml.sax.Attributes;

/**
 * @author Susana Fernandes
 */
public class Element {
    String uri;

    String localName;

    String qName;

    String value = null;

    Attributes attributes;

    public Element(String uri, String localName, String qName, Attributes attributes) {
        this.uri = uri;
        this.localName = localName;
        this.qName = qName;
        this.attributes = attributes;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public String getLocalName() {
        return localName;
    }

    public String getQName() {
        return qName;
    }

    public String getUri() {
        return uri;
    }

    public String getValue() {
        return value;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setLocalName(String string) {
        localName = string;
    }

    public void setQName(String string) {
        qName = string;
    }

    public void setUri(String string) {
        uri = string;
    }

    public void setValue(String string) {
        value = string;
    }
}