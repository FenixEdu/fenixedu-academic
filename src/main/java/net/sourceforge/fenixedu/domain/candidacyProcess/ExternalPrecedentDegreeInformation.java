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

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.LocalDate;

@Deprecated
public class ExternalPrecedentDegreeInformation extends ExternalPrecedentDegreeInformation_Base {

    private ExternalPrecedentDegreeInformation() {
        super();
    }

    private ExternalPrecedentDegreeInformation(final IndividualCandidacy candidacy, final String degreeDesignation,
            final LocalDate conclusionDate, final Unit sourceInstitution, final String conclusionGrade, Country country) {
        this();
        checkParameters(candidacy, degreeDesignation, sourceInstitution, conclusionGrade);
        setDegreeDesignation(degreeDesignation);
        setConclusionDate(conclusionDate);
        setSourceInstitution(sourceInstitution);
        setConclusionGrade(conclusionGrade);
        setCountry(country);
    }

    private void checkParameters(final IndividualCandidacy candidacy, final String degreeDesignation,
            final Unit sourceInstitution, final String conclusionGrade) {

        if (candidacy == null) {
            throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.candidacy");
        }

        if (degreeDesignation == null || degreeDesignation.length() == 0) {
            throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.degreeDesignation");
        }

        if (sourceInstitution == null) {
            throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.institution");
        }
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    public void init(final Integer numberOfEnroledCurricularCourses, final Integer numberOfApprovedCurricularCourses,
            final BigDecimal gradeSum, final BigDecimal approvedEcts, final BigDecimal enroledEcts) {
        checkParameters(numberOfEnroledCurricularCourses, numberOfApprovedCurricularCourses, gradeSum, approvedEcts, enroledEcts);
        setNumberOfEnroledCurricularCourses(numberOfEnroledCurricularCourses);
        setNumberOfApprovedCurricularCourses(numberOfApprovedCurricularCourses);
        setGradeSum(gradeSum);
        setApprovedEcts(approvedEcts);
        setEnroledEcts(enroledEcts);
    }

    private void checkParameters(final Integer numberOfEnroledCurricularCourses, final Integer numberOfApprovedCurricularCourses,
            final BigDecimal gradeSum, final BigDecimal approvedEcts, final BigDecimal enroledEcts) {
        if (numberOfEnroledCurricularCourses != null && numberOfEnroledCurricularCourses.intValue() == 0) {
            throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.numberOfEnroledCurricularCourses");
        }
        if (numberOfApprovedCurricularCourses != null && numberOfApprovedCurricularCourses.intValue() == 0) {
            throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.numberOfApprovedCurricularCourses");
        }
        checkBigDecimal(gradeSum, "gradeSum");
        checkBigDecimal(approvedEcts, "approvedEcts");
        checkBigDecimal(enroledEcts, "enroledEcts");
    }

    private void checkBigDecimal(final BigDecimal value, final String property) {
        if (value != null && value.signum() == 0) {
            throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid." + property);
        }
    }

    @Override
    public Integer getConclusionYear() {
        if (super.getConclusionYear() != null) {
            return super.getConclusionYear();
        }
        return hasConclusionDate() ? getConclusionDate().getYear() : null;
    }

    @Deprecated
    public boolean hasGradeSum() {
        return getGradeSum() != null;
    }

    @Deprecated
    public boolean hasNumberOfEnroledCurricularCourses() {
        return getNumberOfEnroledCurricularCourses() != null;
    }

    @Deprecated
    public boolean hasEnroledEcts() {
        return getEnroledEcts() != null;
    }

    @Deprecated
    public boolean hasSourceInstitution() {
        return getSourceInstitution() != null;
    }

    @Override
    @Deprecated
    public boolean hasConclusionDate() {
        return getConclusionDate() != null;
    }

    @Deprecated
    public boolean hasApprovedEcts() {
        return getApprovedEcts() != null;
    }

    @Override
    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

    @Deprecated
    public boolean hasNumberOfApprovedCurricularCourses() {
        return getNumberOfApprovedCurricularCourses() != null;
    }

    @Deprecated
    public boolean hasConclusionGrade() {
        return getConclusionGrade() != null;
    }

    @Deprecated
    public boolean hasDegreeDesignation() {
        return getDegreeDesignation() != null;
    }

    @Deprecated
    public boolean hasConclusionYear() {
        return getConclusionYear() != null;
    }

}
