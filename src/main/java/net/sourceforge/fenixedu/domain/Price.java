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
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

public class Price extends Price_Base {

    public Price() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Price(GraduationType graduationType, DocumentType documentType, String description, Double price) {
        this();
        this.setDescription(description);
        this.setDocumentType(documentType);
        this.setGraduationType(graduationType);
        this.setPrice(price);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static Price readByGraduationTypeAndDocumentTypeAndDescription(GraduationType graduationType,
            DocumentType documentType, String description) {
        for (final Price price : Bennu.getInstance().getPricesSet()) {
            if (price.getGraduationType() == graduationType && price.getDocumentType() == documentType
                    && price.getDescription().equals(description)) {
                return price;
            }
        }
        return null;
    }

    public static List<Price> readByGraduationTypeAndDocumentTypes(GraduationType graduationType, List<DocumentType> documentTypes) {
        final List<Price> result = new ArrayList<Price>();
        for (final Price price : Bennu.getInstance().getPricesSet()) {
            if (price.getGraduationType() == graduationType && documentTypes.contains(price.getDocumentType())) {
                result.add(price);
            }
        }
        return result;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPrice() {
        return getPrice() != null;
    }

    @Deprecated
    public boolean hasDocumentType() {
        return getDocumentType() != null;
    }

    @Deprecated
    public boolean hasGraduationType() {
        return getGraduationType() != null;
    }

}
