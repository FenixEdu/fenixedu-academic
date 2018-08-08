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
package org.fenixedu.academic.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Comparator;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.ExternalCurricularCourse;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.EctsConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EmptyConversionTable;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class ExternalEnrolment extends ExternalEnrolment_Base implements IEnrolment {

    static public final Comparator<ExternalEnrolment> COMPARATOR_BY_NAME = new Comparator<ExternalEnrolment>() {
        @Override
        public int compare(ExternalEnrolment o1, ExternalEnrolment o2) {
            int result = o1.getName().compareTo(o2.getName());
            return (result != 0) ? result : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    static final public Comparator<ExternalEnrolment> COMPARATOR_BY_EXECUTION_PERIOD_AND_EVALUATION_DATE =
            new Comparator<ExternalEnrolment>() {
                @Override
                public int compare(ExternalEnrolment o1, ExternalEnrolment o2) {
                    final ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain.addComparator(ExternalEnrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
                    comparatorChain.addComparator(ExternalEnrolment.COMPARATOR_BY_APPROVEMENT_DATE);

                    return comparatorChain.compare(o1, o2);
                }
            };

    protected ExternalEnrolment() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCreationDateDateTime(new DateTime());
        if (AccessControl.getPerson() != null) {
            setCreatedBy(AccessControl.getPerson().getUsername());
        }
    }

    public ExternalEnrolment(final Registration registration, final ExternalCurricularCourse externalCurricularCourse,
            final Grade grade, final Grade normalizedGrade, final ExecutionSemester executionSemester, final YearMonthDay evaluationDate,
            final Double ectsCredits) {
        this();

        checkConstraints(registration, externalCurricularCourse, executionSemester, grade, ectsCredits);
        checkIfCanCreateExternalEnrolment(registration, externalCurricularCourse);

        setRegistration(registration);
        setExternalCurricularCourse(externalCurricularCourse);
        setGrade(grade);
        setNormalizedEctsGrade(normalizedGrade);
        setEctsConversionTable(EmptyConversionTable.getInstance());
        setExecutionPeriod(executionSemester);
        setEvaluationDate(evaluationDate);
        setEctsCredits(ectsCredits);
    }

    private void checkIfCanCreateExternalEnrolment(final Registration registration,
            final ExternalCurricularCourse externalCurricularCourse) {
        for (final ExternalEnrolment externalEnrolment : registration.getExternalEnrolmentsSet()) {
            if (externalEnrolment.getExternalCurricularCourse() == externalCurricularCourse) {
                throw new DomainException(
                        "error.studentCurriculum.ExternalEnrolment.already.exists.externalEnrolment.for.externalCurricularCourse",
                        externalCurricularCourse.getName());
            }
        }
    }

    private void checkConstraints(final Registration registration, final ExternalCurricularCourse externalCurricularCourse,
            final ExecutionSemester executionSemester, final Grade grade, final Double ectsCredits) {
        if (registration == null) {
            throw new DomainException("error.externalEnrolment.student.cannot.be.null");
        }
        if (externalCurricularCourse == null) {
            throw new DomainException("error.externalEnrolment.externalCurricularCourse.cannot.be.null");
        }
        if (executionSemester == null) {
            throw new DomainException("error.externalEnrolment.executionPeriod.cannot.be.null");
        }
        if (grade == null) {
            throw new DomainException("error.externalEnrolment.invalid.grade");
        }
        if (ectsCredits == null) {
            throw new DomainException("error.externalEnrolment.ectsCredits.cannot.be.null");
        }
    }

    public void edit(final Registration registration, final Grade grade, final Grade normalizedGrade, final ExecutionSemester executionSemester,
            final YearMonthDay evaluationDate, final Double ectsCredits) {

        if (registration != getRegistration()) {
            checkIfCanCreateExternalEnrolment(registration, getExternalCurricularCourse());
        }

        checkConstraints(registration, getExternalCurricularCourse(), executionSemester, grade, ectsCredits);

        setRegistration(registration);
        setGrade(grade);
        setNormalizedEctsGrade(normalizedGrade);
        setEctsConversionTable(EmptyConversionTable.getInstance());
        setExecutionPeriod(executionSemester);
        setEvaluationDate(evaluationDate);
        setEctsCredits(ectsCredits);
    }

    @Override
    public LocalizedString getName() {
        return new LocalizedString(I18N.getLocale(), getExternalCurricularCourse().getName());
    }

    @Override
    public String getCode() {
        return getExternalCurricularCourse().getCode();
    }

    public String getFullPathName() {
        return getExternalCurricularCourse().getFullPathName();
    }

    @Override
    public String getDescription() {
        return getFullPathName();
    }

    @Override
    public void delete() {
        checkRulesToDelete();

        setEctsConversionTable(null);
        setExecutionPeriod(null);
        setExternalCurricularCourse(null);
        setRegistration(null);
        setRootDomainObject(null);
        getNotNeedToEnrollCurricularCoursesSet().clear();
        super.deleteDomainObject();
    }

    private void checkRulesToDelete() {
        if (!getEnrolmentWrappersSet().isEmpty()) {
            throw new DomainException("error.Enrolment.is.origin.in.some.Equivalence");
        }
    }

    @Override
    final public boolean isApproved() {
        return true;
    }

    @Override
    final public boolean isEnroled() {
        return true;
    }

    @Override
    final public boolean isExternalEnrolment() {
        return true;
    }

    @Override
    final public boolean isEnrolment() {
        return false;
    }

    @Override
    public Integer getFinalGrade() {
        final String grade = getGradeValue();
        return (StringUtils.isEmpty(grade) || !StringUtils.isNumeric(grade)) ? null : Integer.valueOf(grade);
    }

    @Override
    final public ExecutionYear getExecutionYear() {
        return getExecutionPeriod() != null ? getExecutionPeriod().getExecutionYear() : null;
    }

    @Override
    final public YearMonthDay getApprovementDate() {
        return getEvaluationDate() == null && hasExecutionPeriod() ? getExecutionPeriod().getEndDateYearMonthDay() : getEvaluationDate();
    }

    @Override
    public Unit getAcademicUnit() {
        return getExternalCurricularCourse().getAcademicUnit();
    }

    @Override
    public String getGradeValue() {
        return getGrade().getValue();
    }

    @Override
    public Grade getEctsGrade(StudentCurricularPlan scp, DateTime processingDate) {
        return getNormalizedEctsGrade();
    }

    @Override
    public EctsConversionTable getEctsConversionTable(StudentCurricularPlan scp, DateTime processingDate) {
        return getEctsConversionTable();
    }

    @Override
    final public BigDecimal getEctsCreditsForCurriculum() {
        return BigDecimal.valueOf(getEctsCredits());
    }

    @Override
    public Double getWeigth() {
        return getEctsCredits();
    }

    @Override
    final public BigDecimal getWeigthForCurriculum() {
        return BigDecimal.valueOf(getWeigth());
    }

    /**
     * There is no thesis associated to an external enrolment.
     * 
     * @return <code>null</code>
     */
    @Override
    public Thesis getThesis() {
        return null;
    }

    public boolean isResultOfMobility() {
        if (!hasExecutionPeriod()) {
            return false;
        }

        return getRegistration().getRegistrationStatesTypes(getExecutionYear()).contains(RegistrationStateType.MOBILITY);
    }

    @Override
    public boolean isAnual() {
        return getRegime() != null ? getRegime() == RegimeType.ANUAL : false;
    }

    @Override
    public String getEnrolmentTypeName() {
        return "COMPULSORY_ENROLMENT";
    }

    @Override
    public LocalizedString getPresentationName() {
        return getName();
    }

    @Deprecated
    public java.util.Date getCreationDate() {
        org.joda.time.DateTime dt = getCreationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreationDate(java.util.Date date) {
        if (date == null) {
            setCreationDateDateTime(null);
        } else {
            setCreationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Override
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Override
    public void setNormalizedEctsGrade(Grade normalizedEctsGrade) {
        super.setNormalizedEctsGrade(normalizedEctsGrade);
        super.setEctsConversionTable(EmptyConversionTable.getInstance());
    }
}
