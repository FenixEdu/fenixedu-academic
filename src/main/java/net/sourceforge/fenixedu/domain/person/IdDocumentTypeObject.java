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

import org.fenixedu.bennu.core.domain.Bennu;

public class IdDocumentTypeObject extends IdDocumentTypeObject_Base {

    public IdDocumentTypeObject() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public static IdDocumentTypeObject readByIDDocumentType(final IDDocumentType documentType) {
        for (final IdDocumentTypeObject idDocumentType : Bennu.getInstance().getIdDocumentTypesSet()) {
            if (idDocumentType.getValue() == documentType) {
                return idDocumentType;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.person.IdDocument> getIdDocuments() {
        return getIdDocumentsSet();
    }

    @Deprecated
    public boolean hasAnyIdDocuments() {
        return !getIdDocumentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
