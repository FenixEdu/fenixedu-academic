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

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.AverageType;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;

import org.joda.time.LocalDate;
import org.joda.time.base.BasePartial;

@Deprecated
public class InstitutionPrecedentDegreeInformation extends InstitutionPrecedentDegreeInformation_Base {

    private InstitutionPrecedentDegreeInformation() {
        super();
    }

    private InstitutionPrecedentDegreeInformation(final IndividualCandidacy candidacy,
            final StudentCurricularPlan studentCurricularPlan) {
        this(candidacy, studentCurricularPlan, null);
    }

    private InstitutionPrecedentDegreeInformation(final IndividualCandidacy candidacy,
            final StudentCurricularPlan studentCurricularPlan, final CycleType cycleType) {
        this();
        checkParameters(candidacy, studentCurricularPlan, cycleType);
        setStudentCurricularPlan(studentCurricularPlan);
        setCycleType(cycleType);
    }

    private void checkParameters(final IndividualCandidacy candidacy, final StudentCurricularPlan studentCurricularPlan,
            final CycleType cycleType) {
        if (candidacy == null) {
            throw new DomainException("error.InstitutionPrecedentDegreeInformation.invalid.candidacy");
        }
        if (studentCurricularPlan == null) {
            throw new DomainException("error.InstitutionPrecedentDegreeInformation.invalid.studentCurricularPlan");
        }
        if (studentCurricularPlan.isBolonhaDegree() && cycleType == null) {
            throw new DomainException("error.InstitutionPrecedentDegreeInformation.invalid.cycleType");
        }
    }

    private boolean isBolonha() {
        return getStudentCurricularPlan().isBolonhaDegree();
    }

    @Override
    public LocalDate getConclusionDate() {
        final BasePartial date =
                isBolonha() ? (getStudentCurricularPlan().getCycle(getCycleType()) != null ? getStudentCurricularPlan()
                        .getConclusionDate(getCycleType()) : null) : getRegistration().getConclusionDate();
        return date != null ? new LocalDate(date) : null;
    }

    @Override
    protected Integer getConclusionYear() {
        final LocalDate localDate = getConclusionDate();
        return localDate != null ? localDate.getYear() : null;
    }

    @Override
    public String getConclusionGrade() {
        final Integer result =
                isBolonha() ? (getStudentCurricularPlan().getCycle(getCycleType()) != null ? getStudentCurricularPlan().getCycle(
                        getCycleType()).getFinalAverage() : null) : getRegistration().getFinalAverage();
        return (result == null) ? null : String.valueOf(result);
    }

    @Override
    public String getDegreeDesignation() {
        return getStudentCurricularPlan().getName();
    }

    @Override
    public Unit getInstitution() {
        return getRootDomainObject().getInstitutionUnit();
    }

    public Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

    @Override
    public Integer getNumberOfEnroledCurricularCourses() {
        return getStudentCurricularPlan().getRoot().getNumberOfAllEnroledCurriculumLines();
    }

    @Override
    public Integer getNumberOfApprovedCurricularCourses() {
        return getStudentCurricularPlan().getRoot().getNumberOfAllApprovedCurriculumLines();
    }

    @Override
    public BigDecimal getGradeSum() {
        final Curriculum curriculum = getStudentCurricularPlan().getRoot().getCurriculum();
        curriculum.setAverageType(AverageType.SIMPLE);
        return curriculum.getSumPiCi();
    }

    @Override
    public BigDecimal getApprovedEcts() {
        return BigDecimal.valueOf(getStudentCurricularPlan().getRoot().getAprovedEctsCredits());
    }

    @Override
    public BigDecimal getEnroledEcts() {
        return BigDecimal.valueOf(getStudentCurricularPlan().getRoot().getEctsCredits());
    }

    @Override
    public boolean isInternal() {
        return true;
    }

    @Deprecated
    public boolean hasStudentCurricularPlan() {
        return getStudentCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasCycleType() {
        return getCycleType() != null;
    }

}
