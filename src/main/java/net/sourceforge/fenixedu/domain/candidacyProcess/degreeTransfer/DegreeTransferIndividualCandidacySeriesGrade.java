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
package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

public class DegreeTransferIndividualCandidacySeriesGrade extends DegreeTransferIndividualCandidacySeriesGrade_Base {

    public DegreeTransferIndividualCandidacySeriesGrade() {
        super();
    }

    public boolean isClean() {
        if (!(this.getAffinity() == null && this.getDegreeNature() == null && this.getApprovedEctsRate() == null
                && this.getGradeRate() == null && this.getSeriesCandidacyGrade() == null)) {
            return false;
        } else {
            return true;
        }
    }

    public void delete() {
        setIndividualCandidacy(null);
        setDegree(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasApprovedEctsRate() {
        return getApprovedEctsRate() != null;
    }

    @Deprecated
    public boolean hasSeriesCandidacyGrade() {
        return getSeriesCandidacyGrade() != null;
    }

    @Deprecated
    public boolean hasGradeRate() {
        return getGradeRate() != null;
    }

}
