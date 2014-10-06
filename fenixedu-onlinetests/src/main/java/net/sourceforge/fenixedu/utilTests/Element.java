/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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