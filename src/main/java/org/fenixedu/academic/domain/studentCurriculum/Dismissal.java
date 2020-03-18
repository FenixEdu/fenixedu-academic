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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.enrolment.DismissalCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.log.DismissalLog;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EnrolmentAction;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.google.common.collect.Sets;

public class Dismissal extends Dismissal_Base implements ICurriculumEntry {

    public Dismissal() {
        super();
    }

    public Dismissal(Credits credits, CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        init(credits, curriculumGroup, curricularCourse);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    protected void init(Credits credits, CurriculumGroup curriculumGroup) {
        if (credits == null || curriculumGroup == null) {
            throw new DomainException("error.dismissal.wrong.arguments");
        }
        setCredits(credits);
        setCurriculumGroup(curriculumGroup);
    }

    protected void init(final Credits credits, final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse) {
        if (curricularCourse == null) {
            throw new DomainException("error.dismissal.wrong.arguments");
        }
        checkCurriculumGroupCurricularCourse(credits, curriculumGroup, curricularCourse);
        checkIfAcademicPeriodOfExecutionIntervalIsValid(credits, curriculumGroup, curricularCourse);
        init(credits, curriculumGroup);
        setCurricularCourse(curricularCourse);
    }

    private void checkCurriculumGroupCurricularCourse(final Credits credits, final CurriculumGroup curriculumGroup,
            final CurricularCourse curricularCourse) {
        if (!(curriculumGroup instanceof NoCourseGroupCurriculumGroup)) {
            if (!curriculumGroup.getCurricularCoursesToDismissal(credits.getExecutionPeriod()).contains(curricularCourse)) {
                throw new DomainException("error.dismissal.invalid.curricular.course.to.dismissal",
                        curriculumGroup.getName().getContent(), curricularCourse.getName(),
                        credits.getExecutionPeriod().getQualifiedName());
            }
        }
    }

    private void checkIfAcademicPeriodOfExecutionIntervalIsValid(Credits credits, CurriculumGroup curriculumGroup,
            CurricularCourse curricularCourse) {
        if (!curricularCourse.getParentContextsByExecutionYear(credits.getExecutionInterval().getExecutionYear()).stream()
                .filter(ctx -> curriculumGroup.isNoCourseGroupCurriculumGroup()
                        || ctx.getParentCourseGroup() == curriculumGroup.getDegreeModule())
                .anyMatch(ctx -> ctx.getCurricularPeriod().getAcademicPeriod()
                        .equals(credits.getExecutionInterval().getAcademicPeriod()))) {
            throw new DomainException(
                    "error.dismissal.execution.interval.academic.period.does.not.match.context.curricular.period",
                    (curricularCourse.getCode() != null ? curricularCourse.getCode() + " - " : "") + curricularCourse.getName());
        }

    }

    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan,
            final CourseGroup courseGroup, final Collection<CurricularCourse> noEnrolCurricularCourses) {
        return new CreditsDismissal(credits, findCurriculumGroupForCourseGroup(studentCurricularPlan, courseGroup),
                noEnrolCurricularCourses);
    }

    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan,
            final CurriculumGroup curriculumGroup, final Collection<CurricularCourse> noEnrolCurricularCourses) {
        return new CreditsDismissal(credits, curriculumGroup, noEnrolCurricularCourses);
    }

    static private CurriculumGroup findCurriculumGroupForCourseGroup(final StudentCurricularPlan studentCurricularPlan,
            final CourseGroup courseGroup) {
        final CurriculumGroup curriculumGroup = studentCurricularPlan.findCurriculumGroupFor(courseGroup);
        if (curriculumGroup != null) {
            return curriculumGroup;
        }
        throw new DomainException("error.studentCurricularPlan.doesnot.have.courseGroup", courseGroup.getName());
    }

    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan,
            CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse) {
        return new Dismissal(credits, curriculumGroup, curricularCourse);
    }

    static protected Dismissal createNewOptionalDismissal(final Credits credits,
            final StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            final OptionalCurricularCourse optionalCurricularCourse, final Double ectsCredits) {
        return new OptionalDismissal(credits, curriculumGroup, optionalCurricularCourse, ectsCredits);
    }

    @Override
    public StringBuilder print(String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[D ").append(getDegreeModule() != null ? getDegreeModule().getName() : "").append(" ");
        builder.append(getEctsCredits()).append(" ects ]\n");
        return builder;
    }

    @Override
    public boolean isApproved() {
        return true;
    }

    @Override
    final public ExecutionYear getIEnrolmentsLastExecutionYear() {
        ExecutionYear result = null;

        for (final IEnrolment iEnrolment : this.getSourceIEnrolments()) {
            final ExecutionYear executionYear = iEnrolment.getExecutionYear();
            if (result == null || result.isBefore(executionYear)) {
                result = executionYear;
            }
        }

        return result;
    }

    public Collection<IEnrolment> getSourceIEnrolments() {
        return getCredits().getIEnrolments();
    }

    final public boolean hasSourceIEnrolments(final IEnrolment iEnrolment) {
        return getCredits().hasIEnrolments(iEnrolment);
    }

    @Override
    public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionInterval executionInterval) {
        return isValid(executionInterval) && hasCurricularCourse(getCurricularCourse(), curricularCourse, executionInterval);
    }

    @Override
    public boolean isValid(final ExecutionInterval executionInterval) {
        return executionInterval == null || getExecutionInterval() == null
                || getExecutionInterval().isBeforeOrEquals(executionInterval);
    }

    protected boolean isValid(final ExecutionYear executionYear) {
        return executionYear == null || getExecutionInterval() == null
                || getExecutionInterval().getExecutionYear().isBeforeOrEquals(executionYear);
    }

    @Override
    public Double getEctsCredits() {

        final Function<ICurriculumEntry, BigDecimal> provider = EctsAndWeightProviderRegistry.getEctsProvider(Dismissal.class);
        if (provider != null) {
            final BigDecimal providedValue = provider.apply(this);
            return providedValue != null ? providedValue.doubleValue() : null;
        }

        // FIXME must migrate Dismissal with optional curricular courses to
        // OptionalDismissal
        return getCurricularCourse().isOptionalCurricularCourse() ? getEnrolmentsEcts() : getCurricularCourse()
                .getEctsCredits(getExecutionInterval());
    }

    @Override
    final public BigDecimal getEctsCreditsForCurriculum() {

        final Function<ICurriculumEntry, BigDecimal> provider =
                EctsAndWeightProviderRegistry.getEctsForCurriculumProvider(Dismissal.class);
        if (provider != null) {
            return provider.apply(this);
        }

        return BigDecimal.valueOf(getEctsCredits());
    }

    @Override
    public double getAccumulatedEctsCredits(final ExecutionInterval executionInterval) {
        return getEctsCredits().doubleValue();
    }

    protected Double getEnrolmentsEcts() {
        return getCredits().getEnrolmentsEcts();
    }

    @Override
    public Double getAprovedEctsCredits() {
        return isExtraCurricular() ? Double.valueOf(0d) : getEctsCredits();
    }

    @Override
    public Double getEnroledEctsCredits(final ExecutionInterval executionInterval) {
        return Double.valueOf(0d);
    }

    @Override
    public Double getEnroledEctsCredits(ExecutionYear executionYear) {
        return Double.valueOf(0d);
    }

    @Override
    public void collectDismissals(final List<Dismissal> result) {
        result.add(this);
    }

    @Override
    public boolean isDismissal() {
        return true;
    }

    public boolean isSimilar(final Dismissal dismissal) {
        return hasSameDegreeModules(dismissal) && hasSameSourceIEnrolments(getSourceIEnrolments(), dismissal);
    }

    protected boolean hasSameDegreeModules(final Dismissal dismissal) {
        return (getDegreeModule() == dismissal.getDegreeModule()
                || getCurricularCourse().isEquivalent(dismissal.getCurricularCourse()));
    }

    protected boolean hasSameSourceIEnrolments(final Collection<IEnrolment> ienrolments, final Dismissal dismissal) {
        return ienrolments.containsAll(dismissal.getSourceIEnrolments())
                && ienrolments.size() == dismissal.getSourceIEnrolments().size();
    }

    @Override
    public Dismissal getDismissal(final CurricularCourse curricularCourse) {
        return (getCurricularCourse() == curricularCourse) ? this : null;
    }

    void deleteFromCredits() {
        createCurriculumLineLog(EnrolmentAction.UNENROL);
        setCredits(null);
        super.delete();
    }

    @Override
    public void delete() {
        createCurriculumLineLog(EnrolmentAction.UNENROL);

        final Credits credits = getCredits();
        setCredits(null);
        if (credits != null && credits.getDismissalsSet().isEmpty()) {
            credits.delete();
        }
        super.delete();
    }

    @Override
    public ConclusionValue isConcluded(final ExecutionYear executionYear) {
        return ConclusionValue.create(executionYear == null || getExecutionInterval() == null
                || getExecutionInterval().getExecutionYear().isBeforeOrEquals(executionYear));
    }

    @Override
    public Double getCreditsConcluded(ExecutionYear executionYear) {
        return isConcluded(executionYear).value() && !getCredits().isTemporary() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    public YearMonthDay calculateConclusionDate() {

        if (getCredits().getOfficialDate() != null) {
            return new YearMonthDay(getCredits().getOfficialDate());
        }

        final SortedSet<IEnrolment> iEnrolments = new TreeSet<IEnrolment>(IEnrolment.COMPARATOR_BY_APPROVEMENT_DATE);
        iEnrolments.addAll(getSourceIEnrolments());

        final YearMonthDay beginDate = getExecutionInterval().getBeginDateYearMonthDay();
        if (!iEnrolments.isEmpty()) {
            final IEnrolment enrolment = iEnrolments.last();
            final YearMonthDay approvementDate = enrolment.getApprovementDate();
            return approvementDate != null ? approvementDate : beginDate;
        } else {
            return beginDate;
        }
    }

    @Override
    final public Curriculum getCurriculum(final DateTime when, final ExecutionYear year) {

        if (wasCreated(when) && isValid(year)) {
            return getCredits().getCurriculum(this, when, year);

        } else if (getCredits().isInternalSubstitution()) {
            // Evaluate each origin creation date and add them to averageEnrolmentRelatedEntries 
            return getCredits().getCurriculum(this, when, year);

        } else {
            return Curriculum.createEmpty(this, year);
        }
    }

    @Override
    public Grade getGrade() {
        return getCredits().isEquivalence() ? getCredits().getGrade() : Grade.createEmptyGrade();
    }

    @Override
    public String getGradeValue() {
        return getGrade().getValue();
    }

    @Override
    public Set<CurriculumLine> getCurriculumLinesForCurriculum(final StudentCurricularPlan studentCurricularPlan) {
        return getCredits().isEquivalence() ? Sets.newHashSet(this) : Sets.newHashSet();
    }

    public Double getWeigth() {

        final Function<ICurriculumEntry, BigDecimal> provider = EctsAndWeightProviderRegistry.getWeightProvider(Dismissal.class);
        if (provider != null) {
            final BigDecimal providedValue = provider.apply(this);
            return providedValue != null ? providedValue.doubleValue() : null;
        }

        return getCredits().isEquivalence() ? getEctsCredits() : null;
    }

    @Override
    final public BigDecimal getWeigthForCurriculum() {

        final Function<ICurriculumEntry, BigDecimal> provider =
                EctsAndWeightProviderRegistry.getWeightForCurriculumProvider(Dismissal.class);
        if (provider != null) {
            return provider.apply(this);
        }

        return BigDecimal.valueOf(getWeigth());
    }

    @Override
    public String getCode() {
        return hasCurricularCourse() ? getCurricularCourse().getCode() : null;
    }

    /**
     * @deprecated use {@link #getExecutionInterval()} instead.
     */
    @Deprecated
    @Override
    public ExecutionInterval getExecutionPeriod() {
        return getCredits().getExecutionInterval();
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionInterval executionInterval) {
        if (executionInterval != null && executionInterval != getExecutionInterval()) {
            return Collections.EMPTY_SET;
        }

        final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>(1);
        result.add(new DismissalCurriculumModuleWrapper(this, executionInterval));
        return result;
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
        new DismissalLog(action, getRegistration(), getCurricularCourse(), getCredits(), getExecutionPeriod(), getCurrentUser());
    }

    public String getEnrolmentTypeName() {
        if (isExtraCurricular()) {
            return "EXTRA_CURRICULAR_ENROLMENT";
        } else if (isOptional()) {
            return "ENROLMENT_IN_OPTIONAL_DEGREE_MODULE";
        } else {
            return "COMPULSORY_ENROLMENT";
        }
    }

    public boolean isAnual() {
        final CurricularCourse curricularCourse = getCurricularCourse();
        return curricularCourse != null && curricularCourse.isAnual();
    }

    @Override
    public String getModuleTypeName() {
        return BundleUtil.getString(Bundle.ENUMERATION, this.getClass().getName());
    }

    public ExecutionInterval getExecutionInterval() {
        return getExecutionPeriod();
    }

}
