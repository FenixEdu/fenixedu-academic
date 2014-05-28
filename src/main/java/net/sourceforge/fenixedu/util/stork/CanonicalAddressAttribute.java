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
package net.sourceforge.fenixedu.util.stork;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.StorkAttributeType;
import net.sourceforge.fenixedu.util.stork.exceptions.StorkRuntimeException;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class CanonicalAddressAttribute extends Attribute {
    private String address;
    private String city;
    private String zipCode;
    private String countryCode;

    public CanonicalAddressAttribute(Integer id, StorkAttributeType type, Boolean mandatory, String value) {
        super(id, type, mandatory, value);

        if (getSemanticValue() != null) {
            parseAddressCompounds();
        }
    }

    private void parseAddressCompounds() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            dbf.setValidating(false);
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getValue().getBytes(CharEncoding.UTF_8));

            Document doc = db.parse(byteArrayInputStream);
            setFields(doc);
        } catch (ParserConfigurationException e) {
            throw new StorkRuntimeException(e);
        } catch (SAXException e) {
            throw new StorkRuntimeException(e);
        } catch (IOException e) {
            throw new StorkRuntimeException(e);
        }
    }

    private void setFields(Document doc) {
        /*
         * Assuming that root node is 'canonicalResidenceAddress'
         */
        String apartmentNumber = "";
        String streetNumber = "";
        Node iterSiblingNode = doc.getFirstChild();
        while (iterSiblingNode != null) {
            if (iterSiblingNode.getNodeName().equals("town")) {
                city = iterSiblingNode.getNodeValue().trim();
            } else if (iterSiblingNode.getNodeName().equals("postalCode")) {
                zipCode = iterSiblingNode.getNodeValue().trim();
            } else if (iterSiblingNode.getNodeName().equals("streetName")) {
                address = iterSiblingNode.getNodeName().trim();
            } else if (iterSiblingNode.getNodeName().equals("streetNumber")) {
                streetNumber = iterSiblingNode.getNodeValue();
            } else if (iterSiblingNode.getNodeName().equals("apartmentNumber")) {
                apartmentNumber = iterSiblingNode.getNodeValue().trim();
            } else if (iterSiblingNode.getNodeName().equals("state")) {
                countryCode = iterSiblingNode.getNodeName().trim();
            }
        }

        this.address = String.format("%s %s %s", this.address, streetNumber, apartmentNumber).trim();
    }

    public String getAddress() {
        if (StringUtils.isEmpty(address)) {
            return null;
        }

        if ("null".equals(address)) {
            return null;
        }

        return address;
    }

    public String getCity() {
        if (StringUtils.isEmpty(city)) {
            return null;
        }

        if ("null".equals(city)) {
            return null;
        }

        return city;
    }

    public String getCountryCode() {
        if (StringUtils.isEmpty(countryCode)) {
            return null;
        }

        if ("null".equals(countryCode)) {
            return null;
        }

        return countryCode;
    }

    public Country getCountry() {
        if (StringUtils.isEmpty(getCountryCode())) {
            return null;
        }

        return Country.readByTwoLetterCode(getCountryCode());
    }

    public String getZipCode() {
        if (StringUtils.isEmpty(zipCode)) {
            return null;
        }

        if ("null".equals(zipCode)) {
            return null;
        }

        return zipCode;
    }

    public boolean isValidCanonicalAddressAttribute() {
        return getSemanticValue() != null;
    }
}
