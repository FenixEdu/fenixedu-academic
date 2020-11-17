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
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedOptionalCurricularCourse;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import com.google.common.collect.Sets;

public class Credits extends Credits_Base {

    public Credits() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Credits(StudentCurricularPlan studentCurricularPlan, Collection<SelectedCurricularCourse> dismissals,
            Collection<IEnrolment> enrolments, ExecutionInterval executionInterval) {
        this();
        init(studentCurricularPlan, dismissals, enrolments, executionInterval);
    }

    public Credits(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
            Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, ExecutionInterval executionInterval) {
        this();
        init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionInterval);
    }

    public Credits(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            Collection<IEnrolment> enrolments, Double credits, ExecutionInterval executionInterval) {
        this();
        init(studentCurricularPlan, curriculumGroup, enrolments, new HashSet<CurricularCourse>(0), credits, executionInterval);
    }

    final protected void initExecutionPeriod(ExecutionInterval executionInterval) {
        if (executionInterval == null) {
            throw new DomainException("error.credits.wrong.arguments");
        }
        setExecutionPeriod(executionInterval);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
            Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, ExecutionInterval executionInterval) {
        if (studentCurricularPlan == null || courseGroup == null || credits == null) {
            throw new DomainException("error.credits.wrong.arguments");
        }

        checkGivenCredits(studentCurricularPlan, courseGroup, credits, executionInterval);
        initExecutionPeriod(executionInterval);

        setStudentCurricularPlan(studentCurricularPlan);
        setGivenCredits(credits);
        addEnrolments(enrolments);

        Dismissal.createNewDismissal(this, studentCurricularPlan, courseGroup, noEnrolCurricularCourses);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            Collection<IEnrolment> enrolments, Collection<CurricularCourse> noEnrolCurricularCourses, Double credits,
            ExecutionInterval executionInterval) {
        if (studentCurricularPlan == null || curriculumGroup == null || credits == null) {
            throw new DomainException("error.credits.wrong.arguments");
        }

        initExecutionPeriod(executionInterval);

        setStudentCurricularPlan(studentCurricularPlan);
        setGivenCredits(credits);
        addEnrolments(enrolments);

        Dismissal.createNewDismissal(this, studentCurricularPlan, curriculumGroup, noEnrolCurricularCourses);
    }

    private void checkGivenCredits(final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup,
            final Double credits, final ExecutionInterval executionInterval) {
        if (!allowsEctsCredits(studentCurricularPlan, courseGroup, executionInterval, credits.doubleValue())) {
            throw new DomainException("error.credits.invalid.credits", credits.toString());
        }
    }

    private boolean allowsEctsCredits(final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup,
            final ExecutionInterval executionInterval, final double ectsCredits) {
        final double ectsCreditsForCourseGroup =
                studentCurricularPlan.getCreditsConcludedForCourseGroup(courseGroup).doubleValue();
        if (ectsCredits + ectsCreditsForCourseGroup > courseGroup.getMaxEctsCredits(executionInterval).doubleValue()) {
            return false;
        }
        if (courseGroup.isCycleCourseGroup() || courseGroup.isRoot()) {
            return true;
        }
        for (final Context context : courseGroup.getParentContextsSet()) {
            if (context.isOpen(executionInterval)) {
                if (allowsEctsCredits(studentCurricularPlan, context.getParentCourseGroup(), executionInterval, ectsCredits)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, Collection<SelectedCurricularCourse> dismissals,
            Collection<IEnrolment> enrolments, ExecutionInterval executionInterval) {
        if (studentCurricularPlan == null || dismissals == null || dismissals.isEmpty()) {
            throw new DomainException("error.credits.wrong.arguments");
        }

        initExecutionPeriod(executionInterval);
        setStudentCurricularPlan(studentCurricularPlan);
        addEnrolments(enrolments);

        for (final SelectedCurricularCourse selectedCurricularCourse : dismissals) {
            if (selectedCurricularCourse.isOptional()) {
                final SelectedOptionalCurricularCourse selectedOptionalCurricularCourse =
                        (SelectedOptionalCurricularCourse) selectedCurricularCourse;
                Dismissal.createNewOptionalDismissal(this, studentCurricularPlan,
                        selectedOptionalCurricularCourse.getCurriculumGroup(),
                        selectedOptionalCurricularCourse.getCurricularCourse(), selectedOptionalCurricularCourse.getCredits());
            } else {
                Dismissal.createNewDismissal(this, studentCurricularPlan, selectedCurricularCourse.getCurriculumGroup(),
                        selectedCurricularCourse.getCurricularCourse());
            }
        }
    }

    private void addEnrolments(final Collection<IEnrolment> enrolments) {
        if (enrolments != null) {
            for (final IEnrolment enrolment : enrolments) {
                EnrolmentWrapper.create(this, enrolment);
            }
        }
    }

    static protected boolean isBefore(final IEnrolment enrolment, final ExecutionYear year) {
        return year == null || enrolment.getExecutionYear() == null || enrolment.getExecutionYear().isBefore(year);
    }

    protected Set<EnrolmentWrapper> getEnrolmentsSetBefore(final ExecutionYear executionYear) {
        final Set<EnrolmentWrapper> result = Sets.newHashSet();

        for (final EnrolmentWrapper wrapper : getEnrolmentsSet()) {
            final IEnrolment enrolment = wrapper.getIEnrolment();
            if (enrolment != null && isBefore(enrolment, executionYear)) {
                result.add(wrapper);
            }
        }

        return result;
    }

    final public Collection<IEnrolment> getIEnrolments() {
        final Set<IEnrolment> result = new HashSet<IEnrolment>();
        for (final EnrolmentWrapper enrolmentWrapper : this.getEnrolmentsSet()) {
            IEnrolment enrolment = enrolmentWrapper.getIEnrolment();
            if (enrolment != null) {
                result.add(enrolmentWrapper.getIEnrolment());
            }
        }
        return result;
    }

    final public boolean hasIEnrolments(final IEnrolment iEnrolment) {
        for (final EnrolmentWrapper enrolmentWrapper : this.getEnrolmentsSet()) {
            if (enrolmentWrapper.getIEnrolment() == iEnrolment) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyIEnrolments() {
        return !getEnrolmentsSet().isEmpty();
    }

    @Override
    final public Double getGivenCredits() {
        if (super.getGivenCredits() == null) {
            BigDecimal bigDecimal = BigDecimal.ZERO;
            for (Dismissal dismissal : getDismissalsSet()) {
                bigDecimal = bigDecimal.add(new BigDecimal(dismissal.getEctsCredits()));
            }
            return Double.valueOf(bigDecimal.doubleValue());
        }
        return super.getGivenCredits();
    }

    public String getGivenGrade() {
        return null;
    }

    public Grade getGrade() {
        return null;
    }

    final public void delete() {
        disconnect();
        super.deleteDomainObject();
    }

    protected void disconnect() {
        for (; !getDismissalsSet().isEmpty(); getDismissalsSet().iterator().next().deleteFromCredits()) {
            ;
        }

        for (; !getEnrolmentsSet().isEmpty(); getEnrolmentsSet().iterator().next().delete()) {
            ;
        }

        setStudentCurricularPlan(null);
        setRootDomainObject(null);
        setExecutionPeriod(null);
    }

    final public Double getEnrolmentsEcts() {
        Double result = 0d;
        for (final IEnrolment enrolment : getIEnrolments()) {
            result = result + enrolment.getEctsCredits();
        }
        return result;
    }

    final public boolean hasGivenCredits() {
        return getGivenCredits() != null;
    }

    final public boolean hasGivenCredits(final Double ectsCredits) {
        return hasGivenCredits() && getGivenCredits().equals(ectsCredits);
    }

    public boolean isTemporary() {
        return false;
    }

    public boolean isCredits() {
        return true;
    }

    public boolean isSubstitution() {
        return false;
    }

    public boolean isInternalSubstitution() {
        return false;
    }

    public boolean isEquivalence() {
        return false;
    }

    public boolean hasAnyDismissalInCurriculum() {
        for (final Dismissal dismissal : getDismissalsSet()) {
            if (!dismissal.parentCurriculumGroupIsNoCourseGroupCurriculumGroup()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyDismissalInCycle(final CycleType cycleType) {
        for (final Dismissal dismissal : getDismissalsSet()) {
            final CycleCurriculumGroup cycle = dismissal.getParentCycleCurriculumGroup();
            if (cycle != null && cycle.getCycleType().equals(cycleType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Standard behaviour, may be overriden
     */
    protected Curriculum getCurriculum(final Dismissal dismissal, final DateTime when, final ExecutionYear year) {
        return new Curriculum(dismissal, year, Collections.<ICurriculumEntry> emptySet(), getAverageEntries(dismissal, year),
                Collections.<ICurriculumEntry> singleton(dismissal));
    }

    /**
     * Standard behaviour, may be overriden
     */
    protected Collection<ICurriculumEntry> getAverageEntries(final Dismissal dismissal, final ExecutionYear executionYear) {
        return Collections.<ICurriculumEntry> emptyList();
    }

    public String getDescription() {
        return BundleUtil.getString("resources.StudentResources", "label.dismissal.Credits");
    }

    public boolean isAllEnrolmentsAreExternal() {
        if (getEnrolmentsSet().isEmpty()) {
            return false;
        }

        for (EnrolmentWrapper wrapper : getEnrolmentsSet()) {
            if (!wrapper.getIEnrolment().isExternalEnrolment()) {
                return false;
            }
        }

        return true;
    }

    /**
     * @deprecated use {@link #getExecutionInterval()} instead.
     */
    @Deprecated
    @Override
    public ExecutionInterval getExecutionPeriod() {
        return getExecutionInterval();
    }

    public ExecutionInterval getExecutionInterval() {
        return super.getExecutionPeriod();
    }
}
