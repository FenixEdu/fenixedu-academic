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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.studentCurriculum.BranchCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Credits;
import org.fenixedu.academic.domain.studentCurriculum.CreditsManager;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroupFactory;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByExecutionInterval;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByExecutionYear;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByType;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.Equivalence;
import org.fenixedu.academic.domain.studentCurriculum.ExternalCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.ExtraCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.InternalSubstitution;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import org.fenixedu.academic.domain.studentCurriculum.PropaedeuticsCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.StandaloneCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Substitution;
import org.fenixedu.academic.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import org.fenixedu.academic.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.AcademicPermissionService;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.academic.util.predicates.ResultCollection;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlan extends StudentCurricularPlan_Base {

    static final public Comparator<StudentCurricularPlan> STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME =
            new Comparator<StudentCurricularPlan>() {

                @Override
                public int compare(StudentCurricularPlan o1, StudentCurricularPlan o2) {
                    final Degree degree1 = o1.getDegree();
                    final Degree degree2 = o2.getDegree();
                    final int ct = degree1.getDegreeType().compareTo(degree2.getDegreeType());
                    return ct == 0 ? degree1.getName().compareTo(degree2.getName()) : ct;
                }

            };

    static final public Comparator<StudentCurricularPlan> STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE =
            new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(final StudentCurricularPlan o1, final StudentCurricularPlan o2) {
                    return o1.getStartDateYearMonthDay().compareTo(o2.getStartDateYearMonthDay());
                }
            };

    private StudentCurricularPlan() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenDateTime(new DateTime());
        setGivenCredits(Double.valueOf(0));
    }

    private StudentCurricularPlan(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate,
            ExecutionInterval executionInterval, CycleType cycleType) {
        this();
        init(registration, degreeCurricularPlan, startDate, executionInterval);
        CurriculumGroupFactory.createRoot(this, getDegreeCurricularPlan().getRoot(), executionInterval, cycleType); // createStructure
    }

    static public StudentCurricularPlan createBolonhaStudentCurricularPlan(Registration registration,
            DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate, ExecutionInterval executionInterval) {
        return createBolonhaStudentCurricularPlan(registration, degreeCurricularPlan, startDate, executionInterval,
                (CycleType) null);
    }

    static public StudentCurricularPlan createBolonhaStudentCurricularPlan(Registration registration,
            DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate, ExecutionInterval executionInterval,
            CycleType cycleType) {
        return new StudentCurricularPlan(registration, degreeCurricularPlan, startDate, executionInterval, cycleType);
    }

    private void init(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate,
            ExecutionInterval startInterval) {

        checkParameters(registration, degreeCurricularPlan, startDate, startInterval);

        setDegreeCurricularPlan(degreeCurricularPlan);
        setRegistration(registration);
        setStartDateYearMonthDay(startDate);
        setStartExecutionInterval(startInterval);
    }

    private void checkParameters(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate,
            ExecutionInterval startInterval) {

        if (registration == null) {
            throw new DomainException("error.studentCurricularPlan.registration.cannot.be.null");
        }
        if (degreeCurricularPlan == null) {
            throw new DomainException("error.studentCurricularPlan.degreeCurricularPlan.cannot.be.null");
        }
        if (startDate == null) {
            throw new DomainException("error.studentCurricularPlan.startDate.cannot.be.null");
        }
        if (startInterval == null) {
            throw new DomainException("error.studentCurricularPlan.startInterval.cannot.be.null");
        }

        if (registration.getStudentCurricularPlan(degreeCurricularPlan) != null) {
            throw new DomainException("error.registrationAlreadyHasSCPWithGivenDCP");
        }

        if (registration.getStudentCurricularPlansSet().stream().anyMatch(
                scp -> scp.getStartExecutionInterval() == startInterval && scp.getStartDateYearMonthDay().equals(startDate))) {
            throw new DomainException("error.registrationAlreadyHasSCPWithGivenStartIntervalAndDates");
        }
    }

    public void editStart(final ExecutionInterval startInterval) {
        final YearMonthDay startDate = startInterval.getBeginDateYearMonthDay();

        if (getRegistration().getStudentCurricularPlansSet().stream().filter(scp -> scp != this).anyMatch(
                scp -> (scp.getStartExecutionInterval() == startInterval || scp.getStartExecutionYear() == startInterval)
                        && scp.getStartDateYearMonthDay().equals(startDate))) {
            throw new DomainException("error.registrationAlreadyHasSCPWithGivenStartIntervalAndDates");
        }

        setStartExecutionInterval(startInterval);
        setStartDate(startDate);
    }

    public void delete() throws DomainException {

        for (; !getEnrolmentsSet().isEmpty(); getEnrolmentsSet().iterator().next().delete()) {
            ;
        }

        getRoot().delete();

        for (; !getCreditsSet().isEmpty(); getCreditsSet().iterator().next().delete()) {
            ;
        }

        setStartExecutionInterval(null);
        setDegreeCurricularPlan(null);
        setStudent(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    final public boolean isFirstCycle() {
        return getDegreeType().isFirstCycle();
    }

    final public boolean isSecondCycle() {
        return getDegreeType().isSecondCycle();
    }

    final public boolean hasConcludedCycle(CycleType cycleType) {
        return getRoot().hasConcludedCycle(cycleType);
    }

    final public boolean hasConcludedCycle(CycleType cycleType, final ExecutionYear executionYear) {
        return getRoot().hasConcludedCycle(cycleType, executionYear);
    }

    final public YearMonthDay getConclusionDate(final CycleType cycleType) {
        if (getDegreeType().getCycleTypes().isEmpty()) {
            throw new DomainException("StudentCurricularPlan.has.no.cycle.type");
        }

        if (!getDegreeType().hasCycleTypes(cycleType)) {
            throw new DomainException("StudentCurricularPlan.doesnt.have.such.cycle.type");
        }

        return getCycle(cycleType).getConclusionDate();
    }

    public YearMonthDay calculateConclusionDate(final CycleType cycleType) {
        if (cycleType == null) {
            return getLastApprovementDate();
        }

        if (getDegreeType().getCycleTypes().isEmpty()) {
            throw new DomainException("StudentCurricularPlan.has.no.cycle.type");
        }

        if (!getDegreeType().hasCycleTypes(cycleType)) {
            throw new DomainException("StudentCurricularPlan.doesnt.have.such.cycle.type");
        }

        return getCycle(cycleType).calculateConclusionDate();
    }

    final public boolean isConcluded() {
        return ProgramConclusion.isConcluded(this);
    }

    final public boolean isConclusionProcessed() {
        return ProgramConclusion.isConclusionProcessed(this);
    }

    final public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear) {
        return getRoot().getCurriculum(when, executionYear);
    }

    final public boolean isActive() {
        return isLastStudentCurricularPlanFromRegistration() && getRegistration().isActive();
    }

    final public boolean isPast() {
        return getDegreeCurricularPlan().isPast();
    }

    final public Person getPerson() {
        return getRegistration().getPerson();
    }

    final public Department getDepartment() {
        return getDegree().getUnit().getDepartment();
    }

    final public Degree getDegree() {
        return getDegreeCurricularPlan().getDegree();
    }

    final public DegreeType getDegreeType() {
        return getDegree().getDegreeType();
    }

    final public Integer getDegreeDuration() {
        return getDegreeCurricularPlan().getDegreeDuration();
    }

    @Override
    @Deprecated
    final public Registration getStudent() {
        return this.getRegistration();
    }

    public void setStartDate(YearMonthDay startDate) {
        if (startDate != null && getStartExecutionInterval() != null
                && !getStartExecutionInterval().getAcademicInterval().contains(startDate.toDateTimeAtMidnight())) {
            throw new DomainException("error.StudentCurricularPlan.setting.startDate.outsideExecutionInterval");
        }
        super.setStartDateYearMonthDay(startDate);
    }

    @Override
    @Deprecated
    public void setStudent(final Registration registration) {
        this.setRegistration(registration);
    }

    public Registration getRegistration() {
        return super.getStudent();
    }

    public void setRegistration(final Registration registration) {
        if (registration != null) {
            if (registration.getDegree() != null) {
                if (!registration.getDegree().getDegreeCurricularPlansSet().contains(getDegreeCurricularPlan())) {
                    throw new DomainException("error.StudentCurricularPlan.setting.registration.with.different.degree");
                }
            } else {
                registration.setDegree(getDegree());
            }
        }

        super.setStudent(registration);
    }

    public boolean hasRegistration() {
        return super.getStudent() != null;
    }

    public Set<CurriculumLine> getAllCurriculumLines() {
        return getRoot().getAllCurriculumLines();
    }

    public Set<CurriculumGroup> getAllCurriculumGroups() {
        return getRoot().getAllCurriculumGroups();
    }

    public Set<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        return getRoot().getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups();
    }

    @Override
    final public Set<Enrolment> getEnrolmentsSet() {
        return getRoot().getEnrolmentsSet();
    }

    final public boolean hasAnyEnrolments() {
        return getRoot().hasAnyEnrolments();
    }

    final public boolean hasAnyCurriculumLines() {
        return hasAnyCurriculumModules(new CurriculumModulePredicateByType(CurriculumLine.class));
    }

    final public boolean hasAnyCurriculumLines(final ExecutionYear executionYear) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionYear(executionYear));

        return hasAnyCurriculumModules(andPredicate);

    }

    public boolean hasAnyCurriculumLines(final ExecutionInterval executionInterval) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionInterval(executionInterval));

        return hasAnyCurriculumModules(andPredicate);

    }

    final public boolean hasEnrolments(final Enrolment enrolment) {
        return getRoot().hasCurriculumModule(enrolment);
    }

    final public boolean hasEnrolments(final ExecutionYear executionYear) {
        return getRoot().hasEnrolment(executionYear);
    }

    public boolean hasEnrolments(final ExecutionInterval executionInterval) {
        return getRoot().hasEnrolment(executionInterval);
    }

    final public long countCurrentEnrolments() {
        return getEnrolmentsSet().stream()
                .filter(enrolment -> enrolment.getExecutionInterval().getExecutionYear().isCurrent() && enrolment.isEnroled())
                .count();
    }

    final public long getCountCurrentEnrolments() {
        return countCurrentEnrolments();
    }

    final public List<Enrolment> getEnrolments(final CurricularCourse curricularCourse) {
        final List<Enrolment> results = new ArrayList<Enrolment>();

        for (final Enrolment enrollment : this.getEnrolmentsSet()) {
            if (enrollment.getCurricularCourse() == curricularCourse) {
                results.add(enrollment);
            }
        }

        return results;
    }

    final public int countEnrolmentsByCurricularCourse(final CurricularCourse curricularCourse) {
        int count = 0;
        for (Enrolment enrolment : this.getEnrolmentsSet()) {
            if (enrolment.getCurricularCourse() == curricularCourse) {
                count++;
            }
        }
        return count;
    }

    public int countEnrolmentsByCurricularCourse(final CurricularCourse curricularCourse,
            final ExecutionInterval untilExecutionInterval) {
        int count = 0;
        for (Enrolment enrolment : getEnrolments(curricularCourse)) {
            if (enrolment.getExecutionInterval().isBeforeOrEquals(untilExecutionInterval)) {
                count++;
            }
        }
        return count;
    }

    public List<Enrolment> getEnrolmentsByExecutionYear(final ExecutionYear executionYear) {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionInterval().getExecutionYear() == executionYear) {
                result.add(enrolment);
            }
        }

        return result;
    }

    public List<Enrolment> getEnrolmentsByExecutionPeriod(final ExecutionInterval executionInterval) {
        List<Enrolment> results = new ArrayList<Enrolment>();
        for (Enrolment enrolment : this.getEnrolmentsSet()) {
            if (enrolment.getExecutionInterval() == executionInterval) {
                results.add(enrolment);
            }
        }
        return results;
    }

    final public Collection<Enrolment> getStudentEnrollmentsWithEnrolledState() {
        final List<Enrolment> result = new ArrayList<Enrolment>();

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.isEnroled() && !enrolment.isInvisible()) {
                result.add(enrolment);
            }
        }

        return result;
    }

    final public int getNumberOfEnrolledCurricularCourses() {
        return getStudentEnrollmentsWithEnrolledState().size();
    }

    private Collection<Enrolment> getVisibleEnroledEnrolments(final ExecutionInterval executionInterval) {
        final Collection<Enrolment> result = new ArrayList<Enrolment>();

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (!enrolment.isAnnulled() && !enrolment.isInvisible()
                    && (executionInterval == null || enrolment.isValid(executionInterval))) {
                result.add(enrolment);
            }
        }

        return result;
    }

    final public Collection<Enrolment> getLatestCurricularCoursesEnrolments(final ExecutionYear executionYear) {
        final Map<CurricularCourse, Enrolment> result = new HashMap<CurricularCourse, Enrolment>();

        for (final Enrolment enrolment : getEnrolmentsByExecutionYear(executionYear)) {
            if (!result.containsKey(enrolment.getCurricularCourse())
                    || result.get(enrolment.getCurricularCourse()).isBefore(enrolment)) {
                result.put(enrolment.getCurricularCourse(), enrolment);
            }
        }

        return result.values();
    }

    public void addApprovedEnrolments(final Collection<Enrolment> enrolments) {
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (!enrolment.isInvisible() && enrolment.isApproved()) {
                enrolments.add(enrolment);
            }
        }
    }

    public Set<Enrolment> getDismissalApprovedEnrolments() {
        Set<Enrolment> aprovedEnrolments = new HashSet<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.canBeUsedAsCreditsSource()) {
                aprovedEnrolments.add(enrolment);
            }
        }
        return aprovedEnrolments;
    }

    public Collection<? extends CurriculumModule> getCurriculumModules(final ResultCollection<CurriculumModule> collection) {
        getRoot().getCurriculumModules(collection);
        return collection.getResult();
    }

    public boolean hasAnyCurriculumModules(final Predicate<CurriculumModule> predicate) {
        return getRoot().hasAnyCurriculumModules(predicate);
    }

    final public Collection<CurriculumLine> getApprovedCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();
        getRoot().addApprovedCurriculumLines(result);
        return result;
    }

    final public ExecutionYear getApprovedCurriculumLinesLastExecutionYear() {
        return getRoot().getApprovedCurriculumLinesLastExecutionYear();
    }

    final public CurriculumLine getLastApprovement() {
        return getAprovedEnrolments().stream().sorted(CurriculumLine.COMPARATOR_BY_APPROVEMENT_DATE_AND_ID.reversed()).findFirst()
                .orElse(null);
    }

    final public YearMonthDay getLastApprovementDate() {
        final CurriculumLine lastApprovement = getLastApprovement();
        return lastApprovement == null ? null : lastApprovement.getApprovementDate();
    }

    final public ExecutionYear getLastApprovementExecutionYear() {
        return hasAnyApprovedCurriculumLines() ? getApprovedCurriculumLinesLastExecutionYear() : null;
    }

    final public boolean hasAnyApprovedCurriculumLines() {
        return getRoot().hasAnyApprovedCurriculumLines();
    }

    final public List<Enrolment> getAprovedEnrolments() {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        addApprovedEnrolments(result);
        return result;
    }

    public boolean hasAnyApprovedEnrolment() {
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.isApproved()) {
                return true;
            }
        }
        return false;
    }

    final public Collection<Enrolment> getDissertationEnrolments() {
        final Collection<Enrolment> result = new HashSet<Enrolment>();

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getCurricularCourse().isDissertation()) {
                result.add(enrolment);
            }
        }

        for (Dismissal dismissal : getDismissals()) {
            for (IEnrolment enrolment : dismissal.getSourceIEnrolments()) {
                if (!enrolment.isEnrolment()) {
                    continue;
                }

                Enrolment realEnrolment = (Enrolment) enrolment;
                if (realEnrolment.getCurricularCourse().isDissertation()) {
                    result.add(realEnrolment);
                }
            }
        }

        return result;
    }

    final public Enrolment getLatestDissertationEnrolment() {
        final TreeSet<Enrolment> result = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
        result.addAll(getDissertationEnrolments());
        return result.isEmpty() ? null : result.last();
    }

    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionInterval executionInterval) {
        return getRoot().findEnrolmentFor(curricularCourse, executionInterval);
    }

    public Enrolment getEnrolmentByCurricularCourseAndExecutionPeriod(final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getCurricularCourse().equals(curricularCourse) && enrolment.isValid(executionInterval)) {
                return enrolment;
            }
        }

        return null;
    }

    public Set<ExecutionInterval> getEnrolmentsExecutionPeriods() {
        final Set<ExecutionInterval> result = new HashSet<>();

        for (final Enrolment enrolment : this.getEnrolmentsSet()) {
            result.add(enrolment.getExecutionInterval());
        }

        return result;
    }

    public ExecutionYear getStartExecutionYear() {
        return getStartExecutionInterval().getExecutionYear();
    }

    public ExecutionInterval getStartExecutionPeriod() {
        return getStartExecutionInterval();
    }

    public YearMonthDay getEndDate() {

        final StudentCurricularPlan nextStudentCurricularPlan = getNextStudentCurricularPlan();
        if (nextStudentCurricularPlan != null) {
            return nextStudentCurricularPlan.getStartDateYearMonthDay().minusDays(1);
        } else if (getRegistration() != null && !getRegistration().isActive()) {
            return getRegistration().getActiveState().getStateDate().toYearMonthDay();
        }

        return null;
    }

    private StudentCurricularPlan getNextStudentCurricularPlan() {
        if (getRegistration() != null) {
            for (Iterator<StudentCurricularPlan> iter = getRegistration().getSortedStudentCurricularPlans().iterator(); iter
                    .hasNext();) {
                if (iter.next() == this) {
                    return iter.hasNext() ? iter.next() : null;
                }
            }
        }
        return null;
    }

    public boolean isActive(ExecutionYear executionYear) {
        return !getStartDateYearMonthDay().isAfter(executionYear.getEndDateYearMonthDay())
                && (getEndDate() == null || !getEndDate().isBefore(executionYear.getBeginDateYearMonthDay()));
    }

    public ExecutionYear getLastExecutionYear() {
        ExecutionYear result = null;

        for (final CurriculumLine curriculumLine : this.getAllCurriculumLines()) {
            final ExecutionInterval executionInterval = curriculumLine.getExecutionInterval();
            if (result == null || executionInterval != null && result.isBefore(executionInterval.getExecutionYear())) {
                result = executionInterval.getExecutionYear();
            }
        }

        return result;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    public List<Enrolment> getAllEnrollments() {
        List<Enrolment> allEnrollments = new ArrayList<Enrolment>();
        addNonInvisibleEnrolments(allEnrollments, getEnrolmentsSet());

        for (final StudentCurricularPlan studentCurricularPlan : getRegistration().getStudentCurricularPlansSet()) {
            addNonInvisibleEnrolments(allEnrollments, studentCurricularPlan.getEnrolmentsSet());
        }

        return allEnrollments;
    }

    private void addNonInvisibleEnrolments(Collection<Enrolment> allEnrollments, Collection<Enrolment> enrollmentsToAdd) {
        for (Enrolment enrolment : enrollmentsToAdd) {
            if (!enrolment.isInvisible()) {
                allEnrollments.add(enrolment);
            }
        }
    }

    final public int getNumberOfStudentEnrollments() {
        return getAllEnrollments().size();
    }

    public List<Enrolment> getStudentEnrollmentsWithApprovedState() {
        final List<Enrolment> result = new ArrayList<Enrolment>();

        for (final Enrolment enrolment : getAllEnrollments()) {
            if (enrolment.isApproved()) {
                result.add(enrolment);
            }
        }

        return result;
    }

    protected boolean isApproved(CurricularCourse curricularCourse, List<CurricularCourse> approvedCourses) {
        return hasEquivalenceIn(curricularCourse, approvedCourses);
    }

    public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionInterval executionInterval) {
        return getRoot().isApproved(curricularCourse, executionInterval);
    }

    public boolean isCurricularCourseApproved(CurricularCourse curricularCourse) {
        List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        List<CurricularCourse> result =
                (List<CurricularCourse>) CollectionUtils.collect(studentApprovedEnrollments, new Transformer() {
                    @Override
                    final public Object transform(Object obj) {
                        Enrolment enrollment = (Enrolment) obj;

                        return enrollment.getCurricularCourse();

                    }
                });

        return isApproved(curricularCourse, result);
    }

    public boolean isApproved(final CurricularCourse curricularCourse) {
        return getRoot().isApproved(curricularCourse);
    }

    public int getNumberOfApprovedCurricularCourses() {
        int counter = 0;

        int size = getDegreeCurricularPlan().getCurricularCoursesSet().size();
        for (CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCoursesSet()) {
            if (isCurricularCourseApproved(curricularCourse)) {
                counter++;
            }
        }

        return counter;
    }

    final protected boolean hasEquivalenceIn(CurricularCourse curricularCourse, List<CurricularCourse> otherCourses) {
        if (otherCourses.isEmpty()) {
            return false;
        }

        if (isThisCurricularCoursesInTheList(curricularCourse, otherCourses)) {
            return true;
        }

        return false;
    }

    public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {
        return getRoot().isEnroledInExecutionPeriod(curricularCourse, executionInterval);
    }

    public double getAccumulatedEctsCredits(final ExecutionInterval executionInterval) {
        double result = 0.0;

        for (final Enrolment enrolment : getVisibleEnroledEnrolments(executionInterval)) {
            result += getAccumulatedEctsCredits(executionInterval, enrolment.getCurricularCourse());
        }

        return result;
    }

    public double getAccumulatedEctsCredits(final ExecutionInterval executionInterval, final CurricularCourse curricularCourse) {
        return isAccumulated(executionInterval, curricularCourse) ? MaximumNumberOfCreditsForEnrolmentPeriod.getAccumulatedEcts(
                curricularCourse,
                executionInterval) : curricularCourse.getEctsCredits(executionInterval.getChildOrder(), executionInterval);
    }

    private boolean isAccumulated(final ExecutionInterval executionInterval, final CurricularCourse curricularCourse) {
        return hasEnrolmentInCurricularCourseBefore(curricularCourse, executionInterval);
    }

    private boolean hasEnrolmentInCurricularCourseBefore(final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {
        return getRoot().hasEnrolmentInCurricularCourseBefore(curricularCourse, executionInterval);
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    public double getEnrolmentsEctsCredits(final ExecutionYear executionYear) {
        return executionYear.getChildIntervals().stream().mapToDouble(interval -> getEnrolmentsEctsCredits(interval)).sum();
    }

    public double getEnrolmentsEctsCredits(final ExecutionInterval executionInterval) {
        return getEnrolmentsSet().stream().filter(e -> e.isValid(executionInterval)).mapToDouble(Enrolment::getEctsCredits).sum();
    }

    final public double getDismissalsEctsCredits() {
        return getDismissals().stream().mapToDouble(Dismissal::getEctsCredits).sum();
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    private boolean isThisCurricularCoursesInTheList(final CurricularCourse curricularCourse,
            List<CurricularCourse> curricularCourses) {
        for (CurricularCourse otherCourse : curricularCourses) {
            if (curricularCourse == otherCourse || haveSameCompetence(curricularCourse, otherCourse)) {
                return true;
            }
        }
        return false;
    }

    private boolean haveSameCompetence(CurricularCourse course1, CurricularCourse course2) {
        CompetenceCourse comp1 = course1.getCompetenceCourse();
        CompetenceCourse comp2 = course2.getCompetenceCourse();
        return comp1 != null && comp1 == comp2;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    public boolean isEnroledInSpecialSeason(final ExecutionInterval executionInterval) {
        return getRoot().isEnroledInSpecialSeason(executionInterval);
    }

    public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear) {
        return getRoot().isEnroledInSpecialSeason(executionYear);
    }

    final public Collection<EnrolmentEvaluation> getEnroledSpecialSeasons(final ExecutionInterval input) {
        final Set<EnrolmentEvaluation> result = Sets.newHashSet();

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.isValid(input)) {

                for (final EnrolmentEvaluation evaluation : enrolment.getEvaluationsSet()) {
                    final EvaluationSeason season = evaluation.getEvaluationSeason();

                    if (season.isSpecial()) {
                        final Optional<EnrolmentEvaluation> search =
                                enrolment.getEnrolmentEvaluation(season, input, (Boolean) null);
                        if (search.isPresent() && search.get() == evaluation) {
                            result.add(evaluation);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Has special season in given semester if is enroled in special season in
     * previous semester
     *
     * @param executionInterval
     *
     */
    public boolean hasSpecialSeasonFor(final ExecutionInterval executionInterval) {
        final ExecutionInterval interval = executionInterval.getPrevious();
        return interval != null && isEnroledInSpecialSeason(interval.getExecutionYear());
    }

    // SpecialSeasons
    public void createEnrolmentEvaluationForSpecialSeason(final Collection<Enrolment> toCreate, final Person person,
            final EvaluationSeason evaluationSeason) {

        final Collection<EnrolmentEvaluation> created = new HashSet<EnrolmentEvaluation>();

        for (final Enrolment enrolment : toCreate) {
            created.add(enrolment.createTemporaryEvaluationForSpecialSeason(person, evaluationSeason));
        }
    }

    // Improvements
    public void createEnrolmentEvaluationForImprovement(final Collection<Enrolment> toCreate, final Person person,
            final ExecutionInterval executionInterval, final EvaluationSeason evaluationSeason) {

        final Collection<EnrolmentEvaluation> created = new HashSet<EnrolmentEvaluation>();

        for (final Enrolment enrolment : toCreate) {
            created.add(enrolment.createTemporaryEvaluationForImprovement(person, evaluationSeason, executionInterval));
        }

// qubExtension, removed, using ITreasuryBridgeAPI instead in enrolment.createTemporaryEvaluationForImprovement
//        if (isToPayImprovementOfApprovedEnrolments()) {
//            new ImprovementOfApprovedEnrolmentEvent(this.getDegree().getAdministrativeOffice(), getPerson(), created);
//        }
    }

    public Set<EnrolmentEvaluation> getEnroledImprovements(final ExecutionInterval input) {
        final Set<EnrolmentEvaluation> result = Sets.newHashSet();

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            for (final EnrolmentEvaluation evaluation : enrolment.getEvaluationsSet()) {
                final EvaluationSeason season = evaluation.getEvaluationSeason();

                if (season.isImprovement()) {
                    final Optional<EnrolmentEvaluation> search = enrolment.getEnrolmentEvaluation(season, input, (Boolean) null);
                    if (search.isPresent() && search.get() == evaluation) {
                        result.add(evaluation);
                    }
                }
            }
        }

        return result;
    }

    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionInterval executionInterval) {
        return getRoot().getDegreeModulesToEvaluate(executionInterval);
    }

    public RuleResult enrol(final ExecutionInterval executionInterval, final Set<IDegreeModuleToEvaluate> degreeModulesToEnrol,
            final List<CurriculumModule> curriculumModulesToRemove, final CurricularRuleLevel curricularRuleLevel) {

        return enrol(executionInterval, degreeModulesToEnrol, curriculumModulesToRemove, curricularRuleLevel,
                (EvaluationSeason) null);
    }

    @Atomic
    public RuleResult enrol(final ExecutionInterval executionInterval, final Set<IDegreeModuleToEvaluate> degreeModulesToEnrol,
            final List<CurriculumModule> curriculumModulesToRemove, final CurricularRuleLevel curricularRuleLevel,
            final EvaluationSeason season) {

        final EnrolmentContext enrolmentContext = new EnrolmentContext(this, executionInterval, degreeModulesToEnrol,
                curriculumModulesToRemove, curricularRuleLevel, season);

        return org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(enrolmentContext)
                .manage();
    }

    public RuleResult enrol(final ExecutionInterval executionInterval, final CurricularRuleLevel curricularRuleLevel) {
        return enrol(executionInterval, Collections.EMPTY_SET, Collections.EMPTY_LIST, curricularRuleLevel);
    }

    @Atomic
    public void enrolInAffinityCycle(final CycleCourseGroup cycleCourseGroup, final ExecutionInterval executionInterval) {
        CurriculumGroupFactory.createGroup(getRoot(), cycleCourseGroup, executionInterval);
    }

    final public String getName() {
        return getDegreeCurricularPlan().getName();
    }

    final public String getPresentationName() {
        return getPresentationName(ExecutionYear.findCurrent(getDegree().getCalendar()));
    }

    final public String getPresentationName(final ExecutionYear executionYear) {
        return getDegreeCurricularPlan().getPresentationName(executionYear);
    }

    final public Space getCurrentCampus() {
        final Space currentCampus = getDegreeCurricularPlan().getCurrentCampus();
        return currentCampus == null ? getLastCampus() : currentCampus;
    }

    final public Space getCampus(final ExecutionYear executionYear) {
        final Space result = getDegreeCurricularPlan().getCampus(executionYear);
        return result == null ? getLastCampus() : result;
    }

    final public Space getLastCampus() {
        final Space lastScpCampus = getDegreeCurricularPlan().getCampus(getLastExecutionYear());
        return lastScpCampus == null ? getDegreeCurricularPlan().getLastCampus() : lastScpCampus;
    }

    final public void createOptionalEnrolment(final CurriculumGroup curriculumGroup, final ExecutionInterval executionInterval,
            final OptionalCurricularCourse optionalCurricularCourse, final CurricularCourse curricularCourse,
            final EnrollmentCondition enrollmentCondition) {
        if (getRoot().isApproved(curricularCourse, executionInterval)) {
            throw new DomainException("error.already.aproved", new String[] { curricularCourse.getName() });
        }
        if (getRoot().isEnroledInExecutionPeriod(curricularCourse, executionInterval)) {
            throw new DomainException("error.already.enroled.in.executionPeriod",
                    new String[] { curricularCourse.getName(), executionInterval.getQualifiedName() });
        }

        new OptionalEnrolment(this, curriculumGroup, curricularCourse, executionInterval, enrollmentCondition,
                Authenticate.getUser().getUsername(), optionalCurricularCourse);
    }

    @Deprecated
    final public RuleResult createNoCourseGroupCurriculumGroupEnrolment(final NoCourseGroupEnrolmentBean bean) {
        return org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolment
                .createManager(EnrolmentContext.createForNoCourseGroupCurriculumGroupEnrolment(this, bean)).manage();
    }

    @Atomic
    public RuleResult removeCurriculumModulesFromNoCourseGroupCurriculumGroup(final List<CurriculumModule> curriculumModules,
            final ExecutionInterval executionInterval, final NoCourseGroupCurriculumGroupType groupType) {
        final EnrolmentContext context = new EnrolmentContext(this, executionInterval, Collections.EMPTY_SET, curriculumModules,
                groupType.getCurricularRuleLevel());
        return org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(context).manage();
    }

    final public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup(final NoCourseGroupCurriculumGroupType groupType) {
        return getRoot().getNoCourseGroupCurriculumGroup(groupType);
    }

    final public NoCourseGroupCurriculumGroup createNoCourseGroupCurriculumGroup(
            final NoCourseGroupCurriculumGroupType groupType) {
        final NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup = getNoCourseGroupCurriculumGroup(groupType);
        if (noCourseGroupCurriculumGroup != null) {
            throw new DomainException("error.studentCurricularPlan.already.has.noCourseGroupCurriculumGroup.with.same.groupType");
        }
        return NoCourseGroupCurriculumGroup.create(groupType, getRoot());
    }

    public ExtraCurriculumGroup createExtraCurriculumGroup() {
        return (ExtraCurriculumGroup) createNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR);
    }

    public ExtraCurriculumGroup getExtraCurriculumGroup() {
        return (ExtraCurriculumGroup) getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR);
    }

    public boolean hasExtraCurriculumGroup() {
        return getExtraCurriculumGroup() != null;
    }

    public StandaloneCurriculumGroup getStandaloneCurriculumGroup() {
        return (StandaloneCurriculumGroup) getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.STANDALONE);
    }

    public boolean hasStandaloneCurriculumGroup() {
        return getStandaloneCurriculumGroup() != null;
    }

    final public Collection<CurriculumLine> getExtraCurricularCurriculumLines() {
        final Collection<CurriculumLine> result = new ArrayList<CurriculumLine>();

        if (hasExtraCurriculumGroup()) {
            for (final CurriculumLine curriculumLine : getExtraCurriculumGroup().getCurriculumLines()) {
                result.add(curriculumLine);
            }
        }

        return result;
    }

    final public Collection<CurriculumLine> getStandaloneCurriculumLines() {
        final Collection<CurriculumLine> result = new ArrayList<CurriculumLine>();

        if (hasStandaloneCurriculumGroup()) {
            for (final CurriculumLine curriculumLine : getStandaloneCurriculumGroup().getCurriculumLines()) {
                result.add(curriculumLine);
            }
        }

        return result;
    }

    public PropaedeuticsCurriculumGroup getPropaedeuticCurriculumGroup() {
        return (PropaedeuticsCurriculumGroup) getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.PROPAEDEUTICS);
    }

    public boolean hasPropaedeuticsCurriculumGroup() {
        return getPropaedeuticCurriculumGroup() != null;
    }

    final public Collection<CurriculumLine> getPropaedeuticCurriculumLines() {
        final Collection<CurriculumLine> result = new ArrayList<CurriculumLine>();

        if (hasPropaedeuticsCurriculumGroup()) {
            for (final CurriculumLine curriculumLine : getPropaedeuticCurriculumGroup().getCurriculumLines()) {
                result.add(curriculumLine);
            }
        }

        return result;
    }

    /**
     * Note that this method must not use the ExtraCurriculumGroup due to the
     * pre-Bolonha SCPs
     *
     * @return get propaedeutic enrolments
     */
    final public Collection<Enrolment> getPropaedeuticEnrolments() {
        final Collection<Enrolment> result = new ArrayList<Enrolment>();

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.isPropaedeutic()) {
                result.add(enrolment);
            }
        }

        return result;
    }

    public Collection<CurricularCourse> getAllCurricularCoursesToDismissal(final ExecutionInterval input) {
        return getCourseGroupsToApplyDismissals().stream().flatMap(group -> group.getAllCurricularCourses(input).stream())
                .filter(course -> !isApproved(course)).collect(Collectors.toSet());
    }

    private Collection<CourseGroup> getCourseGroupsToApplyDismissals() {
        return getRoot().getCurriculumGroups().stream().filter(i -> i.getDegreeModule() != null).map(i -> i.getDegreeModule())
                .collect(Collectors.toSet());
    }

    final public Credits createNewCreditsDismissal(CourseGroup courseGroup, CurriculumGroup curriculumGroup,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
            ExecutionInterval executionInterval) {

        checkPermission(courseGroup, curriculumGroup, dismissals);

        if (courseGroup != null) {
            Collection<CurricularCourse> noEnrolCurricularCourse = new ArrayList<CurricularCourse>();
            if (dismissals != null) {
                for (SelectedCurricularCourse selectedCurricularCourse : dismissals) {
                    noEnrolCurricularCourse.add(selectedCurricularCourse.getCurricularCourse());
                }
            }
            return new Credits(this, courseGroup, enrolments, noEnrolCurricularCourse, givenCredits, executionInterval);
        } else if (curriculumGroup != null) {
            return new Credits(this, curriculumGroup, enrolments, givenCredits, executionInterval);
        } else {
            return new Credits(this, dismissals, enrolments, executionInterval);
        }
    }

    public List<Dismissal> getDismissals() {
        final List<Dismissal> result = new ArrayList<Dismissal>();
        getRoot().collectDismissals(result);
        return result;
    }

    public Dismissal getDismissal(final CurricularCourse curricularCourse) {
        return getRoot().getDismissal(curricularCourse);
    }

    final public Equivalence createNewEquivalenceDismissal(CourseGroup courseGroup, CurriculumGroup curriculumGroup,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
            Grade givenGrade, ExecutionInterval executionInterval) {

        checkPermission(courseGroup, curriculumGroup, dismissals);

        return CreditsManager.createEquivalence(this, courseGroup, curriculumGroup, dismissals, enrolments, givenCredits,
                givenGrade, executionInterval);
    }

    final public Substitution createNewSubstitutionDismissal(CourseGroup courseGroup, CurriculumGroup curriculumGroup,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
            ExecutionInterval executionInterval) {

        checkPermission(courseGroup, curriculumGroup, dismissals);

        return CreditsManager.createSubstitution(this, courseGroup, curriculumGroup, dismissals, enrolments, givenCredits,
                executionInterval);
    }

    public InternalSubstitution createNewInternalSubstitution(CourseGroup courseGroup, CurriculumGroup curriculumGroup,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
            ExecutionInterval executionInterval) {

        checkPermission(courseGroup, curriculumGroup, dismissals);

        return CreditsManager.createInternalSubstitution(this, courseGroup, curriculumGroup, dismissals, enrolments, givenCredits,
                executionInterval);

    }

    private void checkPermission(final CourseGroup courseGroup, final CurriculumGroup curriculumGroup,
            final Collection<SelectedCurricularCourse> dismissals) {

        final Person person = AccessControl.getPerson();

        final boolean hasUpdateRegistrationAfterConclusionProcessPermission =
                AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                        getDegree(), person.getUser())
                        || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_CONCLUSION", getDegree(), person.getUser());

        if (courseGroup != null) {
            final CurriculumGroup group = findCurriculumGroupFor(courseGroup);
            if (group != null && group.getParentCycleCurriculumGroup() != null
                    && group.getParentCycleCurriculumGroup().isConclusionProcessed()
                    && !hasUpdateRegistrationAfterConclusionProcessPermission) {
                throw new DomainException("error.StudentCurricularPlan.cannot.create.dismissals");
            }
        } else if (curriculumGroup != null) {
            if (curriculumGroup.getParentCycleCurriculumGroup() != null
                    && curriculumGroup.getParentCycleCurriculumGroup().isConclusionProcessed()
                    && !hasUpdateRegistrationAfterConclusionProcessPermission) {
                throw new DomainException("error.StudentCurricularPlan.cannot.create.dismissals");
            }
        } else {
            for (final SelectedCurricularCourse selected : dismissals) {
                if (selected.getCurriculumGroup().getParentCycleCurriculumGroup() != null
                        && selected.getCurriculumGroup().getParentCycleCurriculumGroup().isConclusionProcessed()
                        && !hasUpdateRegistrationAfterConclusionProcessPermission) {
                    throw new DomainException("error.StudentCurricularPlan.cannot.create.dismissals");
                }
            }
        }

        if (getRegistration().isRegistrationConclusionProcessed() && !hasUpdateRegistrationAfterConclusionProcessPermission) {
            throw new DomainException("error.StudentCurricularPlan.cannot.create.dismissals");
        }

    }

    final public Set<DegreeModule> getAllDegreeModules() {
        final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);
        getRoot().getAllDegreeModules(degreeModules);
        return degreeModules;
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return getRoot().hasDegreeModule(degreeModule);
    }

    public boolean hasCurriculumModule(final CurriculumModule curriculumModule) {
        return getRoot().hasCurriculumModule(curriculumModule);
    }

    public CurriculumGroup findCurriculumGroupFor(final CourseGroup courseGroup) {
        return getRoot().findCurriculumGroupFor(courseGroup);
    }

    public boolean isConcluded(final DegreeModule degreeModule, final ExecutionYear executionYear) {
        return getRoot().hasConcluded(degreeModule, executionYear);
    }

    final public Double getCreditsConcludedForCourseGroup(final CourseGroup courseGroup) {
        final CurriculumGroup curriculumGroup = findCurriculumGroupFor(courseGroup);
        return curriculumGroup == null ? Double.valueOf(0d) : curriculumGroup.getCreditsConcluded();
    }

    public boolean isLastStudentCurricularPlanFromRegistration() {
        return hasRegistration() && getRegistration().getLastStudentCurricularPlan() == this;
    }

    public void moveCurriculumLines(final MoveCurriculumLinesBean moveCurriculumLinesBean) {
        boolean runRules = false;
        Person responsible = AccessControl.getPerson();

        for (final CurriculumLineLocationBean curriculumLineLocationBean : moveCurriculumLinesBean.getCurriculumLineLocations()) {
            final CurriculumGroup destination = curriculumLineLocationBean.getCurriculumGroup();
            final CurriculumLine curriculumLine = curriculumLineLocationBean.getCurriculumLine();

            if (curriculumLine.getCurriculumGroup() != destination) {

                checkPermission(responsible, curriculumLineLocationBean);

                if (!destination.canAdd(curriculumLine)) {
                    throw new DomainException("error.StudentCurricularPlan.cannot.move.curriculum.line.to.curriculum.group",
                            curriculumLine.getFullPath(), destination.getFullPath());
                }

                if (curriculumLine.getExecutionInterval() != null && curriculumLine.getExecutionInterval().getExecutionYear()
                        .isBefore(destination.getRegistration().getRegistrationYear())) {
                    throw new DomainException(
                            "error.StudentCurricularPlan.cannot.move.curriculum.line.to.curriculum.group.invalid.period",
                            curriculumLine.getFullPath(), destination.getFullPath(),
                            curriculumLine.getExecutionInterval().getQualifiedName(),
                            destination.getRegistration().getRegistrationYear().getQualifiedName());
                }

                if (!destination.isExtraCurriculum()) {
                    runRules = true;
                }
                curriculumLine.setCurriculumGroup(destination);
            }

            // if curriculum line is moved then change created by

            curriculumLine.setCreatedBy(responsible != null ? responsible.getUsername() : curriculumLine.getCreatedBy());
        }

        if (runRules) {
            ExecutionYear.findCurrent(getDegree().getCalendar()).getChildIntervals().stream()
                    .filter(interval -> interval.isCurrent())
                    .forEach(interval -> checkEnrolmentRules(moveCurriculumLinesBean.getIDegreeModulesToEvaluate(interval),
                            interval));

        }
    }

    public void moveCurriculumLinesWithoutRules(final Person responsiblePerson,
            final MoveCurriculumLinesBean moveCurriculumLinesBean) {
        for (final CurriculumLineLocationBean curriculumLineLocationBean : moveCurriculumLinesBean.getCurriculumLineLocations()) {

            final CurriculumGroup destination = curriculumLineLocationBean.getCurriculumGroup();
            final CurriculumLine curriculumLine = curriculumLineLocationBean.getCurriculumLine();

            if (curriculumLine.getCurriculumGroup() != destination) {
                checkPermission(responsiblePerson, curriculumLineLocationBean);

                if (curriculumLine.getExecutionInterval() != null && curriculumLine.getExecutionInterval().getExecutionYear()
                        .isBefore(destination.getRegistration().getStartExecutionYear())) {
                    throw new DomainException(
                            "error.StudentCurricularPlan.cannot.move.curriculum.line.to.curriculum.group.invalid.period",
                            curriculumLine.getFullPath(), destination.getFullPath(),
                            curriculumLine.getExecutionInterval().getQualifiedName(),
                            destination.getRegistration().getStartExecutionYear().getQualifiedName());
                }

                curriculumLine.setCurriculumGroup(destination);
            }

            // if curriculum line is moved then change created by
            curriculumLine
                    .setCreatedBy(responsiblePerson != null ? responsiblePerson.getUsername() : curriculumLine.getCreatedBy());
        }
    }

    private void checkPermission(final Person responsiblePerson, final CurriculumLineLocationBean bean) {

        final boolean hasUpdateRegistrationAfterConclusionPermission =
                AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                        getDegree(), responsiblePerson.getUser())
                        || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_CONCLUSION", getDegree(), responsiblePerson.getUser());

        if (bean.getCurriculumGroup().getParentCycleCurriculumGroup() != null
                && bean.getCurriculumGroup().getParentCycleCurriculumGroup().isConclusionProcessed()
                && !hasUpdateRegistrationAfterConclusionPermission) {
            throw new DomainException("error.StudentCurricularPlan.cannot.move.is.not.authorized");
        }

        if (bean.getCurriculumLine().getParentCycleCurriculumGroup() != null
                && bean.getCurriculumLine().getParentCycleCurriculumGroup().isConclusionProcessed()
                && !hasUpdateRegistrationAfterConclusionPermission) {
            throw new DomainException("error.StudentCurricularPlan.cannot.move.is.not.authorized");
        }
    }

    @SuppressWarnings("unchecked")
    private void checkEnrolmentRules(final Set<IDegreeModuleToEvaluate> degreeModuleToEvaluate,
            final ExecutionInterval executionInterval) {
        enrol(executionInterval, degreeModuleToEvaluate, Collections.EMPTY_LIST, CurricularRuleLevel.ENROLMENT_WITH_RULES);
    }

    public AdministrativeOffice getAdministrativeOffice() {
        return getDegree().getAdministrativeOffice();
    }

    public CycleCurriculumGroup getCycle(final CycleType cycleType) {
        return getRoot().getCycleCurriculumGroup(cycleType);
    }

    public boolean hasCycleCurriculumGroup(final CycleType cycleType) {
        return getCycle(cycleType) != null;
    }

    public CycleCourseGroup getCycleCourseGroup(final CycleType cycleType) {
        return getDegreeCurricularPlan().getCycleCourseGroup(cycleType);
    }

    public Collection<CurriculumGroup> getCurricularCoursePossibleGroups(final CurricularCourse curricularCourse) {
        return getRoot().getCurricularCoursePossibleGroups(curricularCourse);
    }

    public Collection<CurriculumGroup> getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(
            final CurricularCourse curricularCourse) {
        return getRoot().getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(curricularCourse);
    }

    public CycleCurriculumGroup getFirstCycle() {
        return getRoot().getCycleCurriculumGroup(CycleType.FIRST_CYCLE);
    }

    public CycleCurriculumGroup getSecondCycle() {
        return getRoot().getCycleCurriculumGroup(CycleType.SECOND_CYCLE);
    }

    public CycleCurriculumGroup getThirdCycle() {
        return getRoot().getCycleCurriculumGroup(CycleType.THIRD_CYCLE);
    }

    public CycleCurriculumGroup getFirstOrderedCycleCurriculumGroup() {
        return getRoot().getFirstOrderedCycleCurriculumGroup();
    }

    public CycleCurriculumGroup getLastOrderedCycleCurriculumGroup() {
        return getRoot().getLastOrderedCycleCurriculumGroup();
    }

    public CycleCurriculumGroup getLastConcludedCycleCurriculumGroup() {
        return getRoot().getLastConcludedCycleCurriculumGroup();
    }

    public Collection<CycleCurriculumGroup> getCycleCurriculumGroups() {
        return getRoot().getCycleCurriculumGroups();
    }

    public List<CycleCurriculumGroup> getInternalCycleCurriculumGrops() {
        return getRoot().getInternalCycleCurriculumGroups();
    }

    public Collection<ExternalCurriculumGroup> getExternalCurriculumGroups() {
        return getRoot().getExternalCycleCurriculumGroups();
    }

    public boolean hasExternalCycleCurriculumGroups() {
        return getRoot().hasExternalCycles();
    }

    public Integer getInternalCycleCurriculumGroupsSize() {
        return getInternalCycleCurriculumGrops().size();
    }

    public List<CycleType> getSupportedCycleTypesToEnrol() {
        final List<CycleType> result = new ArrayList<CycleType>();

        final List<CycleType> supportedCyclesToEnrol = new ArrayList<CycleType>(getDegreeType().getSupportedCyclesToEnrol());
        Collections.sort(supportedCyclesToEnrol, CycleType.COMPARATOR_BY_GREATER_WEIGHT);

        for (final CycleType cycleType : supportedCyclesToEnrol) {
            if (hasCycleCurriculumGroup(cycleType)) {
                break;
            } else {
                result.add(0, cycleType);
            }
        }

        return result;
    }

    public boolean isEmpty() {
        if (hasAnyEnrolments()) {
            return false;
        }
        if (!getRoot().getAllCurriculumLines().isEmpty()) {
            return false;
        }

        if (!getCreditsSet().isEmpty()) {
            return false;
        }
        return true;
    }

    public Collection<NoCourseGroupCurriculumGroup> getNoCourseGroupCurriculumGroups() {
        return getRoot().getNoCourseGroupCurriculumGroups();
    }

    public Collection<CycleType> getCycleTypes() {
        return getDegreeType().getCycleTypes();
    }

    public CurriculumLine getApprovedCurriculumLine(final CurricularCourse curricularCourse) {
        return getRoot().getApprovedCurriculumLine(curricularCourse);
    }

    public OptionalEnrolment convertEnrolmentToOptionalEnrolment(final Enrolment enrolment, final CurriculumGroup curriculumGroup,
            final OptionalCurricularCourse curricularCourse) {

        final Person person = AccessControl.getPerson();

        if (enrolment.getParentCycleCurriculumGroup() != null && enrolment.getParentCycleCurriculumGroup().isConclusionProcessed()
                && !(AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                        getDegree(), person.getUser())
                        || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_CONCLUSION", getDegree(), person.getUser()))) {
            throw new DomainException("error.StudentCurricularPlan.cannot.move.is.not.authorized");
        }

        if (!hasEnrolments(enrolment)) {
            // if we remove this test, then check if enrolment period is before
            // registration start
            throw new DomainException("error.StudentCurricularPlan.doesnot.have.enrolment", enrolment.getName().getContent());
        }

        if (isApproved(curricularCourse)) {
            throw new DomainException("error.Enrolment.duplicate.enrolment", curricularCourse.getName());
        }

        final OptionalEnrolment result = OptionalEnrolment.createBasedOn(enrolment, curriculumGroup, curricularCourse);
        enrolment.delete();

        if (result.getStudentCurricularPlan() != this) {
            correctInvalidAttends(result.getStudentCurricularPlan());
        }

        return result;
    }

    private void correctInvalidAttends(final StudentCurricularPlan to) {
        for (final Attends attend : getRegistration().getAssociatedAttendsSet()) {
            if (!attend.hasExecutionCourseTo(this.getDegreeCurricularPlan()) && attend.canMove(this, to)) {
                getRegistration().changeShifts(attend, to.getRegistration());
                attend.setRegistration(to.getRegistration());
            }
        }
    }

    public Enrolment convertOptionalEnrolmentToEnrolment(final OptionalEnrolment enrolment,
            final CurriculumGroup curriculumGroup) {

        final Person person = AccessControl.getPerson();

        if (enrolment.getParentCycleCurriculumGroup() != null && enrolment.getParentCycleCurriculumGroup().isConclusionProcessed()
                && !(AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                        getDegree(), person.getUser())
                        || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_CONCLUSION", getDegree(), person.getUser()))) {
            throw new DomainException("error.StudentCurricularPlan.cannot.move.is.not.authorized");
        }

        if (!hasEnrolments(enrolment)) {
            // if we remove this test, then check if enrolment period is before
            // registration start
            throw new DomainException("error.StudentCurricularPlan.doesnot.have.enrolment", enrolment.getName().getContent());
        }

        final Enrolment result = Enrolment.createBasedOn(enrolment, curriculumGroup);
        enrolment.delete();

        if (result.getStudentCurricularPlan() != this) {
            correctInvalidAttends(result.getStudentCurricularPlan());
        }

        return result;
    }

    @Deprecated
    public boolean isEmptyDegree() {
        return getDegreeCurricularPlan().isEmpty();
    }

    public Set<BranchCurriculumGroup> getBranchCurriculumGroups() {
        return getRoot().getBranchCurriculumGroups();
    }

    public Set<BranchCurriculumGroup> getMajorBranchCurriculumGroups() {
        return getRoot().getMajorBranchCurriculumGroups();
    }

    public Set<BranchCurriculumGroup> getMinorBranchCurriculumGroups() {
        return getRoot().getMinorBranchCurriculumGroups();
    }

    public String getMajorBranchNames() {
        return getMajorBranchCurriculumGroups().stream().map(b -> b.getName().getContent()).collect(Collectors.joining(","));
    }

    public String getMinorBranchNames() {
        return getMinorBranchCurriculumGroups().stream().map(b -> b.getName().getContent()).collect(Collectors.joining(","));
    }

    public Double getApprovedEctsCredits() {
        return getRoot().getAprovedEctsCredits();
    }

    public Double getApprovedEctsCredits(CycleType cycleType) {
        CycleCurriculumGroup cycle = getCycle(cycleType);
        return cycle != null ? cycle.getAprovedEctsCredits() : 0d;
    }

    public Double getApprovedEctsCreditsForFirstCycle(CycleType cycleType) {
        return getApprovedEctsCredits(CycleType.FIRST_CYCLE);
    }

    public Double getApprovedEctsCreditsForSecondCycle(CycleType cycleType) {
        return getApprovedEctsCredits(CycleType.SECOND_CYCLE);
    }

    @Deprecated
    public java.util.Date getStartDate() {
        org.joda.time.YearMonthDay ymd = getStartDateYearMonthDay();
        return ymd == null ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setStartDate(java.util.Date date) {
        if (date == null) {
            setStartDateYearMonthDay(null);
        } else {
            setStartDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getWhen() {
        org.joda.time.DateTime dt = getWhenDateTime();
        return dt == null ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setWhen(java.util.Date date) {
        if (date == null) {
            setWhenDateTime(null);
        } else {
            setWhenDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    public boolean isAllowedToDelete() {
        final Set<StudentCurricularPlan> plans = getRegistration().getStudentCurricularPlansSet();
        return isAllowedToManageEnrolments() && plans.size() > 1;
    }

    public boolean isAllowedToManageEnrolments() {
        return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, getDegree(),
                Authenticate.getUser())
                || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_ENROLMENTS", getDegree(), Authenticate.getUser());
    }

    public boolean isAllowedToManageEquivalencies() {
        return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_EQUIVALENCES, getDegree(),
                Authenticate.getUser())
                || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_CREDITS_TRANSFER", getDegree(), Authenticate.getUser());
    }

    public Stream<Enrolment> getEnrolmentStream() {
        return getRoot().getCurriculumLineStream().filter(cl -> cl.isEnrolment()).map(cl -> (Enrolment) cl);
    }

}
