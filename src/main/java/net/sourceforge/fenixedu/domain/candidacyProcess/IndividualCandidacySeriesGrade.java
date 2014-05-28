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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import org.fenixedu.bennu.core.domain.Bennu;

public class IndividualCandidacySeriesGrade extends IndividualCandidacySeriesGrade_Base {

    public IndividualCandidacySeriesGrade() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Deprecated
    public boolean hasNotes() {
        return getNotes() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasIndividualCandidacy() {
        return getIndividualCandidacy() != null;
    }

    @Deprecated
    public boolean hasDegreeNature() {
        return getDegreeNature() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasAffinity() {
        return getAffinity() != null;
    }

}
