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

import org.fenixedu.bennu.core.domain.Bennu;

public class DegreeInfoCandidacy extends DegreeInfoCandidacy_Base {

    protected DegreeInfoCandidacy(final DegreeInfo degreeInfo) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setDegreeInfo(degreeInfo);
    }

    public void delete() {
        setDegreeInfo(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSelectionResultDeadline() {
        return getSelectionResultDeadline() != null;
    }

    @Deprecated
    public boolean hasDegreeInfo() {
        return getDegreeInfo() != null;
    }

    @Deprecated
    public boolean hasCandidacyDocuments() {
        return getCandidacyDocuments() != null;
    }

    @Deprecated
    public boolean hasCandidacyPeriod() {
        return getCandidacyPeriod() != null;
    }

    @Deprecated
    public boolean hasEnrolmentPeriod() {
        return getEnrolmentPeriod() != null;
    }

    @Deprecated
    public boolean hasAccessRequisites() {
        return getAccessRequisites() != null;
    }

    @Deprecated
    public boolean hasTestIngression() {
        return getTestIngression() != null;
    }

}
