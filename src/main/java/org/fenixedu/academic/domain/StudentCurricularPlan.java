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

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accounting.events.EnrolmentEvaluationEvent;
import org.fenixedu.academic.domain.accounting.events.EnrolmentOutOfPeriodEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degree.enrollment.CurricularCourse2Enroll;
import org.fenixedu.academic.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.gratuity.GratuitySituationType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurricularPlan.Specialization;
import org.fenixedu.academic.domain.studentCurricularPlan.StudentCurricularPlanState;
import org.fenixedu.academic.domain.studentCurriculum.BranchCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Credits;
import org.fenixedu.academic.domain.studentCurriculum.CreditsManager;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroupFactory;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByApproval;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByExecutionSemester;
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
import org.fenixedu.academic.domain.studentCurriculum.RootCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.StandaloneCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Substitution;
import org.fenixedu.academic.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import org.fenixedu.academic.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.StudentCurricularPlanPredicates;
import org.fenixedu.academic.service.services.exceptions.BothAreasAreTheSameServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedBranchChangeException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EnrolmentEvaluationState;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.academic.util.predicates.ResultCollection;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlan extends StudentCurricularPlan_Base {

    public static final Comparator<StudentCurricularPlan> COMPARATOR_BY_STUDENT_NUMBER = new Comparator<StudentCurricularPlan>() {

        @Override
        public int compare(StudentCurricularPlan o1, StudentCurricularPlan o2) {
            return o1.getStudent().getNumber().compareTo(o2.getStudent().getNumber());
        }

    };

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

    static final public Comparator<StudentCurricularPlan> STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME =
            new Comparator<StudentCurricularPlan>() {

                @Override
                public int compare(StudentCurricularPlan o1, StudentCurricularPlan o2) {
                    final int cd = o1.getDegree().getName().compareTo(o2.getDegree().getName());
                    if (cd != 0) {
                        return cd;
                    }
                    final int cn = o1.getStudent().getNumber().compareTo(o2.getStudent().getNumber());
                    return cn == 0 ? o1.getPerson().getName().compareTo(o2.getPerson().getName()) : cn;
                }

            };

    public static final Comparator<StudentCurricularPlan> COMPARATOR_BY_DEGREE_TYPE = new Comparator<StudentCurricularPlan>() {
        @Override
        public int compare(final StudentCurricularPlan studentCurricularPlan1, final StudentCurricularPlan studentCurricularPlan2) {
            final DegreeType degreeType1 = studentCurricularPlan1.getDegreeType();
            final DegreeType degreeType2 = studentCurricularPlan2.getDegreeType();
            return degreeType1.compareTo(degreeType2);
        }
    };

    static final public Comparator<StudentCurricularPlan> STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE =
            new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(final StudentCurricularPlan o1, final StudentCurricularPlan o2) {
                    return o1.getStartDateYearMonthDay().compareTo(o2.getStartDateYearMonthDay());
                }
            };

    static final public Comparator<StudentCurricularPlan> DATE_COMPARATOR = new Comparator<StudentCurricularPlan>() {
        @Override
        public int compare(StudentCurricularPlan leftState, StudentCurricularPlan rightState) {
            int comparationResult = leftState.getStartDateYearMonthDay().compareTo(rightState.getStartDateYearMonthDay());
            return (comparationResult == 0) ? leftState.getExternalId().compareTo(rightState.getExternalId()) : comparationResult;
        }
    };

    static final public Comparator<StudentCurricularPlan> COMPARATOR_BY_STUDENT_IST_ID = new Comparator<StudentCurricularPlan>() {
        @Override
        public int compare(final StudentCurricularPlan redSCP, final StudentCurricularPlan blueSCP) {
            return redSCP.getPerson().getUsername() == null ? -1 : (blueSCP.getPerson().getUsername() == null ? 1 : redSCP
                    .getPerson().getUsername().compareTo(blueSCP.getPerson().getUsername()));
        }
    };

    private StudentCurricularPlan() {
        super();
        setCurrentState(StudentCurricularPlanState.ACTIVE);
        setRootDomainObject(Bennu.getInstance());
        setWhenDateTime(new DateTime());
        setGivenCredits(Double.valueOf(0));
    }

    static public StudentCurricularPlan createPreBolonhaMasterDegree(Registration registration,
            DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate, Branch branch, Double givenCredits,
            Specialization specialization) {
        return new StudentCurricularPlan(registration, degreeCurricularPlan, startDate, branch, givenCredits, specialization);
    }

    private StudentCurricularPlan(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate,
            Branch branch, Double givenCredits, Specialization specialization) {

        this(registration, degreeCurricularPlan, startDate);

        setBranch(branch);
        setGivenCredits(givenCredits);
        setSpecialization(specialization);
    }

    private StudentCurricularPlan(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate) {
        this();
        init(registration, degreeCurricularPlan, startDate);
    }

    static public StudentCurricularPlan createWithEmptyStructure(final Registration registration,
            final DegreeCurricularPlan degreeCurricularPlan, final YearMonthDay startDate) {

        final StudentCurricularPlan result = new StudentCurricularPlan(registration, degreeCurricularPlan, startDate);

        CurriculumGroupFactory.createRoot(result, degreeCurricularPlan.getRoot(), null);

        return result;
    }

    static public StudentCurricularPlan createWithEmptyStructure(final Registration registration,
            final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType, final YearMonthDay startDate) {

        final StudentCurricularPlan result = new StudentCurricularPlan(registration, degreeCurricularPlan, startDate);

        CurriculumGroupFactory.createRoot(result, degreeCurricularPlan.getRoot(), cycleType);

        return result;
    }

    static public StudentCurricularPlan createBolonhaStudentCurricularPlan(Registration registration,
            DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate, ExecutionSemester executionSemester) {
        return createBolonhaStudentCurricularPlan(registration, degreeCurricularPlan, startDate, executionSemester,
                (CycleType) null);
    }

    static public StudentCurricularPlan createBolonhaStudentCurricularPlan(Registration registration,
            DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate, ExecutionSemester executionSemester,
            CycleType cycleType) {
        return new StudentCurricularPlan(registration, degreeCurricularPlan, startDate, executionSemester, cycleType);
    }

    private StudentCurricularPlan(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate,
            ExecutionSemester executionSemester, CycleType cycleType) {

        this(registration, degreeCurricularPlan, startDate);
        createStructure(executionSemester, cycleType);
    }

    private void createStructure(final ExecutionSemester executionSemester, CycleType cycleType) {
        CurriculumGroupFactory.createRoot(this, getDegreeCurricularPlan().getRoot(), executionSemester, cycleType);
    }

    private void init(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate) {

        checkParameters(registration, degreeCurricularPlan, startDate);

        setDegreeCurricularPlan(degreeCurricularPlan);
        setRegistration(registration);
        setStartDateYearMonthDay(startDate);
    }

    private void checkParameters(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate) {

        if (registration == null) {
            throw new DomainException("error.studentCurricularPlan.registration.cannot.be.null");
        }
        if (degreeCurricularPlan == null) {
            throw new DomainException("error.studentCurricularPlan.degreeCurricularPlan.cannot.be.null");
        }
        if (startDate == null) {
            throw new DomainException("error.studentCurricularPlan.startDate.cannot.be.null");
        }
    }

    public void delete() throws DomainException {

        checkRulesToDelete();

        for (; !getEnrolmentsSet().isEmpty(); getEnrolmentsSet().iterator().next().delete()) {
            ;
        }

        getRoot().delete();

        for (Iterator<NotNeedToEnrollInCurricularCourse> iter = getNotNeedToEnrollCurricularCoursesSet().iterator(); iter
                .hasNext();) {
            NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = iter.next();
            iter.remove();
            notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(null);
            notNeedToEnrollInCurricularCourse.delete();
        }

        for (; !getCreditsSet().isEmpty(); getCreditsSet().iterator().next().delete()) {
            ;
        }

        setDegreeCurricularPlan(null);
        setBranch(null);
        setStudent(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    private void checkRulesToDelete() {
        if (!getGratuityEventsSet().isEmpty()) {
            throw new DomainException("error.StudentCurricularPlan.cannot.delete.because.already.has.gratuity.events");
        }

        if (!getGratuitySituationsSet().isEmpty()) {
            throw new DomainException("error.StudentCurricularPlan.cannot.delete.because.already.has.gratuity.situations");
        }
    }

    final public String print() {
        final StringBuilder result = new StringBuilder();
        result.append("[SCP ").append(this.getExternalId()).append("] ").append(this.getName()).append("\n");
        result.append(getRoot().print(""));
        return result.toString();
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

    public boolean hasConcludedAnyInternalCycle() {
        for (final CycleCurriculumGroup cycleCurriculumGroup : getInternalCycleCurriculumGrops()) {
            if (cycleCurriculumGroup.isConcluded()) {
                return true;
            }
        }

        return false;
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

        if (isEmptyDegree()) {
            return getRegistration().getLastActiveState().getStateType().equals(RegistrationStateType.CONCLUDED);
        }

        return ProgramConclusion.isConcluded(this);

    }

    final public boolean isConclusionProcessed() {

        if (isEmptyDegree()) {
            return getRegistration().getLastActiveState().getStateType().equals(RegistrationStateType.CONCLUDED);
        }

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

    public boolean hasIncompleteState() {
        return getCurrentState().equals(StudentCurricularPlanState.INCOMPLETE);
    }

    public boolean isTransition() {
        return getRegistration().isTransition();
    }

    final public boolean isBolonhaDegree() {
        return getDegreeCurricularPlan().isBolonhaDegree();
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

    final public boolean hasClassification() {
        return getClassification() != null && getClassification().doubleValue() != 0d;
    }

    @Override
    @Deprecated
    final public Registration getStudent() {
        return this.getRegistration();
    }

    public void setStartDate(YearMonthDay startDate) {
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

    final public boolean hasAnyCurriculumLines(final ExecutionSemester executionSemester) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionSemester(executionSemester));

        return hasAnyCurriculumModules(andPredicate);

    }

    final public boolean hasEnrolments(final Enrolment enrolment) {
        return getRoot().hasCurriculumModule(enrolment);
    }

    final public boolean hasEnrolments(final ExecutionYear executionYear) {
        return getRoot().hasEnrolment(executionYear);
    }

    final public boolean hasEnrolments(final ExecutionSemester executionSemester) {
        return getRoot().hasEnrolment(executionSemester);
    }

    final public long countCurrentEnrolments() {
        return getEnrolmentsSet().stream()
                .filter(enrolment -> enrolment.getExecutionPeriod().getExecutionYear().isCurrent() && enrolment.isEnroled())
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

    final public int countEnrolmentsByCurricularCourse(final CurricularCourse curricularCourse,
            final ExecutionSemester untilExecutionPeriod) {
        int count = 0;
        for (Enrolment enrolment : getEnrolments(curricularCourse)) {
            if (enrolment.getExecutionPeriod().isBeforeOrEquals(untilExecutionPeriod)) {
                count++;
            }
        }
        return count;
    }

    final public List<Enrolment> getEnrolmentsByState(final EnrollmentState state) {
        List<Enrolment> results = new ArrayList<Enrolment>();
        for (Enrolment enrolment : this.getEnrolmentsSet()) {
            if (enrolment.getEnrollmentState().equals(state)) {
                results.add(enrolment);
            }
        }
        return results;
    }

    final public List<Enrolment> getEnrolmentsByExecutionPeriod(final ExecutionSemester executionSemester) {
        List<Enrolment> results = new ArrayList<Enrolment>();
        for (Enrolment enrolment : this.getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod() == executionSemester) {
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

    private Collection<Enrolment> getVisibleEnroledEnrolments(final ExecutionSemester executionSemester) {
        final Collection<Enrolment> result = new ArrayList<Enrolment>();

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (!enrolment.isAnnulled() && !enrolment.isInvisible()
                    && (executionSemester == null || enrolment.isValid(executionSemester))) {
                result.add(enrolment);
            }
        }

        return result;
    }

    final public int countEnrolments(final ExecutionSemester executionSemester) {
        int numberEnrolments = 0;
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod() == executionSemester) {
                numberEnrolments++;
            }
        }
        return numberEnrolments;
    }

    final public boolean hasAnyEnrolmentForExecutionPeriod(final ExecutionSemester executionSemester) {
        for (final Enrolment enrolment : this.getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod() == executionSemester) {
                return true;
            }
        }
        return false;
    }

    final public List<Enrolment> getEnrolmentsByExecutionYear(final ExecutionYear executionYear) {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod().getExecutionYear() == executionYear) {
                result.add(enrolment);
            }
        }

        return result;
    }

    final public int countEnrolments(final ExecutionYear executionYear) {
        int numberEnrolments = 0;
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod().getExecutionYear() == executionYear) {
                numberEnrolments++;
            }
        }
        return numberEnrolments;
    }

    final public boolean hasAnyEnrolmentForExecutionYear(final ExecutionYear executionYear) {
        for (final Enrolment enrolment : this.getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasAnyEnrolmentForCurrentExecutionYear() {
        return hasAnyEnrolmentForExecutionYear(ExecutionYear.readCurrentExecutionYear());
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

    public Collection<CurriculumLine> getApprovedCurriculumLines(final ExecutionSemester executionSemester) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionSemester(executionSemester));
        andPredicate.add(new CurriculumModulePredicateByApproval());

        return (Collection<CurriculumLine>) getCurriculumModules(new ResultCollection<CurriculumModule>(andPredicate));
    }

    public Collection<CurriculumLine> getApprovedCurriculumLines(final ExecutionYear executionYear) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionYear(executionYear));
        andPredicate.add(new CurriculumModulePredicateByApproval());

        return (Collection<CurriculumLine>) getCurriculumModules(new ResultCollection<CurriculumModule>(andPredicate));
    }

    final public ExecutionYear getApprovedCurriculumLinesLastExecutionYear() {
        return getRoot().getApprovedCurriculumLinesLastExecutionYear();
    }

    final public CurriculumLine getLastApprovement() {
        return getAprovedEnrolments().stream().sorted(CurriculumLine.COMPARATOR_BY_APPROVEMENT_DATE_AND_ID.reversed())
                .findFirst().orElse(null);
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

    final public List<Enrolment> getAprovedEnrolmentsInExecutionPeriod(final ExecutionSemester executionSemester) {
        final List<Enrolment> result = new ArrayList<Enrolment>();

        for (final Enrolment enrolment : getEnrolmentsByExecutionPeriod(executionSemester)) {
            if (enrolment.isApproved()) {
                result.add(enrolment);
            }
        }

        return result;
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

    final public Enrolment getEnrolment(String executionYear, Integer semester, String code) {
        for (Enrolment enrolment : this.getEnrolmentsSet()) {
            if (enrolment.getCurricularCourse().getCode().equals(code)
                    && enrolment.getExecutionPeriod().getSemester().equals(semester)
                    && enrolment.getExecutionPeriod().getExecutionYear().getYear().equals(executionYear)) {
                return enrolment;
            }
        }
        return null;
    }

    final public Enrolment getEnrolment(ExecutionSemester executionSemester, String code) {
        for (Enrolment enrolment : this.getEnrolmentsSet()) {
            if (enrolment.getCurricularCourse().getCode().equals(code) && enrolment.getExecutionPeriod() == executionSemester) {
                return enrolment;
            }
        }
        return null;
    }

    final public Enrolment findEnrolmentByEnrolmentID(final String enrolmentID) {
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExternalId().equals(enrolmentID)) {
                return enrolment;
            }
        }
        return null;
    }

    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return getRoot().findEnrolmentFor(curricularCourse, executionSemester);
    }

    final public Enrolment getEnrolmentByCurricularCourseAndExecutionPeriod(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getCurricularCourse().equals(curricularCourse) && enrolment.isValid(executionSemester)) {
                return enrolment;
            }
        }

        return null;
    }

    final public Set<ExecutionSemester> getEnrolmentsExecutionPeriods() {
        final Set<ExecutionSemester> result = new HashSet<ExecutionSemester>();

        for (final Enrolment enrolment : this.getEnrolmentsSet()) {
            result.add(enrolment.getExecutionPeriod());
        }

        return result;
    }

    final public Set<ExecutionYear> getEnrolmentsExecutionYears() {
        final Set<ExecutionYear> result = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);

        for (final Enrolment enrolment : this.getEnrolmentsSet()) {
            result.add(enrolment.getExecutionPeriod().getExecutionYear());
        }

        return result;
    }

    final public ExecutionYear getStartExecutionYear() {
        return ExecutionYear.getExecutionYearByDate(getStartDateYearMonthDay());
    }

    public ExecutionSemester getStartExecutionPeriod() {
        ExecutionSemester result = null;

        final YearMonthDay startDate = getStartDateYearMonthDay();
        if (startDate != null) {
            result = ExecutionSemester.readByDateTime(startDate.toDateTimeAtMidnight());

            if (result == null) {
                result = ExecutionYear.readByDateTime(startDate.toDateTimeAtMidnight()).getFirstExecutionPeriod();
            }
        }

        return result != null ? result : getFirstExecutionPeriod();
    }

    final public ExecutionSemester getFirstExecutionPeriod() {
        ExecutionSemester result = null;

        for (final CurriculumLine curriculumLine : this.getAllCurriculumLines()) {
            final ExecutionSemester executionSemester = curriculumLine.getExecutionPeriod();
            if (result == null || (executionSemester != null && result.isAfter(executionSemester))) {
                result = executionSemester;
            }
        }

        return result;
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

    final public ExecutionYear getLastExecutionYear() {
        ExecutionYear result = null;

        for (final CurriculumLine curriculumLine : this.getAllCurriculumLines()) {
            final ExecutionSemester executionSemester = curriculumLine.getExecutionPeriod();
            if (result == null || (executionSemester != null && result.isBefore(executionSemester.getExecutionYear()))) {
                result = executionSemester.getExecutionYear();
            }
        }

        return result;
    }

    public boolean hasGivenCredits() {
        return getGivenCredits() != null && getGivenCredits().doubleValue() != Double.valueOf(0).doubleValue();
    }

    public Integer getCreditsInSecundaryArea() {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // return a value
        return Integer.valueOf(0);
    }

    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // set a value
    }

    public Integer getCreditsInSpecializationArea() {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // return a value
        return Integer.valueOf(0);
    }

    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // set a value
    }

    public Branch getSecundaryBranch() {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // return a value
        return null;
    }

    public boolean hasSecundaryBranch() {
        return (getSecundaryBranch() != null);
    }

    public void setSecundaryBranch(Branch secundaryBranch) {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // set a value
    }

    final public Integer getSecundaryBranchKey() {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // return a value
        return null;
    }

    public void setSecundaryBranchKey(Integer secundaryBranchKey) {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // set a value
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

    final public int getNumberOfStudentEnrollmentsWithApprovedState() {
        int result = 0;

        for (final Enrolment enrolment : getAllEnrollments()) {
            if (enrolment.isApproved()) {
                result++;
            }
        }

        return result;
    }

    final protected boolean isApproved(CurricularCourse curricularCourse, List<CurricularCourse> approvedCourses) {
        return hasEquivalenceIn(curricularCourse, approvedCourses) || hasEquivalenceInNotNeedToEnroll(curricularCourse);
    }

    final public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return getRoot().isApproved(curricularCourse, executionSemester);
    }

    final public boolean isCurricularCourseApproved(CurricularCourse curricularCourse) {
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

    final public boolean isApproved(final CurricularCourse curricularCourse) {
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

    final public boolean isCurricularCourseNotExtraApprovedInCurrentOrPreviousPeriod(final CurricularCourse course,
            final ExecutionSemester executionSemester) {
        final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();
        final List<CurricularCourse> approvedCurricularCourses = new ArrayList<CurricularCourse>();

        for (Iterator iter = studentApprovedEnrollments.iterator(); iter.hasNext();) {
            Enrolment enrolment = (Enrolment) iter.next();
            if (!enrolment.isExtraCurricular() && enrolment.getExecutionPeriod().compareTo(executionSemester) <= 0) {
                approvedCurricularCourses.add(enrolment.getCurricularCourse());
            }
        }

        return isApproved(course, approvedCurricularCourses);
    }

    final public boolean isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {

        for (final Enrolment enrolment : getStudentEnrollmentsWithApprovedState()) {
            if (enrolment.getCurricularCourse().getExternalId().equals(curricularCourse.getExternalId())
                    && enrolment.isApproved() && (enrolment.getExecutionPeriod().compareTo(executionSemester) <= 0)) {
                return true;
            }
        }

        return false;
    }

    final public boolean isEquivalentAproved(CurricularCourse curricularCourse) {
        List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        List<CurricularCourse> result = (List) CollectionUtils.collect(studentApprovedEnrollments, new Transformer() {
            @Override
            final public Object transform(Object obj) {
                Enrolment enrollment = (Enrolment) obj;

                return enrollment.getCurricularCourse();

            }
        });

        return isThisCurricularCoursesInTheList(curricularCourse, result) || hasEquivalenceInNotNeedToEnroll(curricularCourse);
    }

    private boolean hasEquivalenceInNotNeedToEnroll(CurricularCourse curricularCourse) {

        if (notNeedToEnroll(curricularCourse)) {
            return true;
        }

        for (CurricularCourseEquivalence equiv : curricularCourse.getCurricularCourseEquivalencesSet()) {
            if (allNotNeedToEnroll(equiv.getOldCurricularCoursesSet())) {
                return true;
            }
        }

        return false;
    }

    final public void initEctsCreditsToEnrol(List<CurricularCourse2Enroll> setOfCurricularCoursesToEnroll,
            ExecutionSemester executionSemester) {
        for (CurricularCourse2Enroll curricularCourse2Enroll : setOfCurricularCoursesToEnroll) {
            curricularCourse2Enroll.setEctsCredits(this.getAccumulatedEctsCredits(executionSemester,
                    curricularCourse2Enroll.getCurricularCourse()));
        }
    }

    private boolean allNotNeedToEnroll(Collection<CurricularCourse> oldCurricularCourses) {
        for (CurricularCourse course : oldCurricularCourses) {
            if (!notNeedToEnroll(course)) {
                return false;
            }
        }
        return true;
    }

    final protected boolean hasEquivalenceIn(CurricularCourse curricularCourse, List<CurricularCourse> otherCourses) {
        if (otherCourses.isEmpty()) {
            return false;
        }

        if (isThisCurricularCoursesInTheList(curricularCourse, otherCourses)) {
            return true;
        }

        for (CurricularCourseEquivalence equiv : curricularCourse.getCurricularCourseEquivalencesSet()) {
            if (allCurricularCoursesInTheList(equiv.getOldCurricularCoursesSet(), otherCourses)) {
                return true;
            }
        }

        return false;
    }

    private boolean allCurricularCoursesInTheList(Collection<CurricularCourse> oldCurricularCourses,
            List<CurricularCourse> otherCourses) {
        for (CurricularCourse oldCurricularCourse : oldCurricularCourses) {
            if (!isThisCurricularCoursesInTheList(oldCurricularCourse, otherCourses)
                    && !hasEquivalenceInNotNeedToEnroll(oldCurricularCourse)) {
                return false;
            }
        }
        return true;
    }

    final public boolean isCurricularCourseEnrolled(CurricularCourse curricularCourse) {
        List result = (List) CollectionUtils.collect(getStudentEnrollmentsWithEnrolledState(), new Transformer() {
            @Override
            final public Object transform(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getCurricularCourse();
            }
        });

        return result.contains(curricularCourse);
    }

    final public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse) {
        return isEnroledInExecutionPeriod(curricularCourse, ExecutionSemester.readActualExecutionSemester());
    }

    final public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        return getRoot().isEnroledInExecutionPeriod(curricularCourse, executionSemester);
    }

    final public Integer getCurricularCourseAcumulatedEnrollments(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester) {

        Integer curricularCourseAcumulatedEnrolments = calculateStudentAcumulatedEnrollments(curricularCourse, executionSemester);

        if (curricularCourseAcumulatedEnrolments == null) {
            curricularCourseAcumulatedEnrolments = Integer.valueOf(0);
        }

        if (curricularCourseAcumulatedEnrolments.intValue() >= curricularCourse.getMinimumValueForAcumulatedEnrollments()
                .intValue()) {
            curricularCourseAcumulatedEnrolments = curricularCourse.getMaximumValueForAcumulatedEnrollments();
        }

        if (curricularCourseAcumulatedEnrolments.intValue() == 0) {
            curricularCourseAcumulatedEnrolments = curricularCourse.getMinimumValueForAcumulatedEnrollments();
        }

        return curricularCourseAcumulatedEnrolments;
    }

    final public Integer getCurricularCourseAcumulatedEnrollments(CurricularCourse curricularCourse) {
        return getCurricularCourseAcumulatedEnrollments(curricularCourse, ExecutionSemester.readActualExecutionSemester());
    }

    final public List<Enrolment> getAllStudentEnrolledEnrollmentsInExecutionPeriod(final ExecutionSemester executionSemester) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        for (Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod().equals(executionSemester) && enrolment.isEnroled() && !enrolment.isInvisible()) {
                result.add(enrolment);
            }
        }
        return result;
    }

    final public boolean hasEnrolledStateInPreviousExecutionPerdiod(CurricularCourse curricularCourse,
            List<Enrolment> enrollmentsWithEnrolledStateInPreviousExecutionPeriod) {
        for (Enrolment enrolment : enrollmentsWithEnrolledStateInPreviousExecutionPeriod) {
            if (enrolment.getCurricularCourse().equals(curricularCourse)) {
                return true;
            }
        }
        return false;
    }

    public boolean areNewAreasCompatible(Branch specializationArea, Branch secundaryArea)
            throws BothAreasAreTheSameServiceException, InvalidArgumentsServiceException {

        return true;
    }

    public boolean getCanChangeSpecializationArea() {
        if (getBranch() != null) {
            return false;
        }
        return true;
    }

    final public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester) {
        double result = 0.0;

        for (final Enrolment enrolment : getVisibleEnroledEnrolments(executionSemester)) {
            result += getAccumulatedEctsCredits(executionSemester, enrolment.getCurricularCourse());
        }

        return result;
    }

    final public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester,
            final CurricularCourse curricularCourse) {
        if (curricularCourse.isBolonhaDegree()) {
            return isAccumulated(executionSemester, curricularCourse) ? MaximumNumberOfCreditsForEnrolmentPeriod
                    .getAccumulatedEcts(curricularCourse, executionSemester) : curricularCourse.getEctsCredits(
                    executionSemester.getSemester(), executionSemester);
        } else {
            return getAccumulatedEctsCreditsForOldCurricularCourses(curricularCourse, executionSemester);
        }
    }

    private double getAccumulatedEctsCreditsForOldCurricularCourses(final CurricularCourse curricularCourse,
            ExecutionSemester executionSemester) {
        Double factor;
        Integer curricularCourseAcumulatedEnrolments = calculateStudentAcumulatedEnrollments(curricularCourse, executionSemester);
        if (curricularCourseAcumulatedEnrolments == null || curricularCourseAcumulatedEnrolments.intValue() == 0) {
            factor = 1.0;
        } else {
            factor = 0.75;
        }
        return curricularCourse.getEctsCredits() * factor;
    }

    private boolean isAccumulated(final ExecutionSemester executionSemester, final CurricularCourse curricularCourse) {
        if (curricularCourse.isBolonhaDegree()) {
            return hasEnrolmentInCurricularCourseBefore(curricularCourse, executionSemester);
        } else {
            Integer curricularCourseAcumulatedEnrolments =
                    calculateStudentAcumulatedEnrollments(curricularCourse, executionSemester);
            return curricularCourseAcumulatedEnrolments != null && curricularCourseAcumulatedEnrolments.intValue() != 0;
        }
    }

    private boolean hasEnrolmentInCurricularCourseBefore(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        return getRoot().hasEnrolmentInCurricularCourseBefore(curricularCourse, executionSemester);
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    public double getEnrolmentsEctsCredits(final ExecutionYear executionYear) {
        return executionYear.getExecutionPeriodsSet().stream().mapToDouble(semester -> getEnrolmentsEctsCredits(semester)).sum();
    }

    final public double getEnrolmentsEctsCredits(final ExecutionSemester executionSemester) {
        return getEnrolmentsSet().stream().filter(e -> e.isValid(executionSemester)).mapToDouble(Enrolment::getEctsCredits).sum();
    }

    final public double getDismissalsEctsCredits() {
        return getDismissals().stream().mapToDouble(Dismissal::getEctsCredits).sum();
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    private Integer calculateStudentAcumulatedEnrollments(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        if (!this.isBolonhaDegree()) {
            return getRoot().calculateStudentAcumulatedEnrollments(curricularCourse, executionSemester);
        }
        return null;
    }

    private Set getCurricularCoursesInCurricularCourseEquivalences(final CurricularCourse curricularCourse) {

        final Set<CurricularCourse> curricularCoursesEquivalent = new HashSet<CurricularCourse>();
        final List<CurricularCourse> sameCompetenceCurricularCourses;

        if (curricularCourse.getCompetenceCourse() == null) {
            sameCompetenceCurricularCourses = new ArrayList<CurricularCourse>();
            sameCompetenceCurricularCourses.add(curricularCourse);
        } else {
            sameCompetenceCurricularCourses = new ArrayList<CurricularCourse>();
            for (final CurricularCourse course : curricularCourse.getCompetenceCourse().getAssociatedCurricularCoursesSet()) {
                if (!course.isBolonhaDegree()) {
                    sameCompetenceCurricularCourses.add(course);
                }
            }
        }

        for (CurricularCourse course : sameCompetenceCurricularCourses) {
            for (CurricularCourseEquivalence curricularCourseEquivalence : course.getOldCurricularCourseEquivalencesSet()) {
                curricularCoursesEquivalent.add(curricularCourseEquivalence.getEquivalentCurricularCourse());
            }
        }

        return curricularCoursesEquivalent;
    }

    private boolean isThisCurricularCoursesInTheList(final CurricularCourse curricularCourse,
            List<CurricularCourse> curricularCourses) {
        for (CurricularCourse otherCourse : curricularCourses) {
            if ((curricularCourse == otherCourse) || haveSameCompetence(curricularCourse, otherCourse)) {
                return true;
            }
        }
        return false;
    }

    final public NotNeedToEnrollInCurricularCourse findNotNeddToEnrol(final CurricularCourse curricularCourse) {
        for (NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse : getNotNeedToEnrollCurricularCoursesSet()) {
            final CurricularCourse otherCourse = notNeedToEnrollInCurricularCourse.getCurricularCourse();
            if ((curricularCourse == otherCourse) || haveSameCompetence(curricularCourse, otherCourse)) {
                return notNeedToEnrollInCurricularCourse;
            }
        }
        return null;
    }

    final public boolean notNeedToEnroll(final CurricularCourse curricularCourse) {
        return findNotNeddToEnrol(curricularCourse) != null;
    }

    private boolean haveSameCompetence(CurricularCourse course1, CurricularCourse course2) {
        CompetenceCourse comp1 = course1.getCompetenceCourse();
        CompetenceCourse comp2 = course2.getCompetenceCourse();
        return (comp1 != null) && (comp1 == comp2);
    }

    private List<CurricularCourse> getStudentNotNeedToEnrollCurricularCourses() {
        return (List<CurricularCourse>) CollectionUtils.collect(getNotNeedToEnrollCurricularCoursesSet(), new Transformer() {
            @Override
            final public Object transform(Object obj) {
                NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (NotNeedToEnrollInCurricularCourse) obj;
                return notNeedToEnrollInCurricularCourse.getCurricularCourse();
            }
        });
    }

    final protected boolean hasCurricularCourseEquivalenceIn(CurricularCourse curricularCourse, List curricularCoursesEnrollments) {

        int size = curricularCoursesEnrollments.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse tempCurricularCourse = ((Enrolment) curricularCoursesEnrollments.get(i)).getCurricularCourse();
            Set curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(tempCurricularCourse);
            if (curricularCourseEquivalences.contains(curricularCourse)) {
                return true;
            }
        }

        List<CurricularCourse> studentNotNeedToEnrollCourses = getStudentNotNeedToEnrollCurricularCourses();

        if (isThisCurricularCoursesInTheList(curricularCourse, studentNotNeedToEnrollCourses)) {
            return true;
        }

        size = studentNotNeedToEnrollCourses.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse ccNotNeedToDo = studentNotNeedToEnrollCourses.get(i);
            Set curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(ccNotNeedToDo);
            if (curricularCourseEquivalences.contains(curricularCourse)) {
                return true;
            }
        }

        return false;
    }

    final protected boolean isMathematicalCourse(CurricularCourse curricularCourse) {
        String code = curricularCourse.getCode();

        return code.equals("QN") || code.equals("PY") || code.equals("P5") || code.equals("UN") || code.equals("U8")
                || code.equals("AZ2") || code.equals("AZ3") || code.equals("AZ4") || code.equals("AZ5") || code.equals("AZ6");
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    final public void setStudentAreasWithoutRestrictions(Branch specializationArea, Branch secundaryArea) throws DomainException {

        if (specializationArea != null && secundaryArea != null && specializationArea.equals(secundaryArea)) {
            throw new DomainException("error.student.curricular.plan.areas.conflict");
        }

        setBranch(specializationArea);
        setSecundaryBranch(secundaryArea);
    }

    final public void setStudentAreas(Branch specializationArea, Branch secundaryArea) throws NotAuthorizedBranchChangeException,
            BothAreasAreTheSameServiceException, InvalidArgumentsServiceException, DomainException {

        if (!getCanChangeSpecializationArea()) {
            throw new NotAuthorizedBranchChangeException();
        }

        if (areNewAreasCompatible(specializationArea, secundaryArea)) {
            setStudentAreasWithoutRestrictions(specializationArea, secundaryArea);
        }

        else {
            throw new DomainException("error.student.curricular.plan.areas.conflict");
        }
    }

    final public GratuitySituation getGratuitySituationByGratuityValues(final GratuityValues gratuityValues) {
        for (final GratuitySituation gratuitySituation : getGratuitySituationsSet()) {
            if (gratuitySituation.getGratuityValues().equals(gratuityValues)) {
                return gratuitySituation;
            }
        }

        return null;
    }

    final public GratuitySituation getGratuitySituationByGratuityValuesAndGratuitySituationType(
            final GratuitySituationType gratuitySituationType, final GratuityValues gratuityValues) {

        GratuitySituation gratuitySituation = this.getGratuitySituationByGratuityValues(gratuityValues);
        if (gratuitySituation != null
                && (gratuitySituationType == null || ((gratuitySituationType.equals(GratuitySituationType.CREDITOR) && gratuitySituation
                        .getRemainingValue() < 0.0)
                        || (gratuitySituationType.equals(GratuitySituationType.DEBTOR) && gratuitySituation.getRemainingValue() > 0.0) || (gratuitySituationType
                        .equals(GratuitySituationType.REGULARIZED) && gratuitySituation.getRemainingValue() == 0.0)))) {
            return gratuitySituation;
        }
        return null;
    }

    final public <T extends GratuityEvent> Stream<T> getGratuityEvent(final ExecutionYear executionYear,
            final Class<T> type) {
        return getGratuityEvent(executionYear, type, true);
    }

    final public <T extends GratuityEvent> Stream<T> getGratuityEvent(final ExecutionYear executionYear,
            final Class<T> type, boolean excludeCanceled) {

        Stream<T> eventStream = getGratuityEventsSet().stream().filter(g -> g.getExecutionYear().equals(executionYear))
                .filter(type::isInstance).map(type::cast);

        if (excludeCanceled) {
            eventStream = eventStream.filter(g -> !g.isCancelled());
        }

        return eventStream;
    }

    final public boolean hasGratuityEvent(final ExecutionYear executionYear, final Class<? extends GratuityEvent> type) {
        return getGratuityEvent(executionYear, type).count() > 0;
    }

    final public Set<GratuityEvent> getNotPayedGratuityEvents() {
        final Set<GratuityEvent> result = new HashSet<GratuityEvent>();

        for (final GratuityEvent gratuityEvent : getGratuityEventsSet()) {
            if (gratuityEvent.isInDebt()) {
                result.add(gratuityEvent);
            }
        }

        return result;
    }

    final public boolean hasAnyNotPayedGratuityEvents() {
        for (final GratuityEvent gratuityEvent : getGratuityEventsSet()) {
            if (gratuityEvent.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyNotPayedGratuityEventsUntil(final ExecutionYear executionYear) {
        for (final GratuityEvent gratuityEvent : getGratuityEventsSet()) {
            if (gratuityEvent.getExecutionYear().isBeforeOrEquals(executionYear) && gratuityEvent.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    /*
     * Check payed gratuity events until given execution year (exclusive)
     */
    final public boolean hasAnyNotPayedGratuityEventsForPreviousYears(final ExecutionYear limitExecutionYear) {

        for (final GratuityEvent gratuityEvent : getGratuityEventsSet()) {
            if (gratuityEvent.getExecutionYear().isBefore(limitExecutionYear) && gratuityEvent.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    public int numberCompletedCoursesForSpecifiedDegrees(final Set<Degree> degrees) {
        int numberCompletedCourses = 0;
        for (final StudentCurricularPlan studentCurricularPlan : getRegistration().getStudentCurricularPlansSet()) {
            for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (!enrolment.isInvisible() && enrolment.isApproved()) {
                    final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
                    final ExecutionYear executionYear = executionSemester.getExecutionYear();
                    if (!executionYear.isCurrent()) {
                        final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
                        final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                        final Degree degree = degreeCurricularPlan.getDegree();
                        final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
                        if (degrees.contains(degree)
                                || (competenceCourse != null && competenceCourse.isAssociatedToAnyDegree(degrees))) {
                            numberCompletedCourses++;
                        }
                    }
                }
            }
        }
        return numberCompletedCourses;
    }

    public boolean isEnroledInSpecialSeason(final ExecutionSemester executionSemester) {
        return getRoot().isEnroledInSpecialSeason(executionSemester);
    }

    public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear) {
        return getRoot().isEnroledInSpecialSeason(executionYear);
    }

    final public Collection<Enrolment> getSpecialSeasonToEnrol(ExecutionYear executionYear) {
        Map<CurricularCourse, Enrolment> result = new HashMap<CurricularCourse, Enrolment>();

        for (Enrolment enrolment : getEnrolmentsSet()) {
            if (!enrolment.getEvaluationSeason().isSpecial()
                    && enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear) && !enrolment.isApproved()) {
                if (result.get(enrolment.getCurricularCourse()) != null) {
                    Enrolment enrolmentMap = result.get(enrolment.getCurricularCourse());
                    if (enrolment.getExecutionPeriod().compareTo(enrolmentMap.getExecutionPeriod()) > 0) {
                        result.put(enrolment.getCurricularCourse(), enrolment);
                    }
                } else {
                    result.put(enrolment.getCurricularCourse(), enrolment);
                }
            }
        }
        return new HashSet<Enrolment>(result.values());
    }

    final public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionYear executionYear) {
        if (isBolonhaDegree()) {
            return getRoot().getSpecialSeasonEnrolments(executionYear);
        }

        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getEvaluationSeason().isSpecial()
                    && enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                result.add(enrolment);
            }
        }
        return result;
    }

    final public Collection<Enrolment> getExtraordinarySeasonEnrolments(ExecutionYear executionYear) {
        if (isBolonhaDegree()) {
            return getRoot().getExtraordinarySeasonEnrolments(executionYear);
        }

        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getEvaluationSeason().isExtraordinary()
                    && enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                result.add(enrolment);
            }
        }
        return result;
    }

    public BigDecimal getSpecialSeasonEcts(ExecutionYear executionYear) {
        Collection<Enrolment> specialSeasonEnrolments = getSpecialSeasonEnrolments(executionYear);
        BigDecimal result = BigDecimal.ZERO;
        for (Enrolment enrolment : specialSeasonEnrolments) {
            result = result.add(BigDecimal.valueOf(enrolment.getEctsCredits()));
        }
        return result;
    }

    /**
     * Has special season in given semester if is enroled in special season in
     * previous semester
     * @param executionSemester
     * 
     */
    public boolean hasSpecialSeasonFor(final ExecutionSemester executionSemester) {
        final ExecutionSemester previous = executionSemester.getPreviousExecutionPeriod();
        return previous != null && isEnroledInSpecialSeason(previous.getExecutionYear());
    }

    // Improvements
    final public void createEnrolmentEvaluationForImprovement(final Collection<Enrolment> toCreate, final Person person,
            final ExecutionSemester executionSemester) {


        for (final Enrolment enrolment : toCreate) {
            final EnrolmentEvaluation enrolmentEvaluationForImprovement =
                    enrolment.createEnrolmentEvaluationForImprovement(person, executionSemester);
            if (isToPayImprovementOfApprovedEnrolments()) {
                EnrolmentEvaluationEvent.create(enrolmentEvaluationForImprovement);
            }
        }


    }

    private boolean isToPayImprovementOfApprovedEnrolments() {
        final RegistrationProtocol protocol = getRegistration().getRegistrationProtocol();
        return !protocol.isMilitaryAgreement();
    }

    final public List<Enrolment> getEnroledImprovements() {
        final List<Enrolment> enroledImprovements = new ArrayList<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.isImprovementEnroled()) {
                enroledImprovements.add(enrolment);
            }
        }
        return enroledImprovements;
    }

    public List<Enrolment> getEnroledImprovements(final ExecutionSemester executionSemester) {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.hasImprovementFor(executionSemester)) {
                result.add(enrolment);
            }
        }
        return result;
    }

    final public void createFirstTimeStudentEnrolmentsFor(ExecutionSemester executionSemester, String createdBy) {
        internalCreateFirstTimeStudentEnrolmentsFor(getRoot(), getDegreeCurricularPlan().getCurricularPeriodFor(1, 1),
                executionSemester, createdBy);
    }

    /**
     * Note: This is enrolment without rules and should only be used for first
     * time enrolments
     */
    private void internalCreateFirstTimeStudentEnrolmentsFor(CurriculumGroup curriculumGroup, CurricularPeriod curricularPeriod,
            ExecutionSemester executionSemester, String createdBy) {

        if (curriculumGroup.getDegreeModule() != null) {
            for (final Context context : curriculumGroup.getDegreeModule().getContextsWithCurricularCourseByCurricularPeriod(
                    curricularPeriod, executionSemester)) {
                new Enrolment(this, curriculumGroup, (CurricularCourse) context.getChildDegreeModule(), executionSemester,
                        EnrollmentCondition.FINAL, createdBy);
            }
        }

        if (!curriculumGroup.getCurriculumModulesSet().isEmpty()) {
            for (final CurriculumModule curriculumModule : curriculumGroup.getCurriculumModulesSet()) {
                if (!curriculumModule.isLeaf()) {
                    internalCreateFirstTimeStudentEnrolmentsFor((CurriculumGroup) curriculumModule, curricularPeriod,
                            executionSemester, createdBy);
                }
            }
        }
    }

    final public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionSemester executionSemester) {
        return isBolonhaDegree() ? getRoot().getDegreeModulesToEvaluate(executionSemester) : Collections.EMPTY_SET;
    }

    final public RuleResult enrol(final ExecutionSemester executionSemester,
            final Set<IDegreeModuleToEvaluate> degreeModulesToEnrol, final List<CurriculumModule> curriculumModulesToRemove,
            final CurricularRuleLevel curricularRuleLevel) {
        check(this, StudentCurricularPlanPredicates.ENROL);

        final EnrolmentContext enrolmentContext =
                new EnrolmentContext(this, executionSemester, degreeModulesToEnrol, curriculumModulesToRemove,
                        curricularRuleLevel);

        return org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(enrolmentContext)
                .manage();
    }

    final public RuleResult enrol(final ExecutionSemester executionSemester, final CurricularRuleLevel curricularRuleLevel) {
        return enrol(executionSemester, Collections.EMPTY_SET, Collections.EMPTY_LIST, curricularRuleLevel);
    }

    @Atomic
    public void enrolInAffinityCycle(final CycleCourseGroup cycleCourseGroup, final ExecutionSemester executionSemester) {
        check(this, StudentCurricularPlanPredicates.ENROL_IN_AFFINITY_CYCLE);
        CurriculumGroupFactory.createGroup(getRoot(), cycleCourseGroup, executionSemester);
    }

    final public String getName() {
        return getDegreeCurricularPlan().getName();
    }

    final public String getPresentationName() {
        return getPresentationName(ExecutionYear.readCurrentExecutionYear());
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

    final public void createOptionalEnrolment(final CurriculumGroup curriculumGroup, final ExecutionSemester executionSemester,
            final OptionalCurricularCourse optionalCurricularCourse, final CurricularCourse curricularCourse,
            final EnrollmentCondition enrollmentCondition) {
        if (getRoot().isApproved(curricularCourse, executionSemester)) {
            throw new DomainException("error.already.aproved", new String[] { curricularCourse.getName() });
        }
        if (getRoot().isEnroledInExecutionPeriod(curricularCourse, executionSemester)) {
            throw new DomainException("error.already.enroled.in.executionPeriod", new String[] { curricularCourse.getName(),
                    executionSemester.getQualifiedName() });
        }

        new OptionalEnrolment(this, curriculumGroup, curricularCourse, executionSemester, enrollmentCondition, Authenticate
                .getUser().getUsername(), optionalCurricularCourse);
    }

    final public RuleResult createNoCourseGroupCurriculumGroupEnrolment(final NoCourseGroupEnrolmentBean bean) {
        return org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(
                EnrolmentContext.createForNoCourseGroupCurriculumGroupEnrolment(this, bean)).manage();
    }

    @Atomic
    public RuleResult removeCurriculumModulesFromNoCourseGroupCurriculumGroup(final List<CurriculumModule> curriculumModules,
            final ExecutionSemester executionSemester, final NoCourseGroupCurriculumGroupType groupType) {
        final EnrolmentContext context =
                new EnrolmentContext(this, executionSemester, Collections.EMPTY_SET, curriculumModules,
                        groupType.getCurricularRuleLevel());
        return org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(context).manage();
    }

    final public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup(final NoCourseGroupCurriculumGroupType groupType) {
        return getRoot().getNoCourseGroupCurriculumGroup(groupType);
    }

    final public NoCourseGroupCurriculumGroup createNoCourseGroupCurriculumGroup(final NoCourseGroupCurriculumGroupType groupType) {
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

    /**
     * Note that this method must not use the ExtraCurriculumGroup due to the
     * pre-Bolonha SCPs
     * @return extra curricular enrolments
     */
    final public Collection<Enrolment> getExtraCurricularEnrolments() {
        final Collection<Enrolment> result = new ArrayList<Enrolment>();

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.isExtraCurricular()) {
                result.add(enrolment);
            }
        }

        return result;
    }

    public List<Enrolment> getExtraCurricularApprovedEnrolmentsNotInDismissal() {
        final List<Enrolment> result = new ArrayList<Enrolment>();

        for (final CurriculumLine curriculumLine : getExtraCurriculumGroup().getApprovedCurriculumLines()) {

            if (curriculumLine.isEnrolment()) {

                final Enrolment enrolment = (Enrolment) curriculumLine;
                if (!enrolment.isSourceOfAnyCreditsInCurriculum()) {
                    result.add(enrolment);
                }

            } else if (curriculumLine.isDismissal()) {

                final Dismissal dismissal = (Dismissal) curriculumLine;
                if (dismissal.getCredits().isSubstitution()) {
                    for (final IEnrolment enrolment : dismissal.getSourceIEnrolments()) {
                        if (enrolment.isEnrolment()) {
                            result.add((Enrolment) enrolment);
                        }
                    }
                }
            }
        }

        return result;
    }

    public List<Enrolment> getStandaloneApprovedEnrolmentsNotInDismissal() {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        StandaloneCurriculumGroup standaloneGroup = getStandaloneCurriculumGroup();
        if (standaloneGroup == null) {
            return result;
        }
        for (final Enrolment enrolment : standaloneGroup.getEnrolments()) {
            if (!enrolment.isApproved() || enrolment.isSourceOfAnyCreditsInCurriculum()) {
                continue;
            }
            result.add(enrolment);
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

    public Collection<CurricularCourse> getAllCurricularCoursesToDismissal(final ExecutionSemester executionSemester) {
        final Collection<CurricularCourse> result = new HashSet<CurricularCourse>();
        if (isBolonhaDegree()) {
            for (final CycleType cycleType : getDegreeType().getSupportedCyclesToEnrol()) {
                final CourseGroup courseGroup = getCourseGroupWithCycleTypeToCollectCurricularCourses(cycleType);
                if (courseGroup != null) {
                    for (final CurricularCourse curricularCourse : courseGroup.getAllCurricularCourses(executionSemester)) {
                        if (!isApproved(curricularCourse)) {
                            result.add(curricularCourse);
                        }
                    }
                }
            }
        } else {
            for (final CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCoursesSet()) {
                if (curricularCourse.isActive(executionSemester) && !isApproved(curricularCourse)) {
                    result.add(curricularCourse);
                }
            }
        }
        return result;
    }

    public Collection<CurricularCourse> getAllCurricularCoursesToDismissal() {
        return getAllCurricularCoursesToDismissal(null);
    }

    private CourseGroup getCourseGroupWithCycleTypeToCollectCurricularCourses(final CycleType cycleType) {
        final CycleCurriculumGroup curriculumGroup = getCycle(cycleType);
        return curriculumGroup != null ? curriculumGroup.getDegreeModule() : getDegreeCurricularPlan().getCycleCourseGroup(
                cycleType);
    }

    final public Credits createNewCreditsDismissal(CourseGroup courseGroup, CurriculumGroup curriculumGroup,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
            ExecutionSemester executionSemester) {

        checkPermission(courseGroup, curriculumGroup, dismissals);

        if (courseGroup != null) {
            Collection<CurricularCourse> noEnrolCurricularCourse = new ArrayList<CurricularCourse>();
            if (dismissals != null) {
                for (SelectedCurricularCourse selectedCurricularCourse : dismissals) {
                    noEnrolCurricularCourse.add(selectedCurricularCourse.getCurricularCourse());
                }
            }
            return new Credits(this, courseGroup, enrolments, noEnrolCurricularCourse, givenCredits, executionSemester);
        } else if (curriculumGroup != null) {
            return new Credits(this, curriculumGroup, enrolments, givenCredits, executionSemester);
        } else {
            return new Credits(this, dismissals, enrolments, executionSemester);
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

    public Substitution getSubstitution(final IEnrolment iEnrolment) {
        for (final Dismissal dismissal : getDismissals()) {
            if (dismissal.getCredits().isSubstitution() && dismissal.getSourceIEnrolments().contains(iEnrolment)) {
                return (Substitution) dismissal.getCredits();
            }
        }

        return null;
    }

    final public Equivalence createNewEquivalenceDismissal(CourseGroup courseGroup, CurriculumGroup curriculumGroup,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
            Grade givenGrade, ExecutionSemester executionSemester) {

        checkPermission(courseGroup, curriculumGroup, dismissals);

        return CreditsManager.createEquivalence(this, courseGroup, curriculumGroup, dismissals, enrolments, givenCredits,
                givenGrade, executionSemester);
    }

    final public Substitution createNewSubstitutionDismissal(CourseGroup courseGroup, CurriculumGroup curriculumGroup,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
            ExecutionSemester executionSemester) {

        checkPermission(courseGroup, curriculumGroup, dismissals);

        return CreditsManager.createSubstitution(this, courseGroup, curriculumGroup, dismissals, enrolments, givenCredits,
                executionSemester);
    }

    public InternalSubstitution createNewInternalSubstitution(CourseGroup courseGroup, CurriculumGroup curriculumGroup,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
            ExecutionSemester executionSemester) {

        checkPermission(courseGroup, curriculumGroup, dismissals);

        return CreditsManager.createInternalSubstitution(this, courseGroup, curriculumGroup, dismissals, enrolments,
                givenCredits, executionSemester);

    }

    private void checkPermission(final CourseGroup courseGroup, final CurriculumGroup curriculumGroup,
            final Collection<SelectedCurricularCourse> dismissals) {

        final Person person = AccessControl.getPerson();

        final boolean hasUpdateRegistrationAfterConclusionProcessPermission =
                AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                        getDegree(), person.getUser());

        if (courseGroup != null) {
            final CurriculumGroup group = findCurriculumGroupFor(courseGroup);
            if (group != null && group.getParentCycleCurriculumGroup() != null
                    && group.getParentCycleCurriculumGroup().isConclusionProcessed()
                    && !hasUpdateRegistrationAfterConclusionProcessPermission) {
                throw new DomainException("error.StudentCurricularPlan.cannot.create.dismissals.curriculumGroup.concluded", group.getPresentationName().getContent());
            }
        } else if (curriculumGroup != null) {
            if (curriculumGroup.getParentCycleCurriculumGroup() != null
                    && curriculumGroup.getParentCycleCurriculumGroup().isConclusionProcessed()
                    && !hasUpdateRegistrationAfterConclusionProcessPermission) {
                throw new DomainException("error.StudentCurricularPlan.cannot.create.dismissals.curriculumGroup.concluded", curriculumGroup.getPresentationName().getContent());
            }
        } else {
            for (final SelectedCurricularCourse selected : dismissals) {
                if (selected.getCurriculumGroup().getParentCycleCurriculumGroup() != null
                        && selected.getCurriculumGroup().getParentCycleCurriculumGroup().isConclusionProcessed()
                        && !hasUpdateRegistrationAfterConclusionProcessPermission) {
                    CurricularCourse curricularCourse = selected.getCurricularCourse();
                    CurriculumGroup group = selected.getCurriculumGroup();
                    throw new DomainException("error.StudentCurricularPlan.cannot.create.dismissals.selected.curricularCourse",
                            curricularCourse.getNameI18N().getContent(), group.getPresentationName().getContent());
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
        return (curriculumGroup == null) ? Double.valueOf(0d) : curriculumGroup.getCreditsConcluded();
    }

    final public void setIsFirstTimeToNull() {
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            enrolment.setIsFirstTime(null);
        }
    }

    final public void resetIsFirstTimeEnrolmentForCurricularCourse(final CurricularCourse curricularCourse) {
        final SortedSet<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (curricularCourse == enrolment.getCurricularCourse()) {
                enrolments.add(enrolment);
            }
        }
        Boolean b = Boolean.TRUE;
        for (final Enrolment enrolment : enrolments) {
            if (!enrolment.isAnnulled()) {
                enrolment.setIsFirstTime(b);
                b = Boolean.FALSE;
            } else {
                enrolment.setIsFirstTime(Boolean.FALSE);
            }
        }
    }

    final public StudentCurricularPlanEquivalencePlan createStudentCurricularPlanEquivalencePlan() {
        return new StudentCurricularPlanEquivalencePlan(this);
    }

    final public boolean hasEnrolmentOrAprovalInCurriculumModule(final DegreeModule degreeModule) {
        final RootCurriculumGroup rootCurriculumGroup = getRoot();
        return rootCurriculumGroup != null && hasEnrolmentOrAprovalInCurriculumModule(rootCurriculumGroup, degreeModule);
    }

    private boolean hasEnrolmentOrAprovalInCurriculumModule(final CurriculumModule curriculumModule,
            final DegreeModule degreeModule) {
        if (curriculumModule.getDegreeModule() == degreeModule) {
            return true;
        }
        if (curriculumModule.isLeaf()) {
            return false;
        }
        final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
        for (final CurriculumModule child : curriculumGroup.getCurriculumModulesSet()) {
            if (hasEnrolmentOrAprovalInCurriculumModule(child, degreeModule)) {
                return true;
            }
        }
        return false;
    }

    public List<Enrolment> getEnrolmentsWithExecutionYearBeforeOrEqualTo(final ExecutionYear executionYear) {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod().getExecutionYear().isBeforeOrEquals(executionYear)) {
                result.add(enrolment);
            }
        }
        return result;
    }

    public boolean getHasAnyEquivalences() {
        return !this.getNotNeedToEnrollCurricularCoursesSet().isEmpty();
    }

    public boolean isLastStudentCurricularPlanFromRegistration() {
        return hasRegistration() && getRegistration().getLastStudentCurricularPlan() == this;
    }

    public void moveCurriculumLines(final MoveCurriculumLinesBean moveCurriculumLinesBean) {
        check(this, StudentCurricularPlanPredicates.MOVE_CURRICULUM_LINES);
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

                if (curriculumLine.hasExecutionPeriod()
                        && curriculumLine.getExecutionYear().isBefore(destination.getRegistration().getStartExecutionYear())) {
                    throw new DomainException(
                            "error.StudentCurricularPlan.cannot.move.curriculum.line.to.curriculum.group.invalid.period",
                            curriculumLine.getFullPath(), destination.getFullPath(), curriculumLine.getExecutionPeriod()
                                    .getQualifiedName(), destination.getRegistration().getStartExecutionYear().getQualifiedName());
                }

                if (!destination.isExtraCurriculum()) {
                    runRules = true;
                }
                curriculumLine.setCurriculumGroup(destination);
            }

            // if curriculum line is moved then change created by

            curriculumLine.setCreatedBy(responsible != null ? responsible.getUsername() : curriculumLine.getCreatedBy());
        }

        runRules &= isBolonhaDegree();

        if (runRules) {
            checkEnrolmentRules(
                    moveCurriculumLinesBean.getIDegreeModulesToEvaluate(ExecutionSemester.readActualExecutionSemester()),
                    ExecutionSemester.readActualExecutionSemester());
        }
    }

    public void moveCurriculumLinesWithoutRules(final Person responsiblePerson,
            final MoveCurriculumLinesBean moveCurriculumLinesBean) {
        check(this, StudentCurricularPlanPredicates.MOVE_CURRICULUM_LINES_WITHOUT_RULES);

        for (final CurriculumLineLocationBean curriculumLineLocationBean : moveCurriculumLinesBean.getCurriculumLineLocations()) {

            final CurriculumGroup destination = curriculumLineLocationBean.getCurriculumGroup();
            final CurriculumLine curriculumLine = curriculumLineLocationBean.getCurriculumLine();

            if (curriculumLine.getCurriculumGroup() != destination) {
                checkPermission(responsiblePerson, curriculumLineLocationBean);

                if (curriculumLine.hasExecutionPeriod()
                        && curriculumLine.getExecutionYear().isBefore(destination.getRegistration().getStartExecutionYear())) {
                    throw new DomainException(
                            "error.StudentCurricularPlan.cannot.move.curriculum.line.to.curriculum.group.invalid.period",
                            curriculumLine.getFullPath(), destination.getFullPath(), curriculumLine.getExecutionPeriod()
                                    .getQualifiedName(), destination.getRegistration().getStartExecutionYear().getQualifiedName());
                }

                curriculumLine.setCurriculumGroup(destination);
            }

            // if curriculum line is moved then change created by
            curriculumLine.setCreatedBy(responsiblePerson != null ? responsiblePerson.getUsername() : curriculumLine
                    .getCreatedBy());
        }
    }

    private void checkPermission(final Person responsiblePerson, final CurriculumLineLocationBean bean) {

        final boolean hasUpdateRegistrationAfterConclusionPermission =
                AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                        getDegree(), responsiblePerson.getUser());

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
            final ExecutionSemester executionSemester) {
        enrol(executionSemester, degreeModuleToEvaluate, Collections.EMPTY_LIST, CurricularRuleLevel.ENROLMENT_WITH_RULES);
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
        if (getEquivalencePlan() != null) {
            return false;
        }
        if (!getGratuityEventsSet().isEmpty()) {
            return false;
        }
        if (!getNotNeedToEnrollCurricularCoursesSet().isEmpty()) {
            return false;
        }
        if (!getGratuitySituationsSet().isEmpty()) {
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

    public boolean hasAnyActiveRegistrationWithFirstCycleAffinity() {
        final CycleCurriculumGroup firstCycle = getFirstCycle();
        if (firstCycle == null) {
            return false;
        }
        final Student student = getRegistration().getStudent();
        for (final CycleCourseGroup affinity : getCycleCourseGroup(firstCycle.getCycleType()).getDestinationAffinitiesSet()) {
            if (student.hasActiveRegistrationFor(affinity.getParentDegreeCurricularPlan())) {
                return true;
            }
        }
        return false;
    }

    public Collection<CycleType> getCycleTypes() {
        return getDegreeType().getCycleTypes();
    }

    public CurriculumLine getApprovedCurriculumLine(final CurricularCourse curricularCourse) {
        return getRoot().getApprovedCurriculumLine(curricularCourse);
    }

    @Override
    public Set<EnrolmentOutOfPeriodEvent> getEnrolmentOutOfPeriodEventsSet() {
        final Set<EnrolmentOutOfPeriodEvent> result = new HashSet<EnrolmentOutOfPeriodEvent>();

        for (final EnrolmentOutOfPeriodEvent each : super.getEnrolmentOutOfPeriodEventsSet()) {
            if (!each.isCancelled()) {
                result.add(each);
            }
        }

        return result;
    }

    public OptionalEnrolment convertEnrolmentToOptionalEnrolment(final Enrolment enrolment,
            final CurriculumGroup curriculumGroup, final OptionalCurricularCourse curricularCourse) {

        final Person person = AccessControl.getPerson();

        if (enrolment.getParentCycleCurriculumGroup() != null
                && enrolment.getParentCycleCurriculumGroup().isConclusionProcessed()
                && !AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                        getDegree(), person.getUser())) {
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
        enrolment.getProgramCertificateRequestsSet().clear();
        enrolment.getCourseLoadRequestsSet().clear();
        enrolment.getExamDateCertificateRequestsSet().clear();
        enrolment.delete();

        if (result.getStudentCurricularPlan() != this) {
            correctInvalidAttends(result.getStudentCurricularPlan());
        }

        return result;
    }

    private void correctInvalidAttends(final StudentCurricularPlan to) {
        for (final Attends attend : getRegistration().getAssociatedAttendsSet()) {
            if (!attend.hasExecutionCourseTo(this) && attend.canMove(this, to)) {
                getRegistration().changeShifts(attend, to.getRegistration());
                attend.setRegistration(to.getRegistration());
            }
        }
    }

    public Enrolment convertOptionalEnrolmentToEnrolment(final OptionalEnrolment enrolment, final CurriculumGroup curriculumGroup) {

        final Person person = AccessControl.getPerson();

        if (enrolment.getParentCycleCurriculumGroup() != null
                && enrolment.getParentCycleCurriculumGroup().isConclusionProcessed()
                && !AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                        getDegree(), person.getUser())) {
            throw new DomainException("error.StudentCurricularPlan.cannot.move.is.not.authorized");
        }

        if (!hasEnrolments(enrolment)) {
            // if we remove this test, then check if enrolment period is before
            // registration start
            throw new DomainException("error.StudentCurricularPlan.doesnot.have.enrolment", enrolment.getName().getContent());
        }

        final Enrolment result = Enrolment.createBasedOn(enrolment, curriculumGroup);
        enrolment.getProgramCertificateRequestsSet().clear();
        enrolment.getCourseLoadRequestsSet().clear();
        enrolment.getExamDateCertificateRequestsSet().clear();
        enrolment.delete();

        if (result.getStudentCurricularPlan() != this) {
            correctInvalidAttends(result.getStudentCurricularPlan());
        }

        return result;
    }

    public boolean isEmptyDegree() {
        return getDegreeCurricularPlan().isEmpty();
    }

    public boolean hasAnyGratuityEventFor(final ExecutionYear executionYear) {
        for (final GratuityEvent gratuityEvent : getGratuityEventsSet()) {
            if (gratuityEvent.isFor(executionYear)) {
                return true;
            }
        }

        return false;

    }

    public boolean hasAnyGratuitySituationFor(final ExecutionYear executionYear) {
        for (final GratuitySituation gratuitySituation : getGratuitySituationsSet()) {
            if (gratuitySituation.getGratuityValues().getExecutionDegree().getExecutionYear() == executionYear) {
                return true;
            }
        }

        return false;
    }

    @Atomic
    public void setEvaluationsForCurriculumValidation(List<List<MarkSheetEnrolmentEvaluationBean>> enrolmentEvaluationsBeanList) {
        for (List<MarkSheetEnrolmentEvaluationBean> evaluationsList : enrolmentEvaluationsBeanList) {
            setIndividualEvaluationsForCurriculumValidation(evaluationsList);
        }
    }

    private List<MarkSheetEnrolmentEvaluationBean> setIndividualEvaluationsForCurriculumValidation(
            List<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationsBeanList) {

        if (enrolmentEvaluationsBeanList.size() > 0) {
            Enrolment enrolmentForWeightSet = enrolmentEvaluationsBeanList.iterator().next().getEnrolment();
            enrolmentForWeightSet.setWeigth(enrolmentEvaluationsBeanList.iterator().next().getWeight());
        }

        for (MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : enrolmentEvaluationsBeanList) {
            Enrolment enrolment = enrolmentEvaluationBean.getEnrolment();

            EnrolmentEvaluation enrolmentEvaluation = enrolmentEvaluationBean.getLatestEnrolmentEvaluation();

            if (StringUtils.isEmpty(enrolmentEvaluationBean.getGradeValue())) {
                enrolmentEvaluationBean.setEnrolmentEvaluationSet(Boolean.FALSE);
                continue;
            }

            enrolmentEvaluation =
                    enrolment.addNewEnrolmentEvaluation(EnrolmentEvaluationState.TEMPORARY_OBJ,
                            enrolmentEvaluationBean.getEvaluationSeason(), AccessControl.getPerson(),
                            enrolmentEvaluationBean.getGradeValue(), new java.util.Date(),
                            enrolmentEvaluationBean.getEvaluationDate(), enrolmentEvaluationBean.getExecutionSemester(),
                            enrolmentEvaluationBean.getBookReference(), enrolmentEvaluationBean.getPage(),
                            enrolmentEvaluationBean.getGradeScale());
            enrolmentEvaluation.confirmSubmission(AccessControl.getPerson(),
                    BundleUtil.getString(Bundle.ACADEMIC, "message.curriculum.validation.observation"));

            enrolmentEvaluationBean.setEnrolmentEvaluationSet(Boolean.TRUE);
        }

        return enrolmentEvaluationsBeanList;
    }

    @Atomic
    public void editEndStageDate(LocalDate date) {
        this.setEndStageDate(date);
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
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
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
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
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
                Authenticate.getUser());
    }

    public boolean isAllowedToManageEquivalencies() {
        return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_EQUIVALENCES, getDegree(),
                Authenticate.getUser());
    }

    public boolean isAllowedToManageAccountingEvents() {
        return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_ACCOUNTING_EVENTS, getDegree(),
                Authenticate.getUser());
    }

    public Stream<Enrolment> getEnrolmentStream() {
        return getRoot().getCurriculumLineStream()
            .filter(cl -> cl.isEnrolment())
            .map(cl -> (Enrolment) cl);
    }

}
