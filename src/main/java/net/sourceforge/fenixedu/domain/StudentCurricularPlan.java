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
package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedBranchChangeException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.AverageType;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.studentCurriculum.BranchCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.CreditsManager;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroupFactory;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByApproval;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByExecutionSemester;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByExecutionYear;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.CurriculumModulePredicateByType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.Equivalence;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExtraCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.InternalSubstitution;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.domain.studentCurriculum.PropaedeuticsCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.RootCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.StandaloneCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Substitution;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.StudentCurricularPlanPredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.predicates.ResultCollection;

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

        if (degreeCurricularPlan.isBoxStructure()) {
            CurriculumGroupFactory.createRoot(result, degreeCurricularPlan.getRoot(), null);
        }

        return result;
    }

    static public StudentCurricularPlan createWithEmptyStructure(final Registration registration,
            final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType, final YearMonthDay startDate) {

        final StudentCurricularPlan result = new StudentCurricularPlan(registration, degreeCurricularPlan, startDate);

        if (degreeCurricularPlan.isBoxStructure()) {
            CurriculumGroupFactory.createRoot(result, degreeCurricularPlan.getRoot(), cycleType);
        }

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
        if (getDegreeCurricularPlan().isBoxStructure()) {
            CurriculumGroupFactory.createRoot(this, getDegreeCurricularPlan().getRoot(), executionSemester, cycleType);
        }
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

        setDegreeCurricularPlan(null);
        setBranch(null);
        setEmployee(null);
        setMasterDegreeThesis(null);

        for (; !getEnrolmentsSet().isEmpty(); getEnrolmentsSet().iterator().next().delete()) {
            ;
        }

        if (getRoot() != null) {
            getRoot().delete();
        }

        for (Iterator<NotNeedToEnrollInCurricularCourse> iter = getNotNeedToEnrollCurricularCoursesSet().iterator(); iter
                .hasNext();) {
            NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = iter.next();
            iter.remove();
            notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(null);
            notNeedToEnrollInCurricularCourse.delete();
        }

        for (; !getCreditsInAnySecundaryAreas().isEmpty(); getCreditsInAnySecundaryAreas().iterator().next().delete()) {
            ;
        }

        for (Iterator<CreditsInScientificArea> iter = getCreditsInScientificAreasSet().iterator(); iter.hasNext();) {
            CreditsInScientificArea creditsInScientificArea = iter.next();
            iter.remove();
            creditsInScientificArea.setStudentCurricularPlan(null);
            creditsInScientificArea.delete();
        }

        for (; hasAnyCredits(); getCredits().iterator().next().delete()) {
            ;
        }
        for (; hasAnyTutorships(); getTutorships().iterator().next().delete()) {
            ;
        }

        setStudent(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    private void checkRulesToDelete() {
        if (hasAnyGratuityEvents()) {
            throw new DomainException("error.StudentCurricularPlan.cannot.delete.because.already.has.gratuity.events");
        }

        if (hasAnyGratuitySituations()) {
            throw new DomainException("error.StudentCurricularPlan.cannot.delete.because.already.has.gratuity.situations");
        }
    }

    final public String print() {
        if (hasRoot()) {
            final StringBuilder result = new StringBuilder();
            result.append("[SCP ").append(this.getExternalId()).append("] ").append(this.getName()).append("\n");
            result.append(getRoot().print(""));
            return result.toString();
        } else {
            return "";
        }
    }

    final public boolean isFirstCycle() {
        return getDegreeType().isFirstCycle();
    }

    final public boolean isSecondCycle() {
        return getDegreeType().isSecondCycle();
    }

    final public boolean hasConcludedCycle(CycleType cycleType) {
        return hasRoot() ? getRoot().hasConcludedCycle(cycleType) : null;
    }

    final public boolean hasConcludedCycle(CycleType cycleType, final ExecutionYear executionYear) {
        return hasRoot() ? getRoot().hasConcludedCycle(cycleType, executionYear) : null;
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

    public Integer getFinalAverage(final CycleType cycleType) {
        if (getDegreeType().getCycleTypes().isEmpty()) {
            throw new DomainException("StudentCurricularPlan.has.no.cycle.type");
        }

        if (!getDegreeType().hasCycleTypes(cycleType)) {
            throw new DomainException("StudentCurricularPlan.doesnt.have.such.cycle.type");
        }

        return getCycle(cycleType).getFinalAverage();
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

    final public boolean isConclusionProcessed() {
        if (!isBolonhaDegree()) {
            return getRegistration().hasConclusionProcess();
        }

        if (isEmptyDegree()) {
            return getRegistration().getLastActiveState().getStateType().equals(RegistrationStateType.CONCLUDED);
        }

        for (final CycleCurriculumGroup cycleCurriculumGroup : getInternalCycleCurriculumGrops()) {
            if (!cycleCurriculumGroup.isConclusionProcessed()) {
                return false;
            }
        }
        return true;
    }

    final public boolean isConclusionProcessed(final CycleType cycleType) {
        final CycleCurriculumGroup cycleCurriculumGroup = getCycle(cycleType);
        return cycleCurriculumGroup != null && cycleCurriculumGroup.isConclusionProcessed();
    }

    final public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear) {
        final RootCurriculumGroup rootCurriculumGroup = getRoot();
        if (rootCurriculumGroup == null) {
            return Curriculum.createEmpty(executionYear);
        } else {
            return rootCurriculumGroup.getCurriculum(when, executionYear);
        }
    }

    final public AverageType getAverageType() {
        return getDegreeCurricularPlan().getAverageType();
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

    /**
     * Temporary method, after all degrees migration this is no longer necessary
     * 
     */
    final public boolean isBoxStructure() {
        return hasRoot();
    }

    final public boolean isEnrolable() {
        return isBoxStructure() && getRegistration().isActive() && getRegistration().getLastStudentCurricularPlan().equals(this);
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
            if (registration.hasDegree()) {
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
        return isBoxStructure() ? getRoot().getAllCurriculumLines() : new HashSet<CurriculumLine>(super.getEnrolmentsSet());
    }

    public Set<CurriculumGroup> getAllCurriculumGroups() {
        return isBoxStructure() ? getRoot().getAllCurriculumGroups() : Collections.EMPTY_SET;
    }

    public Set<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        return isBoxStructure() ? getRoot().getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() : Collections.EMPTY_SET;
    }

    @Override
    final public Set<Enrolment> getEnrolmentsSet() {
        return hasRoot() ? getRoot().getEnrolmentsSet() : super.getEnrolmentsSet();
    }

    final public boolean hasAnyEnrolments() {
        return hasRoot() ? getRoot().hasAnyEnrolments() : !getEnrolmentsSet().isEmpty();
    }

    final public boolean hasAnyCurriculumLines() {
        return hasRoot() ? hasAnyCurriculumModules(new CurriculumModulePredicateByType(CurriculumLine.class)) : hasAnyEnrolments();
    }

    final public boolean hasAnyCurriculumLines(final ExecutionYear executionYear) {
        if (hasRoot()) {
            final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
            andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
            andPredicate.add(new CurriculumModulePredicateByExecutionYear(executionYear));

            return hasAnyCurriculumModules(andPredicate);
        }

        return hasEnrolments(executionYear);
    }

    final public boolean hasAnyCurriculumLines(final ExecutionSemester executionSemester) {
        if (hasRoot()) {
            final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
            andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
            andPredicate.add(new CurriculumModulePredicateByExecutionSemester(executionSemester));

            return hasAnyCurriculumModules(andPredicate);
        }

        return hasEnrolments(executionSemester);
    }

    final public boolean hasEnrolments(final Enrolment enrolment) {
        return hasRoot() ? getRoot().hasCurriculumModule(enrolment) : getEnrolmentsSet().contains(enrolment);
    }

    final public boolean hasEnrolments(final ExecutionYear executionYear) {
        if (hasRoot()) {
            return getRoot().hasEnrolment(executionYear);
        } else {
            for (final Enrolment enrolment : super.getEnrolmentsSet()) {
                final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
                if (executionSemester.getExecutionYear() == executionYear) {
                    return true;
                }
            }
        }
        return false;
    }

    final public boolean hasEnrolments(final ExecutionSemester executionSemester) {
        if (hasRoot()) {
            return getRoot().hasEnrolment(executionSemester);
        } else {
            for (final Enrolment enrolment : super.getEnrolmentsSet()) {
                if (enrolment.getExecutionPeriod() == executionSemester) {
                    return true;
                }
            }
        }
        return false;
    }

    final public int getEnrolmentsCount() {
        return hasRoot() ? getEnrolmentsSet().size() : super.getEnrolmentsSet().size();
    }

    final public int countCurrentEnrolments() {
        int result = 0;
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            final ExecutionYear executionYear = enrolment.getExecutionPeriod().getExecutionYear();
            if (executionYear.isCurrent() && enrolment.isEnroled()) {
                result++;
            }
        }
        return result;
    }

    final public int getCountCurrentEnrolments() {
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
        if (hasRoot()) {
            getRoot().getCurriculumModules(collection);
            return collection.getResult();
        } else {
            throw new DomainException("not.supported");
        }
    }

    public boolean hasAnyCurriculumModules(final Predicate<CurriculumModule> predicate) {
        if (hasRoot()) {
            return getRoot().hasAnyCurriculumModules(predicate);
        } else {
            throw new DomainException("not.supported");
        }
    }

    final public Collection<CurriculumLine> getApprovedCurriculumLines() {
        if (hasRoot()) {
            final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();
            getRoot().addApprovedCurriculumLines(result);
            return result;
        } else {
            return new HashSet<CurriculumLine>(getAprovedEnrolments());
        }
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
        if (hasRoot()) {
            return getRoot().getApprovedCurriculumLinesLastExecutionYear();
        } else {
            final SortedSet<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);

            for (final CurriculumLine curriculumLine : getAprovedEnrolments()) {
                if (curriculumLine.hasExecutionPeriod()) {
                    executionYears.add(curriculumLine.getExecutionPeriod().getExecutionYear());
                }
            }

            return executionYears.isEmpty() ? ExecutionYear.readCurrentExecutionYear() : executionYears.last();
        }
    }

    final public CurriculumLine getLastApprovement() {
        final SortedSet<CurriculumLine> curriculumLines =
                new TreeSet<CurriculumLine>(CurriculumLine.COMPARATOR_BY_APPROVEMENT_DATE_AND_ID);

        if (hasRoot()) {
            for (final CurriculumModule module : getRoot().getCurriculumModules()) {
                if (!module.isNoCourseGroupCurriculumGroup()) {
                    module.addApprovedCurriculumLines(curriculumLines);
                }
            }
        } else {
            curriculumLines.addAll(getAprovedEnrolments());
        }

        return curriculumLines.isEmpty() ? null : curriculumLines.last();
    }

    final public YearMonthDay getLastApprovementDate() {
        final CurriculumLine lastApprovement = getLastApprovement();
        return lastApprovement == null ? null : lastApprovement.getApprovementDate();
    }

    final public ExecutionYear getLastApprovementExecutionYear() {
        return hasAnyApprovedCurriculumLines() ? getApprovedCurriculumLinesLastExecutionYear() : null;
    }

    final public boolean hasAnyApprovedCurriculumLines() {
        return hasRoot() ? getRoot().hasAnyApprovedCurriculumLines() : hasAnyApprovedEnrolment();
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
        return isBoxStructure() ? getRoot().findEnrolmentFor(curricularCourse, executionSemester) : getEnrolmentByCurricularCourseAndExecutionPeriod(
                curricularCourse, executionSemester);
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

        for (final StudentCurricularPlan studentCurricularPlan : getRegistration().getStudentCurricularPlans()) {
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

    final public boolean isCurricularCourseApprovedInCurrentOrPreviousPeriod(final CurricularCourse course,
            final ExecutionSemester executionSemester) {
        final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();
        final List<CurricularCourse> approvedCurricularCourses = new ArrayList<CurricularCourse>();

        for (Iterator iter = studentApprovedEnrollments.iterator(); iter.hasNext();) {
            Enrolment enrolment = (Enrolment) iter.next();
            if (enrolment.getExecutionPeriod().compareTo(executionSemester) <= 0) {
                approvedCurricularCourses.add(enrolment.getCurricularCourse());
            }
        }

        return isApproved(course, approvedCurricularCourses);
    }

    final protected boolean isApproved(CurricularCourse curricularCourse, List<CurricularCourse> approvedCourses) {
        return hasEquivalenceIn(curricularCourse, approvedCourses) || hasEquivalenceInNotNeedToEnroll(curricularCourse);
    }

    final public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return isBoxStructure() ? getRoot().isApproved(curricularCourse, executionSemester) : isCurricularCourseApprovedInCurrentOrPreviousPeriod(
                curricularCourse, executionSemester);
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
        return isBoxStructure() ? getRoot().isApproved(curricularCourse) : isCurricularCourseApproved(curricularCourse);
    }

    public int getNumberOfApprovedCurricularCourses() {
        int counter = 0;

        int size = getDegreeCurricularPlan().getCurricularCourses().size();
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

        for (CurricularCourseEquivalence equiv : curricularCourse.getCurricularCourseEquivalences()) {
            if (allNotNeedToEnroll(equiv.getOldCurricularCourses())) {
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

        for (CurricularCourseEquivalence equiv : curricularCourse.getCurricularCourseEquivalences()) {
            if (allCurricularCoursesInTheList(equiv.getOldCurricularCourses(), otherCourses)) {
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
        return isBoxStructure() ? isEnroledInExecutionPeriod(curricularCourse, ExecutionSemester.readActualExecutionSemester()) : isCurricularCourseEnrolled(curricularCourse);
    }

    final public boolean isCurricularCourseEnrolledInExecutionPeriod(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester) {

        List<Enrolment> studentEnrolledEnrollments = getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionSemester);
        for (Enrolment enrolment : studentEnrolledEnrollments) {
            if (enrolment.getCurricularCourse().equals(curricularCourse)) {
                return true;
            }
        }
        return false;
    }

    final public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        return isBoxStructure() ? getRoot().isEnroledInExecutionPeriod(curricularCourse, executionSemester) : isCurricularCourseEnrolledInExecutionPeriod(
                curricularCourse, executionSemester);
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
        if (hasRoot()) {
            return getRoot().hasEnrolmentInCurricularCourseBefore(curricularCourse, executionSemester);
        }
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (!enrolment.isAnnulled() && enrolment.getExecutionPeriod().isBefore(executionSemester)
                    && enrolment.getCurricularCourse() == curricularCourse) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    public double getEnrolmentsEctsCredits(final ExecutionYear executionYear) {
        double result = 0.0;

        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            result += getEnrolmentsEctsCredits(executionSemester);
        }

        return result;
    }

    final public double getEnrolmentsEctsCredits(final ExecutionSemester executionSemester) {
        double result = 0.0;

        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.isValid(executionSemester)) {
                result += enrolment.getEctsCredits();
            }
        }

        return result;
    }

    final public double getDismissalsEctsCredits() {
        double result = 0.0;

        for (final Dismissal dismissal : getDismissals()) {
            result += dismissal.getEctsCredits();
        }

        return result;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    private Integer calculateStudentAcumulatedEnrollments(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        if (!this.isBolonhaDegree()) {
            int result = 0;
            if (hasRoot()) {
                result += getRoot().calculateStudentAcumulatedEnrollments(curricularCourse, executionSemester);
            } else {
                for (final Enrolment enrolment : super.getEnrolmentsSet()) {
                    if (!enrolment.isAnnulled()
                            && enrolment.getExecutionPeriod().isBefore(executionSemester)
                            && enrolment.getCurricularCourse().getCurricularCourseUniqueKeyForEnrollment()
                                    .equalsIgnoreCase(curricularCourse.getCurricularCourseUniqueKeyForEnrollment())) {
                        result++;
                    }
                }
            }
            return Integer.valueOf(result);
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
            for (final CurricularCourse course : curricularCourse.getCompetenceCourse().getAssociatedCurricularCourses()) {
                if (!course.isBolonhaDegree()) {
                    sameCompetenceCurricularCourses.add(course);
                }
            }
        }

        for (CurricularCourse course : sameCompetenceCurricularCourses) {
            for (CurricularCourseEquivalence curricularCourseEquivalence : course.getOldCurricularCourseEquivalences()) {
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
        return (List<CurricularCourse>) CollectionUtils.collect(getNotNeedToEnrollCurricularCourses(), new Transformer() {
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
        for (final GratuitySituation gratuitySituation : getGratuitySituations()) {
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

    final public <T extends GratuityEvent> T getGratuityEvent(final ExecutionYear executionYear,
            final Class<? extends GratuityEvent> type) {
        for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
            if (!gratuityEvent.isCancelled() && gratuityEvent.getExecutionYear() == executionYear
                    && gratuityEvent.getClass().equals(type)) {
                return (T) gratuityEvent;
            }
        }

        return null;
    }

    final public boolean hasGratuityEvent(final ExecutionYear executionYear, final Class<? extends GratuityEvent> type) {
        return getGratuityEvent(executionYear, type) != null;
    }

    final public Set<GratuityEvent> getNotPayedGratuityEvents() {
        final Set<GratuityEvent> result = new HashSet<GratuityEvent>();

        for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
            if (gratuityEvent.isInDebt()) {
                result.add(gratuityEvent);
            }
        }

        return result;
    }

    final public boolean hasAnyNotPayedGratuityEvents() {
        for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
            if (gratuityEvent.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyNotPayedGratuityEventsUntil(final ExecutionYear executionYear) {
        for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
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

        for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
            if (gratuityEvent.getExecutionYear().isBefore(limitExecutionYear) && gratuityEvent.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    final public MasterDegreeProofVersion readActiveMasterDegreeProofVersion() {
        MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
        if (masterDegreeThesis != null) {
            for (MasterDegreeProofVersion masterDegreeProofVersion : masterDegreeThesis.getMasterDegreeProofVersions()) {
                if (masterDegreeProofVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    return masterDegreeProofVersion;
                }
            }
        }
        return null;
    }

    final public List<MasterDegreeProofVersion> readNotActiveMasterDegreeProofVersions() {
        MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
        List<MasterDegreeProofVersion> masterDegreeProofVersions = new ArrayList<MasterDegreeProofVersion>();
        if (masterDegreeThesis != null) {
            for (MasterDegreeProofVersion masterDegreeProofVersion : masterDegreeThesis.getMasterDegreeProofVersions()) {
                if (!masterDegreeProofVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    masterDegreeProofVersions.add(masterDegreeProofVersion);
                }
            }
        }
        Collections.sort(masterDegreeProofVersions, new ReverseComparator(MasterDegreeProofVersion.LAST_MODIFICATION_COMPARATOR));
        return masterDegreeProofVersions;
    }

    final public MasterDegreeThesisDataVersion readActiveMasterDegreeThesisDataVersion() {
        MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
        if (masterDegreeThesis != null) {
            for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesis
                    .getMasterDegreeThesisDataVersions()) {
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    return masterDegreeThesisDataVersion;
                }
            }
        }
        return null;
    }

    final public List<MasterDegreeThesisDataVersion> readNotActiveMasterDegreeThesisDataVersions() {
        MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = new ArrayList<MasterDegreeThesisDataVersion>();
        if (masterDegreeThesis != null) {
            for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesis
                    .getMasterDegreeThesisDataVersions()) {
                if (!masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    masterDegreeThesisDataVersions.add(masterDegreeThesisDataVersion);
                }
            }
        }
        Collections.sort(masterDegreeThesisDataVersions, new ReverseComparator(
                MasterDegreeThesisDataVersion.LAST_MODIFICATION_COMPARATOR));
        return masterDegreeThesisDataVersions;
    }

    final public boolean approvedInAllCurricularCoursesUntilInclusiveCurricularYear(final Integer curricularYearInteger) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            final Collection<CurricularCourseScope> activeCurricularCourseScopes = curricularCourse.getActiveScopes();
            for (final CurricularCourseScope curricularCourseScope : activeCurricularCourseScopes) {
                final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
                final CurricularYear curricularYear = curricularSemester.getCurricularYear();
                if (curricularYearInteger == null || curricularYear.getYear().intValue() <= curricularYearInteger.intValue()) {
                    if (!isCurricularCourseApproved(curricularCourse)) {
                        return false;
                    }
                }
            }
        }
        return true;
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
        return hasRoot() ? getRoot().isEnroledInSpecialSeason(executionSemester) : false;
    }

    public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear) {
        return hasRoot() ? getRoot().isEnroledInSpecialSeason(executionYear) : false;
    }

    final public Collection<Enrolment> getSpecialSeasonToEnrol(ExecutionYear executionYear) {
        Map<CurricularCourse, Enrolment> result = new HashMap<CurricularCourse, Enrolment>();

        for (Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON
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
            if (enrolment.getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON
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
     * 
     */
    public boolean hasSpecialSeasonFor(final ExecutionSemester executionSemester) {
        final ExecutionSemester previous = executionSemester.getPreviousExecutionPeriod();
        return previous != null && isEnroledInSpecialSeason(previous.getExecutionYear());
    }

    // Improvements
    final public void createEnrolmentEvaluationForImprovement(final Collection<Enrolment> toCreate, final Person person,
            final ExecutionSemester executionSemester) {

        final Collection<EnrolmentEvaluation> created = new HashSet<EnrolmentEvaluation>();

        for (final Enrolment enrolment : toCreate) {
            created.add(enrolment.createEnrolmentEvaluationForImprovement(person, executionSemester));
        }

        if (isToPayImprovementOfApprovedEnrolments()) {
            new ImprovementOfApprovedEnrolmentEvent(this.getDegree().getAdministrativeOffice(), getPerson(), created);
        }
    }

    private boolean isToPayImprovementOfApprovedEnrolments() {
        final RegistrationAgreement registrationAgreement = getRegistration().getRegistrationAgreement();
        return registrationAgreement != RegistrationAgreement.MA && registrationAgreement != RegistrationAgreement.AFA;
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

        if (curriculumGroup.hasDegreeModule()) {
            for (final Context context : curriculumGroup.getDegreeModule().getContextsWithCurricularCourseByCurricularPeriod(
                    curricularPeriod, executionSemester)) {
                new Enrolment(this, curriculumGroup, (CurricularCourse) context.getChildDegreeModule(), executionSemester,
                        EnrollmentCondition.FINAL, createdBy);
            }
        }

        if (curriculumGroup.hasAnyCurriculumModules()) {
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

        return net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(enrolmentContext)
                .manage();
    }

    final public RuleResult enrol(final ExecutionSemester executionSemester, final CurricularRuleLevel curricularRuleLevel) {
        return enrol(executionSemester, Collections.EMPTY_SET, Collections.EMPTY_LIST, curricularRuleLevel);
    }

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
        return net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(
                EnrolmentContext.createForNoCourseGroupCurriculumGroupEnrolment(this, bean)).manage();
    }

    @Atomic
    public RuleResult removeCurriculumModulesFromNoCourseGroupCurriculumGroup(final List<CurriculumModule> curriculumModules,
            final ExecutionSemester executionSemester, final NoCourseGroupCurriculumGroupType groupType) {
        final EnrolmentContext context =
                new EnrolmentContext(this, executionSemester, Collections.EMPTY_SET, curriculumModules,
                        groupType.getCurricularRuleLevel());
        return net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(context).manage();
    }

    final public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup(final NoCourseGroupCurriculumGroupType groupType) {
        return (getRoot() != null) ? getRoot().getNoCourseGroupCurriculumGroup(groupType) : null;
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
        if (isBoxStructure()) {
            getRoot().collectDismissals(result);
        }
        return result;
    }

    public Dismissal getDismissal(final CurricularCourse curricularCourse) {
        return isBoxStructure() ? getRoot().getDismissal(curricularCourse) : null;
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
                AcademicAuthorizationGroup.getProgramsForOperation(person,
                        AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION).contains(this.getDegree());

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
        final RootCurriculumGroup rootCurriculumGroup = getRoot();
        if (rootCurriculumGroup != null) {
            rootCurriculumGroup.getAllDegreeModules(degreeModules);
        }
        return degreeModules;
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return isBoxStructure() ? getRoot().hasDegreeModule(degreeModule) : false;
    }

    public boolean hasCurriculumModule(final CurriculumModule curriculumModule) {
        return isBoxStructure() ? getRoot().hasCurriculumModule(curriculumModule) : false;
    }

    public CurriculumGroup findCurriculumGroupFor(final CourseGroup courseGroup) {
        return isBoxStructure() ? getRoot().findCurriculumGroupFor(courseGroup) : null;
    }

    public boolean isConcluded(final DegreeModule degreeModule, final ExecutionYear executionYear) {
        return isBoxStructure() ? getRoot().hasConcluded(degreeModule, executionYear) : false;
    }

    final public MasterDegreeCandidate getMasterDegreeCandidate() {
        if (getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
            if (this.getEnrolmentsSet().size() > 0) {
                ExecutionDegree firstExecutionDegree =
                        this.getDegreeCurricularPlan()
                                .getExecutionDegreeByYear(this.getFirstExecutionPeriod().getExecutionYear());
                for (final MasterDegreeCandidate candidate : this.getPerson().getMasterDegreeCandidates()) {
                    if (candidate.getExecutionDegree() == firstExecutionDegree) {
                        return candidate;
                    }
                }
            } else if (this.getPerson().getMasterDegreeCandidatesSet().size() == 1) {
                return this.getPerson().getMasterDegreeCandidates().iterator().next();
            }
        }
        return null;
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

    public Tutorship getLastTutorship() {
        if (hasAnyTutorships()) {
            return Collections.max(getTutorships(), Tutorship.TUTORSHIP_START_DATE_COMPARATOR);
        }
        return null;
    }

    public Tutorship getActiveTutorship() {
        Collection<Tutorship> tutorships = getTutorships();
        if (!tutorships.isEmpty() && getLastTutorship().isActive()) {
            return getLastTutorship();
        }
        return null;
    }

    @Override
    public void addTutorships(Tutorship tutorships) throws DomainException {
        for (Tutorship tutorship : getTutorships()) {
            if (tutorship.getTeacher().equals(tutorships.getTeacher()) && tutorship.getEndDate().equals(tutorships.getEndDate())) {
                throw new DomainException("error.tutorships.duplicatedTutorship");
            }

            if (tutorship.isActive()) {
                throw new DomainException("error.tutorships.onlyOneActiveTutorship");
            }
        }

        super.addTutorships(tutorships);
    }

    public boolean getHasAnyEquivalences() {
        return !this.getNotNeedToEnrollCurricularCourses().isEmpty();
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

            curriculumLine.setCreatedBy(responsible != null ? responsible.getIstUsername() : curriculumLine.getCreatedBy());
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
            curriculumLine.setCreatedBy(responsiblePerson != null ? responsiblePerson.getIstUsername() : curriculumLine
                    .getCreatedBy());
        }
    }

    private void checkPermission(final Person responsiblePerson, final CurriculumLineLocationBean bean) {

        final boolean hasUpdateRegistrationAfterConclusionPermission =
                AcademicAuthorizationGroup.getProgramsForOperation(responsiblePerson,
                        AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION).contains(this.getDegree());

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
        return isBoxStructure() ? getRoot().getCycleCurriculumGroup(cycleType) : null;
    }

    public boolean hasCycleCurriculumGroup(final CycleType cycleType) {
        return getCycle(cycleType) != null;
    }

    public CycleCourseGroup getCycleCourseGroup(final CycleType cycleType) {
        return isBoxStructure() ? getDegreeCurricularPlan().getCycleCourseGroup(cycleType) : null;
    }

    public Collection<CurriculumGroup> getCurricularCoursePossibleGroups(final CurricularCourse curricularCourse) {
        return getRoot().getCurricularCoursePossibleGroups(curricularCourse);
    }

    public Collection<CurriculumGroup> getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(
            final CurricularCourse curricularCourse) {
        return getRoot().getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(curricularCourse);
    }

    public CycleCurriculumGroup getFirstCycle() {
        return isBoxStructure() ? getRoot().getCycleCurriculumGroup(CycleType.FIRST_CYCLE) : null;
    }

    public CycleCurriculumGroup getSecondCycle() {
        return isBoxStructure() ? getRoot().getCycleCurriculumGroup(CycleType.SECOND_CYCLE) : null;
    }

    public CycleCurriculumGroup getThirdCycle() {
        return isBoxStructure() ? getRoot().getCycleCurriculumGroup(CycleType.THIRD_CYCLE) : null;
    }

    public CycleCurriculumGroup getFirstOrderedCycleCurriculumGroup() {
        return isBoxStructure() ? getRoot().getFirstOrderedCycleCurriculumGroup() : null;
    }

    public CycleCurriculumGroup getLastOrderedCycleCurriculumGroup() {
        return isBoxStructure() ? getRoot().getLastOrderedCycleCurriculumGroup() : null;
    }

    public CycleCurriculumGroup getLastConcludedCycleCurriculumGroup() {
        return isBoxStructure() ? getRoot().getLastConcludedCycleCurriculumGroup() : null;
    }

    public Collection<CycleCurriculumGroup> getCycleCurriculumGroups() {
        return hasRoot() ? getRoot().getCycleCurriculumGroups() : Collections.<CycleCurriculumGroup> emptySet();
    }

    public List<CycleCurriculumGroup> getInternalCycleCurriculumGrops() {
        return hasRoot() ? getRoot().getInternalCycleCurriculumGroups() : Collections.<CycleCurriculumGroup> emptyList();
    }

    public Collection<ExternalCurriculumGroup> getExternalCurriculumGroups() {
        return hasRoot() ? getRoot().getExternalCycleCurriculumGroups() : Collections.EMPTY_LIST;
    }

    public boolean hasExternalCycleCurriculumGroups() {
        return hasRoot() ? getRoot().hasExternalCycles() : false;
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
        if (hasAnyTutorships()) {
            return false;
        }
        if (hasRoot() && !getRoot().getAllCurriculumLines().isEmpty()) {
            return false;
        }
        if (hasEquivalencePlan()) {
            return false;
        }
        if (hasAnyGratuityEvents()) {
            return false;
        }
        if (hasAnyNotNeedToEnrollCurricularCourses()) {
            return false;
        }
        if (hasAnyCreditsInAnySecundaryAreas()) {
            return false;
        }
        if (hasAnyGratuitySituations()) {
            return false;
        }
        if (hasMasterDegreeThesis()) {
            return false;
        }
        if (hasAnyCreditsInScientificAreas()) {
            return false;
        }
        if (hasAnyCredits()) {
            return false;
        }
        return true;
    }

    public Collection<NoCourseGroupCurriculumGroup> getNoCourseGroupCurriculumGroups() {
        return isBoxStructure() ? getRoot().getNoCourseGroupCurriculumGroups() : new HashSet<NoCourseGroupCurriculumGroup>();
    }

    public boolean hasAnyActiveRegistrationWithFirstCycleAffinity() {
        final CycleCurriculumGroup firstCycle = getFirstCycle();
        if (firstCycle == null) {
            return false;
        }
        final Student student = getRegistration().getStudent();
        for (final CycleCourseGroup affinity : getCycleCourseGroup(firstCycle.getCycleType()).getDestinationAffinities()) {
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
        return isBoxStructure() ? getRoot().getApprovedCurriculumLine(curricularCourse) : null;
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
                && !AcademicAuthorizationGroup.getProgramsForOperation(person,
                        AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION).contains(this.getDegree())) {
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
        enrolment.getProgramCertificateRequests().clear();
        enrolment.getCourseLoadRequests().clear();
        enrolment.getExamDateCertificateRequests().clear();
        enrolment.delete();

        if (result.getStudentCurricularPlan() != this) {
            correctInvalidAttends(result.getStudentCurricularPlan());
        }

        return result;
    }

    private void correctInvalidAttends(final StudentCurricularPlan to) {
        for (final Attends attend : getRegistration().getAssociatedAttends()) {
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
                && !AcademicAuthorizationGroup.getProgramsForOperation(person,
                        AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION).contains(this.getDegree())) {
            throw new DomainException("error.StudentCurricularPlan.cannot.move.is.not.authorized");
        }

        if (!hasEnrolments(enrolment)) {
            // if we remove this test, then check if enrolment period is before
            // registration start
            throw new DomainException("error.StudentCurricularPlan.doesnot.have.enrolment", enrolment.getName().getContent());
        }

        final Enrolment result = Enrolment.createBasedOn(enrolment, curriculumGroup);
        enrolment.getProgramCertificateRequests().clear();
        enrolment.getCourseLoadRequests().clear();
        enrolment.getExamDateCertificateRequests().clear();
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
        for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
            if (gratuityEvent.isFor(executionYear)) {
                return true;
            }
        }

        return false;

    }

    public boolean hasAnyGratuitySituationFor(final ExecutionYear executionYear) {
        for (final GratuitySituation gratuitySituation : getGratuitySituations()) {
            if (gratuitySituation.getGratuityValues().getExecutionDegree().getExecutionYear() == executionYear) {
                return true;
            }
        }

        return false;
    }

    public Boolean getEvaluationForCurriculumValidationAllowed() {
        ExecutionSemester FENIX_START_DATE_SEMESTER = ExecutionSemester.readBySemesterAndExecutionYear(1, "2006/2007");
        return this.getStartExecutionPeriod().isBefore(FENIX_START_DATE_SEMESTER) && !this.isBolonhaDegree();
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
                            enrolmentEvaluationBean.getEnrolmentEvaluationType(),
                            enrolmentEvaluationBean.getCurriculumValidationEvaluationPhase(), AccessControl.getPerson(),
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
        return hasRoot() ? getRoot().getBranchCurriculumGroups() : Collections.<BranchCurriculumGroup> emptySet();
    }

    public Set<BranchCurriculumGroup> getMajorBranchCurriculumGroups() {
        return hasRoot() ? getRoot().getMajorBranchCurriculumGroups() : Collections.<BranchCurriculumGroup> emptySet();
    }

    public Set<BranchCurriculumGroup> getMinorBranchCurriculumGroups() {
        return hasRoot() ? getRoot().getMinorBranchCurriculumGroups() : Collections.<BranchCurriculumGroup> emptySet();
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

    public boolean isAllowedToManageEnrolments() {
        return AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
                AcademicOperationType.STUDENT_ENROLMENTS).contains(this.getDegree());
    }

    public boolean isAllowedToManageAccountingEvents() {
        return AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
                AcademicOperationType.MANAGE_ACCOUNTING_EVENTS).contains(this.getDegree());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent> getEnrolmentOutOfPeriodEvents() {
        return getEnrolmentOutOfPeriodEventsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentOutOfPeriodEvents() {
        return !getEnrolmentOutOfPeriodEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getPrecedentDegreeInformations() {
        return getPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyPrecedentDegreeInformations() {
        return !getPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent> getGratuityEvents() {
        return getGratuityEventsSet();
    }

    @Deprecated
    public boolean hasAnyGratuityEvents() {
        return !getGratuityEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Tutorship> getTutorships() {
        return getTutorshipsSet();
    }

    @Deprecated
    public boolean hasAnyTutorships() {
        return !getTutorshipsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea> getCreditsInAnySecundaryAreas() {
        return getCreditsInAnySecundaryAreasSet();
    }

    @Deprecated
    public boolean hasAnyCreditsInAnySecundaryAreas() {
        return !getCreditsInAnySecundaryAreasSet().isEmpty();
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
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.Credits> getCredits() {
        return getCreditsSet();
    }

    @Deprecated
    public boolean hasAnyCredits() {
        return !getCreditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CreditsInScientificArea> getCreditsInScientificAreas() {
        return getCreditsInScientificAreasSet();
    }

    @Deprecated
    public boolean hasAnyCreditsInScientificAreas() {
        return !getCreditsInScientificAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.InstitutionPrecedentDegreeInformation> getCandidacyPrecedentDegreeInformations() {
        return getCandidacyPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyPrecedentDegreeInformations() {
        return !getCandidacyPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GratuitySituation> getGratuitySituations() {
        return getGratuitySituationsSet();
    }

    @Deprecated
    public boolean hasAnyGratuitySituations() {
        return !getGratuitySituationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasBranch() {
        return getBranch() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMasterDegreeThesis() {
        return getMasterDegreeThesis() != null;
    }

    @Deprecated
    public boolean hasEndStageDate() {
        return getEndStageDate() != null;
    }

    @Deprecated
    public boolean hasEnrolledCourses() {
        return getEnrolledCourses() != null;
    }

    @Deprecated
    public boolean hasObservations() {
        return getObservations() != null;
    }

    @Deprecated
    public boolean hasSpecialization() {
        return getSpecialization() != null;
    }

    @Deprecated
    public boolean hasCurrentState() {
        return getCurrentState() != null;
    }

    @Deprecated
    public boolean hasEmployee() {
        return getEmployee() != null;
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasCompletedCourses() {
        return getCompletedCourses() != null;
    }

    @Deprecated
    public boolean hasWhenDateTime() {
        return getWhenDateTime() != null;
    }

    @Deprecated
    public boolean hasEquivalencePlan() {
        return getEquivalencePlan() != null;
    }

    @Deprecated
    public boolean hasRoot() {
        return getRoot() != null;
    }

    @Deprecated
    public boolean hasStartDateYearMonthDay() {
        return getStartDateYearMonthDay() != null;
    }

}
