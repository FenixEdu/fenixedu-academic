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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsTableIndex;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
            final Grade grade, final ExecutionSemester executionSemester, final YearMonthDay evaluationDate,
            final Double ectsCredits) {
        this();

        checkConstraints(registration, externalCurricularCourse, executionSemester, grade, ectsCredits);
        checkIfCanCreateExternalEnrolment(registration, externalCurricularCourse);

        setRegistration(registration);
        setExternalCurricularCourse(externalCurricularCourse);
        setGrade(grade);
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
        if (grade == null || grade.isEmpty()) {
            throw new DomainException("error.externalEnrolment.invalid.grade");
        }
        if (ectsCredits == null) {
            throw new DomainException("error.externalEnrolment.ectsCredits.cannot.be.null");
        }
    }

    public void edit(final Registration registration, final Grade grade, final ExecutionSemester executionSemester,
            final YearMonthDay evaluationDate, final Double ectsCredits) {

        if (registration != getRegistration()) {
            checkIfCanCreateExternalEnrolment(registration, getExternalCurricularCourse());
        }

        checkConstraints(registration, getExternalCurricularCourse(), executionSemester, grade, ectsCredits);

        setRegistration(registration);
        setGrade(grade);
        setExecutionPeriod(executionSemester);
        setEvaluationDate(evaluationDate);
        setEctsCredits(ectsCredits);
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString(getExternalCurricularCourse().getName());
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

        setExecutionPeriod(null);
        setExternalCurricularCourse(null);
        setRegistration(null);
        setRootDomainObject(null);
        getNotNeedToEnrollCurricularCourses().clear();
        super.deleteDomainObject();
    }

    private void checkRulesToDelete() {
        if (hasAnyEnrolmentWrappers()) {
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
        Grade grade = getGrade();
        Set<Dismissal> dismissals = new HashSet<Dismissal>();
        for (EnrolmentWrapper wrapper : getEnrolmentWrappersSet()) {
            if (wrapper.getCredits().getStudentCurricularPlan().equals(scp)) {
                for (Dismissal dismissal : wrapper.getCredits().getDismissalsSet()) {
                    dismissals.add(dismissal);
                }
            }
        }
        Dismissal dismissal = dismissals.iterator().next();
        if (dismissals.size() == 1) {
            if (dismissal instanceof OptionalDismissal || dismissal instanceof CreditsDismissal) {
                return EctsTableIndex.convertGradeToEcts(scp.getDegree(), dismissal, grade, processingDate);
            } else {
                CurricularCourse curricularCourse = dismissal.getCurricularCourse();
                if (curricularCourse != null) {
                    return EctsTableIndex.convertGradeToEcts(curricularCourse, dismissal, grade, processingDate);
                } else {
                    return EctsTableIndex.convertGradeToEcts(scp.getDegree(), dismissal, grade, processingDate);
                }
            }
        } else {
            // if more than one exists we can't base the conversion on the
            // origin, so step up to the degree, on a context based on one
            // of the sources.
            return EctsTableIndex.convertGradeToEcts(scp.getDegree(), dismissal, grade, processingDate);
        }
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

    @Override
    public BigDecimal getWeigthTimesGrade() {
        return getGrade().isNumeric() ? getWeigthForCurriculum().multiply(getGrade().getNumericValue()) : null;
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
    public MultiLanguageString getPresentationName() {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse> getNotNeedToEnrollCurricularCourses() {
        return getNotNeedToEnrollCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyNotNeedToEnrollCurricularCourses() {
        return !getNotNeedToEnrollCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolmentWrapper> getEnrolmentWrappers() {
        return getEnrolmentWrappersSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentWrappers() {
        return !getEnrolmentWrappersSet().isEmpty();
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRegime() {
        return getRegime() != null;
    }

    @Deprecated
    public boolean hasGrade() {
        return getGrade() != null;
    }

    @Deprecated
    public boolean hasCreationDateDateTime() {
        return getCreationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasExternalCurricularCourse() {
        return getExternalCurricularCourse() != null;
    }

    @Deprecated
    public boolean hasCreatedBy() {
        return getCreatedBy() != null;
    }

    @Override
    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasEctsCredits() {
        return getEctsCredits() != null;
    }

    @Deprecated
    public boolean hasEvaluationDate() {
        return getEvaluationDate() != null;
    }

}
