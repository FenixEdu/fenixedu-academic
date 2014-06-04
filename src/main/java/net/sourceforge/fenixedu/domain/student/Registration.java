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
package net.sourceforge.fenixedu.domain.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

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
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.log.CurriculumLineLog;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PastDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.student.curriculum.AverageType;
import net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcess;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.RegistrationConclusionProcess;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.domain.studentCurriculum.StandaloneCurriculumGroup;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.RegistrationPredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

public class Registration extends Registration_Base {

    private static final Logger logger = LoggerFactory.getLogger(Registration.class);

    static private final List<DegreeType> DEGREE_TYPES_TO_ENROL_BY_STUDENT = Arrays.asList(new DegreeType[] {
            DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE, DegreeType.BOLONHA_MASTER_DEGREE,
            DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA });

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

    private static final List<RegistrationAgreement> RAIDES_MOBILITY = Arrays.asList(RegistrationAgreement.ALMEIDA_GARRETT,
            RegistrationAgreement.BILATERAL_AGREEMENT, RegistrationAgreement.ERASMUS, RegistrationAgreement.SOCRATES_ERASMUS,
            RegistrationAgreement.SMILE, RegistrationAgreement.IST_USP, RegistrationAgreement.BRAZIL_AGREEMENTS,
            RegistrationAgreement.SCIENCE_WITHOUT_BORDERS, RegistrationAgreement.RUSSIA_AGREEMENTS,
            RegistrationAgreement.IBERO_SANTANDER, RegistrationAgreement.TECMIC, RegistrationAgreement.INOV_IST);

    private Registration() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRegistrationAgreement(RegistrationAgreement.NORMAL);
    }

    private Registration(final Person person, final DateTime start, final Integer registrationNumber, final Degree degree) {
        this();
        setStudent(person.hasStudent() ? person.getStudent() : new Student(person, registrationNumber));
        setNumber(registrationNumber == null ? getStudent().getNumber() : registrationNumber);
        setStartDate(start.toYearMonthDay());
        setDegree(degree);
        RegistrationStateCreator.createState(this, AccessControl.getPerson(), start, RegistrationStateType.REGISTERED);
    }

    public Registration(final Person person, final StudentCandidacy studentCandidacy) {
        this(person, null, RegistrationAgreement.NORMAL, null, studentCandidacy);
    }

    public Registration(final Person person, final Integer studentNumber, final Degree degree) {
        this(person, studentNumber, RegistrationAgreement.NORMAL, null, degree);
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan) {
        this(person, degreeCurricularPlan, RegistrationAgreement.NORMAL, null, null);
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType) {
        this(person, degreeCurricularPlan, RegistrationAgreement.NORMAL, cycleType, null);
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final RegistrationAgreement agreement, final CycleType cycleType, final ExecutionYear executionYear) {

        this(person, null, agreement, executionYear, degreeCurricularPlan != null ? degreeCurricularPlan.getDegree() : null);
        createStudentCurricularPlan(person, degreeCurricularPlan, cycleType, executionYear);
    }

    public static Registration createRegistrationWithCustomStudentNumber(final Person person,
            final DegreeCurricularPlan degreeCurricularPlan, final StudentCandidacy studentCandidacy,
            final RegistrationAgreement agreement, final CycleType cycleType, final ExecutionYear executionYear,
            Integer studentNumber) {
        final Degree degree = degreeCurricularPlan == null ? null : degreeCurricularPlan.getDegree();
        Registration registration = new Registration(person, calculateStartDate(executionYear), studentNumber, degree);
        registration.setRegistrationYear(executionYear == null ? ExecutionYear.readCurrentExecutionYear() : executionYear);
        registration.setRequestedChangeDegree(false);
        registration.setRequestedChangeBranch(false);
        registration.setRegistrationAgreement(agreement == null ? RegistrationAgreement.NORMAL : agreement);
        registration.createStudentCurricularPlan(person, degreeCurricularPlan, cycleType, executionYear);
        registration.setStudentCandidacyInformation(studentCandidacy);

        return registration;
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan, final StudentCandidacy candidacy,
            final RegistrationAgreement agreement, final CycleType cycleType) {
        this(person, degreeCurricularPlan, candidacy, agreement, cycleType, null);
    }

    public Registration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final StudentCandidacy studentCandidacy, final RegistrationAgreement agreement, final CycleType cycleType,
            final ExecutionYear executionYear) {

        this(person, degreeCurricularPlan, agreement, cycleType, executionYear);
        setStudentCandidacyInformation(studentCandidacy);
        EventGenerator.generateNecessaryEvents(getLastStudentCurricularPlan(), person, executionYear);
    }

    private Registration(final Person person, final Integer registrationNumber, final RegistrationAgreement agreement,
            final ExecutionYear executionYear, final StudentCandidacy studentCandidacy) {

        this(person, registrationNumber, agreement, executionYear, getDegreeFromCandidacy(studentCandidacy));
        setStudentCandidacyInformation(studentCandidacy);
    }

    private static Degree getDegreeFromCandidacy(StudentCandidacy studentCandidacy) {
        return studentCandidacy == null ? null : studentCandidacy.getExecutionDegree().getDegree();
    }

    private Registration(final Person person, final Integer registrationNumber, final RegistrationAgreement agreement,
            final ExecutionYear executionYear, final Degree degree) {

        this(person, calculateStartDate(executionYear), registrationNumber, degree);

        setRegistrationYear(executionYear == null ? ExecutionYear.readCurrentExecutionYear() : executionYear);
        setRequestedChangeDegree(false);
        setRequestedChangeBranch(false);
        setRegistrationAgreement(agreement == null ? RegistrationAgreement.NORMAL : agreement);
    }

    private void setStudentCandidacyInformation(final StudentCandidacy studentCandidacy) {
        setStudentCandidacy(studentCandidacy);
        if (studentCandidacy != null) {
            super.setEntryPhase(studentCandidacy.getEntryPhase());
            super.setIngression(studentCandidacy.getIngression());

            if (studentCandidacy.getIngression() == Ingression.RI) {
                final Degree sourceDegree = studentCandidacy.getDegreeCurricularPlan().getEquivalencePlan().getSourceDegree();
                Registration registration = getStudent().readRegistrationByDegree(sourceDegree);
                if (registration == null) {
                    final Collection<Registration> registrations = getStudent().getRegistrationsByDegreeType(DegreeType.DEGREE);
                    registrations.remove(this);
                    registration = registrations.size() == 1 ? registrations.iterator().next() : null;
                }

                setSourceRegistration(registration);
            }
        }
    }

    private void createStudentCurricularPlan(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final CycleType cycleType, final ExecutionYear executionYear) {
        final YearMonthDay startDay;
        final ExecutionSemester executionSemester;
        if (executionYear == null || executionYear.isCurrent()) {
            startDay = new YearMonthDay();
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        } else {
            startDay = executionYear.getBeginDateYearMonthDay();
            executionSemester = executionYear.getFirstExecutionPeriod();
        }

        final StudentCurricularPlan scp =
                StudentCurricularPlan.createBolonhaStudentCurricularPlan(this, degreeCurricularPlan, startDay, executionSemester,
                        cycleType);

    }

    private static DateTime calculateStartDate(final ExecutionYear executionYear) {
        return executionYear == null || executionYear.isCurrent() ? new DateTime() : executionYear.getBeginDateYearMonthDay()
                .toDateTimeAtMidnight();
    }

    @Override
    final public void setNumber(Integer number) {
        super.setNumber(number);
        if (number == null && hasRegistrationNumber()) {
            getRegistrationNumber().delete();
        } else if (number != null) {
            if (hasRegistrationNumber()) {
                getRegistrationNumber().setNumber(number);
            } else {
                new RegistrationNumber(this);
            }
        }
    }

    @Deprecated
    public void delete() {

        checkRulesToDelete();

        for (; hasAnyRegistrationStates(); getRegistrationStates().iterator().next().delete()) {
            ;
        }
        for (; hasAnyStudentCurricularPlans(); getStudentCurricularPlans().iterator().next().delete()) {
            ;
        }
        for (; hasAnyAssociatedAttends(); getAssociatedAttends().iterator().next().delete()) {
            ;
        }
        for (; hasAnyExternalEnrolments(); getExternalEnrolments().iterator().next().delete()) {
            ;
        }
        for (; hasAnyRegistrationDataByExecutionYear(); getRegistrationDataByExecutionYear().iterator().next().delete()) {
            ;
        }
        for (; hasAnyAcademicServiceRequests(); getAcademicServiceRequests().iterator().next().delete()) {
            ;
        }
        for (; hasAnyRegistrationRegimes(); getRegistrationRegimes().iterator().next().delete()) {
            ;
        }
        for (; hasAnyCurriculumLineLogs(); getCurriculumLineLogs().iterator().next().delete()) {
            ;
        }
        for (; hasAnyRegistrationStateLogs(); getRegistrationStateLogs().iterator().next().delete()) {
            ;
        }

        if (hasRegistrationNumber()) {
            getRegistrationNumber().delete();
        }
        if (hasExternalRegistrationData()) {
            getExternalRegistrationData().delete();
        }
        if (hasSenior()) {
            getSenior().delete();
        }
        if (hasStudentCandidacy()) {
            getStudentCandidacy().delete();
        }

        setIndividualCandidacy(null);
        setSourceRegistration(null);
        setRegistrationYear(null);
        setDegree(null);
        setStudent(null);
        setRegistrationProtocol(null);
        setRootDomainObject(null);

        getDestinyRegistrations().clear();
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
        return hasAnyStudentCurricularPlans() ? (StudentCurricularPlan) Collections.min(getStudentCurricularPlans(),
                StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE) : null;
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
        for (final Attends attends : getAssociatedAttends()) {
            if (attends.isFor(executionCourse)) {
                return true;
            }
        }
        return false;
    }

    final public List<WrittenEvaluation> getWrittenEvaluations(final ExecutionSemester executionSemester) {
        final List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.isFor(executionSemester)) {
                for (final Evaluation evaluation : attend.getExecutionCourse().getAssociatedEvaluations()) {
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
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof Exam
                    && writtenEvaluationEnrolment.isForExecutionPeriod(executionSemester)) {
                result.add((Exam) writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }

    final public List<Exam> getUnenroledExams(final ExecutionSemester executionSemester) {
        final List<Exam> result = new ArrayList<Exam>();
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.isFor(executionSemester)) {
                for (final Evaluation evaluation : attend.getExecutionCourse().getAssociatedEvaluations()) {
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
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof WrittenTest
                    && writtenEvaluationEnrolment.isForExecutionPeriod(executionSemester)) {
                result.add((WrittenTest) writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }

    final public List<WrittenTest> getUnenroledWrittenTests(final ExecutionSemester executionSemester) {
        final List<WrittenTest> result = new ArrayList<WrittenTest>();
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                for (final Evaluation evaluation : attend.getExecutionCourse().getAssociatedEvaluations()) {
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
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.isFor(executionSemester)) {
                for (final Evaluation evaluation : attend.getExecutionCourse().getAssociatedEvaluations()) {
                    if (evaluation instanceof Project) {
                        result.add((Project) evaluation);
                    }
                }
            }
        }
        return result;
    }

    final public boolean isEnroledIn(final Evaluation evaluation) {
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == evaluation) {
                return true;
            }
        }
        return false;
    }

    final public Space getRoomFor(final WrittenEvaluation writtenEvaluation) {
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this.getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == writtenEvaluation) {
                return writtenEvaluationEnrolment.getRoom();
            }
        }
        return null;
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
        if (!hasAnyStudentCurricularPlans()) {
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

            final CycleCurriculumGroup cycleCurriculumGroup = studentCurricularPlan.getCycle(cycleType);
            if (cycleCurriculumGroup == null) {
                return Curriculum.createEmpty(executionYear);
            }

            return cycleCurriculumGroup.getCurriculum(when, executionYear);
        } else {
            final List<StudentCurricularPlan> sortedSCPs = getSortedStudentCurricularPlans();
            final ListIterator<StudentCurricularPlan> sortedSCPsIterator = sortedSCPs.listIterator(sortedSCPs.size());
            final StudentCurricularPlan lastStudentCurricularPlan = sortedSCPsIterator.previous();

            final ICurriculum curriculum;
            if (lastStudentCurricularPlan.isBoxStructure()) {
                curriculum = lastStudentCurricularPlan.getCurriculum(when, executionYear);

                for (; sortedSCPsIterator.hasPrevious();) {
                    final StudentCurricularPlan studentCurricularPlan = sortedSCPsIterator.previous();
                    if (executionYear == null || studentCurricularPlan.getStartExecutionYear().isBeforeOrEquals(executionYear)) {
                        ((Curriculum) curriculum).add(studentCurricularPlan.getCurriculum(when, executionYear));
                    }
                }

                return curriculum;

            } else {
                curriculum = new StudentCurriculum(this, executionYear);
            }

            return curriculum;
        }
    }

    public int getNumberOfCurriculumEntries() {
        return getCurriculum().getCurriculumEntries().size();
    }

    final public BigDecimal getSumPiCi(final ExecutionYear executionYear) {
        return getCurriculum(executionYear).getSumPiCi();
    }

    final public BigDecimal getSumPi(final ExecutionYear executionYear) {
        return getCurriculum(executionYear).getSumPi();
    }

    final public BigDecimal getAverage() {
        if (!isBolonha() && isRegistrationConclusionProcessed()) {
            return getConclusionProcess().getAverage();
        }
        return getAverage((ExecutionYear) null, (CycleType) null);
    }

    final public BigDecimal getAverage(final ExecutionYear executionYear) {
        return getAverage(executionYear, (CycleType) null);
    }

    final public BigDecimal getAverage(final CycleType cycleType) {
        return getAverage((ExecutionYear) null, cycleType);
    }

    final public BigDecimal getAverage(final ExecutionYear executionYear, final CycleType cycleType) {
        return executionYear == null && cycleType == null && isConcluded() && isRegistrationConclusionProcessed() ? BigDecimal
                .valueOf(getFinalAverage()) : getCurriculum(executionYear, cycleType).getAverage();
    }

    final public BigDecimal getAverage(String cycleTypeName) {
        return getAverage(CycleType.valueOf(cycleTypeName));
    }

    final public BigDecimal getEctsCredits(final ExecutionYear executionYear, final CycleType cycleType) {
        return getCurriculum(executionYear, cycleType).getSumEctsCredits();
    }

    final public AverageType getAverageType() {
        if (getDegreeType() == DegreeType.MASTER_DEGREE) {
            return getLastStudentCurricularPlan().getAverageType();
        } else {
            return AverageType.WEIGHTED;
        }
    }

    final public BigDecimal calculateAverage() {
        final ICurriculum curriculum = getCurriculum();
        final BigDecimal weighted = curriculum.getAverage();

        switch (getAverageType()) {
        case SIMPLE:
            curriculum.setAverageType(AverageType.SIMPLE);
            return curriculum.getAverage();
        case BEST:
            curriculum.setAverageType(AverageType.SIMPLE);
            final BigDecimal simple = curriculum.getAverage();

            return weighted.max(simple);
        default:
            return weighted;
        }
    }

    public Integer calculateRoundedAverage() {
        if (isBolonha()) {
            throw new DomainException("error.Registration.for.cannot.calculate.final.average.in.registration.for.bolonha");
        }

        return Curriculum.getRoundedAverage(calculateAverage());
    }

    final public Integer getFinalAverage() {
        if (isBolonha()) {
            final List<CycleCurriculumGroup> internalCycleCurriculumGrops =
                    getLastStudentCurricularPlan().getInternalCycleCurriculumGrops();
            if (internalCycleCurriculumGrops.size() == 1) {
                return internalCycleCurriculumGrops.iterator().next().getFinalAverage();
            } else {
                throw new DomainException("error.bolonha.Registration.must.get.final.average.from.cycle.curriculum.groups");
            }
        }

        return isRegistrationConclusionProcessed() ? getConclusionProcess().getFinalAverage() : null;
    }

    final public Integer getFinalAverage(final CycleType cycleType) {
        if (cycleType == null) {
            return getFinalAverage();
        }

        if (getDegreeType().getCycleTypes().isEmpty()) {
            throw new DomainException("Registration.has.no.cycle.type");
        }

        if (!getDegreeType().hasCycleTypes(cycleType)) {
            throw new DomainException("Registration.doesnt.have.such.cycle.type");
        }

        return getLastStudentCurricularPlan().getCycle(cycleType).getFinalAverage();

    }

    final public String getFinalAverageDescription() {
        return getFinalAverageDescription((CycleType) null);
    }

    final public String getFinalAverageDescription(final CycleType cycleType) {
        final Integer finalAverage = getFinalAverage(cycleType);
        return finalAverage == null ? null : BundleUtil.getString(Bundle.ENUMERATION, 
                finalAverage.toString());
    }

    final public String getFinalAverageQualified() {
        return getFinalAverageQualified((CycleType) null);
    }

    final public String getFinalAverageQualified(final CycleType cycleType) {
        final Integer finalAverage = getFinalAverage(cycleType);
        return finalAverage == null ? null : getDegreeType().getGradeScale().getQualifiedName(finalAverage.toString());
    }

    final public boolean isInFinalDegreeYear() {
        return getCurricularYear() == getDegreeType().getYears();
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
                years += getDegreeType().getYears(type);
            }
        }
        return getCurricularYear() == years;
    }

    public Grade findGradeForCurricularCourse(final CurricularCourse curricularCourse) {
        final SortedSet<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.REVERSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                final CurricularCourse enrolmentCurricularCourse = enrolment.getCurricularCourse();
                if (enrolmentCurricularCourse == curricularCourse
                        || (enrolmentCurricularCourse.getCompetenceCourse() != null && enrolmentCurricularCourse
                        .getCompetenceCourse() == curricularCourse.getCompetenceCourse())
                        || hasGlobalEquivalence(curricularCourse, enrolmentCurricularCourse)) {
                    enrolments.add(enrolment);
                }
            }
        }

        for (final Enrolment enrolment : enrolments) {
            final EnrolmentEvaluation enrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
            if (enrolmentEvaluation != null && enrolmentEvaluation.isApproved()) {
                return enrolmentEvaluation.getGrade();
            }
        }

        return null;
    }

    private boolean hasGlobalEquivalence(final CurricularCourse curricularCourse, final CurricularCourse enrolmentCurricularCourse) {
        return false;
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
        for (final ExternalEnrolment externalEnrolment : getExternalEnrolments()) {
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

        if (getExternalEnrolments().isEmpty()) {
            return null;
        }

        ExternalEnrolment externalEnrolment =
                Collections.max(getExternalEnrolments(), ExternalEnrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_EVALUATION_DATE);

        return externalEnrolment.getApprovementDate() != null ? externalEnrolment.getApprovementDate() : externalEnrolment
                .hasExecutionPeriod() ? externalEnrolment.getExecutionPeriod().getEndDateYearMonthDay() : null;
    }

    final public YearMonthDay getLastApprovedEnrolmentEvaluationDate() {
        final SortedSet<Enrolment> enrolments =
                new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_LATEST_ENROLMENT_EVALUATION_AND_ID);
        enrolments.addAll(getApprovedEnrolments());

        YearMonthDay internalEnrolmentExamDate =
                enrolments.isEmpty() ? null : enrolments.last().getLatestEnrolmentEvaluation().getExamDateYearMonthDay();

        YearMonthDay externalEnrolmentExamDate =
                getExternalEnrolments().isEmpty() ? null : getLastExternalApprovedEnrolmentEvaluationDate();

        if (internalEnrolmentExamDate == null && externalEnrolmentExamDate == null) {
            return null;
        }

        if (internalEnrolmentExamDate == null) {
            return externalEnrolmentExamDate;
        }

        if (externalEnrolmentExamDate == null) {
            return internalEnrolmentExamDate;
        }

        return internalEnrolmentExamDate.compareTo(externalEnrolmentExamDate) > 1 ? internalEnrolmentExamDate : externalEnrolmentExamDate;

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

        result.addAll(getExternalEnrolments());

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
        for (final ExternalEnrolment externalEnrolment : this.getExternalEnrolments()) {
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
        return hasAnyExternalEnrolments();
    }

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
        int count = 0;
        for (ExecutionYear year : getEnrolmentsExecutionYears()) {
            if (year.isBeforeOrEquals(executionYear)) {
                count++;
            }
        }
        return count;
    }

    final public SortedSet<ExecutionYear> getSortedEnrolmentsExecutionYears() {
        final SortedSet<ExecutionYear> result = new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);
        result.addAll(getEnrolmentsExecutionYears());
        return result;
    }

    public Set<ExecutionYear> getAllRelatedRegistrationsEnrolmentsExecutionYears(Set<ExecutionYear> result) {
        if (result == null) {
            result = new HashSet<ExecutionYear>();
        }
        result.addAll(getEnrolmentsExecutionYears());

        if (this.isBolonha()) {
            Registration source = hasSourceRegistration() ? getSourceRegistration() : getSourceRegistrationForTransition();
            if (source != null) {
                source.getAllRelatedRegistrationsEnrolmentsExecutionYears(result);
            }

        } else {
            Collection<Registration> registrations = getStudent().getRegistrations();
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

    final public List<Advise> getAdvisesByTeacher(final Teacher teacher) {
        return (List<Advise>) CollectionUtils.select(getAdvises(), new Predicate() {

            @Override
            final public boolean evaluate(Object arg0) {
                Advise advise = (Advise) arg0;
                return advise.getTeacher() == teacher;
            }
        });
    }

    final public List<Advise> getAdvisesByType(final AdviseType adviseType) {
        return (List<Advise>) CollectionUtils.select(getAdvises(), new Predicate() {
            @Override
            final public boolean evaluate(Object arg0) {
                Advise advise = (Advise) arg0;
                return advise.getAdviseType().equals(adviseType);
            }
        });
    }

    final public Set<Attends> getOrderedAttends() {
        final Set<Attends> result = new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR);
        result.addAll(getAssociatedAttends());
        return result;
    }

    final public int countCompletedCoursesForActiveUndergraduateCurricularPlan() {
        return getActiveStudentCurricularPlan().getAprovedEnrolments().size();
    }

    public List<StudentCurricularPlan> getStudentCurricularPlansBySpecialization(Specialization specialization) {
        List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
            if (studentCurricularPlan.getSpecialization() != null
                    && studentCurricularPlan.getSpecialization().equals(specialization)) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansByDegree(Degree degree) {
        final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
            if (studentCurricularPlan.getDegree() == degree) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    final public StudentCurricularPlan getPastStudentCurricularPlanByDegree(Degree degree) {
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
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
        for (final Attends attend : this.getAssociatedAttends()) {
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

    @Deprecated
    final public static Registration readRegistrationByNumberAndDegreeTypes(Integer number, DegreeType... degreeTypes) {
        final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()) {
                final Registration registration = registrationNumber.getRegistration();
                if (degreeTypesList.contains(registration.getDegreeType())) {
                    return registration;
                }
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
                    && registrationNumber.getRegistration().getRegistrationAgreement().isNormal() == normalAgreement) {
                registrations.add(registrationNumber.getRegistration());
            }
        }
        return registrations;
    }

    final public static void readMasterDegreeStudentsByNameDocIDNumberIDTypeAndStudentNumber(
            final Collection<Registration> result, String studentName, String docIdNumber, IDDocumentType idType,
            Integer studentNumber) {

        if (studentNumber != null && studentNumber > 0) {
            result.addAll(Registration.readRegistrationsByNumberAndDegreeTypes(studentNumber, DegreeType.MASTER_DEGREE,
                    DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
        }

        if (!StringUtils.isEmpty(studentName)) {
            for (Person person : Person.readPersonsByName(studentName)) {
                if (person.hasStudent()) {
                    result.addAll(person.getStudent().getRegistrationsByDegreeTypes(DegreeType.MASTER_DEGREE,
                            DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
                }
            }
        }

        if (!StringUtils.isEmpty(docIdNumber)) {
            for (Person person : Person.readByDocumentIdNumber(docIdNumber)) {
                if (person.hasStudent()) {
                    result.addAll(person.getStudent().getRegistrationsByDegreeTypes(DegreeType.MASTER_DEGREE,
                            DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
                }
            }
        }

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

    @Deprecated
    final public static List<Registration> readStudentsByDegreeType(DegreeType degreeType) {
        return readRegistrationsByDegreeType(degreeType);
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
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
            GratuitySituation gratuitySituation = studentCurricularPlan.getGratuitySituationByGratuityValues(gratuityValues);
            if (gratuitySituation != null) {
                return gratuitySituation;
            }
        }
        return null;
    }

    final public FinalDegreeWorkGroup findFinalDegreeWorkGroupForExecutionYear(final ExecutionDegree executionDegree) {
        for (final GroupStudent groupStudent : getAssociatedGroupStudents()) {
            final FinalDegreeWorkGroup group = groupStudent.getFinalDegreeDegreeWorkGroup();
            if (executionDegree == group.getExecutionDegree()) {
                return group;
            }
        }
        return null;
    }

    final public List<InsuranceTransaction> readAllInsuranceTransactionByExecutionYear(ExecutionYear executionYear) {
        List<InsuranceTransaction> insuranceTransactions = new ArrayList<InsuranceTransaction>();
        for (InsuranceTransaction insuranceTransaction : this.getInsuranceTransactions()) {
            if (insuranceTransaction.getExecutionYear().equals(executionYear)) {
                insuranceTransactions.add(insuranceTransaction);
            }
        }
        return insuranceTransactions;
    }

    final public List<InsuranceTransaction> readAllNonReimbursedInsuranceTransactionsByExecutionYear(ExecutionYear executionYear) {
        List<InsuranceTransaction> nonReimbursedInsuranceTransactions = new ArrayList<InsuranceTransaction>();
        for (InsuranceTransaction insuranceTransaction : this.getInsuranceTransactions()) {
            if (insuranceTransaction.getExecutionYear().equals(executionYear)) {
                GuideEntry guideEntry = insuranceTransaction.getGuideEntry();
                if (guideEntry == null || guideEntry.getReimbursementGuideEntries().isEmpty()) {
                    nonReimbursedInsuranceTransactions.add(insuranceTransaction);
                } else {
                    boolean isReimbursed = false;
                    for (ReimbursementGuideEntry reimbursementGuideEntry : guideEntry.getReimbursementGuideEntries()) {
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

    // TODO: to remove as soon as possible
    boolean hasToPayMasterDegreeInsurance(final ExecutionYear executionYear) {
        final StudentCurricularPlan activeStudentCurricularPlan = getActiveStudentCurricularPlan();
        final boolean isSpecialization =
                (activeStudentCurricularPlan.getSpecialization() == Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION);

        if (isSpecialization) {

            if (!getEnrolments(executionYear).isEmpty()) {
                return true;
            }

            final ExecutionDegree executionDegreeByYear = getActiveDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);
            if (executionDegreeByYear != null && executionDegreeByYear.isFirstYear()) {
                return true;
            }

            return false;
        }

        return readAllNonReimbursedInsuranceTransactionsByExecutionYear(executionYear).isEmpty();
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

    final public List<Enrolment> getEnroledImprovements() {
        final List<Enrolment> enroledImprovements = new ArrayList<Enrolment>();
        for (final StudentCurricularPlan scp : getStudentCurricularPlans()) {
            if (!scp.isBoxStructure() && scp.getDegreeCurricularPlan().getDegree().getDegreeType().equals(DegreeType.DEGREE)) {
                enroledImprovements.addAll(scp.getEnroledImprovements());
            }
        }
        return enroledImprovements;
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

        return CollectionUtils.exists(enroledShifts, new Predicate() {
            @Override
            final public boolean evaluate(Object object) {
                Shift enroledShift = (Shift) object;
                return enroledShift.getExecutionCourse() == executionCourse && enroledShift.containsType(shiftType);
            }
        });
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
        Set<SchoolClass> schoolClasses =
                executionCourse.getSchoolClassesBy(getActiveStudentCurricularPlan().getDegreeCurricularPlan());
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
                || !AcademicAuthorizationGroup.getProgramsForOperation(userView.getPerson(),
                        AcademicOperationType.STUDENT_ENROLMENTS).contains(this.getDegree())) {
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
        if (attend.hasEnrolment()) {
            throw new DomainException("errors.student.already.enroled");
        }
    }

    public void checkIfHasShiftsFor(final ExecutionCourse executionCourse) {
        if (!getShiftsFor(executionCourse).isEmpty()) {
            throw new DomainException("errors.student.already.enroled.in.shift");
        }
    }

    public Tutorship getActiveTutorship() {
        return getLastStudentCurricularPlan().getActiveTutorship();
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
        return hasStudentCandidacy() ? getStudentCandidacy().getEntryGrade() : null;
    }

    final public void setEntryGrade(Double entryGrade) {
        if (hasStudentCandidacy()) {
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
        return getIngression() == Ingression.AG1C;
    }

    public boolean isSecondCycleInternalCandidacyIngression() {
        return getIngression() == Ingression.CIA2C;
    }

    @Override
    public void setIngression(Ingression ingression) {
        checkIngression(ingression);
        super.setIngression(ingression);
    }

    private void checkIngression(final Ingression ingression) {
        checkIngression(ingression, getPerson(), getFirstStudentCurricularPlan().getDegreeCurricularPlan());
    }

    public static void checkIngression(Ingression ingression, Person person, DegreeCurricularPlan degreeCurricularPlan) {
        if (ingression == Ingression.RI) {
            if (person == null || !person.hasStudent()) {
                throw new DomainException("error.registration.preBolonhaSourceDegreeNotFound");
            }
            if (degreeCurricularPlan.hasEquivalencePlan()) {
                final Student student = person.getStudent();
                final Degree sourceDegree = degreeCurricularPlan.getEquivalencePlan().getSourceDegreeCurricularPlan().getDegree();

                Registration sourceRegistration = person.getStudent().readRegistrationByDegree(sourceDegree);
                if (sourceRegistration == null) {
                    final Collection<Registration> registrations = student.getRegistrationsByDegreeType(DegreeType.DEGREE);
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
        if (!isBolonha() && isRegistrationConclusionProcessed()) {
            return getConclusionProcess().getIngressionYear();
        }

        return calculateIngressionYear();
    }

    public ExecutionYear calculateIngressionYear() {
        return inspectIngressionYear(this);
    }

    private ExecutionYear inspectIngressionYear(final Registration registration) {
        if (!registration.hasSourceRegistration()) {
            return registration.getStartExecutionYear();
        }

        return inspectIngressionYear(registration.getSourceRegistration());
    }

    final public String getContigent() {
        return hasStudentCandidacy() ? getStudentCandidacy().getContigent() : null;
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

    final public String getDegreeDescription(final CycleType cycleType) {
        return getDegreeDescription(cycleType, I18N.getLocale());
    }

    final public String getDegreeDescription(ExecutionYear executionYear, final CycleType cycleType) {
        return getDegreeDescription(executionYear, cycleType, I18N.getLocale());
    }

    final public String getDegreeDescription(final CycleType cycleType, final Locale locale) {
        return getDegreeDescription(this.getStartExecutionYear(), cycleType, locale);
    }

    final public String getDegreeDescription(ExecutionYear executionYear, final CycleType cycleType, final Locale locale) {
        final StringBuilder res = new StringBuilder();

        final Degree degree = getDegree();
        final DegreeType degreeType = degree.getDegreeType();
        if (getDegreeType() != DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA && cycleType != null) {
            res.append(cycleType.getDescription(locale)).append(",");
            res.append(" ").append(BundleUtil.getString(Bundle.ACADEMIC,"label.of.the.male")).append(" ");
        }

        if (!isEmptyDegree() && !degreeType.isEmpty()) {
            res.append(degreeType.getPrefix(locale));
            res.append(degreeType.getFilteredName(locale).toUpperCase());

            if (getDegreeType() == DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA && cycleType != null) {
                res.append(" (").append(cycleType.getDescription(locale)).append(")");
            }
            res.append(" ").append(BundleUtil.getString(Bundle.ACADEMIC,"label.in")).append(" ");
        }

        res.append(degree.getFilteredName(executionYear, locale).toUpperCase());

        return res.toString();
    }

    public String getDegreeCurricularPlanName() {
        return getLastDegreeCurricularPlan().getName();
    }

    @Override
    final public Degree getDegree() {
        return super.getDegree() != null ? super.getDegree() : (hasAnyStudentCurricularPlans() ? getLastStudentCurricularPlan()
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
        return getDegreeType().getAdministrativeOfficeType() == AdministrativeOfficeType.DEGREE;
    }

    final public boolean isForOffice(final AdministrativeOffice administrativeOffice) {
        return getDegree().getAdministrativeOffice().equals(administrativeOffice);
    }

    final public boolean isAllowedToManageRegistration() {
        return AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
                AcademicOperationType.MANAGE_REGISTRATIONS).contains(getDegree())
                || AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
                        AcademicOperationType.VIEW_FULL_STUDENT_CURRICULUM).contains(getDegree());
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
        if (hasAnyRegistrationStates()) {
            RegistrationState activeState = null;
            for (RegistrationState state : getRegistrationStates()) {
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
        for (final RegistrationState state : getRegistrationStates()) {
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

        List<RegistrationState> sortedRegistrationStates = new ArrayList<RegistrationState>(getRegistrationStates());
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
        final List<RegistrationState> sortedRegistrationStates = new ArrayList<RegistrationState>(getRegistrationStates());
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
        List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(getRegistrationStates());
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
        return hasAnyRegistrationStates() ? Collections.min(getRegistrationStates(), RegistrationState.DATE_COMPARATOR) : null;
    }

    final public RegistrationState getLastRegistrationState(final ExecutionYear executionYear) {
        List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(getRegistrationStates());
        Collections.sort(sortedRegistrationsStates, RegistrationState.DATE_COMPARATOR);

        for (ListIterator<RegistrationState> iter = sortedRegistrationsStates.listIterator(sortedRegistrationsStates.size()); iter
                .hasPrevious();) {
            RegistrationState state = iter.previous();
            if (state.getStateDate().isAfter(executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight())) {
                continue;
            }
            return state;
        }

        return null;
    }

    public boolean hasState(final RegistrationStateType stateType) {
        return hasAnyState(Collections.singletonList(stateType));
    }

    public boolean hasAnyState(final Collection<RegistrationStateType> stateTypes) {
        for (final RegistrationState registrationState : getRegistrationStates()) {
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
        for (final RegistrationState registrationState : getRegistrationStates()) {
            if (registrationStateTypes.contains(registrationState.getStateType())) {
                result.add(registrationState);
            }
        }
        return result;
    }

    final public double getEctsCredits() {
        if (!isBolonha() && isRegistrationConclusionProcessed()) {
            return getConclusionProcess().getCredits().doubleValue();
        }

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
        if (getDegreeType().isBolonhaType()) {
            return getLastStudentCurricularPlan().isConclusionProcessed();
        } else {
            return hasConclusionProcess();
        }
    }

    public boolean isRegistrationConclusionProcessed(final CycleType cycleType) {
        if (cycleType == null) {
            return isRegistrationConclusionProcessed();
        } else if (getDegreeType().isBolonhaType() && getDegreeType().hasCycleTypes(cycleType)) {
            return getLastStudentCurricularPlan().isConclusionProcessed(cycleType);
        }

        throw new DomainException("Registration.degree.type.has.no.such.cycle.type");
    }

    public boolean isQualifiedToRegistrationConclusionProcess() {
        return isActive() || isConcluded();
    }

    public ExecutionYear getConclusionYear() {
        return isRegistrationConclusionProcessed() ? getConclusionProcess() != null ? getConclusionProcess().getConclusionYear() : null : null;
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
        return getDegreeType().equals(DegreeType.MASTER_DEGREE);
    }

    public YearMonthDay getConclusionDate() {
        if (isBolonha()) {
            throw new DomainException("error.Registration.for.cannot.get.conclusion.date.in.registration.for.bolonha");
        }

        return isRegistrationConclusionProcessed() ? getConclusionProcess().getConclusionYearMonthDay() : null;
    }

    public YearMonthDay getConclusionDateForBolonha() {
        if (isBolonha()) {
            if (hasConcluded()) {
                final SortedSet<CycleCurriculumGroup> concludeCycles =
                        new TreeSet<CycleCurriculumGroup>(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
                concludeCycles.addAll(getLastStudentCurricularPlan().getInternalCycleCurriculumGrops());
                final CycleCurriculumGroup lastConcludedCycle = concludeCycles.last();
                return (lastConcludedCycle.isConclusionProcessed() ? lastConcludedCycle.getConclusionDate() : lastConcludedCycle
                        .calculateConclusionDate());
            }

        } else {
            return getConclusionDate();
        }
        return null;
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
        if (isBolonha()) {
            return getLastStudentCurricularPlan().getLastApprovementDate();
        } else {
            YearMonthDay result = null;

            for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
                final YearMonthDay date = plan.getLastApprovementDate();
                if (date != null && (result == null || result.isBefore(date))) {
                    result = date;
                }
            }

            if (getDegreeType() == DegreeType.MASTER_DEGREE) {
                final LocalDate date = getDissertationThesisDiscussedDate();
                if (date != null && (result == null || result.isBefore(date))) {
                    result = new YearMonthDay(date);
                }

                if (result == null && hasState(RegistrationStateType.SCHOOLPARTCONCLUDED)) {
                    return getFirstRegistrationState(RegistrationStateType.SCHOOLPARTCONCLUDED).getStateDate().toYearMonthDay();
                }
            }

            if (result == null && hasState(RegistrationStateType.CONCLUDED)) {
                return getFirstRegistrationState(RegistrationStateType.CONCLUDED).getStateDate().toYearMonthDay();
            }

            return result;
        }
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

    final public String getGraduateTitle() {
        return getGraduateTitle((CycleType) null, I18N.getLocale());
    }

    final public String getGraduateTitle(final CycleType cycleType, final Locale locale) {
        if (cycleType == null) {
            if (isRegistrationConclusionProcessed()) {
                return getLastDegreeCurricularPlan().getGraduateTitle(getConclusionYear(), locale);
            }

            throw new DomainException("Registration.is.not.concluded");
        }

        if (hasConcludedCycle(cycleType)) {
            return getLastDegreeCurricularPlan().getGraduateTitle(
                    getLastStudentCurricularPlan().getCycle(cycleType).getConclusionYear(), cycleType, locale);
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

        if (lastStudentCurricularPlan == null || !lastStudentCurricularPlan.isBolonhaDegree()) {
            return hasConclusionProcess();
        }

        for (final CycleCurriculumGroup cycleCurriculumGroup : lastStudentCurricularPlan.getInternalCycleCurriculumGrops()) {
            if (!cycleCurriculumGroup.isConcluded()) {
                return false;
            }
        }

        return !lastStudentCurricularPlan.getCycleCurriculumGroups().isEmpty();
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
            return getLastStudentCurricularPlan().getFirstOrderedCycleCurriculumGroup().getCycleType();
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

    public void conclude() {
        if (isBolonha()) {
            throw new DomainException("error.Registration.cannot.apply.to.bolonha");
        }

        if (isRegistrationConclusionProcessed()) {
            if (!canRepeatConclusionProcess(AccessControl.getPerson())) {
                throw new DomainException("error.Registration.already.concluded");
            }
        }

        if (hasConclusionProcess()) {
            getConclusionProcess().update(new RegistrationConclusionBean(this));
        } else {
            RegistrationConclusionProcess.conclude(new RegistrationConclusionBean(this));
        }

    }

    public boolean canRepeatConclusionProcess(Person person) {
        return AcademicAuthorizationGroup.getProgramsForOperation(person, AcademicOperationType.REPEAT_CONCLUSION_PROCESS)
                .contains(this.getDegree());
    }

    public void editConclusionInformation(final Integer finalAverage, final YearMonthDay conclusion, final String notes) {
        editConclusionInformation(AccessControl.getPerson(), finalAverage, conclusion, notes);
    }

    public void editConclusionInformation(final Person editor, final Integer finalAverage, final YearMonthDay conclusion,
            final String notes) {
        if (!isRegistrationConclusionProcessed()) {
            throw new DomainException("error.Registration.its.only.possible.to.edit.after.conclusion.process.has.been.performed");
        }
        String[] args = {};

        if (finalAverage == null) {
            throw new DomainException("error.Registration.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (conclusion == null) {
            throw new DomainException("error.Registration.argument.must.not.be.null", args1);
        }

        getConclusionProcess().update(editor, finalAverage, conclusion.toLocalDate(), notes);
    }

    public void editConclusionInformation(final Person editor, final Integer finalAverage, final BigDecimal average,
            final YearMonthDay conclusion, final String notes) {

        if (!isRegistrationConclusionProcessed()) {
            throw new DomainException("error.Registration.its.only.possible.to.edit.after.conclusion.process.has.been.performed");
        }
        String[] args = {};

        if (finalAverage == null) {
            throw new DomainException("error.Registration.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (conclusion == null) {
            throw new DomainException("error.Registration.argument.must.not.be.null", args1);
        }
        String[] args2 = {};
        if (average == null) {
            throw new DomainException("error.Registration.argument.must.not.be.null", args2);
        }

        getConclusionProcess().update(editor, finalAverage, average, conclusion.toLocalDate(), notes);
    }

    public void conclude(final CycleCurriculumGroup cycleCurriculumGroup) {
        check(this, RegistrationPredicates.MANAGE_CONCLUSION_PROCESS);
        if (!isBolonha()) {
            throw new DomainException("error.Registration.cannot.apply.to.preBolonha");
        }

        if (cycleCurriculumGroup == null || !getLastStudentCurricularPlan().hasCurriculumModule(cycleCurriculumGroup)) {
            throw new DomainException("error.Registration.invalid.cycleCurriculumGroup");
        }

        cycleCurriculumGroup.conclude();

        if (!isConcluded() && isRegistrationConclusionProcessed()) {
            if (isDEA() && hasPhdIndividualProgramProcess()) {
                RegistrationStateCreator.createState(this, AccessControl.getPerson(), new DateTime(),
                        RegistrationStateType.SCHOOLPARTCONCLUDED);
            } else {
                RegistrationStateCreator.createState(this, AccessControl.getPerson(), new DateTime(),
                        RegistrationStateType.CONCLUDED);
            }

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
        return (degreeType == DegreeType.MASTER_DEGREE || degreeType == DegreeType.BOLONHA_MASTER_DEGREE);
    }

    final public boolean isDEA() {
        return getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
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
        RegistrationDataByExecutionYear registrationData = getRegistrationDataByExecutionYear(year);
        if (registrationData == null) {
            registrationData = new RegistrationDataByExecutionYear(this, year);
        }
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

        if (hasStudentCandidacy()) {
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
            if (studentCurricularPlan.hasAnyNotPayedGratuityEventsForPreviousYears(limitExecutionYear)) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasToPayGratuityOrInsurance() {
        return getInterruptedStudies() ? false : getRegistrationAgreement().isToPayGratuity();
    }

    final public DiplomaRequest getDiplomaRequest(final CycleType cycleType) {
        for (final DocumentRequest documentRequest : getDocumentRequests()) {
            if (documentRequest.isDiploma() && !documentRequest.finishedUnsuccessfully()) {
                final DiplomaRequest diplomaRequest = (DiplomaRequest) documentRequest;
                if (cycleType == null || cycleType == diplomaRequest.getWhatShouldBeRequestedCycle()) {
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
        for (final DocumentRequest documentRequest : getDocumentRequests()) {
            if (documentRequest.isRegistryDiploma() && !documentRequest.finishedUnsuccessfully()) {
                final RegistryDiplomaRequest registryDiplomaRequest = (RegistryDiplomaRequest) documentRequest;
                if (cycleType == null || cycleType == registryDiplomaRequest.getRequestedCycle()) {
                    return registryDiplomaRequest;
                }
            }
        }

        return null;
    }

    final public DiplomaSupplementRequest getDiplomaSupplementRequest(final CycleType cycleType) {
        for (DocumentRequest documentRequest : getDocumentRequests()) {
            if (documentRequest.isDiplomaSupplement() && !documentRequest.finishedUnsuccessfully()) {
                DiplomaSupplementRequest supplement = (DiplomaSupplementRequest) documentRequest;
                if (cycleType == supplement.getRequestedCycle()) {
                    return supplement;
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
        if (hasStudentCandidacy()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.student.Registration.studentCandidacy.cannot.be.modified");
        }

        super.setStudentCandidacy(studentCandidacy);
    }

    final public void removeStudentCandidacy() {
        super.setStudentCandidacy(null);
    }

    final public Boolean getPayedTuition() {
        return !hasAnyNotPayedGratuityEventsForPreviousYears(ExecutionYear.readCurrentExecutionYear());
    }

    @Deprecated
    public void setPayedTuition(Boolean value) {
        throw new UnsupportedOperationException();
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
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.isFor(executionCourse)) {
                return attend;
            }
        }
        return null;
    }

    final public void setRegistrationAgreement(RegistrationAgreement registrationAgreement) {
        if (registrationAgreement == null) {
            registrationAgreement = RegistrationAgreement.NORMAL;
        }
        super.setRegistrationProtocol(RegistrationProtocol.serveRegistrationProtocol(registrationAgreement));

        if (registrationAgreement != null && !registrationAgreement.isNormal() && !hasExternalRegistrationData()) {
            new ExternalRegistrationData(this);
        }
    }

    final public boolean hasGratuityEvent(final ExecutionYear executionYear, final Class<? extends GratuityEvent> type) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
            if (studentCurricularPlan.hasGratuityEvent(executionYear, type)) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasDissertationThesis() {
        if (getDegreeType() == DegreeType.MASTER_DEGREE) {
            return getLastStudentCurricularPlan().hasMasterDegreeThesis();
        } else {
            return getDissertationEnrolment() != null && getDissertationEnrolment().getThesis() != null;
        }
    }

    final public String getDissertationThesisTitle() {
        String result = null;

        if (hasDissertationThesis()) {
            if (getDegreeType() == DegreeType.MASTER_DEGREE) {
                result = getMasterDegreeThesis().getDissertationTitle();
            } else {
                result = getDissertationEnrolment().getThesis().getFinalFullTitle().getContent();
            }

            result = result.trim();
        }

        return result;
    }

    final public LocalDate getDissertationThesisDiscussedDate() {
        if (hasDissertationThesis()) {
            if (getDegreeType() == DegreeType.MASTER_DEGREE) {
                final YearMonthDay proofDateYearMonthDay = getMasterDegreeThesis().getProofDateYearMonthDay();
                return proofDateYearMonthDay != null ? proofDateYearMonthDay.toLocalDate() : null;
            } else {
                final Thesis thesis = getDissertationEnrolment().getThesis();
                return thesis.hasCurrentDiscussedDate() ? thesis.getCurrentDiscussedDate().toLocalDate() : null;
            }
        }

        return null;
    }

    public MasterDegreeThesis getMasterDegreeThesis() {
        MasterDegreeThesis result = null;

        for (final StudentCurricularPlan plan : getSortedStudentCurricularPlans()) {
            final MasterDegreeThesis thesis = plan.getMasterDegreeThesis();
            if (result != null && result.isConcluded() && thesis.isConcluded()) {
                throw new DomainException("error.Registration.more.than.one.concluded.thesis");
            }

            if (result == null || !result.isConcluded()) {
                result = thesis;
            }
        }

        return result;
    }

    final public Enrolment getDissertationEnrolment() {
        return getDissertationEnrolment(null);
    }

    final public Enrolment getDissertationEnrolment(DegreeCurricularPlan degreeCurricularPlan) {
        for (StudentCurricularPlan scp : getStudentCurricularPlans()) {
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
        for (StudentCurricularPlan scp : getStudentCurricularPlans()) {
            if (degreeCurricularPlan != null && scp.getDegreeCurricularPlan() != degreeCurricularPlan) {
                continue;
            }
            enrolments.addAll(scp.getDissertationEnrolments());
        }
        return enrolments;
    }

    final public Proposal getDissertationProposal(final ExecutionYear executionYear) {
        for (final GroupStudent groupStudent : getAssociatedGroupStudents()) {
            final FinalDegreeWorkGroup group = groupStudent.getFinalDegreeDegreeWorkGroup();
            final Proposal proposalAttributedByCoordinator = group.getProposalAttributed();
            if (proposalAttributedByCoordinator != null && proposalAttributedByCoordinator.isForExecutionYear(executionYear)) {
                return proposalAttributedByCoordinator;
            }
            final Proposal proposalAttributedByTeacher = group.getProposalAttributedByTeacher();
            if (proposalAttributedByTeacher != null && proposalAttributedByTeacher.isForExecutionYear(executionYear)) {
                // if
                // (proposalAttributedByTeacher.isProposalConfirmedByTeacherAndStudents(group))
                // {
                return proposalAttributedByTeacher;
                // }
            }
        }
        return null;
    }

    final public boolean isAvailableDegreeTypeForInquiries() {
        final DegreeType degreeType = getDegreeType();
        return degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE
                || degreeType == DegreeType.BOLONHA_MASTER_DEGREE
                || ExecutionCourse.THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.contains(getDegree().getSigla().toLowerCase());
    }

    final public boolean hasInquiryResponseFor(final ExecutionCourse executionCourse) {
        for (final InquiriesRegistry inquiriesRegistry : getAssociatedInquiriesRegistries()) {
            if (inquiriesRegistry.getExecutionCourse() == executionCourse) {
                return true;
            }
        }
        return false;
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
        for (final ExternalEnrolment externalEnrolment : this.getExternalEnrolments()) {
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

    public RegistrationAgreement getRegistrationAgreement() {
        return super.getRegistrationProtocol().getRegistrationAgreement();
    }

    public Registration getSourceRegistrationForTransition() {
        if (!getLastDegreeCurricularPlan().hasEquivalencePlan()) {
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
        check(this, RegistrationPredicates.TRANSIT_TO_BOLONHA);

        if (!isActive()) {
            throw new DomainException("error.student.Registration.cannot.transit.non.active.registrations");
        }

        RegistrationStateCreator.createState(this, person, when, RegistrationStateType.TRANSITED);

        for (final Registration registration : getTargetTransitionRegistrations()) {
            if (registration.getDegreeType() == DegreeType.BOLONHA_DEGREE) {
                RegistrationStateCreator.createState(registration, person, when,
                        registration.hasConcluded() ? RegistrationStateType.CONCLUDED : RegistrationStateType.REGISTERED);
            } else {
                RegistrationStateCreator.createState(registration, person, when, RegistrationStateType.REGISTERED);
            }

            registration.setRegistrationAgreement(getRegistrationAgreement());
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
        return isActive() && getRegistrationAgreement().isEnrolmentByStudentAllowed()
                && getDegreeTypesToEnrolByStudent().contains(getDegreeType());
    }

    public List<DegreeType> getDegreeTypesToEnrolByStudent() {
        return DEGREE_TYPES_TO_ENROL_BY_STUDENT;
    }

    public boolean isEnrolmentByStudentInShiftsAllowed() {
        return isActive();
    }

    @Deprecated
    final public boolean getIsForDegreeOffice() {
        return isForOffice(AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE));
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

    public boolean isSeniorStatuteApplicable(ExecutionYear executionYear) {

        if (hasAlreadySeniorStatute(executionYear)) {
            return false;
        }

        if (hasBeenSeniorForTheLastTwoConsecutiveYears(executionYear)) {
            return false;
        }

        return getDegreeType().hasSeniorEligibility(this, executionYear);
    }

    private boolean hasAlreadySeniorStatute(ExecutionYear executionYear) {
        for (SeniorStatute seniorStatute : getSeniorStatuteSet()) {
            if (seniorStatute.isValidOnAnyExecutionPeriodFor(executionYear)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasBeenSeniorForTheLastTwoConsecutiveYears(ExecutionYear executionYear) {
        if (!isSenior(executionYear)) {
            return false;
        }
        if (!isSenior(executionYear.getPreviousExecutionYear())) {
            return false;
        }

        return true;
    }

    private boolean isSenior(ExecutionYear executionYear) {
        for (SeniorStatute seniorStatute : getSeniorStatuteSet()) {
            if (seniorStatute.isValidOnAnyExecutionPeriodFor(executionYear.getPreviousExecutionYear())) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public StudentStatute grantSeniorStatute(ExecutionYear executionYear) {
        return StudentStatuteType.SENIOR.createStudentStatute(getStudent(), this, executionYear.getFirstExecutionPeriod(),
                executionYear.getLastExecutionPeriod());
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
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
            for (final EnrolmentOutOfPeriodEvent event : studentCurricularPlan.getEnrolmentOutOfPeriodEvents()) {
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
        if (hasPhdIndividualProgramProcess()) {
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
        if (hasPhdIndividualProgramProcess()) {
            return false;
        }

        if (getPrecedentDegreeInformation(executionYear) != null
                && !getPersonalInformationBean(executionYear).isEditableByAcademicService()) {
            return false;
        }

        return true;
    }

    public boolean isReingression(final ExecutionYear executionYear) {
        final SortedSet<RegistrationState> states = new TreeSet<RegistrationState>(RegistrationState.DATE_COMPARATOR);
        states.addAll(getRegistrationStates());

        Registration sourceRegistration = getSourceRegistration();
        while (sourceRegistration != null) {
            states.addAll(sourceRegistration.getRegistrationStates());
            sourceRegistration = sourceRegistration.getSourceRegistration();
        }

        if (states.size() == 0) {
            return false;
        }

        RegistrationState previous = null;
        for (final RegistrationState registrationState : states) {
            if (previous != null) {
                if (registrationState.getExecutionYear() == executionYear
                        && (registrationState.isActive() || registrationState.getStateType() == RegistrationStateType.TRANSITED)
                        && (previous.getStateType() == RegistrationStateType.EXTERNAL_ABANDON
                        || previous.getStateType() == RegistrationStateType.INTERRUPTED || previous.getStateType() == RegistrationStateType.FLUNKED)) {
                    return true;
                }
            }

            previous = registrationState;
        }

        return false;

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
        for (PrecedentDegreeInformation precedentDegreeInfo : getPrecedentDegreesInformations()) {
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
        for (PrecedentDegreeInformation pdi : getPrecedentDegreesInformations()) {
            if (!pdi.getExecutionYear().isAfter(currentExecutionYear)) {
                degreeInformations.add(pdi);
            }
        }

        if (degreeInformations.isEmpty()) {
            return null;
        }
        return degreeInformations.iterator().next();
    }

    public int getNumberEnroledCurricularCoursesInCurrentYear() {
        return getLastStudentCurricularPlan() == null ? 0 : getLastStudentCurricularPlan().countEnrolments(
                ExecutionYear.readCurrentExecutionYear());
    }

    public List<CycleCurriculumGroup> getInternalCycleCurriculumGrops() {
        return getLastStudentCurricularPlan().getInternalCycleCurriculumGrops();
    }

    public Collection<CurriculumGroup> getAllCurriculumGroups() {
        Collection<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
        for (final StudentCurricularPlan plan : getStudentCurricularPlans()) {
            result.addAll(plan.getAllCurriculumGroups());
        }
        return result;
    }

    public Collection<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        Collection<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
        for (final StudentCurricularPlan plan : getStudentCurricularPlans()) {
            result.addAll(plan.getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups());
        }
        return result;
    }

    public Boolean hasIndividualCandidacyFor(final ExecutionYear executionYear) {
        return hasIndividualCandidacy()
                && getIndividualCandidacy().getCandidacyProcess().getCandidacyExecutionInterval().equals(executionYear);
    }

    public YearDelegateElection getYearDelegateElectionsGivenExecutionYear(ExecutionYear executionYear) {
        for (DelegateElection delegateElection : getStudent().getDelegateElections()) {
            if (delegateElection instanceof YearDelegateElection) {
                if (delegateElection.getExecutionYear().equals(executionYear) && delegateElection.getDegree().equals(getDegree())) {
                    return (YearDelegateElection) delegateElection;
                }
            }
        }
        return null;
    }

    public void exportValues(StringBuilder result) {
        Formatter formatter = new Formatter(result);
        final Student student = getStudent();
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC,"label.ingression"), getIngression() == null ? " - " : getIngression()
                .getFullDescription());
        formatter.format("%s: %d\n", BundleUtil.getString(Bundle.ACADEMIC,"label.studentNumber"), student.getNumber());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC,"label.Student.Person.name"), student.getPerson().getName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC,"label.degree"), getDegree().getPresentationName());
        formatter.close();
    }

    public RegistrationState getLastActiveState() {
        List<RegistrationState> activeStateList = new ArrayList<RegistrationState>();

        CollectionUtils.select(getRegistrationStates(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((RegistrationState) arg0).getStateType().isActive();
            }

        }, activeStateList);

        return !activeStateList.isEmpty() ? Collections.max(activeStateList, RegistrationState.DATE_COMPARATOR) : null;
    }

    @SuppressWarnings("unchecked")
    public static String readAllStudentInfo() {
        JSONArray infos = new JSONArray();
        for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.isConcluded()) {
                ExecutionYear conclusionYear = new RegistrationConclusionBean(registration).calculateConclusionYear();
                String endDate = Integer.toString(conclusionYear.getEndDateYearMonthDay().getYear());
                String startDate = Integer.toString(registration.getStartDate().getYear());
                String studentName = registration.getName();
                String email = registration.getEmail();
                String number = Integer.toString(registration.getNumber());
                // String number =
                // Integer.toString(registration.getStudent().getNumber());
                String degreeRemoteOid = registration.getDegree().getExternalId();
                String username = registration.getPerson().getUsername();
                JSONObject studentInfo = new JSONObject();
                studentInfo.put("username", username);
                studentInfo.put("studentName", studentName);
                studentInfo.put("email", email);
                studentInfo.put("number", number);
                studentInfo.put("endDate", endDate);
                studentInfo.put("startDate", startDate);
                studentInfo.put("degreeRemoteOid", degreeRemoteOid);
                infos.add(studentInfo);
            }
        }
        final String jsonString = infos.toJSONString();
        return jsonString;
    }

    @SuppressWarnings("unchecked")
    public static String readAllStudentsInfoForJobBank() {
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        Set<Registration> registrations = new HashSet<Registration>();
        LocalDate today = new LocalDate();
        for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.hasAnyActiveState(currentExecutionYear) && registration.isBolonha()
                    && !registration.getDegreeType().equals(DegreeType.EMPTY)) {
                registrations.add(registration);
            }
        }
        for (ConclusionProcess conclusionProcess : Bennu.getInstance().getConclusionProcessesSet()) {
            if (conclusionProcess.getConclusionDate() != null
                    && !conclusionProcess.getConclusionDate().plusYears(1).isBefore(today)) {
                registrations.add(conclusionProcess.getRegistration());
            }
        }
        JSONArray infos = new JSONArray();
        for (Registration registration : registrations) {
            infos.add(getStudentInfoForJobBank(registration));
        }
        return infos.toJSONString();
    }

    @SuppressWarnings("unchecked")
    private static JSONObject getStudentInfoForJobBank(Registration registration) {
        try {
            JSONObject studentInfoForJobBank = new JSONObject();
            studentInfoForJobBank.put("username", registration.getPerson().getUsername());
            studentInfoForJobBank.put("hasPersonalDataAuthorization", registration.getStudent()
                    .hasPersonalDataAuthorizationForProfessionalPurposesAt().toString());
            Person person = registration.getStudent().getPerson();
            studentInfoForJobBank.put("dateOfBirth", person.getDateOfBirthYearMonthDay() == null ? null : person
                    .getDateOfBirthYearMonthDay().toString());
            studentInfoForJobBank.put("nationality", person.getCountry() == null ? null : person.getCountry().getName());
            studentInfoForJobBank.put("address", person.getDefaultPhysicalAddress() == null ? null : person
                    .getDefaultPhysicalAddress().getAddress());
            studentInfoForJobBank.put("area", person.getDefaultPhysicalAddress() == null ? null : person
                    .getDefaultPhysicalAddress().getArea());
            studentInfoForJobBank.put("areaCode", person.getDefaultPhysicalAddress() == null ? null : person
                    .getDefaultPhysicalAddress().getAreaCode());
            studentInfoForJobBank.put("districtSubdivisionOfResidence",
                    person.getDefaultPhysicalAddress() == null ? null : person.getDefaultPhysicalAddress()
                            .getDistrictSubdivisionOfResidence());
            studentInfoForJobBank.put("mobilePhone", person.getDefaultMobilePhoneNumber());
            studentInfoForJobBank.put("phone", person.getDefaultPhoneNumber());
            studentInfoForJobBank.put("email", person.getEmailForSendingEmails());
            studentInfoForJobBank.put("remoteRegistrationOID", registration.getExternalId());
            studentInfoForJobBank.put("number", registration.getNumber().toString());
            studentInfoForJobBank.put("degreeOID", registration.getDegree().getExternalId());
            studentInfoForJobBank.put("isConcluded", String.valueOf(registration.isRegistrationConclusionProcessed()));
            studentInfoForJobBank.put("curricularYear", String.valueOf(registration.getCurricularYear()));
            for (CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
                CycleCurriculumGroup cycle = registration.getLastStudentCurricularPlan().getCycle(cycleType);
                if (cycle != null) {
                    studentInfoForJobBank.put(cycle.getCycleType().name(), cycle.getAverage().toString());
                }
            }
            return studentInfoForJobBank;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new Error(e);
        }
    }

    public boolean hasDissertationEnrolment(final ExecutionDegree executionDegree) {
        final ExecutionYear previousExecutionYear = executionDegree.getExecutionYear();
        if (previousExecutionYear.hasNextExecutionYear()) {
            final ExecutionYear executionYear = previousExecutionYear.getNextExecutionYear();
            for (final Attends attends : getAssociatedAttendsSet()) {
                if (attends.getExecutionYear() == executionYear && attends.hasEnrolment()) {
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
        return isActive() && isBolonha() && !getDegreeType().isEmpty() && !RAIDES_MOBILITY.contains(getRegistrationAgreement());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry> getStudentsInquiryRegistries() {
        return getStudentsInquiryRegistriesSet();
    }

    @Deprecated
    public boolean hasAnyStudentsInquiryRegistries() {
        return !getStudentsInquiryRegistriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment> getWrittenEvaluationEnrolments() {
        return getWrittenEvaluationEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyWrittenEvaluationEnrolments() {
        return !getWrittenEvaluationEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.CurriculumLineLog> getCurriculumLineLogs() {
        return getCurriculumLineLogsSet();
    }

    @Deprecated
    public boolean hasAnyCurriculumLineLogs() {
        return !getCurriculumLineLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Delegate> getDelegates() {
        return getDelegatesSet();
    }

    @Deprecated
    public boolean hasAnyDelegates() {
        return !getDelegatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent> getSecretaryEnrolmentStudents() {
        return getSecretaryEnrolmentStudentsSet();
    }

    @Deprecated
    public boolean hasAnySecretaryEnrolmentStudents() {
        return !getSecretaryEnrolmentStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent> getAssociatedGroupStudents() {
        return getAssociatedGroupStudentsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedGroupStudents() {
        return !getAssociatedGroupStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog> getStudentTestsLogs() {
        return getStudentTestsLogsSet();
    }

    @Deprecated
    public boolean hasAnyStudentTestsLogs() {
        return !getStudentTestsLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.SeniorStatute> getSeniorStatute() {
        return getSeniorStatuteSet();
    }

    @Deprecated
    public boolean hasAnySeniorStatute() {
        return !getSeniorStatuteSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.Advise> getAdvises() {
        return getAdvisesSet();
    }

    @Deprecated
    public boolean hasAnyAdvises() {
        return !getAdvisesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion> getStudentTestsQuestions() {
        return getStudentTestsQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyStudentTestsQuestions() {
        return !getStudentTestsQuestionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent> getTeacherDegreeFinalProjectStudent() {
        return getTeacherDegreeFinalProjectStudentSet();
    }

    @Deprecated
    public boolean hasAnyTeacherDegreeFinalProjectStudent() {
        return !getTeacherDegreeFinalProjectStudentSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.RegistrationDataByExecutionYear> getRegistrationDataByExecutionYear() {
        return getRegistrationDataByExecutionYearSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationDataByExecutionYear() {
        return !getRegistrationDataByExecutionYearSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission> getOutboundMobilityCandidacySubmission() {
        return getOutboundMobilityCandidacySubmissionSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacySubmission() {
        return !getOutboundMobilityCandidacySubmissionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest> getAcademicServiceRequests() {
        return getAcademicServiceRequestsSet();
    }

    @Deprecated
    public boolean hasAnyAcademicServiceRequests() {
        return !getAcademicServiceRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.StudentCurricularPlan> getStudentCurricularPlans() {
        return getStudentCurricularPlansSet();
    }

    @Deprecated
    public boolean hasAnyStudentCurricularPlans() {
        return !getStudentCurricularPlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Registration> getDestinyRegistrations() {
        return getDestinyRegistrationsSet();
    }

    @Deprecated
    public boolean hasAnyDestinyRegistrations() {
        return !getDestinyRegistrationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent> getDfaRegistrationEvents() {
        return getDfaRegistrationEventsSet();
    }

    @Deprecated
    public boolean hasAnyDfaRegistrationEvents() {
        return !getDfaRegistrationEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateLog> getRegistrationStateLogs() {
        return getRegistrationStateLogsSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationStateLogs() {
        return !getRegistrationStateLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment> getExternalEnrolments() {
        return getExternalEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyExternalEnrolments() {
        return !getExternalEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRegistry> getAssociatedInquiriesRegistries() {
        return getAssociatedInquiriesRegistriesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesRegistries() {
        return !getAssociatedInquiriesRegistriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ShiftEnrolment> getShiftEnrolments() {
        return getShiftEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyShiftEnrolments() {
        return !getShiftEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction> getInsuranceTransactions() {
        return getInsuranceTransactionsSet();
    }

    @Deprecated
    public boolean hasAnyInsuranceTransactions() {
        return !getInsuranceTransactionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.RegistrationRegime> getRegistrationRegimes() {
        return getRegistrationRegimesSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationRegimes() {
        return !getRegistrationRegimesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getPrecedentDegreesInformations() {
        return getPrecedentDegreesInformationsSet();
    }

    @Deprecated
    public boolean hasAnyPrecedentDegreesInformations() {
        return !getPrecedentDegreesInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState> getRegistrationStates() {
        return getRegistrationStatesSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationStates() {
        return !getRegistrationStatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Attends> getAssociatedAttends() {
        return getAssociatedAttendsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedAttends() {
        return !getAssociatedAttendsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Shift> getShifts() {
        return getShiftsSet();
    }

    @Deprecated
    public boolean hasAnyShifts() {
        return !getShiftsSet().isEmpty();
    }

    @Deprecated
    public boolean hasEntryPhase() {
        return getEntryPhase() != null;
    }

    @Deprecated
    public boolean hasArithmeticMeanClassification() {
        return getArithmeticMeanClassification() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasApprovationRatioClassification() {
        return getApprovationRatioClassification() != null;
    }

    @Deprecated
    public boolean hasRequestedChangeBranch() {
        return getRequestedChangeBranch() != null;
    }

    @Deprecated
    public boolean hasRegistrationNumber() {
        return getRegistrationNumber() != null;
    }

    @Deprecated
    public boolean hasStudiesStartDate() {
        return getStudiesStartDate() != null;
    }

    @Deprecated
    public boolean hasRegistrationProtocol() {
        return getRegistrationProtocol() != null;
    }

    @Deprecated
    public boolean hasStudentCandidacy() {
        return getStudentCandidacy() != null;
    }

    @Deprecated
    public boolean hasMeasurementTestRoom() {
        return getMeasurementTestRoom() != null;
    }

    @Deprecated
    public boolean hasStartDate() {
        return getStartDate() != null;
    }

    @Deprecated
    public boolean hasAgreementInformation() {
        return getAgreementInformation() != null;
    }

    @Deprecated
    public boolean hasHomologationDate() {
        return getHomologationDate() != null;
    }

    @Deprecated
    public boolean hasSourceRegistration() {
        return getSourceRegistration() != null;
    }

    @Deprecated
    public boolean hasRegistrationYear() {
        return getRegistrationYear() != null;
    }

    @Deprecated
    public boolean hasExternalRegistrationData() {
        return getExternalRegistrationData() != null;
    }

    @Deprecated
    public boolean hasSpecializationDegreeRegistrationEvent() {
        return getSpecializationDegreeRegistrationEvent() != null;
    }

    @Deprecated
    public boolean hasIndividualCandidacy() {
        return getIndividualCandidacy() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasRequestedChangeDegree() {
        return getRequestedChangeDegree() != null;
    }

    @Deprecated
    public boolean hasSenior() {
        return getSenior() != null;
    }

    @Deprecated
    public boolean hasConclusionProcess() {
        return getConclusionProcess() != null;
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasIngression() {
        return getIngression() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasPhdIndividualProgramProcess() {
        return getPhdIndividualProgramProcess() != null;
    }

    @Deprecated
    public boolean hasPrecedentDegreeInformation() {
        return getPrecedentDegreeInformation() != null;
    }

    @Deprecated
    public boolean hasEntryGradeClassification() {
        return getEntryGradeClassification() != null;
    }

    @Deprecated
    public boolean hasInquiryStudentCycleAnswer() {
        return getInquiryStudentCycleAnswer() != null;
    }

}
