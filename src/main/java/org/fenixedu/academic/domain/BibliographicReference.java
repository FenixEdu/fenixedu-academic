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

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class BibliographicReference extends BibliographicReference_Base {

    public static final Comparator<BibliographicReference> COMPARATOR_BY_ORDER = new Comparator<BibliographicReference>() {

        private ComparatorChain chain = null;

        @Override
        public int compare(BibliographicReference one, BibliographicReference other) {
            if (this.chain == null) {
                chain = new ComparatorChain();

                chain.addComparator(new BeanComparator("referenceOrder", new NullComparator(true)));
                chain.addComparator(new BeanComparator("title"));
                chain.addComparator(new BeanComparator("year"));
                chain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
            }

            return chain.compare(one, other);
        }
    };

    public BibliographicReference() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void edit(final String title, final String authors, final String reference, final String year, final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null) {
            throw new NullPointerException();
        }

        setTitle(title);
        setAuthors(authors);
        setReference(reference);
        setYear(year);
        setOptional(optional);
        ExecutionCourse executionCourse = getExecutionCourse();
        final String type;
        if (optional) {
            type = BundleUtil.getString(Bundle.APPLICATION, "option.bibliographicReference.optional");
        } else {
            type = BundleUtil.getString(Bundle.APPLICATION, "option.bibliographicReference.recommended");
        }
        CurricularManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.curricular.bibliographic.edited", type, title, executionCourse.getNameI18N().getContent(),
                executionCourse.getDegreePresentationString());
    }

    public void delete() {
        ExecutionCourse executionCourse = getExecutionCourse();
        String blBibliographicReference = getTitle();
        Boolean optional = getOptional();

        final String type;
        if (optional) {
            type = BundleUtil.getString(Bundle.APPLICATION, "option.bibliographicReference.optional");
        } else {
            type = BundleUtil.getString(Bundle.APPLICATION, "option.bibliographicReference.recommended");
        }

        CurricularManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.curricular.bibliographic.removed", type, blBibliographicReference,
                executionCourse.getNameI18N().getContent(), executionCourse.getDegreePresentationString());

        setExecutionCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isOptional() {
        return getOptional() == null || getOptional();
    }

}
