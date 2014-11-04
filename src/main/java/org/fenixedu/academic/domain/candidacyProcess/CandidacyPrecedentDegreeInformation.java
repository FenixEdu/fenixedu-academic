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
package org.fenixedu.academic.domain.candidacyProcess;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

@Deprecated
abstract public class CandidacyPrecedentDegreeInformation extends CandidacyPrecedentDegreeInformation_Base {

    protected CandidacyPrecedentDegreeInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean isInternal() {
        return false;
    }

    public boolean isExternal() {
        return false;
    }

    abstract public String getDegreeDesignation();

    abstract protected Integer getConclusionYear();

    abstract public LocalDate getConclusionDate();

    abstract public Unit getInstitution();

    abstract public String getConclusionGrade();

    public String getDegreeAndInstitutionName() {
        return getDegreeDesignation() + " / " + getInstitution().getName();
    }

    abstract public Integer getNumberOfEnroledCurricularCourses();

    abstract public Integer getNumberOfApprovedCurricularCourses();

    abstract public BigDecimal getGradeSum();

    abstract public BigDecimal getApprovedEcts();

    abstract public BigDecimal getEnroledEcts();

    protected boolean hasSchoolLevel() {
        return getSchoolLevel() != null;
    }

    protected boolean hasOtherSchoolLevel() {
        return getOtherSchoolLevel() != null && !getOtherSchoolLevel().isEmpty();
    }

}
