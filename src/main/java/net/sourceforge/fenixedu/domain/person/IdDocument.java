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
package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.domain.Bennu;

public class IdDocument extends IdDocument_Base {

    public IdDocument(final Person person, final String value, final IdDocumentTypeObject idDocumentType) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setPerson(person);
        setIdDocumentType(idDocumentType);
        setValue(value);
    }

    public IdDocument(final Person person, final String value, final IDDocumentType documentType) {
        this(person, value, IdDocumentTypeObject.readByIDDocumentType(documentType));
    }

    public static Collection<IdDocument> find(final String idDocumentValue) {
        final Collection<IdDocument> idDocuments = new ArrayList<IdDocument>();
        for (final IdDocument idDocument : Bennu.getInstance().getIdDocumentsSet()) {
            if (idDocument.getValue().equalsIgnoreCase(idDocumentValue)) {
                idDocuments.add(idDocument);
            }
        }

        return idDocuments;
    }

    public void setIdDocumentType(IDDocumentType documentType) {
        super.setIdDocumentType(IdDocumentTypeObject.readByIDDocumentType(documentType));
    }

    public void delete() {
        setPerson(null);
        super.setIdDocumentType(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasIdDocumentType() {
        return getIdDocumentType() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
