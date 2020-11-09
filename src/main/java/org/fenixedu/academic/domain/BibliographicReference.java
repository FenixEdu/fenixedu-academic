/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.Comparator;

import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class BibliographicReference extends BibliographicReference_Base {

    public static final Comparator<BibliographicReference> COMPARATOR_BY_ORDER =
            Comparator.comparing(BibliographicReference::getReferenceOrder).thenComparing(BibliographicReference::getTitle)
                    .thenComparing(BibliographicReference::getYear).thenComparing(BibliographicReference::getExternalId);

    public BibliographicReference() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public static BibliographicReference create(final String title, final String authors, final String reference,
            final String year, final Boolean optional) {
        if (title == null || authors == null || year == null || optional == null) {
            throw new IllegalArgumentException("Required fields not filled");
        }

        final BibliographicReference result = new BibliographicReference();
        result.setTitle(title);
        result.setAuthors(authors);
        result.setReference(reference);
        result.setYear(year);
        result.setOptional(optional);

        return result;
    }

    public void edit(final String title, final String authors, final String reference, final String year, final String url,
            final Boolean optional) {

        if (title == null || authors == null || year == null || optional == null) {
            throw new IllegalArgumentException("Required fields not filled");
        }

        setTitle(title);
        setAuthors(authors);
        setReference(reference);
        setYear(year);
        setOptional(optional);
        setUrl(url);
    }

    public boolean isOptional() {
        return getOptional() != null && getOptional();
    }

    public BibliographicReferenceType getType() {
        return isOptional() ? BibliographicReferenceType.SECONDARY : BibliographicReferenceType.MAIN;
    }

    public void setType(BibliographicReferenceType type) {
        setOptional(BibliographicReferenceType.SECONDARY.equals(type));
    }

    public void delete() {
        setExecutionCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
