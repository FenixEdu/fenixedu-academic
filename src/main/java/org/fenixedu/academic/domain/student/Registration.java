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
package org.fenixedu.academic.domain.student;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeCurricularPlanEquivalencePlan;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GratuitySituation;
import org.fenixedu.academic.domain.GratuityValues;
import org.fenixedu.academic.domain.GuideEntry;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.SchoolLevelType;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenEvaluationEnrolment;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accounting.EnrolmentBlocker;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.EnrolmentOutOfPeriodEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.gratuity.ReimbursementGuideState;
import org.fenixedu.academic.domain.log.CurriculumLineLog;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.reimbursementGuide.ReimbursementGuideEntry;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.PastDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurricularPlan.Specialization;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.domain.studentCurriculum.StandaloneCurriculumGroup;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.transactions.InsuranceTransaction;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.RegistrationPredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

public class Registration extends Registration_Base {

    private static final Logger logger = LoggerFactory.getLogger(Registration.class);

    public static final String REGISTRATION_PROCESS_COMPLETE = "academic.registration.process.complete";

    @Deprecated
    static private final java.util.function.Predicate<DegreeType> DEGREE_TYPES_TO_ENROL_BY_STUDENT = DegreeType.oneOf(
            DegreeType::isBolonhaDegree, DegreeType::isIntegratedMasterDegree, DegreeType::isBolonhaMasterDegree,
            DegreeType::isAdvancedSpecializationDiploma);

    static final public Comparator<Registration> NUMBER_COMPARATOR = new Comparator<Registration>() {
        @Override
        public int compare(Registration o1, Registration o2) {
            return o1.getNumber().compareTo(o2.getNumber());
        }
    };

    static final public Comparator<Registration> COMPARATOR_BY_START_DATE = new Comparator<Registration>() {
        @Override
        public int compare(Registration o1, Registration o2) {
            final int comparationResult = o1.getStartDate().compareTo(o2.getStartDate());
            return comparationResult == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : comparationResult;
        }
    };

    static public final ComparatorChain COMPARATOR_BY_NUMBER_AND_ID = new ComparatorChain();
    static {
        COMPARATOR_BY_NUMBER_AND_ID.addComparator(NUMBER_COMPARATOR);
        COMPARATOR_BY_NUMBER_AND_ID.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
    }

    private Registration() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRegistrationProtocol(RegistrationProtocol.getDefault());
    }

    private Registration(final Person person, final DateTime start, final Integer registrationNumber, final Degree degree) {
        this();
        setStudent(person.getStudent() != null ? person.getStudent() : new Student(person, registrationNumber));
        setNumber(registrationNumber == null ? getStudent().getNumber() : registrationNumber);
        setStartDate(start.toYearMonthDay());
        setDegree(degree);
        RegistrationState.createRegistrationState(this, AccessControl.getPerson(), start, RegistrationStateType.REGISTERED);
    }

    public Registration(final Person person, final StudentCandidacy studentCandidacy) {
        this(person, null, RegistrationProtocol.getDefault(), null, studentCandidacy);
    }

    public Registration(final Person person, final Integer studentNumber, final Degree degree) {
        this(person, studentNumber, RegistrationProtocol.getDefault(), null, degree);
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan) {
        this(person, degreeCurricularPlan, RegistrationProtocol.getDefault(), null, null);
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType) {
        this(person, degreeCurricularPlan, RegistrationProtocol.getDefault(), cycleType, null);
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final RegistrationProtocol protocol, final CycleType cycleType, final ExecutionYear executionYear) {
        this(person, null, protocol, executionYear, degreeCurricularPlan != null ? degreeCurricularPlan.getDegree() : null);
        createStudentCurricularPlan(degreeCurricularPlan, executionYear, cycleType);
    }

    public static Registration createRegistrationWithCustomStudentNumber(final Person person,
            final DegreeCurricularPlan degreeCurricularPlan, final StudentCandidacy studentCandidacy,
            final RegistrationProtocol protocol, final CycleType cycleType, final ExecutionYear executionYear,
            Integer studentNumber) {
        final Degree degree = degreeCurricularPlan == null ? null : degreeCurricularPlan.getDegree();
        Registration registration = new Registration(person, calculateStartDate(executionYear), studentNumber, degree);
        registration.setRegistrationYear(executionYear == null ? ExecutionYear.readCurrentExecutionYear() : executionYear);
        registration.setRequestedChangeDegree(false);
        registration.setRequestedChangeBranch(false);
        registration.setRegistrationProtocol(protocol == null ? RegistrationProtocol.getDefault() : protocol);
        registration.createStudentCurricularPlan(degreeCurricularPlan, executionYear, cycleType);
        registration.setStudentCandidacyInformation(studentCandidacy);

        return registration;
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan, final StudentCandidacy candidacy,
            final RegistrationProtocol protocol, final CycleType cycleType) {
        this(person, degreeCurricularPlan, candidacy, protocol, cycleType, null);
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final StudentCandidacy studentCandidacy, final RegistrationProtocol protocol, final CycleType cycleType,
            final ExecutionYear executionYear) {

        this(person, degreeCurricularPlan, protocol, cycleType, executionYear);
        setStudentCandidacyInformation(studentCandidacy);
        EventGenerator.generateNecessaryEvents(getLastStudentCurricularPlan(), person, executionYear);
    }

    private Registration(final Person person, final Integer registrationNumber, final RegistrationProtocol protocol,
            final ExecutionYear executionYear, final StudentCandidacy studentCandidacy) {

        this(person, registrationNumber, protocol, executionYear, getDegreeFromCandidacy(studentCandidacy));
        setStudentCandidacyInformation(studentCandidacy);
    }

    private static Degree getDegreeFromCandidacy(StudentCandidacy studentCandidacy) {
        return studentCandidacy == null ? null : studentCandidacy.getExecutionDegree().getDegree();
    }

    private Registration(final Person person, final Integer registrationNumber, final RegistrationProtocol protocol,
            final ExecutionYear executionYear, final Degree degree) {

        this(person, calculateStartDate(executionYear), registrationNumber, degree);

        setRegistrationYear(executionYear == null ? ExecutionYear.readCurrentExecutionYear() : executionYear);
        setRequestedChangeDegree(false);
        setRequestedChangeBranch(false);
        setRegistrationProtocol(protocol == null ? RegistrationProtocol.getDefault() : protocol);
    }

    private void setStudentCandidacyInformation(final StudentCandidacy studentCandidacy) {
        setStudentCandidacy(studentCandidacy);
        if (studentCandidacy != null) {
            super.setEntryPhase(studentCandidacy.getEntryPhase());
            super.setIngressionType(studentCandidacy.getIngressionType());

            if (studentCandidacy.getIngressionType() != null && studentCandidacy.getIngressionType().isReIngression()) {
                final Degree sourceDegree = studentCandidacy.getDegreeCurricularPlan().getEquivalencePlan().getSourceDegree();
                Registration registration = getStudent().readRegistrationByDegree(sourceDegree);
                if (registration == null) {
                    final Collection<Registration> registrations =
                            getStudent().getRegistrationsMatchingDegreeType(DegreeType::isPreBolonhaDegree);
                    registrations.remove(this);
                    registration = registrations.size() == 1 ? registrations.iterator().next() : null;
                }

                setSourceRegistration(registration);
            }
        }
    }

    public StudentCurricularPlan createStudentCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear) {

        return createStudentCurricularPlan(degreeCurricularPlan, executionYear, (CycleType) null);
    }

    private StudentCurricularPlan createStudentCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear, final CycleType cycleType) {

        final YearMonthDay startDay;
        final ExecutionSemester executionSemester;

        if (executionYear == null || executionYear.isCurrent()) {
            startDay = new YearMonthDay();
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        } else {
            startDay = executionYear.getBeginDateYearMonthDay();
            executionSemester = executionYear.getFirstExecutionPeriod();
        }

        return StudentCurricularPlan.createBolonhaStudentCurricularPlan(this, degreeCurricularPlan, startDay, executionSemester,
                cycleType);

    }

    private static DateTime calculateStartDate(final ExecutionYear executionYear) {
        DateTime now = new DateTime();
        return executionYear == null || (executionYear.isCurrent() && executionYear.getAcademicInterval().contains(now)) ? now : executionYear
                .getBeginDateYearMonthDay().toDateTimeAtMidnight();
    }

    @Override
    final public void setNumber(Integer number) {
        super.setNumber(number);
        if (number == null && getRegistrationNumber() != null) {
            getRegistrationNumber().delete();
        } else if (number != null) {
            if (getRegistrationNumber() != null) {
                getRegistrationNumber().setNumber(number);
            } else {
                new RegistrationNumber(this);
            }
        }
    }

    public void delete() {

        checkRulesToDelete();

        for (; !getRegistrationStatesSet().isEmpty(); getRegistrationStatesSet().iterator().next().delete()) {
            ;
        }
        for (; !getStudentCurricularPlansSet().isEmpty(); getStudentCurricularPlansSet().iterator().next().delete()) {
            ;
        }
        for (; !getAssociatedAttendsSet().isEmpty(); getAssociatedAttendsSet().iterator().next().delete()) {
            ;
        }
        for (; !getExternalEnrolmentsSet().isEmpty(); getExternalEnrolmentsSet().iterator().next().delete()) {
            ;
        }
        for (; !getRegistrationDataByExecutionYearSet().isEmpty(); getRegistrationDataByExecutionYearSet().iterator().next()
                .delete()) {
            ;
        }
        for (; !getAcademicServiceRequestsSet().isEmpty(); getAcademicServiceRequestsSet().iterator().next().delete()) {
            ;
        }
        for (; !getRegistrationRegimesSet().isEmpty(); getRegistrationRegimesSet().iterator().next().delete()) {
            ;
        }
        for (; !getCurriculumLineLogsSet().isEmpty(); getCurriculumLineLogsSet().iterator().next().delete()) {
            ;
        }
        for (; !getRegistrationStateLogsSet().isEmpty(); getRegistrationStateLogsSet().iterator().next().delete()) {
            ;
        }
        getPrecedentDegreesInformationsSet().forEach(pdi -> pdi.delete());
        getRegistrationStateLogSet().forEach(rsl -> {
            rsl.setRegistration(null);
            rsl.delete();
        });

        if (getRegistrationNumber() != null) {
            getRegistrationNumber().delete();
        }
        if (getExternalRegistrationData() != null) {
            getExternalRegistrationData().delete();
        }
        if (getSenior() != null) {
            getSenior().delete();
        }
        if (getStudentCandidacy() != null) {
            getStudentCandidacy().delete();
        }

        setIndividualCandidacy(null);
        setSourceRegistration(null);
        setRegistrationYear(null);
        setDegree(null);
        setStudent(null);
        super.setRegistrationProtocol(null);
        super.setIngressionType(null);
        setRootDomainObject(null);

        getDestinyRegistrationsSet().clear();
        getShiftsSet().clear();

        super.deleteDomainObject();
    }

    private void checkRulesToDelete() {
        if (getDfaRegistrationEventsSet().size() > 0) {
            throw new DomainException("error.student.Registration.cannot.delete.because.is.associated.to.dfa.registration.event");
        }
    }

    public StudentCurricularPlan getActiveStudentCurricularPlan() {
        return isActive() ? getLastStudentCurricularPlan() : null;
    }

    public StudentCurricularPlan getLastStudentCurricularPlan() {
        final Set<StudentCurricularPlan> studentCurricularPlans = getStudentCurricularPlansSet();

        if (studentCurricularPlans.isEmpty()) {
            return null;
        }
        return Collections.max(studentCurricularPlans, StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE);
    }

    public StudentCurricularPlan getFirstStudentCurricularPlan() {
        return !getStudentCurricularPlansSet().isEmpty() ? (StudentCurricularPlan) Collections.min(
                getStudentCurricularPlansSet(), StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE) : null;
    }

    public List<StudentCurricularPlan> getSortedStudentCurricularPlans() {
        final ArrayList<StudentCurricularPlan> sortedStudentCurricularPlans =
                new ArrayList<StudentCurricularPlan>(super.getStudentCurricularPlansSet());
        Collections.sort(sortedStudentCurricularPlans, StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE);
        return sortedStudentCurricularPlans;
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansExceptPast() {
        List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan studentCurricularPlan : super.getStudentCurricularPlansSet()) {
            if (!studentCurricularPlan.isPast()) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    public boolean attends(final ExecutionCourse executionCourse) {
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.isFor(executionCourse)) {
                return true;
            }
        }
        return false;
    }

    final public List<WrittenEvaluation> getWrittenEvaluations(final ExecutionSemester executionSemester) {
        final List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.isFor(executionSemester)) {
                for (final Evaluation evaluation : attend.getExecutionCourse().getAssociatedEvaluationsSet()) {
                    if (evaluation instanceof WrittenEvaluation && !result.contains(evaluation)) {
                        result.add((WrittenEvaluation) evaluation);
                    }
                }
            }
        }
        return result;
    }

    final public List<Exam> getEnroledExams(final ExecutionSemester executionSemester) {
        final List<Exam> result = new ArrayList<Exam>();
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolmentsSet()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof Exam
                    && writtenEvaluationEnrolment.isForExecutionPeriod(executionSemester)) {
                result.add((Exam) writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }

    final public List<Exam> getUnenroledExams(final ExecutionSemester executionSemester) {
        final List<Exam> result = new ArrayList<Exam>();
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.isFor(executionSemester)) {
                for (final Evaluation evaluation : attend.getExecutionCourse().getAssociatedEvaluationsSet()) {
                    if (evaluation instanceof Exam && !this.isEnroledIn(evaluation)) {
                        result.add((Exam) evaluation);
                    }
                }
            }
        }
        return result;
    }

    final public List<WrittenTest> getEnroledWrittenTests(final ExecutionSemester executionSemester) {
        final List<WrittenTest> result = new ArrayList<WrittenTest>();
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolmentsSet()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof WrittenTest
                    && writtenEvaluationEnrolment.isForExecutionPeriod(executionSemester)) {
                result.add((WrittenTest) writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }

    final public List<WrittenTest> getUnenroledWrittenTests(final ExecutionSemester executionSemester) {
        final List<WrittenTest> result = new ArrayList<WrittenTest>();
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                for (final Evaluation evaluation : attend.getExecutionCourse().getAssociatedEvaluationsSet()) {
                    if (evaluation instanceof WrittenTest && !this.isEnroledIn(evaluation)) {
                        result.add((WrittenTest) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public List<Project> getProjects(final ExecutionSemester executionSemester) {
        final List<Project> result = new ArrayList<Project>();
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.isFor(executionSemester)) {
                for (final Evaluation evaluation : attend.getExecutionCourse().getAssociatedEvaluationsSet()) {
                    if (evaluation instanceof Project) {
                        result.add((Project) evaluation);
                    }
                }
            }
        }
        return result;
    }

    final public boolean isEnroledIn(final Evaluation evaluation) {
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolmentsSet()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == evaluation) {
                return true;
            }
        }
        return false;
    }

    final public Space getRoomFor(final WrittenEvaluation writtenEvaluation) {
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolmentsSet()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == writtenEvaluation) {
                return writtenEvaluationEnrolment.getRoom();
            }
        }
        return null;
    }

    final public Stream<StudentCurricularPlan> getStudentCurricularPlanStream() {
        return getStudentCurricularPlansSet().stream().sorted(StudentCurricularPlan
                .STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE.reversed());
    }

    final private CycleCurriculumGroup getCycleCurriculumGroup(CycleType cycleType) {
        return getStudentCurricularPlanStream().map(scp -> scp.getCycle(cycleType)).filter
                (Objects::nonNull).findFirst().orElse(null);
    }

    final public ICurriculum getCurriculum() {
        return getCurriculum(new DateTime(), (ExecutionYear) null, (CycleType) null);
    }

    final public ICurriculum getCurriculum(final DateTime when) {
        return getCurriculum(when, (ExecutionYear) null, (CycleType) null);
    }

    final public ICurriculum getCurriculum(final ExecutionYear executionYear) {
        return getCurriculum(new DateTime(), executionYear, (CycleType) null);
    }

    final public ICurriculum getCurriculum(final CycleType cycleType) {
        return getCurriculum(new DateTime(), (ExecutionYear) null, cycleType);
    }

    final public ICurriculum getCurriculum(final ExecutionYear executionYear, final CycleType cycleType) {
        return getCurriculum(new DateTime(), executionYear, cycleType);
    }

    final public ICurriculum getCurriculum(final DateTime when, final ExecutionYear executionYear, final CycleType cycleType) {
        if (getStudentCurricularPlansSet().isEmpty()) {
            return Curriculum.createEmpty(executionYear);
        }

        if (getDegreeType().isBolonhaType()) {
            final StudentCurricularPlan studentCurricularPlan = getLastStudentCurricularPlan();
            if (studentCurricularPlan == null) {
                return Curriculum.createEmpty(executionYear);
            }

            if (cycleType == null) {
                return studentCurricularPlan.getCurriculum(when, executionYear);
            }

            final CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroup(cycleType);
            if (cycleCurriculumGroup == null) {
                return Curriculum.createEmpty(executionYear);
            }

            return cycleCurriculumGroup.getCurriculum(when, executionYear);
        } else {
            final List<StudentCurricularPlan> sortedSCPs = getSortedStudentCurricularPlans();
            final ListIterator<StudentCurricularPlan> sortedSCPsIterator = sortedSCPs.listIterator(sortedSCPs.size());
            final StudentCurricularPlan lastStudentCurricularPlan = sortedSCPsIterator.previous();

            final ICurriculum curriculum;
            curriculum = lastStudentCurricularPlan.getCurriculum(when, executionYear);

            for (; sortedSCPsIterator.hasPrevious();) {
                final StudentCurricularPlan studentCurricularPlan = sortedSCPsIterator.previous();
                if (executionYear == null || studentCurricularPlan.getStartExecutionYear().isBeforeOrEquals(executionYear)) {
                    ((Curriculum) curriculum).add(studentCurricularPlan.getCurriculum(when, executionYear));
                }
            }

            return curriculum;

        }
    }

    public int getNumberOfCurriculumEntries() {
        return getCurriculum().getCurriculumEntries().size();
    }

    final public Grade getRawGrade() {
        return ProgramConclusion.getConclusionProcess(getLastStudentCurricularPlan()).map(ConclusionProcess::getRawGrade)
                .orElseGet(this::calculateRawGrade);
    }

    final public Grade calculateRawGrade() {
        return getCurriculum().getRawGrade();
    }

    final public BigDecimal getEctsCredits(final ExecutionYear executionYear, final CycleType cycleType) {
        return getCurriculum(executionYear, cycleType).getSumEctsCredits();
    }

    final public Grade getFinalGrade() {
        return ProgramConclusion.getConclusionProcess(getLastStudentCurricularPlan()).map(ConclusionProcess::getFinalGrade)
                .orElse(null);
    }

    final public Grade getFinalGrade(ProgramConclusion programConclusion) {
        return programConclusion.groupFor(this).map(CurriculumGroup::getFinalGrade).orElse(null);
    }

    final public boolean isInFinalDegreeYear() {
        return getCurricularYear() == getLastStudentCurricularPlan().getDegreeCurricularPlan().getDurationInYears();
    }

    final public boolean isQualifiedForSeniority() {
        return isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree()
                && (isConcluded() || (isActive() && isInFinalDegreeForSeniority()));
    }

    public boolean isInFinalDegreeForSeniority() {
        int years = 0;
        final StudentCurricularPlan studentCurricularPlan = getLastStudentCurricularPlan();
        for (final CycleType type : getDegreeType().getCycleTypes()) {
            if (studentCurricularPlan.hasCycleCurriculumGroup(type)) {
                years += studentCurricularPlan.getDegreeCurricularPlan().getDurationInYears(type);
            }
        }
        return getCurricularYear() == years;
    }

    final public Collection<CurricularCourse> getCurricularCoursesApprovedByEnrolment() {
        final Collection<CurricularCourse> result = new HashSet<CurricularCourse>();

        for (final Enrolment enrolment : getApprovedEnrolments()) {
            result.add(enrolment.getCurricularCourse());
        }

        return result;
    }

    final public Collection<Enrolment> getLatestCurricularCoursesEnrolments(final ExecutionYear executionYear) {
        return getStudentCurricularPlan(executionYear).getLatestCurricularCoursesEnrolments(executionYear);
    }

    final public boolean hasEnrolments(final Enrolment enrolment) {
        if (enrolment == null) {
            return false;
        }

        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasEnrolments(enrolment)) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyEnrolments() {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyEnrolments()) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyCurriculumLines() {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyCurriculumLines()) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyCurriculumLines(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyCurriculumLines(executionYear)) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyCurriculumLines(final ExecutionSemester executionSemester) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyCurriculumLines(executionSemester)) {
                return true;
            }
        }

        return false;
    }

    final public Collection<Enrolment> getEnrolments(final ExecutionYear executionYear) {
        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(executionYear);
        return studentCurricularPlan != null ? studentCurricularPlan.getEnrolmentsByExecutionYear(executionYear) : Collections.EMPTY_LIST;
    }

    final public Collection<Enrolment> getEnrolments(final ExecutionSemester executionSemester) {
        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(executionSemester.getExecutionYear());
        return studentCurricularPlan != null ? studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionSemester) : Collections.EMPTY_LIST;
    }

    final public void addApprovedEnrolments(final Collection<Enrolment> enrolments) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            studentCurricularPlan.addApprovedEnrolments(enrolments);
        }
    }

    final public Collection<Enrolment> getApprovedEnrolments() {
        final Collection<Enrolment> result = new HashSet<Enrolment>();

        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            result.addAll(studentCurricularPlan.getAprovedEnrolments());
        }

        return result;
    }

    final public Collection<ExternalEnrolment> getApprovedExternalEnrolments() {
        final Collection<ExternalEnrolment> result = new HashSet<ExternalEnrolment>();
        for (final ExternalEnrolment externalEnrolment : getExternalEnrolmentsSet()) {
            if (externalEnrolment.isApproved()) {
                result.add(externalEnrolment);
            }
        }
        return result;
    }

    final public Collection<CurriculumLine> getExtraCurricularCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();

        final Collection<StudentCurricularPlan> toInspect =
                (isBolonha() ? Collections.singleton(getLastStudentCurricularPlan()) : getStudentCurricularPlansSet());
        for (final StudentCurricularPlan studentCurricularPlan : toInspect) {
            result.addAll(studentCurricularPlan.getExtraCurricularCurriculumLines());
        }

        return result;
    }

    final public Collection<CurriculumLine> getStandaloneCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();

        final Collection<StudentCurricularPlan> toInspect =
                (isBolonha() ? Collections.singleton(getLastStudentCurricularPlan()) : getStudentCurricularPlansSet());
        for (final StudentCurricularPlan studentCurricularPlan : toInspect) {
            result.addAll(studentCurricularPlan.getStandaloneCurriculumLines());
        }

        return result;
    }

    public void assertConclusionDate(final Collection<CurriculumModule> result) {
        for (final CurriculumLine curriculumLine : getApprovedCurriculumLines()) {
            if (curriculumLine.calculateConclusionDate() == null) {
                result.add(curriculumLine);
            }
        }
    }

    final public Collection<Enrolment> getPropaedeuticEnrolments() {
        final Collection<Enrolment> result = new HashSet<Enrolment>();

        final Collection<StudentCurricularPlan> toInspect =
                (isBolonha() ? Collections.singleton(getLastStudentCurricularPlan()) : getStudentCurricularPlansSet());
        for (final StudentCurricularPlan studentCurricularPlan : toInspect) {
            result.addAll(studentCurricularPlan.getPropaedeuticEnrolments());
        }

        return result;
    }

    final public Collection<CurriculumLine> getPropaedeuticCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();

        final Collection<StudentCurricularPlan> toInspect =
                (isBolonha() ? Collections.singleton(getLastStudentCurricularPlan()) : getStudentCurricularPlansSet());
        for (final StudentCurricularPlan studentCurricularPlan : toInspect) {
            result.addAll(studentCurricularPlan.getPropaedeuticCurriculumLines());
        }

        return result;
    }

    public YearMonthDay getLastExternalApprovedEnrolmentEvaluationDate() {

        if (getExternalEnrolmentsSet().isEmpty()) {
            return null;
        }

        ExternalEnrolment externalEnrolment =
                Collections.max(getExternalEnrolmentsSet(), ExternalEnrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_EVALUATION_DATE);

        return externalEnrolment.getApprovementDate() != null ? externalEnrolment.getApprovementDate() : externalEnrolment
                .hasExecutionPeriod() ? externalEnrolment.getExecutionPeriod().getEndDateYearMonthDay() : null;
    }

    final public Collection<CurriculumLine> getApprovedCurriculumLines() {
        if (isBolonha()) {
            return getLastStudentCurricularPlan().getApprovedCurriculumLines();
        } else {
            final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();

            for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
                result.addAll(plan.getApprovedCurriculumLines());
            }

            return result;
        }

    }

    final public boolean hasAnyApprovedCurriculumLines() {
        return getLastStudentCurricularPlan().hasAnyApprovedCurriculumLines();
    }

    final public Collection<IEnrolment> getApprovedIEnrolments() {
        final Collection<IEnrolment> result = new HashSet<IEnrolment>();

        for (final CurriculumLine curriculumLine : getApprovedCurriculumLines()) {
            if (curriculumLine.isEnrolment()) {
                result.add((Enrolment) curriculumLine);
            } else if (curriculumLine.isDismissal()) {
                result.addAll(((Dismissal) curriculumLine).getSourceIEnrolments());
            }
        }

        result.addAll(getExternalEnrolmentsSet());

        return result;
    }

    final public boolean hasAnyApprovedEnrolment() {
        return getLastStudentCurricularPlan().hasAnyApprovedEnrolment() || hasAnyExternalApprovedEnrolment();
    }

    final public boolean hasAnyApprovedEnrolments(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.isApproved() && enrolment.getExecutionPeriod().getExecutionYear() == executionYear) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyEnroledEnrolments(final ExecutionYear year) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.isEnroled() && enrolment.isValid(year)) {
                    return true;
                }
            }
        }
        return false;
    }

    final public boolean hasAnyEnrolmentsIn(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.getExecutionPeriod().getExecutionYear() == executionYear) {
                    return true;
                }
            }
        }
        return false;
    }

    final public boolean hasAnyEnrolmentsIn(final ExecutionSemester executionSemester) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.getExecutionPeriod() == executionSemester) {
                    return true;
                }
            }
        }
        return false;
    }

    final public boolean hasAnyStandaloneEnrolmentsIn(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            StandaloneCurriculumGroup standaloneCurriculumGroup = studentCurricularPlan.getStandaloneCurriculumGroup();
            if ((standaloneCurriculumGroup != null) && (standaloneCurriculumGroup.hasEnrolment(executionYear))) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasAnyExternalApprovedEnrolment() {
        for (final ExternalEnrolment externalEnrolment : this.getExternalEnrolmentsSet()) {
            if (externalEnrolment.isApproved()) {
                return true;
            }
        }
        return false;
    }

    final public Double getDismissalsEctsCredits() {
        return getLastStudentCurricularPlan().getDismissalsEctsCredits();
    }

    final public boolean getHasExternalEnrolments() {
        return !getExternalEnrolmentsSet().isEmpty();
    }

    final public Stream<ExecutionYear> getEnrolmentsExecutionYearStream() {
        return getStudentCurricularPlansSet().stream()
            .flatMap(scp -> scp.getEnrolmentStream())
            .map(e -> e.getExecutionYear())
            .distinct();
    }

    /**
     * @deprecated use getEnrolmentsExecutionYearStream instead
     */
    @Deprecated
    final public Collection<ExecutionYear> getEnrolmentsExecutionYears() {
        final Set<ExecutionYear> result = new HashSet<ExecutionYear>();

        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                result.add(enrolment.getExecutionPeriod().getExecutionYear());
            }
        }
        return result;
    }

    final public int getNumberOfYearsEnrolledUntil(ExecutionYear executionYear) {
        return Math.toIntExact(getEnrolmentsExecutionYearStream()
            .filter(y -> y.isBeforeOrEquals(executionYear))
            .count());
    }

    final public SortedSet<ExecutionYear> getSortedEnrolmentsExecutionYears() {
        final Supplier<TreeSet<ExecutionYear>> supplier = () -> new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);
        return getEnrolmentsExecutionYearStream().collect(Collectors.toCollection(supplier));
    }

    /**
     * @deprecated method is never used... delete it
     */
    @Deprecated
    public Set<ExecutionYear> getAllRelatedRegistrationsEnrolmentsExecutionYears(Set<ExecutionYear> result) {
        if (result == null) {
            result = new HashSet<ExecutionYear>();
        }
        result.addAll(getEnrolmentsExecutionYears());

        if (this.isBolonha()) {
            Registration source =
                    getSourceRegistration() != null ? getSourceRegistration() : getSourceRegistrationForTransition();
            if (source != null) {
                source.getAllRelatedRegistrationsEnrolmentsExecutionYears(result);
            }

        } else {
            Collection<Registration> registrations = getStudent().getRegistrationsSet();
            for (Registration registration : registrations) {
                if (registration != this && !registration.isBolonha()) {
                    if (!registration.isConcluded()) {
                        result.addAll(registration.getEnrolmentsExecutionYears());
                    }
                }
            }
        }

        return result;
    }

    public Collection<ExecutionYear> getCurriculumLinesExecutionYears() {
        final Collection<ExecutionYear> result = new ArrayList<ExecutionYear>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final CurriculumLine curriculumLine : studentCurricularPlan.getAllCurriculumLines()) {
                if (curriculumLine.hasExecutionPeriod()) {
                    result.add(curriculumLine.getExecutionYear());
                }
            }
        }
        return result;
    }

    public SortedSet<ExecutionYear> getSortedCurriculumLinesExecutionYears() {
        final SortedSet<ExecutionYear> result = new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);
        result.addAll(getCurriculumLinesExecutionYears());
        return result;
    }

    final public ExecutionYear getFirstEnrolmentExecutionYear() {
        final SortedSet<ExecutionYear> sortedEnrolmentsExecutionYears = getSortedEnrolmentsExecutionYears();
        return sortedEnrolmentsExecutionYears.isEmpty() ? null : sortedEnrolmentsExecutionYears.first();
    }

    public ExecutionYear getFirstCurriculumLineExecutionYear() {
        final SortedSet<ExecutionYear> executionYears = getSortedCurriculumLinesExecutionYears();
        return executionYears.isEmpty() ? null : executionYears.first();
    }

    final public ExecutionYear getLastEnrolmentExecutionYear() {
        SortedSet<ExecutionYear> sorted = getSortedEnrolmentsExecutionYears();
        if (!sorted.isEmpty()) {
            return sorted.last();
        } else {
            return null;
        }
    }

    public ExecutionYear getLastApprovementExecutionYear() {
        if (isBolonha()) {
            return getLastStudentCurricularPlan().getLastApprovementExecutionYear();
        } else {
            ExecutionYear result = null;
            for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
                final ExecutionYear year = plan.getLastApprovementExecutionYear();
                if (year != null && (result == null || result.isBefore(year))) {
                    result = year;
                }
            }

            return result;
        }
    }

    final public Collection<ExecutionSemester> getEnrolmentsExecutionPeriods() {
        final Set<ExecutionSemester> result = new HashSet<ExecutionSemester>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                result.add(enrolment.getExecutionPeriod());
            }
        }
        return result;
    }

    final public SortedSet<ExecutionSemester> getSortedEnrolmentsExecutionPeriods() {
        final SortedSet<ExecutionSemester> result =
                new TreeSet<ExecutionSemester>(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
        result.addAll(getEnrolmentsExecutionPeriods());

        return result;
    }

    final public Set<Attends> getOrderedAttends() {
        final Set<Attends> result = new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR);
        result.addAll(getAssociatedAttendsSet());
        return result;
    }

    final public int countCompletedCoursesForActiveUndergraduateCurricularPlan() {
        return getActiveStudentCurricularPlan().getAprovedEnrolments().size();
    }

    public List<StudentCurricularPlan> getStudentCurricularPlansBySpecialization(Specialization specialization) {
        List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getSpecialization() != null
                    && studentCurricularPlan.getSpecialization().equals(specialization)) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansByDegree(Degree degree) {
        final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getDegree() == degree) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    final public StudentCurricularPlan getPastStudentCurricularPlanByDegree(Degree degree) {
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getDegree() == degree && studentCurricularPlan.isPast()) {
                return studentCurricularPlan;
            }
        }
        return null;
    }

    public List<Attends> readAttendsInCurrentExecutionPeriod() {
        final List<Attends> attends = new ArrayList<Attends>();
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.getExecutionCourse().getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                attends.add(attend);
            }
        }
        return attends;
    }

    public List<Attends> readAttendsByExecutionPeriod(final ExecutionSemester executionSemester) {
        final List<Attends> attends = new ArrayList<Attends>();
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.isFor(executionSemester)) {
                attends.add(attend);
            }
        }
        return attends;
    }

    @Deprecated
    final public static Registration readByUsername(String username) {
        final Person person = Person.readPersonByUsername(username);
        if (person != null) {
            for (final Registration registration : person.getStudentsSet()) {
                return registration;
            }
        }
        return null;
    }

    @Deprecated
    final public static Registration readStudentByNumberAndDegreeType(Integer number, DegreeType degreeType) {
        Registration nonActiveRegistration = null;
        for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.getNumber().intValue() == number.intValue() && registration.getDegreeType().equals(degreeType)) {
                if (registration.isActive()) {
                    return registration;
                }
                nonActiveRegistration = registration;
            }
        }
        return nonActiveRegistration;
    }

    final public static Registration readByNumberAndDegreeCurricularPlan(Integer number, DegreeCurricularPlan degreeCurricularPlan) {
        Registration nonActiveRegistration = null;
        for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.getNumber().intValue() == number.intValue()
                    && registration.getDegreeCurricularPlans().contains(degreeCurricularPlan)) {
                if (registration.isActive()) {
                    return registration;
                }
                nonActiveRegistration = registration;
            }
        }
        return nonActiveRegistration;
    }

    final public static Registration readRegisteredRegistrationByNumberAndDegreeType(Integer number, DegreeType degreeType) {
        for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.getNumber().intValue() == number.intValue() && registration.getDegreeType().equals(degreeType)
                    && registration.isInRegisteredState()) {
                return registration;
            }
        }
        return null;
    }

    final public static Collection<Registration> readRegistrationsByNumberAndDegreeTypes(Integer number,
            DegreeType... degreeTypes) {
        List<Registration> result = new ArrayList<Registration>();
        final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()) {
                final Registration registration = registrationNumber.getRegistration();
                if (degreeTypesList.contains(registration.getDegreeType())) {
                    result.add(registration);
                }
            }
        }
        return result;
    }

    final public static List<Registration> readByNumber(Integer number) {
        final List<Registration> registrations = new ArrayList<Registration>();
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()) {
                registrations.add(registrationNumber.getRegistration());
            }
        }
        return registrations;
    }

    final public static List<Registration> readByNumberAndDegreeType(Integer number, DegreeType degreeType) {
        final List<Registration> registrations = new ArrayList<Registration>();
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()
                    && registrationNumber.getRegistration().getDegreeType() == degreeType) {
                registrations.add(registrationNumber.getRegistration());
            }
        }
        return registrations;
    }

    final public static List<Registration> readByNumberAndDegreeTypeAndAgreement(Integer number, DegreeType degreeType,
            boolean normalAgreement) {
        final List<Registration> registrations = new ArrayList<Registration>();
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()
                    && registrationNumber.getRegistration().getDegreeType() == degreeType
                    && (registrationNumber.getRegistration().getRegistrationProtocol() == RegistrationProtocol.getDefault()) == normalAgreement) {
                registrations.add(registrationNumber.getRegistration());
            }
        }
        return registrations;
    }

    final public static List<Registration> readAllStudentsBetweenNumbers(Integer fromNumber, Integer toNumber) {
        int fromNumberInt = fromNumber.intValue();
        int toNumberInt = toNumber.intValue();

        final List<Registration> students = new ArrayList<Registration>();
        for (final Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            int studentNumberInt = registration.getNumber().intValue();
            if (studentNumberInt >= fromNumberInt && studentNumberInt <= toNumberInt) {
                students.add(registration);
            }
        }
        return students;
    }

    final public static List<Registration> readRegistrationsByDegreeType(DegreeType degreeType) {
        final List<Registration> students = new ArrayList<Registration>();
        for (final Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.getDegreeType().equals(degreeType)) {
                students.add(registration);
            }
        }
        return students;
    }

    final public GratuitySituation readGratuitySituationByExecutionDegree(ExecutionDegree executionDegree) {
        GratuityValues gratuityValues = executionDegree.getGratuityValues();
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlansSet()) {
            GratuitySituation gratuitySituation = studentCurricularPlan.getGratuitySituationByGratuityValues(gratuityValues);
            if (gratuitySituation != null) {
                return gratuitySituation;
            }
        }
        return null;
    }

    final public List<InsuranceTransaction> readAllInsuranceTransactionByExecutionYear(ExecutionYear executionYear) {
        List<InsuranceTransaction> insuranceTransactions = new ArrayList<InsuranceTransaction>();
        for (InsuranceTransaction insuranceTransaction : this.getInsuranceTransactionsSet()) {
            if (insuranceTransaction.getExecutionYear().equals(executionYear)) {
                insuranceTransactions.add(insuranceTransaction);
            }
        }
        return insuranceTransactions;
    }

    final public List<InsuranceTransaction> readAllNonReimbursedInsuranceTransactionsByExecutionYear(ExecutionYear executionYear) {
        List<InsuranceTransaction> nonReimbursedInsuranceTransactions = new ArrayList<InsuranceTransaction>();
        for (InsuranceTransaction insuranceTransaction : this.getInsuranceTransactionsSet()) {
            if (insuranceTransaction.getExecutionYear().equals(executionYear)) {
                GuideEntry guideEntry = insuranceTransaction.getGuideEntry();
                if (guideEntry == null || guideEntry.getReimbursementGuideEntriesSet().isEmpty()) {
                    nonReimbursedInsuranceTransactions.add(insuranceTransaction);
                } else {
                    boolean isReimbursed = false;
                    for (ReimbursementGuideEntry reimbursementGuideEntry : guideEntry.getReimbursementGuideEntriesSet()) {
                        if (reimbursementGuideEntry.getReimbursementGuide().getActiveReimbursementGuideSituation()
                                .getReimbursementGuideState().equals(ReimbursementGuideState.PAYED)) {
                            isReimbursed = true;
                            break;
                        }
                    }
                    if (!isReimbursed) {
                        nonReimbursedInsuranceTransactions.add(insuranceTransaction);
                    }
                }
            }
        }
        return nonReimbursedInsuranceTransactions;
    }

    final public Enrolment findEnrolmentByEnrolmentID(final String enrolmentID) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            final Enrolment enrolment = studentCurricularPlan.findEnrolmentByEnrolmentID(enrolmentID);
            if (enrolment != null) {
                return enrolment;
            }
        }
        return null;
    }

    final public Set<ExecutionCourse> getAttendingExecutionCoursesForCurrentExecutionPeriod() {
        final Set<ExecutionCourse> result = new HashSet<ExecutionCourse>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.getExecutionCourse().getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                result.add(attends.getExecutionCourse());
            }
        }
        return result;
    }

    final public Set<ExecutionCourse> getAttendingExecutionCoursesFor() {
        final Set<ExecutionCourse> result = new HashSet<ExecutionCourse>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            result.add(attends.getExecutionCourse());
        }
        return result;
    }

    final public List<ExecutionCourse> getAttendingExecutionCoursesFor(final ExecutionSemester executionSemester) {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.isFor(executionSemester)) {
                result.add(attends.getExecutionCourse());
            }
        }
        return result;
    }

    final public List<ExecutionCourse> getAttendingExecutionCoursesFor(final ExecutionYear executionYear) {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.isFor(executionYear)) {
                result.add(attends.getExecutionCourse());
            }
        }

        return result;
    }

    final public List<Attends> getAttendsForExecutionPeriod(final ExecutionSemester executionSemester) {
        final List<Attends> result = new ArrayList<Attends>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.isFor(executionSemester)) {
                result.add(attends);
            }
        }
        return result;
    }

    final public List<Shift> getShiftsForCurrentExecutionPeriod() {
        final List<Shift> result = new ArrayList<Shift>();
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse().getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                result.add(shift);
            }
        }
        return result;
    }

    final public List<Shift> getShiftsFor(final ExecutionSemester executionSemester) {
        final List<Shift> result = new ArrayList<Shift>();
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                result.add(shift);
            }
        }
        return result;
    }

    final public List<Shift> getShiftsFor(final ExecutionCourse executionCourse) {
        final List<Shift> result = new ArrayList<Shift>();
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse() == executionCourse) {
                result.add(shift);
            }
        }
        return result;
    }

    final public Shift getShiftFor(final ExecutionCourse executionCourse, final ShiftType shiftType) {
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse() == executionCourse && shift.hasShiftType(shiftType)) {
                return shift;
            }
        }
        return null;
    }

    private int countNumberOfDistinctExecutionCoursesOfShiftsFor(final ExecutionSemester executionSemester) {
        final Set<ExecutionCourse> result = new HashSet<ExecutionCourse>();
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                result.add(shift.getExecutionCourse());
            }
        }
        return result.size();
    }

    /**
     * @deprecated method is never used... delete it
     */
    @Deprecated
    final public Integer getNumberOfExecutionCoursesWithEnroledShiftsFor(final ExecutionSemester executionSemester) {
        return getAttendingExecutionCoursesFor(executionSemester).size()
                - countNumberOfDistinctExecutionCoursesOfShiftsFor(executionSemester);
    }

    final public Integer getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(final ExecutionSemester executionSemester) {
        int result = 0;
        final List<Shift> enroledShifts = getShiftsFor(executionSemester);
        for (final ExecutionCourse executionCourse : getAttendingExecutionCoursesFor(executionSemester)) {
            for (final ShiftType shiftType : executionCourse.getOldShiftTypesToEnrol()) {
                if (!enroledShiftsContainsShiftWithSameExecutionCourseAndShiftType(enroledShifts, executionCourse, shiftType)) {
                    result++;
                    break;
                }
            }
        }
        return Integer.valueOf(result);
    }

    private boolean enroledShiftsContainsShiftWithSameExecutionCourseAndShiftType(final List<Shift> enroledShifts,
            final ExecutionCourse executionCourse, final ShiftType shiftType) {
        return enroledShifts.stream().anyMatch(
                enroledShift -> enroledShift.getExecutionCourse() == executionCourse && enroledShift.containsType(shiftType));
    }

    final public Set<SchoolClass> getSchoolClassesToEnrol() {
        final Set<SchoolClass> result = new HashSet<SchoolClass>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            final ExecutionCourse executionCourse = attends.getExecutionCourse();

            if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                result.addAll(getSchoolClassesToEnrolBy(executionCourse));
            }
        }
        return result;
    }

    final public Set<SchoolClass> getSchoolClassesToEnrolBy(final ExecutionCourse executionCourse) {
        StudentCurricularPlan scp = getActiveStudentCurricularPlan();
        Set<SchoolClass> schoolClasses = scp != null ? executionCourse
                .getSchoolClassesBy(scp.getDegreeCurricularPlan()) : new HashSet<SchoolClass>();
        return schoolClasses.isEmpty() ? executionCourse.getSchoolClasses() : schoolClasses;
    }

    public void addAttendsTo(final ExecutionCourse executionCourse) {

        checkIfReachedAttendsLimit();

        if (getStudent().readAttendByExecutionCourse(executionCourse) == null) {
            final Enrolment enrolment =
                    findEnrolment(getActiveStudentCurricularPlan(), executionCourse, executionCourse.getExecutionPeriod());
            if (enrolment != null) {
                enrolment.createAttends(this, executionCourse);
            } else {
                Attends attends = getAttendsForExecutionCourse(executionCourse);
                if (attends != null) {
                    attends.delete();
                }
                new Attends(this, executionCourse);
            }
        }
    }

    private Attends getAttendsForExecutionCourse(ExecutionCourse executionCourse) {
        List<Attends> attendsInExecutionPeriod = getAttendsForExecutionPeriod(executionCourse.getExecutionPeriod());
        for (Attends attends : attendsInExecutionPeriod) {
            for (CurricularCourse curricularCourse : attends.getExecutionCourse().getAssociatedCurricularCoursesSet()) {
                if (executionCourse.getAssociatedCurricularCoursesSet().contains(curricularCourse)) {
                    return attends;
                }
            }
        }
        return null;
    }

    private Enrolment findEnrolment(final StudentCurricularPlan studentCurricularPlan, final ExecutionCourse executionCourse,
            final ExecutionSemester executionSemester) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final Enrolment enrolment =
                    studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionSemester);
            if (enrolment != null) {
                return enrolment;
            }
        }
        return null;
    }

    private static final int MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD = 10;

    // ;

    private void checkIfReachedAttendsLimit() {
        final User userView = Authenticate.getUser();
        if (userView == null
                || !AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, getDegree(),
                        userView.getPerson().getUser())) {
            if (readAttendsInCurrentExecutionPeriod().size() >= MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD) {
                throw new DomainException("error.student.reached.attends.limit",
                        String.valueOf(MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD));
            }
        }
    }

    @Atomic
    final public void removeAttendFor(final ExecutionCourse executionCourse) {
        final Attends attend = readRegistrationAttendByExecutionCourse(executionCourse);
        if (attend != null) {
            checkIfHasEnrolmentFor(attend);
            checkIfHasShiftsFor(executionCourse);
            attend.delete();
        }
    }

    public void checkIfHasEnrolmentFor(final Attends attend) {
        if (attend.getEnrolment() != null) {
            throw new DomainException("errors.student.already.enroled");
        }
    }

    public void checkIfHasShiftsFor(final ExecutionCourse executionCourse) {
        if (!getShiftsFor(executionCourse).isEmpty()) {
            throw new DomainException("errors.student.already.enroled.in.shift");
        }
    }

    @Override
    final public Integer getNumber() {
        return (super.getNumber() != null) ? super.getNumber() : getStudent().getNumber();
    }

    final public Person getPerson() {
        return getStudent().getPerson();
    }

    final public String getName() {
        return getPerson().getName();
    }

    final public String getEmail() {
        return getPerson().getEmail();
    }

    final public Double getEntryGrade() {
        return getStudentCandidacy() != null ? getStudentCandidacy().getEntryGrade() : null;
    }

    final public void setEntryGrade(Double entryGrade) {
        if (getStudentCandidacy() != null) {
            getStudentCandidacy().setEntryGrade(entryGrade);
        } else {
            throw new DomainException("error.registration.withou.student.candidacy");
        }
    }

    final public String getPrecedentDegreeConclusionGrade(final SchoolLevelType levelType) {
        return hasPrecedentDegreeInformation(levelType) ? getPrecedentDegreeInformation(levelType).getConclusionGrade() : null;
    }

    public boolean hasPrecedentDegreeInformation(SchoolLevelType levelType) {
        return getPrecedentDegreeInformation(levelType) != null;
    }

    public PrecedentDegreeInformation getPrecedentDegreeInformation(final SchoolLevelType levelType) {
        return (super.getPrecedentDegreeInformation() != null && super.getPrecedentDegreeInformation().getSchoolLevel() == levelType) ? super
                .getPrecedentDegreeInformation() : null;
    }

    public boolean isFirstCycleAtributionIngression() {
        return getIngressionType() != null && getIngressionType().isFirstCycleAttribution();
    }

    public boolean isSecondCycleInternalCandidacyIngression() {
        return getIngressionType() != null && getIngressionType().isInternal2ndCycleAccess();
    }

    @Override
    public void setIngressionType(IngressionType ingressionType) {
        checkIngressionType(ingressionType);
        super.setIngressionType(ingressionType);
    }

    private void checkIngressionType(final IngressionType ingressionType) {
        checkIngression(ingressionType, getPerson(), getFirstStudentCurricularPlan().getDegreeCurricularPlan());
    }

    public static void checkIngression(IngressionType ingressionType, Person person, DegreeCurricularPlan degreeCurricularPlan) {
        if (ingressionType!=null && ingressionType.isReIngression()) {
            if (person == null || person.getStudent() == null) {
                throw new DomainException("error.registration.preBolonhaSourceDegreeNotFound");
            }
            if (degreeCurricularPlan.getEquivalencePlan() != null) {
                final Student student = person.getStudent();
                final Degree sourceDegree = degreeCurricularPlan.getEquivalencePlan().getSourceDegreeCurricularPlan().getDegree();

                Registration sourceRegistration = person.getStudent().readRegistrationByDegree(sourceDegree);
                if (sourceRegistration == null) {
                    final Collection<Registration> registrations =
                            student.getRegistrationsMatchingDegreeType(DegreeType::isPreBolonhaDegree);
                    registrations.removeAll(student.getRegistrationsFor(degreeCurricularPlan));
                    sourceRegistration = registrations.size() == 1 ? registrations.iterator().next() : null;
                }

                if (sourceRegistration == null) {
                    throw new DomainException("error.registration.preBolonhaSourceDegreeNotFound");
                } else if (!sourceRegistration.getActiveStateType().canReingress()) {
                    throw new DomainException("error.registration.preBolonhaSourceRegistrationCannotReingress");
                }
            }
        }
    }

    final public ExecutionYear getIngressionYear() {
        return calculateIngressionYear();
    }

    public ExecutionYear calculateIngressionYear() {
        return inspectIngressionYear(this);
    }

    private ExecutionYear inspectIngressionYear(final Registration registration) {
        if (registration.getSourceRegistration() == null) {
            return registration.getStartExecutionYear();
        }

        return inspectIngressionYear(registration.getSourceRegistration());
    }

    final public String getContigent() {
        return getStudentCandidacy() != null ? getStudentCandidacy().getContigent() : null;
    }

    public String getDegreeNameWithDegreeCurricularPlanName() {
        final StudentCurricularPlan toAsk =
                getStudentCurricularPlan(getStartExecutionYear()) == null ? getFirstStudentCurricularPlan() : getStudentCurricularPlan(getStartExecutionYear());

        if (toAsk == null) {
            return StringUtils.EMPTY;
        }

        return toAsk.getPresentationName(getStartExecutionYear());
    }

    public String getDegreeNameWithDescription() {
        return getDegree().getPresentationName(getStartExecutionYear());
    }

    public String getDegreeName() {
        return getDegree().getNameFor(getStartExecutionYear()).getContent();
    }

    final public String getDegreeDescription() {
        final DegreeType degreeType = getDegreeType();
        return getDegreeDescription(degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : getLastConcludedCycleType());
    }

    @Deprecated
    final public String getDegreeDescription(final CycleType cycleType) {
        return getDegreeDescription(cycleType, I18N.getLocale());
    }

    @Deprecated
    final public String getDegreeDescription(ExecutionYear executionYear, final CycleType cycleType) {
        return getDegreeDescription(executionYear, cycleType, I18N.getLocale());
    }

    @Deprecated
    final public String getDegreeDescription(final CycleType cycleType, final Locale locale) {
        return getDegreeDescription(this.getStartExecutionYear(), cycleType, locale);
    }

    final public String getDegreeDescription(ExecutionYear executionYear, ProgramConclusion programConclusion, final Locale locale) {
        final StringBuilder res = new StringBuilder();

        final Degree degree = getDegree();
        final DegreeType degreeType = degree.getDegreeType();
        if (programConclusion != null && !programConclusion.isTerminal()
                && !Strings.isNullOrEmpty(programConclusion.getName().getContent(locale))) {
            res.append(programConclusion.getName().getContent(locale));
            res.append(", ").append(BundleUtil.getString(Bundle.ACADEMIC, locale, "label.of.the.male")).append(" ");
        }

        // the degree type description is always given by the program conclusion of a course group matching available degree cycle types
        // if no cycle types available, choose any program conclusion
        if (programConclusion == null) {
            programConclusion =
                    degreeType.getCycleTypes().stream()
                            .map(cycleType -> getLastStudentCurricularPlan().getCycleCourseGroup(cycleType))
                            .filter(Objects::nonNull).map(CycleCourseGroup::getProgramConclusion).filter(Objects::nonNull)
                            .findAny().orElseGet(() -> ProgramConclusion.conclusionsFor(this).findAny().orElse(null));
        }

        if (!isEmptyDegree() && !degreeType.isEmpty()) {
            res.append(degreeType.getPrefix(locale));
            res.append(" ");
            if (programConclusion != null && !Strings.isNullOrEmpty(programConclusion.getDescription().getContent(locale))) {
                res.append(programConclusion.getDescription().getContent(locale).toUpperCase());
                res.append(" ").append(BundleUtil.getString(Bundle.ACADEMIC, locale, "label.in")).append(" ");
            }
        }

        res.append(degree.getFilteredName(executionYear, locale).toUpperCase());

        return res.toString();
    }

    @Deprecated
    final public String getDegreeDescription(ExecutionYear executionYear, final CycleType cycleType, final Locale locale) {
        CycleCourseGroup cycleCourseGroup = getLastStudentCurricularPlan().getCycleCourseGroup(cycleType);
        ProgramConclusion programConclusion = cycleCourseGroup != null ? cycleCourseGroup.getProgramConclusion() : null;

        return getDegreeDescription(executionYear, programConclusion, locale);
    }

    public String getDegreeCurricularPlanName() {
        return getLastDegreeCurricularPlan().getName();
    }

    @Override
    final public Degree getDegree() {
        return super.getDegree() != null ? super.getDegree() : (!getStudentCurricularPlansSet().isEmpty() ? getLastStudentCurricularPlan()
                .getDegree() : null);
    }

    final public DegreeType getDegreeType() {
        return getDegree() == null ? null : getDegree().getDegreeType();
    }

    final public boolean isBolonha() {
        DegreeType degreeType = getDegreeType();
        return degreeType != null && degreeType.isBolonhaType();
    }

    final public boolean isActiveForOffice(Unit office) {
        return isActive() && isForOffice(office.getAdministrativeOffice());
    }

    @Deprecated
    public boolean isDegreeAdministrativeOffice() {
        return getDegree().getAdministrativeOffice().getAdministrativeOfficeType() == AdministrativeOfficeType.DEGREE;
    }

    final public boolean isForOffice(final AdministrativeOffice administrativeOffice) {
        return getDegree().getAdministrativeOffice().equals(administrativeOffice);
    }

    final public boolean isAllowedToManageRegistration() {
		final Degree degree = getDegree();
		final User user = Authenticate.getUser();
		return AcademicAccessRule.getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_REGISTRATIONS, user)
				.anyMatch(ap -> ap == degree)
				|| AcademicAccessRule
						.getProgramsAccessibleToFunction(AcademicOperationType.VIEW_FULL_STUDENT_CURRICULUM, user)
						.anyMatch(ap -> ap == degree);
    }

    public boolean isCurricularCourseApproved(final CurricularCourse curricularCourse) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
                return true;
            }
        }
        return false;
    }

    final public Set<RegistrationStateType> getRegistrationStatesTypes(final ExecutionYear executionYear) {
        final Set<RegistrationStateType> result = new HashSet<RegistrationStateType>();

        for (final RegistrationState registrationState : getRegistrationStates(executionYear)) {
            result.add(registrationState.getStateType());
        }

        return result;
    }

    final public Set<RegistrationStateType> getRegistrationStatesTypes(final ExecutionSemester executionSemester) {
        final Set<RegistrationStateType> result = new HashSet<RegistrationStateType>();

        for (final RegistrationState registrationState : getRegistrationStates(executionSemester)) {
            result.add(registrationState.getStateType());
        }

        return result;
    }

    public boolean isRegistered(final DateTime when) {
        final RegistrationState stateInDate = getStateInDate(when);
        return (stateInDate != null && stateInDate.isActive()) || hasAnyEnrolmentsIn(ExecutionSemester.readByDateTime(when));
    }

    public boolean isRegistered(final ExecutionSemester executionSemester) {
        return hasAnyActiveState(executionSemester) || hasAnyEnrolmentsIn(executionSemester);
    }

    final public boolean isRegistered(final ExecutionYear executionYear) {
        return hasAnyActiveState(executionYear) || hasAnyEnrolmentsIn(executionYear);
    }

    final public RegistrationState getActiveState() {
        if (!getRegistrationStatesSet().isEmpty()) {
            RegistrationState activeState = null;
            for (RegistrationState state : getRegistrationStatesSet()) {
                if (!state.getStateDate().isAfterNow()) {
                    if (activeState == null || RegistrationState.DATE_COMPARATOR.compare(activeState, state) < 0) {
                        activeState = state;
                    }
                }
            }
            return activeState;
        } else {
            return null;
        }
    }

    public RegistrationState getLastState() {
        RegistrationState result = null;
        for (final RegistrationState state : getRegistrationStatesSet()) {
            if (result == null || state.getStateDate().isAfter(result.getStateDate())) {
                result = state;
            }
        }
        return result;
    }

    public RegistrationStateType getLastStateType() {
        final RegistrationState registrationState = getLastState();
        return registrationState == null ? null : registrationState.getStateType();
    }

    final public RegistrationState getFirstState() {
        return getFirstRegistrationState();
    }

    final public RegistrationStateType getActiveStateType() {
        final RegistrationState activeState = getActiveState();
        return activeState != null ? activeState.getStateType() : RegistrationStateType.REGISTERED;
    }

    final public boolean isActive() {
        return getActiveStateType().isActive();
    }

    public boolean hasAnyActiveState(final ExecutionSemester executionSemester) {
        for (RegistrationState registrationState : getRegistrationStates(executionSemester)) {
            if (registrationState.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyActiveState(final ExecutionYear executionYear) {
        for (RegistrationState registrationState : getRegistrationStates(executionYear)) {
            if (registrationState.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveFirstState(final ExecutionYear period) {
        final Set<RegistrationState> states = getRegistrationStates(period);
        return states.isEmpty() ? false : Collections.min(states, RegistrationState.DATE_COMPARATOR).isActive();
    }

    public boolean hasActiveLastState(final ExecutionSemester executionSemester) {
        final Set<RegistrationState> states = getRegistrationStates(executionSemester);
        return states.isEmpty() ? false : Collections.max(states, RegistrationState.DATE_COMPARATOR).isActive();
    }

    public boolean hasRegistrationState(final RegistrationStateType stateType) {
        for (final RegistrationState state : getRegistrationStatesSet()) {
            if (state.getStateType() == stateType) {
                return true;
            }
        }

        return false;
    }

    final public boolean isInRegisteredState() {
        return getActiveStateType() == RegistrationStateType.REGISTERED;
    }

    final public boolean isInternalAbandon() {
        return getActiveStateType() == RegistrationStateType.INTERNAL_ABANDON;
    }

    final public boolean getInterruptedStudies() {
        return isInterrupted();
    }

    public boolean isInterrupted() {
        return getActiveStateType() == RegistrationStateType.INTERRUPTED;
    }

    final public boolean getFlunked() {
        return isFlunked();
    }

    public boolean isFlunked() {
        return getActiveStateType() == RegistrationStateType.FLUNKED;
    }

    final public boolean isInMobilityState() {
        return getActiveStateType() == RegistrationStateType.MOBILITY;
    }

    public boolean isSchoolPartConcluded() {
        return getActiveStateType() == RegistrationStateType.SCHOOLPARTCONCLUDED;
    }

    public boolean isConcluded() {
        return getActiveStateType() == RegistrationStateType.CONCLUDED;
    }

    public boolean isTransited() {
        return getActiveStateType() == RegistrationStateType.TRANSITED;
    }

    public boolean isCanceled() {
        return getActiveStateType() == RegistrationStateType.CANCELED;
    }

    final public boolean isTransited(final DateTime when) {
        final RegistrationState stateInDate = getStateInDate(when);
        return stateInDate != null && stateInDate.getStateType() == RegistrationStateType.TRANSITED;
    }

    final public boolean isTransited(final ExecutionYear executionYear) {
        return hasStateType(executionYear, RegistrationStateType.TRANSITED);
    }

    final public boolean isTransition() {
        return getActiveStateType() == RegistrationStateType.TRANSITION;
    }

    final public boolean isTransition(final ExecutionYear executionYear) {
        return hasStateType(executionYear, RegistrationStateType.TRANSITION);
    }

    final public boolean getWasTransition() {
        return hasState(RegistrationStateType.TRANSITION);
    }

    final public RegistrationState getStateInDate(DateTime dateTime) {

        List<RegistrationState> sortedRegistrationStates = new ArrayList<RegistrationState>(getRegistrationStatesSet());
        Collections.sort(sortedRegistrationStates, RegistrationState.DATE_COMPARATOR);

        for (ListIterator<RegistrationState> iterator = sortedRegistrationStates.listIterator(sortedRegistrationStates.size()); iterator
                .hasPrevious();) {

            RegistrationState registrationState = iterator.previous();
            if (!dateTime.isBefore(registrationState.getStateDate())) {
                return registrationState;
            }
        }

        return null;
    }

    final public RegistrationState getStateInDate(final LocalDate localDate) {
        final List<RegistrationState> sortedRegistrationStates = new ArrayList<RegistrationState>(getRegistrationStatesSet());
        Collections.sort(sortedRegistrationStates, RegistrationState.DATE_COMPARATOR);

        for (ListIterator<RegistrationState> iterator = sortedRegistrationStates.listIterator(sortedRegistrationStates.size()); iterator
                .hasPrevious();) {

            RegistrationState registrationState = iterator.previous();
            if (!localDate.isBefore(registrationState.getStateDate().toLocalDate())) {
                return registrationState;
            }
        }

        return null;
    }

    public Set<RegistrationState> getRegistrationStates(final ExecutionYear executionYear) {
        return getRegistrationStates(executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight(), executionYear
                .getEndDateYearMonthDay().toDateTimeAtMidnight());
    }

    public Set<RegistrationState> getRegistrationStates(final ExecutionSemester executionSemester) {
        return getRegistrationStates(executionSemester.getBeginDateYearMonthDay().toDateTimeAtMidnight(), executionSemester
                .getEndDateYearMonthDay().toDateTimeAtMidnight());
    }

    public Set<RegistrationState> getRegistrationStates(final ReadableInstant beginDateTime, final ReadableInstant endDateTime) {
        final Set<RegistrationState> result = new HashSet<RegistrationState>();
        populateRegistrationStates(beginDateTime, endDateTime, result);
        return result;
    }

    public List<RegistrationState> getRegistrationStatesList(final ExecutionYear executionYear) {
        return getRegistrationStatesList(executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight(), executionYear
                .getEndDateYearMonthDay().toDateTimeAtMidnight());
    }

    public List<RegistrationState> getRegistrationStatesList(final ExecutionSemester executionSemester) {
        return getRegistrationStatesList(executionSemester.getBeginDateYearMonthDay().toDateTimeAtMidnight(), executionSemester
                .getEndDateYearMonthDay().toDateTimeAtMidnight());
    }

    public List<RegistrationState> getRegistrationStatesList(final ReadableInstant beginDateTime,
            final ReadableInstant endDateTime) {
        final List<RegistrationState> result = new ArrayList<RegistrationState>();
        populateRegistrationStates(beginDateTime, endDateTime, result);
        return result;
    }

    private void populateRegistrationStates(final ReadableInstant beginDateTime, final ReadableInstant endDateTime,
            final Collection<RegistrationState> result) {
        List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(getRegistrationStatesSet());
        Collections.sort(sortedRegistrationsStates, RegistrationState.DATE_COMPARATOR);

        for (ListIterator<RegistrationState> iter = sortedRegistrationsStates.listIterator(sortedRegistrationsStates.size()); iter
                .hasPrevious();) {
            RegistrationState state = iter.previous();

            if (state.getStateDate().isAfter(endDateTime)) {
                continue;
            }

            result.add(state);

            if (!state.getStateDate().isAfter(beginDateTime)) {
                break;
            }

        }
    }

    public RegistrationState getFirstRegistrationState() {
        return getRegistrationStatesSet().stream().min(RegistrationState.DATE_COMPARATOR).orElse(null);
    }

    final public RegistrationState getLastRegistrationState(final ExecutionYear executionYear) {
        return getRegistrationStatesSet().stream().sorted(RegistrationState.DATE_COMPARATOR.reversed()).filter(state -> !state
                .getStateDate()
                .isAfter(executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight())).findFirst().orElse(null);
    }

    public boolean hasState(final RegistrationStateType stateType) {
        return hasAnyState(Collections.singletonList(stateType));
    }

    public boolean hasAnyState(final Collection<RegistrationStateType> stateTypes) {
        for (final RegistrationState registrationState : getRegistrationStatesSet()) {
            if (stateTypes.contains(registrationState.getStateType())) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasStateType(final ExecutionSemester executionSemester, final RegistrationStateType registrationStateType) {
        return getRegistrationStatesTypes(executionSemester).contains(registrationStateType);
    }

    final public boolean hasStateType(final ExecutionYear executionYear, final RegistrationStateType registrationStateType) {
        return getRegistrationStatesTypes(executionYear).contains(registrationStateType);
    }

    public boolean hasFlunkedState(final ExecutionYear executionYear) {
        return hasStateType(executionYear, RegistrationStateType.FLUNKED);
    }

    public boolean hasRegisteredActiveState() {
        return getActiveStateType() == RegistrationStateType.REGISTERED;
    }

    public Collection<RegistrationState> getRegistrationStates(final RegistrationStateType registrationStateType) {
        return getRegistrationStates(Collections.singletonList(registrationStateType));
    }

    public Collection<RegistrationState> getRegistrationStates(final Collection<RegistrationStateType> registrationStateTypes) {
        final Collection<RegistrationState> result = new HashSet<RegistrationState>();
        for (final RegistrationState registrationState : getRegistrationStatesSet()) {
            if (registrationStateTypes.contains(registrationState.getStateType())) {
                result.add(registrationState);
            }
        }
        return result;
    }

    final public double getEctsCredits() {
        return calculateCredits();
    }

    public double calculateCredits() {
        return getTotalEctsCredits((ExecutionYear) null).doubleValue();
    }

    final public BigDecimal getTotalEctsCredits(final ExecutionYear executionYear) {
        return getCurriculum(executionYear).getSumEctsCredits();
    }

    public double getEnrolmentsEcts(final ExecutionYear executionYear) {
        return getLastStudentCurricularPlan().getEnrolmentsEctsCredits(executionYear);
    }

    final public int getCurricularYear() {
        return getCurricularYear(ExecutionYear.readCurrentExecutionYear());
    }

    final public int getCurricularYear(ExecutionYear executionYear) {
        return getCurriculum(executionYear).getCurricularYear();
    }

    final public int getCurricularYear(final DateTime when, final ExecutionYear executionYear) {
        return getCurriculum(when, executionYear, (CycleType) null).getCurricularYear();
    }

    final public Person getConclusionProcessResponsible() {
        return isRegistrationConclusionProcessed() ? getConclusionProcess().getResponsible() : null;
    }

    final public Person getConclusionProcessLastResponsible() {
        return isRegistrationConclusionProcessed() ? getConclusionProcess().getLastResponsible() : null;
    }

    public boolean isRegistrationConclusionProcessed() {
        return getLastStudentCurricularPlan().isConclusionProcessed();
    }

    public boolean isQualifiedToRegistrationConclusionProcess() {
        return isActive() || isConcluded() || isSchoolPartConcluded() || isTransited();
    }

    public ExecutionYear calculateConclusionYear() {
        ExecutionYear result = getLastApprovementExecutionYear();

        if (result == null) {
            if (hasState(RegistrationStateType.CONCLUDED)) {
                return getFirstRegistrationState(RegistrationStateType.CONCLUDED).getExecutionYear();

            } else if (isOldMasterDegree() && hasState(RegistrationStateType.SCHOOLPARTCONCLUDED)) {
                return getFirstRegistrationState(RegistrationStateType.SCHOOLPARTCONCLUDED).getExecutionYear();
            }
        }

        return result;
    }

    private RegistrationState getFirstRegistrationState(final RegistrationStateType stateType) {
        final SortedSet<RegistrationState> states = new TreeSet<RegistrationState>(RegistrationState.DATE_COMPARATOR);
        states.addAll(getRegistrationStates(stateType));
        return states.first();
    }

    private boolean isOldMasterDegree() {
        return getDegreeType().isPreBolonhaMasterDegree();
    }

    public YearMonthDay getConclusionDate() {
        return ProgramConclusion.getConclusionProcess(getLastStudentCurricularPlan())
                .map(ConclusionProcess::getConclusionYearMonthDay).orElse(null);
    }

    @Deprecated
    public YearMonthDay getConclusionDateForBolonha() {
        return getConclusionDate();
    }

    final public YearMonthDay getConclusionDate(final CycleType cycleType) {
        if (!getDegreeType().hasAnyCycleTypes()) {
            return getConclusionDate();
        }

        if (!hasConcludedCycle(cycleType)) {
            throw new DomainException("Registration.hasnt.finished.given.cycle");
        }

        final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
        if (lastStudentCurricularPlan == null) {
            throw new DomainException("Registration.has.no.student.curricular.plan");
        }

        return lastStudentCurricularPlan.getConclusionDate(cycleType);
    }

    public YearMonthDay calculateConclusionDate() {
        return getLastStudentCurricularPlan().getLastApprovementDate();
    }

    public YearMonthDay calculateConclusionDate(final CycleType cycleType) {
        if (!getDegreeType().hasAnyCycleTypes()) {
            return calculateConclusionDate();
        }

        if (!hasConcludedCycle(cycleType)) {
            throw new DomainException("Registration.hasnt.finished.given.cycle");
        }

        final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
        if (lastStudentCurricularPlan == null) {
            throw new DomainException("Registration.has.no.student.curricular.plan");
        }

        return lastStudentCurricularPlan.calculateConclusionDate(cycleType);
    }

    final public String getConclusionProcessNotes() {
        return isRegistrationConclusionProcessed() ? getConclusionProcess().getNotes() : null;
    }

    final public DateTime getConclusionProcessCreationDateTime() {
        return isRegistrationConclusionProcessed() ? getConclusionProcess().getCreationDateTime() : null;
    }

    final public DateTime getConclusionProcessLastModificationDateTime() {
        return isRegistrationConclusionProcessed() ? getConclusionProcess().getLastModificationDateTime() : null;
    }

    final public String getGraduateTitle(final ProgramConclusion programConclusion, final Locale locale) {
        if (programConclusion.isConclusionProcessed(this)) {
           return programConclusion.groupFor(this).map(cg -> cg.getDegreeModule().getGraduateTitle(cg.getConclusionYear(), locale)).orElse(null);
        }
        throw new DomainException("Registration.hasnt.concluded.requested.cycle");
    }

    final public boolean hasConcludedFirstCycle() {
        return hasConcludedCycle(CycleType.FIRST_CYCLE);
    }

    final public boolean hasConcludedSecondCycle() {
        return hasConcludedCycle(CycleType.SECOND_CYCLE);
    }

    final public boolean hasConcludedCycle(final CycleType cycleType) {
        return getLastStudentCurricularPlan().hasConcludedCycle(cycleType);
    }

    final public boolean hasConcludedCycle(final CycleType cycleType, final ExecutionYear executionYear) {
        return getLastStudentCurricularPlan().hasConcludedCycle(cycleType, executionYear);
    }

    public boolean hasConcluded() {
        final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
        return lastStudentCurricularPlan.isConcluded();
    }

    public boolean getHasConcluded() {
        return hasConcluded();
    }

    final public Collection<CycleType> getConcludedCycles() {
        if (!getDegreeType().hasAnyCycleTypes()) {
            return Collections.EMPTY_SET;
        }

        final Collection<CycleType> result = new TreeSet<CycleType>(CycleType.COMPARATOR_BY_LESS_WEIGHT);

        for (final CycleType cycleType : getDegreeType().getCycleTypes()) {
            if (hasConcludedCycle(cycleType)) {
                result.add(cycleType);
            }
        }

        return result;
    }

    final public Collection<CycleCurriculumGroup> getConclusionProcessedCycles(final ExecutionYear executionYear) {
        final Collection<CycleCurriculumGroup> result = new HashSet<CycleCurriculumGroup>();

        for (final CycleCurriculumGroup group : getLastStudentCurricularPlan().getInternalCycleCurriculumGrops()) {
            if (group.isConclusionProcessed() && group.getConclusionYear() == executionYear) {
                result.add(group);
            }
        }

        return result;
    }

    final public Collection<CycleType> getConcludedCycles(final ExecutionYear executionYear) {
        if (!getDegreeType().hasAnyCycleTypes()) {
            return Collections.emptySet();
        }

        final Collection<CycleType> result = new TreeSet<CycleType>(CycleType.COMPARATOR_BY_LESS_WEIGHT);

        for (final CycleType cycleType : getDegreeType().getCycleTypes()) {
            if (hasConcludedCycle(cycleType, executionYear)) {
                result.add(cycleType);
            }
        }

        return result;
    }

    final public CycleType getCurrentCycleType() {
        return getCycleType(ExecutionYear.readCurrentExecutionYear());
    }

    final public CycleType getCycleType(final ExecutionYear executionYear) {
        if (!isBolonha() || isEmptyDegree() || getDegreeType().isEmpty()) {
            return null;
        }

        final SortedSet<CycleType> concludedCycles = new TreeSet<CycleType>(getConcludedCycles(executionYear));

        if (concludedCycles.isEmpty()) {
            CycleCurriculumGroup cycleGroup = getLastStudentCurricularPlan().getFirstOrderedCycleCurriculumGroup();
            return cycleGroup != null ? cycleGroup.getCycleType() : null;
        } else {
            CycleType result = null;
            for (CycleType cycleType : concludedCycles) {
                final CycleCurriculumGroup group = getLastStudentCurricularPlan().getCycle(cycleType);
                if (group.hasEnrolment(executionYear)) {
                    result = cycleType;
                }
            }

            if (result != null) {
                return result;
            }

            final CycleType last = concludedCycles.last();
            return last.hasNext() && getDegreeType().hasCycleTypes(last.getNext()) ? last.getNext() : last;
        }
    }

    private boolean isEmptyDegree() {
        return (getLastStudentCurricularPlan() != null ? getLastStudentCurricularPlan().isEmpty() : true);
    }

    final public CycleType getLastConcludedCycleType() {
        final SortedSet<CycleType> concludedCycles = new TreeSet<CycleType>(getConcludedCycles());
        return concludedCycles.isEmpty() ? null : concludedCycles.last();
    }

    public boolean canRepeatConclusionProcess(Person person) {
        return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.REPEAT_CONCLUSION_PROCESS, getDegree(),
                person.getUser());
    }

    public void conclude(final CurriculumGroup curriculumGroup) {
        check(this, RegistrationPredicates.MANAGE_CONCLUSION_PROCESS);

        if (curriculumGroup == null || getStudentCurricularPlansSet().stream().noneMatch(scp -> scp.hasCurriculumModule
                (curriculumGroup))) {
            throw new DomainException("error.Registration.invalid.cycleCurriculumGroup");
        }

        curriculumGroup.conclude();

        ProgramConclusion conclusion = curriculumGroup.getDegreeModule().getProgramConclusion();

        if (conclusion != null && conclusion.getTargetState() != null
                && !conclusion.getTargetState().equals(getActiveStateType())) {
            RegistrationState.createRegistrationState(this, AccessControl.getPerson(), new DateTime(),
                    conclusion.getTargetState());
        }

    }

    final public boolean hasApprovement(ExecutionYear executionYear) {
        int curricularYearInTheBegin = getCurricularYear(executionYear);
        int curricularYearAtTheEnd = getCurricularYear(executionYear.getNextExecutionYear());

        if (curricularYearInTheBegin > curricularYearAtTheEnd) {
            throw new DomainException("Registration.curricular.year.has.decreased");
        }

        return curricularYearAtTheEnd > curricularYearInTheBegin;
    }

    final public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
        return getDegreeType().isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
    }

    final public boolean isMasterDegreeOrBolonhaMasterDegree() {
        final DegreeType degreeType = getDegreeType();
        return (degreeType.isPreBolonhaMasterDegree() || degreeType.isBolonhaMasterDegree());
    }

    final public boolean isDEA() {
        return getDegreeType().isAdvancedSpecializationDiploma();
    }

    final public EnrolmentModel getEnrolmentModelForCurrentExecutionYear() {
        return getEnrolmentModelForExecutionYear(ExecutionYear.readCurrentExecutionYear());
    }

    final public EnrolmentModel getEnrolmentModelForExecutionYear(ExecutionYear year) {
        RegistrationDataByExecutionYear registrationData = getRegistrationDataByExecutionYear(year);
        return registrationData != null ? registrationData.getEnrolmentModel() : null;
    }

    final public void setEnrolmentModelForCurrentExecutionYear(EnrolmentModel model) {
        setEnrolmentModelForExecutionYear(ExecutionYear.readCurrentExecutionYear(), model);
    }

    final public void setEnrolmentModelForExecutionYear(ExecutionYear year, EnrolmentModel model) {
        RegistrationDataByExecutionYear registrationData =
                RegistrationDataByExecutionYear.getOrCreateRegistrationDataByYear(this, year);
        registrationData.setEnrolmentModel(model);
    }

    private RegistrationDataByExecutionYear getRegistrationDataByExecutionYear(ExecutionYear year) {
        for (RegistrationDataByExecutionYear registrationData : getRegistrationDataByExecutionYearSet()) {
            if (registrationData.getExecutionYear().equals(year)) {
                return registrationData;
            }
        }
        return null;
    }

    @Override
    final public ExecutionYear getRegistrationYear() {
        return super.getRegistrationYear() == null ? getFirstEnrolmentExecutionYear() : super.getRegistrationYear();
    }

    final public boolean isFirstTime(ExecutionYear executionYear) {
        return getRegistrationYear() == executionYear;
    }

    final public boolean isFirstTime() {
        return isFirstTime(ExecutionYear.readCurrentExecutionYear());
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final ExecutionYear executionYear) {
        return executionYear == null ? getStudentCurricularPlan(new YearMonthDay()) : getStudentCurricularPlan(executionYear
                .getEndDateYearMonthDay());
    }

    final public StudentCurricularPlan getStudentCurricularPlanForCurrentExecutionYear() {
        return getStudentCurricularPlan(ExecutionYear.readCurrentExecutionYear());
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final ExecutionSemester executionSemester) {
        return executionSemester == null ? getStudentCurricularPlan(new YearMonthDay()) : getStudentCurricularPlan(executionSemester
                .getEndDateYearMonthDay());
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final YearMonthDay date) {
        StudentCurricularPlan result = null;
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            final YearMonthDay startDate = studentCurricularPlan.getStartDateYearMonthDay();
            if (!startDate.isAfter(date) && (result == null || startDate.isAfter(result.getStartDateYearMonthDay()))) {
                result = studentCurricularPlan;
            }
        }
        return result;
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
                return studentCurricularPlan;
            }
        }
        return null;
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final CycleType cycleType) {
        if (cycleType == null) {
            return getLastStudentCurricularPlan();
        }
        return getStudentCurricularPlansSet().stream()
                .filter(scp -> scp.getRoot().getCycleCurriculumGroup(cycleType) != null)
                .max(StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE)
                .orElse(getLastStudentCurricularPlan());
    }

    final public Set<DegreeCurricularPlan> getDegreeCurricularPlans() {
        Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            result.add(studentCurricularPlan.getDegreeCurricularPlan());
        }
        return result;
    }

    @Override
    final public YearMonthDay getStartDate() {

        if (super.getStartDate() != null) {
            return super.getStartDate();
        }

        if (getStudentCandidacy() != null) {
            return getStudentCandidacy().getActiveCandidacySituation().getSituationDate().toYearMonthDay();
        }

        if (getRegistrationYear() != null) {
            return getRegistrationYear().getBeginDateYearMonthDay();
        }

        return null;
    }

    final public ExecutionYear getStartExecutionYear() {
        return ExecutionYear.readByDateTime(getStartDate().toDateTimeAtMidnight());
    }

    final public boolean hasStartedBeforeFirstBolonhaExecutionYear() {
        return getStartExecutionYear().isBefore(ExecutionYear.readFirstBolonhaExecutionYear());
    }

    final public boolean hasStudentCurricularPlanInExecutionPeriod(ExecutionSemester executionSemester) {
        return getStudentCurricularPlan(executionSemester) != null;
    }

    final public boolean isCustomEnrolmentModel(final ExecutionYear executionYear) {
        return getEnrolmentModelForExecutionYear(executionYear) == EnrolmentModel.CUSTOM;
    }

    final public boolean isCustomEnrolmentModel() {
        return isCustomEnrolmentModel(ExecutionYear.readCurrentExecutionYear());
    }

    final public boolean isCompleteEnrolmentModel(final ExecutionYear executionYear) {
        return getEnrolmentModelForExecutionYear(executionYear) == EnrolmentModel.COMPLETE;
    }

    final public boolean isCompleteEnrolmentModel() {
        return isCompleteEnrolmentModel(ExecutionYear.readCurrentExecutionYear());
    }

    final public DegreeCurricularPlan getActiveDegreeCurricularPlan() {
        return (getActiveStudentCurricularPlan() != null ? getActiveStudentCurricularPlan().getDegreeCurricularPlan() : null);
    }

    final public DegreeCurricularPlan getLastDegreeCurricularPlan() {
        return (getLastStudentCurricularPlan() != null ? getLastStudentCurricularPlan().getDegreeCurricularPlan() : null);
    }

    public Degree getLastDegree() {
        return getLastDegreeCurricularPlan().getDegree();
    }

    private boolean hasAnyNotPayedGratuityEvents() {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyNotPayedGratuityEvents()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAnyNotPayedInsuranceEvents() {
        for (final InsuranceEvent event : getPerson().getNotCancelledInsuranceEvents()) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEvents(final AdministrativeOffice office) {
        for (final AdministrativeOfficeFeeAndInsuranceEvent event : getPerson()
                .getNotCancelledAdministrativeOfficeFeeAndInsuranceEvents(office)) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAnyNotPayedGratuityEventUntil(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyNotPayedGratuityEventsUntil(executionYear)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnyNotPayedInsuranceEventUntil(final ExecutionYear executionYear) {
        for (final InsuranceEvent event : getPerson().getNotCancelledInsuranceEventsUntil(executionYear)) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEventUntil(final AdministrativeOffice office,
            final ExecutionYear executionYear) {
        for (final AdministrativeOfficeFeeAndInsuranceEvent event : getPerson()
                .getNotCancelledAdministrativeOfficeFeeAndInsuranceEventsUntil(office, executionYear)) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyNotPayedGratuityEventsForPreviousYears(final ExecutionYear limitExecutionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (EnrolmentBlocker.enrolmentBlocker.isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt(studentCurricularPlan, limitExecutionYear)) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasToPayGratuityOrInsurance() {
        return getInterruptedStudies() ? false : getRegistrationProtocol().isToPayGratuity();
    }

    final public DiplomaRequest getDiplomaRequest(final CycleType cycleType) {
        return getDiplomaRequest(getLastStudentCurricularPlan().getCycleCourseGroup(cycleType).getProgramConclusion());
    }

    final public DiplomaRequest getDiplomaRequest(final ProgramConclusion programConclusion) {
        for (final DocumentRequest documentRequest : getDocumentRequests()) {
            if (documentRequest.isDiploma() && !documentRequest.finishedUnsuccessfully()) {
                final DiplomaRequest diplomaRequest = (DiplomaRequest) documentRequest;
                if (programConclusion == null || programConclusion.equals(diplomaRequest.getProgramConclusion())) {
                    return diplomaRequest;
                }
            }
        }

        return null;
    }

    final public PastDiplomaRequest getPastDiplomaRequest() {
        for (final DocumentRequest documentRequest : getDocumentRequests()) {
            if (documentRequest.isPastDiploma() && !documentRequest.finishedUnsuccessfully()) {
                return (PastDiplomaRequest) documentRequest;
            }
        }
        return null;
    }

    final public RegistryDiplomaRequest getRegistryDiplomaRequest(final CycleType cycleType) {
        return getRegistryDiplomaRequest(getLastStudentCurricularPlan().getCycleCourseGroup(cycleType).getProgramConclusion());
    }

    final public RegistryDiplomaRequest getRegistryDiplomaRequest(final ProgramConclusion programConclusion) {
        for (final DocumentRequest documentRequest : getDocumentRequests()) {
            if (documentRequest.isRegistryDiploma() && !documentRequest.finishedUnsuccessfully()) {
                final RegistryDiplomaRequest registryDiplomaRequest = (RegistryDiplomaRequest) documentRequest;
                if (programConclusion == null || programConclusion.equals(registryDiplomaRequest.getProgramConclusion())) {
                    return registryDiplomaRequest;
                }
            }
        }
        return null;
    }

    final public DiplomaSupplementRequest getDiplomaSupplementRequest(final ProgramConclusion programConclusion) {
        for (final DocumentRequest documentRequest : getDocumentRequests()) {
            if (documentRequest.isDiplomaSupplement() && !documentRequest.finishedUnsuccessfully()) {
                final DiplomaSupplementRequest diplomaSupplementRequest = (DiplomaSupplementRequest) documentRequest;
                if (programConclusion == null || programConclusion.equals(diplomaSupplementRequest.getProgramConclusion())) {
                    return diplomaSupplementRequest;
                }
            }
        }
        return null;
    }

    final public Collection<DocumentRequest> getDocumentRequests() {
        final Set<DocumentRequest> result = new HashSet<DocumentRequest>();
        for (AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if (academicServiceRequest.isDocumentRequest()) {
                result.add((DocumentRequest) academicServiceRequest);
            }
        }
        return result;
    }

    final public Set<DocumentRequest> getDocumentRequests(DocumentRequestType documentRequestType,
            AcademicServiceRequestSituationType academicServiceRequestSituationType, ExecutionYear executionYear,
            boolean collectDocumentsMarkedAsFreeProcessed) {

        final Set<DocumentRequest> result = new HashSet<DocumentRequest>();

        for (final DocumentRequest documentRequest : getDocumentRequests()) {
            if (documentRequest.getDocumentRequestType() == documentRequestType
                    && documentRequest.getAcademicServiceRequestSituationType() == academicServiceRequestSituationType
                    && executionYear.containsDate(documentRequest.getCreationDate())
                    && (!collectDocumentsMarkedAsFreeProcessed || documentRequest.isFreeProcessed())) {

                result.add(documentRequest);
            }
        }

        return result;
    }

    final public Set<DocumentRequest> getSucessfullyFinishedDocumentRequestsBy(ExecutionYear executionYear,
            DocumentRequestType documentRequestType, boolean collectDocumentsMarkedAsFreeProcessed) {

        final Set<DocumentRequest> result = new HashSet<DocumentRequest>();

        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if (academicServiceRequest instanceof DocumentRequest) {
                final DocumentRequest documentRequest = (DocumentRequest) academicServiceRequest;
                if (documentRequest.getDocumentRequestType() == documentRequestType && documentRequest.finishedSuccessfully()
                        && executionYear.containsDate(documentRequest.getCreationDate())
                        && (!collectDocumentsMarkedAsFreeProcessed || documentRequest.isFreeProcessed())) {

                    result.add((DocumentRequest) academicServiceRequest);
                }
            }
        }
        return result;
    }

    final public Collection<DocumentRequest> getSucessfullyFinishedDocumentRequests(final DocumentRequestType documentRequestType) {
        final Collection<DocumentRequest> result = new HashSet<DocumentRequest>();

        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if (academicServiceRequest instanceof DocumentRequest) {
                final DocumentRequest documentRequest = (DocumentRequest) academicServiceRequest;
                if (documentRequest.getDocumentRequestType() == documentRequestType && documentRequest.finishedSuccessfully()) {
                    result.add((DocumentRequest) academicServiceRequest);
                }
            }
        }

        return result;
    }

    final public Collection<? extends AcademicServiceRequest> getAcademicServiceRequests(
            final Class<? extends AcademicServiceRequest> clazz) {
        final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if (clazz != null && academicServiceRequest.getClass().equals(clazz)) {
                result.add(academicServiceRequest);
            }
        }

        return result;
    }

    public Collection<? extends AcademicServiceRequest> getAcademicServiceRequests(
            final Class<? extends AcademicServiceRequest> clazz, final ExecutionYear executionYear) {
        final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();
        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if (clazz != null && academicServiceRequest.getClass().equals(clazz) && academicServiceRequest.isFor(executionYear)) {
                result.add(academicServiceRequest);
            }
        }
        return result;
    }

    final public Collection<? extends AcademicServiceRequest> getAcademicServiceRequests(
            final AcademicServiceRequestSituationType academicServiceRequestSituationType) {

        final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if ((academicServiceRequestSituationType == null && academicServiceRequest.isNewRequest())
                    || academicServiceRequest.getAcademicServiceRequestSituationType() == academicServiceRequestSituationType) {

                result.add(academicServiceRequest);
            }
        }

        return result;
    }

    final public Collection<AcademicServiceRequest> getNewAcademicServiceRequests() {
        return (Collection<AcademicServiceRequest>) getAcademicServiceRequests(AcademicServiceRequestSituationType.NEW);
    }

    final public Collection<AcademicServiceRequest> getProcessingAcademicServiceRequests() {
        final Collection<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();
        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if (academicServiceRequest.hasProcessed() && !academicServiceRequest.isConcluded()
                    && !academicServiceRequest.isDelivered() && !academicServiceRequest.finishedUnsuccessfully()) {
                result.add(academicServiceRequest);
            }
        }
        return result;
    }

    public Collection<AcademicServiceRequest> getToDeliverAcademicServiceRequests() {
        final Collection<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();
        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if (academicServiceRequest.isDeliveredSituationAccepted()) {
                result.add(academicServiceRequest);
            }
        }
        return result;
    }

    final public Collection<AcademicServiceRequest> getConcludedAcademicServiceRequests() {
        return (Collection<AcademicServiceRequest>) getAcademicServiceRequests(AcademicServiceRequestSituationType.CONCLUDED);
    }

    final public Collection<AcademicServiceRequest> getHistoricalAcademicServiceRequests() {
        final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
            if (academicServiceRequest.isHistorical()) {
                result.add(academicServiceRequest);
            }
        }

        return result;
    }

    final public boolean isInactive() {
        return getActiveStateType().isInactive();
    }

    public Space getCampus() {
        return getLastStudentCurricularPlan().getLastCampus();
    }

    public Space getCampus(final ExecutionYear executionYear) {
        final StudentCurricularPlan scp = getStudentCurricularPlan(executionYear);
        return scp == null ? getLastStudentCurricularPlan().getCampus(executionYear) : scp.getCampus(executionYear);
    }

    final public String getIstUniversity() {
        return getCampus().getName();
    }

    @Override
    final public void setStudentCandidacy(StudentCandidacy studentCandidacy) {
        if (getStudentCandidacy() != null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.student.Registration.studentCandidacy.cannot.be.modified");
        }

        super.setStudentCandidacy(studentCandidacy);
    }

    final public void removeStudentCandidacy() {
        super.setStudentCandidacy(null);
    }

    final public Boolean getPayedTuition() {
        return !hasAnyNotPayedGratuityEventsForPreviousYears(ExecutionYear.readCurrentExecutionYear());
    }

    final public boolean getHasGratuityDebtsCurrently() {
        return hasGratuityDebtsCurrently();
    }

    final public boolean hasGratuityDebtsCurrently() {
        return hasAnyNotPayedGratuityEvents();
    }

    final public boolean hasInsuranceDebtsCurrently() {
        return hasAnyNotPayedInsuranceEvents();
    }

    final public boolean hasAdministrativeOfficeFeeAndInsuranceDebtsCurrently(final AdministrativeOffice administrativeOffice) {
        return hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEvents(administrativeOffice);
    }

    final public boolean hasGratuityDebts(final ExecutionYear executionYear) {
        return hasAnyNotPayedGratuityEventUntil(executionYear);
    }

    final public boolean hasInsuranceDebts(final ExecutionYear executionYear) {
        return hasAnyNotPayedInsuranceEventUntil(executionYear);
    }

    final public boolean hasAdministrativeOfficeFeeAndInsuranceDebts(final AdministrativeOffice office,
            final ExecutionYear executionYear) {
        return hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEventUntil(office, executionYear);
    }

    final public Attends readAttendByExecutionCourse(ExecutionCourse executionCourse) {
        return getStudent().readAttendByExecutionCourse(executionCourse);
    }

    final public Attends readRegistrationAttendByExecutionCourse(final ExecutionCourse executionCourse) {
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.isFor(executionCourse)) {
                return attend;
            }
        }
        return null;
    }

    @Override
    public void setRegistrationProtocol(RegistrationProtocol registrationProtocol) {
        if (registrationProtocol == null) {
            registrationProtocol = RegistrationProtocol.getDefault();
        }
        super.setRegistrationProtocol(registrationProtocol);
        if (registrationProtocol != null && registrationProtocol.isEnrolmentByStudentAllowed() && !registrationProtocol.isAlien()
                && getExternalRegistrationData() == null) {
            new ExternalRegistrationData(this);
        }

    }

    final public boolean hasGratuityEvent(final ExecutionYear executionYear, final Class<? extends GratuityEvent> type) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasGratuityEvent(executionYear, type)) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasDissertationThesis() {
        return getDissertationEnrolment() != null && getDissertationEnrolment().getThesis() != null;
    }

    final public String getDissertationThesisTitle() {
        String result = null;

        if (hasDissertationThesis()) {
            result = getDissertationEnrolment().getThesis().getFinalFullTitle().getContent().trim();
        }

        return result;
    }

    final public LocalDate getDissertationThesisDiscussedDate() {
        if (hasDissertationThesis()) {
            final Thesis thesis = getDissertationEnrolment().getThesis();
            return thesis.hasCurrentDiscussedDate() ? thesis.getCurrentDiscussedDate().toLocalDate() : null;
        }

        return null;
    }

    final public Enrolment getDissertationEnrolment() {
        return getDissertationEnrolment(null);
    }

    final public Enrolment getDissertationEnrolment(DegreeCurricularPlan degreeCurricularPlan) {
        for (StudentCurricularPlan scp : getStudentCurricularPlansSet()) {
            if (degreeCurricularPlan != null && scp.getDegreeCurricularPlan() != degreeCurricularPlan) {
                continue;
            }

            Enrolment enrolment = scp.getLatestDissertationEnrolment();
            if (enrolment != null) {
                return enrolment;
            }
        }

        return null;
    }

    public Set<Enrolment> getDissertationEnrolments(DegreeCurricularPlan degreeCurricularPlan) {
        final Set<Enrolment> enrolments = new HashSet<Enrolment>();
        for (StudentCurricularPlan scp : getStudentCurricularPlansSet()) {
            if (degreeCurricularPlan != null && scp.getDegreeCurricularPlan() != degreeCurricularPlan) {
                continue;
            }
            enrolments.addAll(scp.getDissertationEnrolments());
        }
        return enrolments;
    }

    final public StudentCurricularPlan getLastStudentDegreeCurricularPlansByDegree(Degree degree) {
        final SortedSet<StudentCurricularPlan> result = new TreeSet<StudentCurricularPlan>(StudentCurricularPlan.DATE_COMPARATOR);
        for (DegreeCurricularPlan degreeCurricularPlan : this.getDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getDegree() == degree) {
                result.add(this.getStudentCurricularPlan(degreeCurricularPlan));
            }
        }
        return result.last();

    }

    final public ExternalEnrolment findExternalEnrolment(Unit university, ExecutionSemester period, String code) {
        for (final ExternalEnrolment externalEnrolment : this.getExternalEnrolmentsSet()) {
            if (externalEnrolment.getExecutionPeriod() == period
                    && externalEnrolment.getExternalCurricularCourse().getCode().equals(code)
                    && externalEnrolment.getExternalCurricularCourse().getUnit() == university) {
                return externalEnrolment;
            }
        }
        return null;
    }

    final public SortedSet<ExternalEnrolment> getSortedExternalEnrolments() {
        final SortedSet<ExternalEnrolment> result = new TreeSet<ExternalEnrolment>(ExternalEnrolment.COMPARATOR_BY_NAME);
        result.addAll(getExternalEnrolmentsSet());
        return result;
    }

    public Registration getSourceRegistrationForTransition() {
        if (getLastDegreeCurricularPlan().getEquivalencePlan() == null) {
            return null;
        }
        final DegreeCurricularPlanEquivalencePlan equivalencePlan = getLastDegreeCurricularPlan().getEquivalencePlan();
        final List<Registration> registrations =
                getStudent().getRegistrationsFor(equivalencePlan.getSourceDegreeCurricularPlan());
        return registrations.isEmpty() ? null : registrations.iterator().next();
    }

    public List<Registration> getTargetTransitionRegistrations() {
        final List<Registration> result = new ArrayList<Registration>();

        for (final DegreeCurricularPlanEquivalencePlan equivalencePlan : getLastDegreeCurricularPlan()
                .getTargetEquivalencePlans()) {
            final Registration transitionRegistration =
                    getStudent().getTransitionRegistrationFor(equivalencePlan.getDegreeCurricularPlan());
            if (transitionRegistration != null) {
                result.add(transitionRegistration);
            }
        }

        return result;

    }

    public void transitToBolonha(final Person person, final DateTime when) {
        if (!isActive()) {
            throw new DomainException("error.student.Registration.cannot.transit.non.active.registrations");
        }

        RegistrationState.createRegistrationState(this, person, when, RegistrationStateType.TRANSITED);

        for (final Registration registration : getTargetTransitionRegistrations()) {
            if (registration.getDegreeType().isBolonhaDegree()) {
                RegistrationState.createRegistrationState(registration, person, when,
                        registration.hasConcluded() ? RegistrationStateType.CONCLUDED : RegistrationStateType.REGISTERED);
            } else {
                RegistrationState.createRegistrationState(registration, person, when, RegistrationStateType.REGISTERED);
            }

            registration.setRegistrationProtocol(getRegistrationProtocol());
            registration.setSourceRegistration(this);

            changeAttends(registration, when);
        }

        if (!getTargetTransitionRegistrations().isEmpty()) {
            // change remaining attends to any target transition registration
            changeAttends(getTargetTransitionRegistrations().iterator().next(), when);
        }
    }

    private void changeAttends(final Registration newRegistration, final DateTime when) {
        final ExecutionSemester executionSemester = ExecutionSemester.readByDateTime(when);
        if (executionSemester == null) {
            throw new DomainException("error.Registration.invalid.when.date");
        }

        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.getExecutionPeriod().isAfterOrEquals(executionSemester)) {
                for (final CurricularCourse curricularCourse : attends.getExecutionCourse().getAssociatedCurricularCoursesSet()) {
                    if (curricularCourse.getDegreeCurricularPlan() == newRegistration.getLastDegreeCurricularPlan()) {
                        attends.setRegistration(newRegistration);
                        break;
                    }
                }
            }
        }
    }

    public boolean isEnrolmentByStudentAllowed() {
        return isActive() && getRegistrationProtocol().isEnrolmentByStudentAllowed();
    }

    @Deprecated
    public boolean isEnrolmentByStudentAllowed(DegreeType type) {
        return DEGREE_TYPES_TO_ENROL_BY_STUDENT.test(type);
    }

    public boolean isEnrolmentByStudentInShiftsAllowed() {
        return isActive();
    }

    public void editStartDates(final LocalDate startDate, final LocalDate homologationDate, final LocalDate studiesStartDate) {
        editStartDates(new YearMonthDay(startDate), new YearMonthDay(homologationDate), new YearMonthDay(studiesStartDate));
    }

    public void editStartDates(final YearMonthDay startDate, final YearMonthDay homologationDate,
            final YearMonthDay studiesStartDate) {

        setStartDate(startDate);

        // edit RegistrationState start date
        final RegistrationState firstRegistrationState = getFirstRegistrationState();
        firstRegistrationState.setStateDate(startDate);
        if (firstRegistrationState != getFirstRegistrationState()) {
            throw new DomainException("error.Registration.startDate.changes.first.registration.state");
        }

        // edit Scp start date
        final StudentCurricularPlan first = getFirstStudentCurricularPlan();
        first.setStartDate(startDate);
        if (first != getFirstStudentCurricularPlan()) {
            throw new DomainException("error.Registration.startDate.changes.first.scp");
        }

        setHomologationDate(homologationDate);
        setStudiesStartDate(studiesStartDate);
    }

    @Override
    public void setStartDate(YearMonthDay startDate) {

        String[] args = {};
        if (startDate == null) {
            throw new DomainException("error.Registration.null.startDate", args);
        }
        super.setStartDate(startDate);

        final ExecutionYear year = ExecutionYear.readByDateTime(startDate.toLocalDate());
        String[] args1 = {};
        if (year == null) {
            throw new DomainException("error.Registration.invalid.execution.year", args1);
        }
        setRegistrationYear(year);

    }

    @Atomic
    public StudentStatute grantSeniorStatute(ExecutionYear executionYear) {
        return new SeniorStatute(getStudent(), this, StatuteType.findSeniorStatuteType().orElse(null),
                executionYear.getFirstExecutionPeriod(), executionYear.getLastExecutionPeriod());
    }

    public void setHomologationDate(final LocalDate homologationDate) {
        setHomologationDate(new YearMonthDay(homologationDate));
    }

    public void setStudiesStartDate(final LocalDate studiesStartDate) {
        setStudiesStartDate(new YearMonthDay(studiesStartDate));
    }

    public Collection<CurriculumLineLog> getCurriculumLineLogs(final ExecutionSemester executionSemester) {
        final Collection<CurriculumLineLog> res = new HashSet<CurriculumLineLog>();
        for (final CurriculumLineLog curriculumLineLog : getCurriculumLineLogsSet()) {
            if (curriculumLineLog.isFor(executionSemester)) {
                res.add(curriculumLineLog);
            }
        }
        return res;
    }

    public boolean containsEnrolmentOutOfPeriodEventFor(ExecutionSemester executionSemester) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final EnrolmentOutOfPeriodEvent event : studentCurricularPlan.getEnrolmentOutOfPeriodEventsSet()) {
                if (event.getExecutionPeriod() == executionSemester) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasStartedBetween(final ExecutionYear firstExecutionYear, final ExecutionYear finalExecutionYear) {
        return getStartExecutionYear().isAfterOrEquals(firstExecutionYear)
                && getStartExecutionYear().isBeforeOrEquals((finalExecutionYear));
    }

    public boolean hasRegistrationRegime(final ExecutionYear executionYear, final RegistrationRegimeType type) {
        for (final RegistrationRegime regime : getRegistrationRegimesSet()) {
            if (regime.isFor(executionYear) && regime.hasRegime(type)) {
                return true;
            }
        }
        return false;
    }

    public RegistrationRegimeType getRegimeType(final ExecutionYear executionYear) {
        for (final RegistrationRegime regime : getRegistrationRegimesSet()) {
            if (regime.isFor(executionYear)) {
                return regime.getRegimeType();
            }
        }
        // if not specified, use the default regime
        return RegistrationRegimeType.defaultType();
    }

    public boolean isPartialRegime(final ExecutionYear executionYear) {
        return getRegimeType(executionYear) == RegistrationRegimeType.PARTIAL_TIME;
    }

    public boolean isFullRegime(final ExecutionYear executionYear) {
        return getRegimeType(executionYear) == RegistrationRegimeType.FULL_TIME;
    }

    public void changeShifts(final Attends attend, final Registration newRegistration) {
        for (final Shift shift : getShiftsSet()) {
            if (attend.isFor(shift)) {
                shift.removeStudents(this);
                shift.addStudents(newRegistration);
            }
        }
    }

    public boolean hasMissingPersonalInformation(ExecutionYear executionYear) {
        // If this registration is linked to a Phd Process,
        // the personal information should be linked to the
        // PhdIndividualProgramProcess only.
        if (getPhdIndividualProgramProcess() != null) {
            return false;
        }

        if (getPrecedentDegreeInformation(executionYear) != null && getPersonalInformationBean(executionYear).isValid()) {
            return false;
        }

        return true;
    }

    public boolean hasMissingPersonalInformationForAcademicService(ExecutionYear executionYear) {
        // If this registration is linked to a Phd Process,
        // the personal information should be linked to the
        // PhdIndividualProgramProcess only.
        if (getPhdIndividualProgramProcess() != null) {
            return false;
        }

        if (getPrecedentDegreeInformation(executionYear) != null
                && !getPersonalInformationBean(executionYear).isEditableByAcademicService()) {
            return false;
        }

        return true;
    }

    @Atomic
    public void createReingression(ExecutionYear executionYear, LocalDate reingressionDate) {
        RegistrationDataByExecutionYear dataByYear =
                RegistrationDataByExecutionYear.getOrCreateRegistrationDataByYear(this, executionYear);
        dataByYear.createReingression(reingressionDate);
    }

    public boolean hasReingression(ExecutionYear executionYear) {
        RegistrationDataByExecutionYear data = getRegistrationDataByExecutionYear(executionYear);
        if (data != null) {
            return data.isReingression();
        }

        return false;
    }

    public Set<RegistrationDataByExecutionYear> getReingressions() {
        Set<RegistrationDataByExecutionYear> reingressions = new HashSet<RegistrationDataByExecutionYear>();
        for (RegistrationDataByExecutionYear year : getRegistrationDataByExecutionYearSet()) {
            if (year.isReingression()) {
                reingressions.add(year);
            }
        }
        return reingressions;
    }

    @Atomic
    public void deleteReingression(ExecutionYear executionYear) {
        RegistrationDataByExecutionYear dataByExecutionYear = getRegistrationDataByExecutionYear(executionYear);

        if ((dataByExecutionYear == null) || (dataByExecutionYear.getExecutionYear() != executionYear)) {
            throw new DomainException("error.Registration.reingression.not.marked.in.execution.year");
        }

        dataByExecutionYear.deleteReingression();
    }

    public PersonalInformationBean getPersonalInformationBean(ExecutionYear executionYear) {
        PrecedentDegreeInformation precedentInformation = getPrecedentDegreeInformation(executionYear);

        if (precedentInformation == null) {
            precedentInformation = getLatestPrecedentDegreeInformation();
        }
        if (precedentInformation == null) {
            return new PersonalInformationBean(this);
        }

        return new PersonalInformationBean(precedentInformation);
    }

    public PrecedentDegreeInformation getPrecedentDegreeInformation(ExecutionYear executionYear) {
        for (PrecedentDegreeInformation precedentDegreeInfo : getPrecedentDegreesInformationsSet()) {
            if (precedentDegreeInfo.getPersonalIngressionData().getExecutionYear().equals(executionYear)) {
                return precedentDegreeInfo;
            }
        }
        return null;
    }

    public PrecedentDegreeInformation getLatestPrecedentDegreeInformation() {
        TreeSet<PrecedentDegreeInformation> degreeInformations =
                new TreeSet<PrecedentDegreeInformation>(
                        Collections.reverseOrder(PrecedentDegreeInformation.COMPARATOR_BY_EXECUTION_YEAR));
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (PrecedentDegreeInformation pdi : getPrecedentDegreesInformationsSet()) {
            if (!pdi.getExecutionYear().isAfter(currentExecutionYear)) {
                degreeInformations.add(pdi);
            }
        }

        if (degreeInformations.isEmpty()) {
            return null;
        }
        return degreeInformations.iterator().next();
    }

    private int getNumberOfTotalEnrolments(ExecutionInterval interval, Function<ExternalEnrolment,ExecutionInterval> executionIntervalProvider) {
        StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
        if (lastStudentCurricularPlan == null) {
            return 0;
        }

        int countEnrolments = 0;
        if (interval instanceof ExecutionSemester) {
            countEnrolments = lastStudentCurricularPlan.countEnrolments((ExecutionSemester) interval);
        } else if (interval instanceof ExecutionYear){
            countEnrolments = lastStudentCurricularPlan.countEnrolments((ExecutionYear) interval);
        }

        long countExternalEnrolments = getExternalEnrolmentsSet().stream().filter(e -> executionIntervalProvider.apply(e) == interval).count();
        return (int) (countEnrolments + countExternalEnrolments);
    }

    public int getNumberOfTotalEnrolments(ExecutionSemester interval) {
        return getNumberOfTotalEnrolments(interval, ExternalEnrolment::getExecutionPeriod);
    }

    public int getNumberOfTotalEnrolments(ExecutionYear interval) {
        return getNumberOfTotalEnrolments(interval, ExternalEnrolment::getExecutionYear);
    }

    public int getNumberEnroledCurricularCoursesInCurrentYear() {
        return getNumberOfTotalEnrolments(ExecutionYear.readCurrentExecutionYear());
    }

    public List<CycleCurriculumGroup> getInternalCycleCurriculumGrops() {
        return getLastStudentCurricularPlan().getInternalCycleCurriculumGrops();
    }

    public Collection<CurriculumGroup> getAllCurriculumGroups() {
        Collection<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
        for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
            result.addAll(plan.getAllCurriculumGroups());
        }
        return result;
    }

    public Collection<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        Collection<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
        for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
            result.addAll(plan.getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups());
        }
        return result;
    }

    public Boolean hasIndividualCandidacyFor(final ExecutionYear executionYear) {
        return getIndividualCandidacy() != null
                && getIndividualCandidacy().getCandidacyProcess().getCandidacyExecutionInterval().equals(executionYear);
    }

    public void updateEnrolmentDate(final ExecutionYear executionYear) {

        final RegistrationDataByExecutionYear registrationData =
                RegistrationDataByExecutionYear.getOrCreateRegistrationDataByYear(this, executionYear);
        final Collection<Enrolment> executionYearEnrolments = getEnrolments(executionYear);

        if (executionYearEnrolments.isEmpty()) {
            registrationData.setEnrolmentDate(null);

        } else if (registrationData.getEnrolmentDate() == null) {

            final Enrolment firstEnrolment = Collections.min(executionYearEnrolments, new Comparator<Enrolment>() {
                @Override
                public int compare(Enrolment left, Enrolment right) {
                    return left.getCreationDateDateTime().compareTo(right.getCreationDateDateTime());
                }
            });

            registrationData.edit(firstEnrolment.getCreationDateDateTime().toLocalDate());
        }
    }

    public void exportValues(StringBuilder result) {
        Formatter formatter = new Formatter(result);
        final Student student = getStudent();
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.ingression"),
                getIngressionType() == null ? " - " : getIngressionType().getDescription().getContent());
        formatter.format("%s: %d\n", BundleUtil.getString(Bundle.ACADEMIC, "label.studentNumber"), student.getNumber());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.Student.Person.name"), student.getPerson()
                .getName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.degree"), getDegree().getPresentationName());
        formatter.close();
    }

    public RegistrationState getLastActiveState() {
        return getRegistrationStatesSet().stream().filter(s -> s.getStateType().isActive()).max(RegistrationState.DATE_COMPARATOR).orElse(null);
    }

    public boolean hasDissertationEnrolment(final ExecutionDegree executionDegree) {
        final ExecutionYear previousExecutionYear = executionDegree.getExecutionYear();
        if (previousExecutionYear.hasNextExecutionYear()) {
            final ExecutionYear executionYear = previousExecutionYear.getNextExecutionYear();
            for (final Attends attends : getAssociatedAttendsSet()) {
                if (attends.getExecutionYear() == executionYear && attends.getEnrolment() != null) {
                    final Enrolment enrolment = attends.getEnrolment();
                    if (enrolment.isDissertation()
                            && enrolment.getDegreeCurricularPlanOfDegreeModule() == executionDegree.getDegreeCurricularPlan()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isValidForRAIDES() {
        return FenixEduAcademicConfiguration.getConfiguration().getRaidesRequestInfo() && isActive() && isBolonha()
                && !getDegreeType().isEmpty() && getRegistrationProtocol().isForOfficialMobilityReporting();
    }
}
