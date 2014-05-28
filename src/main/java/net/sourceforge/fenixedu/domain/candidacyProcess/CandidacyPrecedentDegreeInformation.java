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

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

@Deprecated
abstract public class CandidacyPrecedentDegreeInformation extends CandidacyPrecedentDegreeInformation_Base {

    protected CandidacyPrecedentDegreeInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean hasInstitution() {
        return getInstitution() != null;
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

    public boolean hasConclusionDate() {
        return getConclusionDate() != null;
    }

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

    @Deprecated
    public boolean hasCandidacy() {
        return getCandidacy() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCountry() {
        return getCountry() != null;
    }

}
