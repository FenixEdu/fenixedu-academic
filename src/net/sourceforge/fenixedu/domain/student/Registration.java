package net.sourceforge.fenixedu.domain.student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.YearStudentSpecialSeasonCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegisteredState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class Registration extends Registration_Base {

    private transient Double approvationRatio;

    private transient Double arithmeticMean;

    private transient Integer approvedEnrollmentsNumber = 0;

    public final static Comparator<Registration> NUMBER_COMPARATOR = new Comparator<Registration>() {
	public int compare(Registration o1, Registration o2) {
	    return o1.getNumber().compareTo(o2.getNumber());
	}
    };

    public Registration() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setStartDate(new YearMonthDay());
	new RegisteredState(this, AccessControl.getUserView() != null ? AccessControl.getUserView()
		.getPerson() : null, new DateTime());
    }

    public Registration(Person person, StudentCandidacy studentCandidacy) {
	this(person, null, RegistrationAgreement.NORMAL, studentCandidacy);
    }

    public Registration(Person person, Integer studentNumber) {
	this(person, studentNumber, RegistrationAgreement.NORMAL, null);
    }

    public Registration(Person person, DegreeCurricularPlan degreeCurricularPlan,
	    StudentCandidacy studentCandidacy) {
	this(person, null, RegistrationAgreement.NORMAL, studentCandidacy, degreeCurricularPlan);

	// create scp
	StudentCurricularPlan.createBolonhaStudentCurricularPlan(this, degreeCurricularPlan,
		StudentCurricularPlanState.ACTIVE, new YearMonthDay(), ExecutionPeriod
			.readActualExecutionPeriod());
    }

    public Registration(Person person, DegreeCurricularPlan degreeCurricularPlan) {
	this(person, null, RegistrationAgreement.NORMAL, null, degreeCurricularPlan);

	// create scp
	StudentCurricularPlan.createBolonhaStudentCurricularPlan(this, degreeCurricularPlan,
		StudentCurricularPlanState.ACTIVE, new YearMonthDay(), ExecutionPeriod
			.readActualExecutionPeriod());
    }

    private Registration(Person person, Integer studentNumber, RegistrationAgreement agreement,
	    StudentCandidacy studentCandidacy, DegreeCurricularPlan degreeCurricularPlan) {
	this(person, studentNumber, agreement, studentCandidacy);
	if (degreeCurricularPlan != null) {
	    setDegree(degreeCurricularPlan.getDegree());
	}
    }

    private Registration(Person person, Integer studentNumber, RegistrationAgreement agreement,
	    StudentCandidacy studentCandidacy) {
	this();
	if (person.hasStudent()) {
	    setStudent(person.getStudent());
	} else {
	    setStudent(new Student(person, studentNumber));
	}
	setPayedTuition(true);
	setRegistrationAgreement(agreement);
	setStudentCandidacy(studentCandidacy);
	setRegistrationYear(ExecutionYear.readCurrentExecutionYear());
	setRequestedChangeDegree(false);
	setRequestedChangeBranch(false);
    }

    @Deprecated
    public void delete() {

	for (; !getStudentCurricularPlans().isEmpty(); getStudentCurricularPlans().get(0).delete())
	    ;

	removeStudent();
	removeRootDomainObject();
	getShiftsSet().clear();
	super.deleteDomainObject();
    }

    public StudentCurricularPlan getActiveStudentCurricularPlan() {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE
		    || studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.SCHOOLPARTCONCLUDED) {
		return studentCurricularPlan;
	    }
	}
	return null;
    }

    public StudentCurricularPlan getConcludedStudentCurricularPlan() {
	if (isConcluded()) {
	    return getLastStudentCurricularPlanExceptPast();
	}
	return null;
    }

    public StudentCurricularPlan getLastStudentCurricularPlan() {

	if (getStudentCurricularPlans().size() == 0) {
	    return null;
	}
	return (StudentCurricularPlan) Collections.max(getStudentCurricularPlans(),
		StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE);
    }

    public StudentCurricularPlan getLastStudentCurricularPlanExceptPast() {

	if (getStudentCurricularPlansExceptPast().size() == 0) {
	    return null;
	}
	return (StudentCurricularPlan) Collections.max(getStudentCurricularPlansExceptPast(),
		new BeanComparator("startDateYearMonthDay"));
    }

    public StudentCurricularPlan getCurrentStudentCurricularPlan() {
	return getLastStudentCurricularPlanExceptPast();
    }

    public StudentCurricularPlan getFirstStudentCurricularPlan() {
	return (StudentCurricularPlan) Collections.min(getStudentCurricularPlans(), new BeanComparator(
		"startDateYearMonthDay"));
    }

    public List<StudentCurricularPlan> getStudentCurricularPlansExceptPast() {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : super.getStudentCurricularPlans()) {
	    if (!studentCurricularPlan.isPast()) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    public StudentCurricularPlan getActiveOrConcludedStudentCurricularPlan() {
	StudentCurricularPlan concludedStudentCurricularPlan = null;
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE) {
		return studentCurricularPlan;
	    }
	    if (concludedStudentCurricularPlan == null
		    && studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.SCHOOLPARTCONCLUDED) {
		concludedStudentCurricularPlan = studentCurricularPlan;
	    }
	}
	return concludedStudentCurricularPlan;
    }

    public StudentCurricularPlan getActiveOrConcludedOrLastStudentCurricularPlan() {
	StudentCurricularPlan studentCurricularPlan = getActiveOrConcludedStudentCurricularPlan();
	if (studentCurricularPlan == null) {
	    studentCurricularPlan = getLastStudentCurricularPlan();
	}
	return studentCurricularPlan;
    }

    public boolean attends(final ExecutionCourse executionCourse) {
	for (final Attends attends : getAssociatedAttends()) {
	    if (attends.getDisciplinaExecucao() == executionCourse) {
		return true;
	    }
	}
	return false;
    }

    public List<WrittenEvaluation> getWrittenEvaluations(final ExecutionPeriod executionPeriod) {
	final List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();
	for (final Attends attend : this.getAssociatedAttends()) {
	    if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		for (final Evaluation evaluation : attend.getDisciplinaExecucao()
			.getAssociatedEvaluations()) {
		    if (evaluation instanceof WrittenEvaluation && !result.contains(evaluation)) {
			result.add((WrittenEvaluation) evaluation);
		    }
		}
	    }
	}
	return result;
    }

    public List<Exam> getEnroledExams(final ExecutionPeriod executionPeriod) {
	final List<Exam> result = new ArrayList<Exam>();
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
		.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof Exam
		    && writtenEvaluationEnrolment.isForExecutionPeriod(executionPeriod)) {
		result.add((Exam) writtenEvaluationEnrolment.getWrittenEvaluation());
	    }
	}
	return result;
    }

    public List<Exam> getUnenroledExams(final ExecutionPeriod executionPeriod) {
	final List<Exam> result = new ArrayList<Exam>();
	for (final Attends attend : this.getAssociatedAttends()) {
	    if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		for (final Evaluation evaluation : attend.getDisciplinaExecucao()
			.getAssociatedEvaluations()) {
		    if (evaluation instanceof Exam && !this.isEnroledIn(evaluation)) {
			result.add((Exam) evaluation);
		    }
		}
	    }
	}
	return result;
    }

    public List<WrittenTest> getEnroledWrittenTests(final ExecutionPeriod executionPeriod) {
	final List<WrittenTest> result = new ArrayList<WrittenTest>();
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
		.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof WrittenTest
		    && writtenEvaluationEnrolment.isForExecutionPeriod(executionPeriod)) {
		result.add((WrittenTest) writtenEvaluationEnrolment.getWrittenEvaluation());
	    }
	}
	return result;
    }

    public List<WrittenTest> getUnenroledWrittenTests(final ExecutionPeriod executionPeriod) {
	final List<WrittenTest> result = new ArrayList<WrittenTest>();
	for (final Attends attend : this.getAssociatedAttends()) {
	    if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		for (final Evaluation evaluation : attend.getDisciplinaExecucao()
			.getAssociatedEvaluations()) {
		    if (evaluation instanceof WrittenTest && !this.isEnroledIn(evaluation)) {
			result.add((WrittenTest) evaluation);
		    }
		}
	    }
	}
	return result;
    }

    public List<Project> getProjects(final ExecutionPeriod executionPeriod) {
	final List<Project> result = new ArrayList<Project>();
	for (final Attends attend : this.getAssociatedAttends()) {
	    if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		for (final Evaluation evaluation : attend.getDisciplinaExecucao()
			.getAssociatedEvaluations()) {
		    if (evaluation instanceof Project) {
			result.add((Project) evaluation);
		    }
		}
	    }
	}
	return result;
    }

    public boolean isEnroledIn(final Evaluation evaluation) {
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
		.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() == evaluation) {
		return true;
	    }
	}
	return false;
    }

    public OldRoom getRoomFor(final WrittenEvaluation writtenEvaluation) {
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
		.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() == writtenEvaluation) {
		return writtenEvaluationEnrolment.getRoom();
	    }
	}
	return null;
    }

    public Double getApprovationRatio() {
	if (this.approvationRatio == null) {
	    calculateApprovationRatioAndArithmeticMeanIfActive(true);
	}
	return this.approvationRatio;
    }

    public Double getArithmeticMean() {
	if (this.arithmeticMean == null) {
	    calculateApprovationRatioAndArithmeticMeanIfActive(true);
	}
	return Double.valueOf(Math.round(this.arithmeticMean * 100) / 100.0);
    }

    public double getAverage() {
	return isConcluded() ? getFinalAverage() : getAverage(null);
    }

    public double getAverage(final ExecutionYear executionYear) {
	return new StudentCurriculum(this).getRoundedAverage(executionYear, true);
    }

    @Override
    public Integer getFinalAverage() {
	if (!isConcluded()) {
	    throw new DomainException(
		    "Registration.getting.final.average.mean.from.non.concluded.registration");
	}

	return super.getFinalAverage() != null ? super.getFinalAverage() : new StudentCurriculum(this)
		.getRoundedAverage(null, false).intValue();
    }

    public void calculateApprovationRatioAndArithmeticMeanIfActive(boolean onlyPreviousExecutionYear) {

	int enrollmentsNumber = 0;
	int approvedEnrollmentsNumber = 0;
	int actualApprovedEnrollmentsNumber = 0;
	int totalGrade = 0;

	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	ExecutionYear previousExecutionYear = currentExecutionYear.getPreviousExecutionYear();

	for (StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
	    for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
		if (enrolment.getEnrolmentCondition() == EnrollmentCondition.INVISIBLE) {
		    continue;
		}

		ExecutionYear enrolmentExecutionYear = enrolment.getExecutionPeriod().getExecutionYear();
		if (onlyPreviousExecutionYear && (previousExecutionYear != enrolmentExecutionYear)) {
		    continue;
		}

		if (enrolmentExecutionYear != currentExecutionYear) {

		    enrollmentsNumber++;
		    if (enrolment.getEnrollmentState() == EnrollmentState.APROVED) {
			actualApprovedEnrollmentsNumber++;

			Integer finalGrade = enrolment.getFinalGrade();
			if (finalGrade != null) {
			    approvedEnrollmentsNumber++;
			    totalGrade += finalGrade;
			} else {
			    enrollmentsNumber--;
			}
		    }
		}
	    }
	}

	setApprovedEnrollmentsNumber(Integer.valueOf(actualApprovedEnrollmentsNumber));

	setApprovationRatio((enrollmentsNumber == 0) ? 0 : (double) approvedEnrollmentsNumber
		/ enrollmentsNumber);
	setArithmeticMean((approvedEnrollmentsNumber == 0) ? 0 : (double) totalGrade
		/ approvedEnrollmentsNumber);

    }

    private void setApprovationRatio(Double approvationRatio) {
	this.approvationRatio = approvationRatio;
    }

    private void setArithmeticMean(Double arithmeticMean) {
	this.arithmeticMean = arithmeticMean;
    }

    public Integer getApprovedEnrollmentsNumber() {
	if (this.approvedEnrollmentsNumber == null) {
	    calculateApprovationRatioAndArithmeticMeanIfActive(true);
	}
	return approvedEnrollmentsNumber;
    }

    private void setApprovedEnrollmentsNumber(Integer approvedEnrollmentsNumber) {
	this.approvedEnrollmentsNumber = approvedEnrollmentsNumber;
    }

    public Collection<Enrolment> getEnrolments(final ExecutionYear executionYear) {
	return getStudentCurricularPlan(executionYear).getEnrolmentsByExecutionYear(executionYear);
    }

    public Collection<Enrolment> getApprovedEnrolments() {
	final Collection<Enrolment> result = new HashSet<Enrolment>();

	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    result.addAll(studentCurricularPlan.getAprovedEnrolments());
	}

	return result;
    }

    public boolean hasAnyApprovedEnrolment() {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.hasAnyApprovedEnrolment()) {
		return true;
	    }
	}

	return false;
    }

    public DateTime getLastApprovedEnrolmentEvaluationDate() {
	final StudentCurricularPlan studentCurricularPlan = getLastStudentCurricularPlanExceptPast();
	final SortedSet<Enrolment> enrolments = new TreeSet<Enrolment>(
		Enrolment.COMPARATOR_BY_EXECUTION_PERIOD);
	enrolments.addAll(studentCurricularPlan.getAprovedEnrolments());

	final Iterator<Enrolment> iterator = enrolments.tailSet(enrolments.last()).iterator();
	final Enrolment oneEnrolment = iterator.next();
	final ExecutionPeriod lastExecutionPeriod = oneEnrolment.getExecutionPeriod();
	DateTime result = oneEnrolment.getLatestEnrolmentEvaluation().getWhenDateTime();
	while (iterator.hasNext()) {
	    final Enrolment anotherEnrolment = iterator.next();
	    if (anotherEnrolment.getExecutionPeriod() != lastExecutionPeriod) {
		break;
	    }

	    final EnrolmentEvaluation enrolmentEvaluation = anotherEnrolment
		    .getLatestEnrolmentEvaluation();
	    if (enrolmentEvaluation.getWhenDateTime().isAfter(result)) {
		result = enrolmentEvaluation.getWhenDateTime();
	    }
	}

	return result;
    }

    public List<Advise> getAdvisesByTeacher(final Teacher teacher) {
	return (List<Advise>) CollectionUtils.select(getAdvises(), new Predicate() {

	    public boolean evaluate(Object arg0) {
		Advise advise = (Advise) arg0;
		return advise.getTeacher() == teacher;
	    }
	});
    }

    public List<Advise> getAdvisesByType(final AdviseType adviseType) {
	return (List<Advise>) CollectionUtils.select(getAdvises(), new Predicate() {
	    public boolean evaluate(Object arg0) {
		Advise advise = (Advise) arg0;
		return advise.getAdviseType().equals(adviseType);
	    }
	});
    }

    public Set<Attends> getOrderedAttends() {
	final Set<Attends> result = new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR);
	result.addAll(getAssociatedAttends());
	return result;
    }

    public int countCompletedCoursesForActiveUndergraduateCurricularPlan() {
	return getActiveStudentCurricularPlan().getAprovedEnrolments().size();
    }

    public List<StudentCurricularPlan> getStudentCurricularPlansByStateAndType(
	    StudentCurricularPlanState state, DegreeType type) {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getCurrentState().equals(state)
		    && studentCurricularPlan.getDegreeCurricularPlan().getDegree().getTipoCurso()
			    .equals(type)) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    public List<StudentCurricularPlan> getStudentCurricularPlansBySpecialization(
	    Specialization specialization) {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getSpecialization() != null
		    && studentCurricularPlan.getSpecialization().equals(specialization)) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    public List<StudentCurricularPlan> getStudentCurricularPlansBySpecializationAndState(
	    Specialization specialization, StudentCurricularPlanState state) {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getSpecialization() != null
		    && studentCurricularPlan.getSpecialization().equals(specialization)
		    && studentCurricularPlan.getCurrentState() != null
		    && studentCurricularPlan.getCurrentState().equals(state)) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    public List<Attends> readAttendsInCurrentExecutionPeriod() {
	final List<Attends> attends = new ArrayList<Attends>();
	for (final Attends attend : this.getAssociatedAttendsSet()) {
	    if (attend.getDisciplinaExecucao().getExecutionPeriod().getState().equals(
		    PeriodState.CURRENT)) {
		attends.add(attend);
	    }
	}
	return attends;
    }

    public List<Attends> readAttendsByExecutionPeriod(ExecutionPeriod executionPeriod) {
	List<Attends> attends = new ArrayList<Attends>();
	for (Attends attend : this.getAssociatedAttends()) {
	    if (attend.getDisciplinaExecucao().getExecutionPeriod().equals(executionPeriod)) {
		attends.add(attend);
	    }
	}
	return attends;
    }

    public static Registration readByUsername(String username) {
	final Person person = Person.readPersonByUsername(username);
	if (person != null) {
	    for (final Registration registration : person.getStudentsSet()) {
		return registration;
	    }
	}
	return null;
    }

    public static Registration readStudentByNumberAndDegreeType(Integer number, DegreeType degreeType) {
	for (Registration registration : RootDomainObject.getInstance().getRegistrations()) {
	    if (registration.getNumber().equals(number)
		    && registration.getDegreeType().equals(degreeType)) {
		return registration;
	    }
	}
	return null;
    }

    public static Registration readRegistrationByNumberAndDegreeTypes(Integer number,
	    DegreeType... degreeTypes) {
	final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
	for (Registration registration : RootDomainObject.getInstance().getRegistrations()) {
	    if (registration.getNumber().equals(number)
		    && degreeTypesList.contains(registration.getDegreeType())) {
		return registration;
	    }
	}
	return null;
    }

    public static Registration readByNumber(Integer number) {
	for (Registration registration : RootDomainObject.getInstance().getRegistrationsSet()) {
	    if (registration.getNumber().equals(number)) {
		return registration;
	    }
	}
	return null;
    }

    public static List<Registration> readMasterDegreeStudentsByNameDocIDNumberIDTypeAndStudentNumber(
	    String studentName, String docIdNumber, IDDocumentType idType, Integer studentNumber) {

	final List<Registration> registrations = new ArrayList<Registration>();

	if (studentNumber != null && studentNumber > 0) {
	    registrations.add(Registration.readRegistrationByNumberAndDegreeTypes(studentNumber,
		    DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
	}

	if (!StringUtils.isEmpty(studentName)) {
	    for (Person person : Person.readPersonsByName(studentName)) {
		if (person.hasStudent()) {
		    registrations.addAll(person.getStudent().getRegistrationsByDegreeTypes(
			    DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
		}
	    }
	}

	if (!StringUtils.isEmpty(docIdNumber)) {
	    for (Person person : Person.readByDocumentIdNumber(docIdNumber)) {
		if (person.hasStudent()) {
		    registrations.addAll(person.getStudent().getRegistrationsByDegreeTypes(
			    DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
		}
	    }
	}

	return registrations;
    }

    public static List<Registration> readAllStudentsBetweenNumbers(Integer fromNumber, Integer toNumber) {
	int fromNumberInt = fromNumber.intValue();
	int toNumberInt = toNumber.intValue();

	final List<Registration> students = new ArrayList<Registration>();
	for (final Registration registration : RootDomainObject.getInstance().getRegistrationsSet()) {
	    int studentNumberInt = registration.getNumber().intValue();
	    if (studentNumberInt >= fromNumberInt && studentNumberInt <= toNumberInt) {
		students.add(registration);
	    }
	}
	return students;
    }

    @Deprecated
    public static List<Registration> readStudentsByDegreeType(DegreeType degreeType) {
	return readRegistrationsByDegreeType(degreeType);
    }

    public static List<Registration> readRegistrationsByDegreeType(DegreeType degreeType) {
	final List<Registration> students = new ArrayList<Registration>();
	for (final Registration registration : RootDomainObject.getInstance().getRegistrationsSet()) {
	    if (registration.getDegreeType().equals(degreeType)) {
		students.add(registration);
	    }
	}
	return students;
    }

    public GratuitySituation readGratuitySituationByExecutionDegree(ExecutionDegree executionDegree) {
	GratuityValues gratuityValues = executionDegree.getGratuityValues();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    GratuitySituation gratuitySituation = studentCurricularPlan
		    .getGratuitySituationByGratuityValues(gratuityValues);
	    if (gratuitySituation != null) {
		return gratuitySituation;
	    }
	}
	return null;
    }

    public Group findFinalDegreeWorkGroupForCurrentExecutionYear() {
	for (final GroupStudent groupStudent : getAssociatedGroupStudents()) {
	    final Group group = groupStudent.getFinalDegreeDegreeWorkGroup();
	    final ExecutionDegree executionDegree = group.getExecutionDegree();
	    final ExecutionYear executionYear = executionDegree.getExecutionYear()
		    .getNextExecutionYear();
	    if (executionYear != null && executionYear.getState().equals(PeriodState.CURRENT)) {
		return group;
	    }
	}
	return null;
    }

    public List readAllInsuranceTransactionByExecutionYear(ExecutionYear executionYear) {
	List<InsuranceTransaction> insuranceTransactions = new ArrayList<InsuranceTransaction>();
	for (InsuranceTransaction insuranceTransaction : this.getInsuranceTransactions()) {
	    if (insuranceTransaction.getExecutionYear().equals(executionYear)) {
		insuranceTransactions.add(insuranceTransaction);
	    }
	}
	return insuranceTransactions;
    }

    public List<InsuranceTransaction> readAllNonReimbursedInsuranceTransactionsByExecutionYear(
	    ExecutionYear executionYear) {
	List<InsuranceTransaction> nonReimbursedInsuranceTransactions = new ArrayList<InsuranceTransaction>();
	for (InsuranceTransaction insuranceTransaction : this.getInsuranceTransactions()) {
	    if (insuranceTransaction.getExecutionYear().equals(executionYear)) {
		GuideEntry guideEntry = insuranceTransaction.getGuideEntry();
		if (guideEntry == null || guideEntry.getReimbursementGuideEntries().isEmpty()) {
		    nonReimbursedInsuranceTransactions.add(insuranceTransaction);
		} else {
		    boolean isReimbursed = false;
		    for (ReimbursementGuideEntry reimbursementGuideEntry : guideEntry
			    .getReimbursementGuideEntries()) {
			if (reimbursementGuideEntry.getReimbursementGuide()
				.getActiveReimbursementGuideSituation().getReimbursementGuideState()
				.equals(ReimbursementGuideState.PAYED)) {
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
    public boolean hasToPayMasterDegreeInsurance(final ExecutionYear executionYear) {
	final boolean isSpecialization = (getActiveStudentCurricularPlan().getSpecialization() == Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION);
	final ExecutionDegree executionDegreeByYear = getActiveDegreeCurricularPlan()
		.getExecutionDegreeByYear(executionYear);
	if (isSpecialization && (executionDegreeByYear == null || !executionDegreeByYear.isFirstYear())) {
	    return false;
	}

	return readAllNonReimbursedInsuranceTransactionsByExecutionYear(executionYear).isEmpty();
    }

    public Enrolment findEnrolmentByEnrolmentID(final Integer enrolmentID) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    final Enrolment enrolment = studentCurricularPlan.findEnrolmentByEnrolmentID(enrolmentID);
	    if (enrolment != null) {
		return enrolment;
	    }
	}
	return null;
    }

    public Collection<DocumentRequest> getDocumentRequests() {
	final Set<DocumentRequest> result = new HashSet<DocumentRequest>();
	for (AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
	    if (academicServiceRequest.isDocumentRequest()) {
		result.add((DocumentRequest) academicServiceRequest);
	    }
	}
	return result;
    }

    public boolean hasDegreeDiplomaDocumentRequest() {
	for (final DocumentRequest documentRequest : getDocumentRequests()) {
	    if (documentRequest.isDegreeDiploma()) {
		return true;
	    }
	}

	return false;
    }

    // Special Season

    public SpecialSeasonCode getSpecialSeasonCodeByExecutionYear(ExecutionYear executionYear) {
	for (YearStudentSpecialSeasonCode yearStudentSpecialSeasonCode : getYearStudentSpecialSeasonCodesSet()) {
	    if (yearStudentSpecialSeasonCode.getExecutionYear() == executionYear) {
		return yearStudentSpecialSeasonCode.getSpecialSeasonCode();
	    }
	}
	return null;
    }

    public void setSpecialSeasonCode(ExecutionYear executionYear, SpecialSeasonCode specialSeasonCode) {
	if (specialSeasonCode == null) {
	    if (!getActiveStudentCurricularPlan().getSpecialSeasonEnrolments(executionYear).isEmpty()) {
		throw new DomainException("error.cannot.change.specialSeasonCode");
	    } else {
		deleteYearSpecialSeasonCode(executionYear);
	    }
	} else {
	    if (specialSeasonCode.getMaxEnrolments() < getActiveStudentCurricularPlan()
		    .getSpecialSeasonEnrolments(executionYear).size()) {
		throw new DomainException("error.cannot.change.specialSeasonCode");
	    } else {
		changeYearSpecialSeasonCode(executionYear, specialSeasonCode);
	    }
	}
    }

    private void changeYearSpecialSeasonCode(ExecutionYear executionYear,
	    SpecialSeasonCode specialSeasonCode) {
	YearStudentSpecialSeasonCode yearStudentSpecialSeasonCode = getYearStudentSpecialSeasonCodeByExecutionYear(executionYear);
	if (yearStudentSpecialSeasonCode != null) {
	    yearStudentSpecialSeasonCode.setSpecialSeasonCode(specialSeasonCode);
	} else {
	    new YearStudentSpecialSeasonCode(this, executionYear, specialSeasonCode);
	}
    }

    private YearStudentSpecialSeasonCode getYearStudentSpecialSeasonCodeByExecutionYear(
	    ExecutionYear executionYear) {
	for (YearStudentSpecialSeasonCode yearStudentSpecialSeasonCode : getYearStudentSpecialSeasonCodesSet()) {
	    if (yearStudentSpecialSeasonCode.getExecutionYear() == executionYear) {
		return yearStudentSpecialSeasonCode;
	    }
	}
	return null;
    }

    private void deleteYearSpecialSeasonCode(ExecutionYear executionYear) {
	for (YearStudentSpecialSeasonCode yearStudentSpecialSeasonCode : getYearStudentSpecialSeasonCodesSet()) {
	    if (yearStudentSpecialSeasonCode.getExecutionYear() == executionYear) {
		yearStudentSpecialSeasonCode.delete();
	    }
	}
    }

    // end Special Season

    // Improvement

    public List<Enrolment> getEnrolmentsToImprov(ExecutionPeriod executionPeriod) {

	if (executionPeriod == null) {
	    throw new DomainException("error.executionPeriod.notExist");
	}

	List<Enrolment> enrolmentsToImprov = new ArrayList<Enrolment>();
	for (StudentCurricularPlan scp : getStudentCurricularPlans()) {
	    if (scp.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(DegreeType.DEGREE)) {
		enrolmentsToImprov.addAll(scp.getEnrolmentsToImprov(executionPeriod));
	    }
	}
	return enrolmentsToImprov;
    }

    public List<Enrolment> getEnroledImprovements() {
	List<Enrolment> enroledImprovements = new ArrayList<Enrolment>();
	for (StudentCurricularPlan scp : getStudentCurricularPlans()) {
	    if (scp.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(DegreeType.DEGREE)) {
		enroledImprovements.addAll(scp.getEnroledImprovements());
	    }
	}
	return enroledImprovements;
    }

    public List<ExecutionCourse> getAttendingExecutionCoursesForCurrentExecutionPeriod() {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    if (attends.getDisciplinaExecucao().getExecutionPeriod().getState().equals(
		    PeriodState.CURRENT)) {
		result.add(attends.getDisciplinaExecucao());
	    }
	}
	return result;
    }

    public List<ExecutionCourse> getAttendingExecutionCoursesFor(final ExecutionPeriod executionPeriod) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    if (attends.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		result.add(attends.getDisciplinaExecucao());
	    }
	}
	return result;
    }

    public List<ExecutionCourse> getAttendingExecutionCoursesFor(final ExecutionYear executionYear) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    if (attends.isFor(executionYear)) {
		result.add(attends.getExecutionCourse());
	    }
	}

	return result;
    }

    public List<Attends> getAttendsForExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final List<Attends> result = new ArrayList<Attends>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    if (attends.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		result.add(attends);
	    }
	}
	return result;
    }

    public List<Shift> getShiftsForCurrentExecutionPeriod() {
	final List<Shift> result = new ArrayList<Shift>();
	for (final Shift shift : getShiftsSet()) {
	    if (shift.getDisciplinaExecucao().getExecutionPeriod().getState()
		    .equals(PeriodState.CURRENT)) {
		result.add(shift);
	    }
	}
	return result;
    }

    public List<Shift> getShiftsFor(final ExecutionPeriod executionPeriod) {
	final List<Shift> result = new ArrayList<Shift>();
	for (final Shift shift : getShiftsSet()) {
	    if (shift.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		result.add(shift);
	    }
	}
	return result;
    }

    public List<Shift> getShiftsFor(final ExecutionCourse executionCourse) {
	final List<Shift> result = new ArrayList<Shift>();
	for (final Shift shift : getShiftsSet()) {
	    if (shift.getDisciplinaExecucao() == executionCourse) {
		result.add(shift);
	    }
	}
	return result;
    }

    private int countNumberOfDistinctExecutionCoursesOfShiftsFor(final ExecutionPeriod executionPeriod) {
	final Set<ExecutionCourse> result = new HashSet<ExecutionCourse>();
	for (final Shift shift : getShiftsSet()) {
	    if (shift.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		result.add(shift.getDisciplinaExecucao());
	    }
	}
	return result.size();
    }

    public Integer getNumberOfExecutionCoursesWithEnroledShiftsFor(final ExecutionPeriod executionPeriod) {
	return getAttendingExecutionCoursesFor(executionPeriod).size()
		- countNumberOfDistinctExecutionCoursesOfShiftsFor(executionPeriod);
    }

    public Integer getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(
	    final ExecutionPeriod executionPeriod) {
	int result = 0;
	final List<Shift> enroledShifts = getShiftsFor(executionPeriod);
	for (final ExecutionCourse executionCourse : getAttendingExecutionCoursesFor(executionPeriod)) {
	    for (final ShiftType shiftType : executionCourse.getOldShiftTypesToEnrol()) {
		if (!enroledShiftsContainsShiftWithSameExecutionCourseAndShiftType(enroledShifts,
			executionCourse, shiftType)) {
		    result++;
		    break;
		}
	    }
	}
	return Integer.valueOf(result);
    }

    private boolean enroledShiftsContainsShiftWithSameExecutionCourseAndShiftType(
	    final List<Shift> enroledShifts, final ExecutionCourse executionCourse,
	    final ShiftType shiftType) {

	return CollectionUtils.exists(enroledShifts, new Predicate() {
	    public boolean evaluate(Object object) {
		Shift enroledShift = (Shift) object;
		return enroledShift.getDisciplinaExecucao() == executionCourse
			&& enroledShift.getTipo() == shiftType;
	    }
	});
    }

    public Set<SchoolClass> getSchoolClassesToEnrol() {
	final Set<SchoolClass> result = new HashSet<SchoolClass>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    final ExecutionCourse executionCourse = attends.getDisciplinaExecucao();

	    if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
		result.addAll(getSchoolClassesToEnrolBy(executionCourse));
	    }
	}
	return result;
    }

    public Set<SchoolClass> getSchoolClassesToEnrolBy(final ExecutionCourse executionCourse) {
	Set<SchoolClass> schoolClasses = executionCourse
		.getSchoolClassesBy(getActiveStudentCurricularPlan().getDegreeCurricularPlan());
	return schoolClasses.isEmpty() ? executionCourse.getSchoolClasses() : schoolClasses;
    }

    public void addAttendsTo(final ExecutionCourse executionCourse) {

	checkIfReachedAttendsLimit();

	if (getStudent().readAttendByExecutionCourse(executionCourse) == null) {
	    final Enrolment enrolment = findEnrolment(getActiveStudentCurricularPlan(), executionCourse,
		    executionCourse.getExecutionPeriod());
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
	List<Attends> attendsInExecutionPeriod = getAttendsForExecutionPeriod(executionCourse
		.getExecutionPeriod());
	for (Attends attends : attendsInExecutionPeriod) {
	    for (CurricularCourse curricularCourse : attends.getExecutionCourse()
		    .getAssociatedCurricularCoursesSet()) {
		if (executionCourse.getAssociatedCurricularCoursesSet().contains(curricularCourse)) {
		    return attends;
		}
	    }
	}
	return null;
    }

    private Enrolment findEnrolment(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionCourse executionCourse, final ExecutionPeriod executionPeriod) {
	for (final CurricularCourse curricularCourse : executionCourse
		.getAssociatedCurricularCoursesSet()) {
	    final Enrolment enrolment = studentCurricularPlan
		    .getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);
	    if (enrolment != null) {
		return enrolment;
	    }
	}
	return null;
    }

    private static final int MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD = 8;

    private void checkIfReachedAttendsLimit() {
	if (readAttendsInCurrentExecutionPeriod().size() >= MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD) {
	    throw new DomainException("error.student.reached.attends.limit", String
		    .valueOf(MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD));
	}
    }

    public void removeAttendFor(final ExecutionCourse executionCourse) {
	final Attends attend = getStudent().readAttendByExecutionCourse(executionCourse);
	if (attend != null) {
	    checkIfHasEnrolmentFor(attend);
	    checkIfHasShiftsFor(executionCourse);
	    attend.delete();
	}
    }

    private void checkIfHasEnrolmentFor(final Attends attend) {
	if (attend.hasEnrolment()) {
	    throw new DomainException("errors.student.already.enroled");
	}
    }

    private void checkIfHasShiftsFor(final ExecutionCourse executionCourse) {
	if (!getShiftsFor(executionCourse).isEmpty()) {
	    throw new DomainException("errors.student.already.enroled.in.shift");
	}
    }

    public boolean isActive() {
	RegistrationStateType activeStateType = getActiveStateType();
	return activeStateType.equals(RegistrationStateType.REGISTERED)
		|| activeStateType.equals(RegistrationStateType.MOBILITY);
    }

    public boolean hasAnyEnrolmentsIn(final ExecutionYear executionYear) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		if (enrolment.getExecutionPeriod().getExecutionYear() == executionYear) {
		    return true;
		}
	    }
	}
	return false;

    }

    public Tutor getAssociatedTutor() {

	StudentCurricularPlan activeStudentCurricularPlan = this
		.getActiveOrConcludedStudentCurricularPlan();
	if (activeStudentCurricularPlan == null) {
	    activeStudentCurricularPlan = this.getLastStudentCurricularPlan();
	}

	return activeStudentCurricularPlan.getAssociatedTutor();
    }

    @Override
    public Integer getNumber() {
	return (super.getNumber() != null) ? super.getNumber() : getStudent().getNumber();
    }

    public Person getPerson() {
	return getStudent().getPerson();
    }

    public String getEmail() {
	return getPerson().getEmail();
    }

    public EntryPhase getEntryPhase() {
	if (hasStudentCandidacy()) {
	    return getStudentCandidacy().getEntryPhase();
	}
	return null;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
	if (hasStudentCandidacy()) {
	    getStudentCandidacy().setEntryPhase(entryPhase);
	} else {
	    throw new DomainException("error.registration.withou.student.candidacy");
	}
    }

    public Double getEntryGrade() {
	return hasStudentCandidacy() ? getStudentCandidacy().getEntryGrade() : null;
    }

    public void setEntryGrade(Double entryGrade) {
	if (hasStudentCandidacy()) {
	    getStudentCandidacy().setEntryGrade(entryGrade);
	} else {
	    throw new DomainException("error.registration.withou.student.candidacy");
	}
    }

    public String getIngression() {
	return hasStudentCandidacy() ? getStudentCandidacy().getIngression() : null;
    }

    public Ingression getIngressionEnum() {
	return getIngression() != null ? Ingression.valueOf(getIngression()) : null;
    }

    public void setIngression(String ingression) {
	if (hasStudentCandidacy()) {
	    getStudentCandidacy().setIngression(ingression);
	} else {
	    throw new DomainException("error.registration.withou.student.candidacy");
	}

    }

    public String getContigent() {
	return hasStudentCandidacy() ? getStudentCandidacy().getContigent() : null;
    }

    public void setContigent(String contigent) {
	if (hasStudentCandidacy()) {
	    getStudentCandidacy().setContigent(contigent);
	} else {
	    throw new DomainException("error.registration.withou.student.candidacy");
	}

    }

    @Override
    public Degree getDegree() {
	return super.getDegree() != null ? super.getDegree() : getLastStudentCurricularPlan()
		.getDegree();
    }

    public DegreeType getDegreeType() {
	return getDegree().getDegreeType();
    }

    public boolean isBolonha() {
	return getDegreeType().isBolonhaType();
    }

    public boolean isActiveForOffice(Unit office) {
	return isActive() && isForOffice(office.getAdministrativeOffice());
    }

    public boolean isForOffice(final AdministrativeOffice administrativeOffice) {
	return getDegreeType().getAdministrativeOfficeType().equals(
		administrativeOffice.getAdministrativeOfficeType());
    }

    public boolean getIsForOffice() {
	final AdministrativeOffice administrativeOffice = AccessControl.getPerson().getEmployee()
		.getAdministrativeOffice();
	return isForOffice(administrativeOffice);
    }

    public List<NewTestGroup> getPublishedTestGroups() {
	List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();

	for (ExecutionCourse executionCourse : this
		.getAttendingExecutionCoursesForCurrentExecutionPeriod()) {
	    testGroups.addAll(executionCourse.getPublishedTestGroups());
	}

	return testGroups;
    }

    public List<NewTestGroup> getFinishedTestGroups() {
	List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();

	for (ExecutionCourse executionCourse : this
		.getAttendingExecutionCoursesForCurrentExecutionPeriod()) {
	    testGroups.addAll(executionCourse.getFinishedTestGroups());
	}

	return testGroups;
    }

    public DegreeCurricularPlan getActiveOrConcludedOrLastDegreeCurricularPlan() {
	final StudentCurricularPlan studentCurricularPlan = getActiveOrConcludedOrLastStudentCurricularPlan();
	return studentCurricularPlan == null ? null : studentCurricularPlan.getDegreeCurricularPlan();

    }

    public boolean isCurricularCourseApproved(final CurricularCourse curricularCourse) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isConcluded() {
	return getActiveStateType() == RegistrationStateType.CONCLUDED;
    }

    public YearMonthDay getConclusionDate() {
	return getActiveStateType() == RegistrationStateType.CONCLUDED ? getActiveState().getStateDate()
		.toYearMonthDay() : getLastApprovedEnrolmentEvaluationDate().toYearMonthDay();
    }

    public double getEctsCredits() {
	return new StudentCurriculum(this).getTotalEctsCredits(null);
    }

    public int getCurricularYear() {
	return new StudentCurriculum(this).calculateCurricularYear(null);
    }

    public int getCurricularYear(ExecutionYear executionYear) {
	return new StudentCurriculum(this).calculateCurricularYear(executionYear);
    }

    public boolean hasApprovement(ExecutionYear executionYear) {
	int curricularYearInTheBegin = getCurricularYear(executionYear);
	int curricularYearAtTheEnd = getCurricularYear(executionYear.getNextExecutionYear());

	if (curricularYearInTheBegin > curricularYearAtTheEnd) {
	    throw new DomainException("Registration.curricular.year.has.decreased");
	}

	return curricularYearAtTheEnd > curricularYearInTheBegin;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	final DegreeType degreeType = getDegreeType();
	return (degreeType == DegreeType.DEGREE || degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
    }

    public boolean isMasterDegreeOrBolonhaMasterDegree() {
	final DegreeType degreeType = getDegreeType();
	return (degreeType == DegreeType.MASTER_DEGREE || degreeType == DegreeType.BOLONHA_MASTER_DEGREE);
    }

    public EnrolmentModel getEnrolmentModelForCurrentExecutionYear() {
	return getEnrolmentModelForExecutionYear(ExecutionYear.readCurrentExecutionYear());
    }

    public EnrolmentModel getEnrolmentModelForExecutionYear(ExecutionYear year) {
	RegistrationDataByExecutionYear registrationData = getRegistrationDataByExecutionYear(year);
	return registrationData != null ? registrationData.getEnrolmentModel() : null;
    }

    public void setEnrolmentModelForCurrentExecutionYear(EnrolmentModel model) {
	setEnrolmentModelForExecutionYear(ExecutionYear.readCurrentExecutionYear(), model);
    }

    public void setEnrolmentModelForExecutionYear(ExecutionYear year, EnrolmentModel model) {
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
    public ExecutionYear getRegistrationYear() {
	return super.getRegistrationYear() == null ? getFirstEnrolmentExecutionYear() : super
		.getRegistrationYear();
    }

    public boolean isFirstTime(ExecutionYear executionYear) {
	return getRegistrationYear() == executionYear;
    }

    public boolean isFirstTime() {
	return isFirstTime(ExecutionYear.readCurrentExecutionYear());
    }

    public Collection<ExecutionYear> getEnrolmentsExecutionYears() {
	final Collection<ExecutionYear> result = new ArrayList<ExecutionYear>();

	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		result.add(enrolment.getExecutionPeriod().getExecutionYear());
	    }
	}

	return result;
    }

    public SortedSet<ExecutionYear> getSortedEnrolmentsExecutionYears() {
	final SortedSet<ExecutionYear> result = new TreeSet<ExecutionYear>(
		ExecutionYear.EXECUTION_YEAR_COMPARATOR_BY_YEAR);
	result.addAll(getEnrolmentsExecutionYears());

	return result;
    }

    public ExecutionYear getFirstEnrolmentExecutionYear() {
	return getSortedEnrolmentsExecutionYears().first();
    }

    public ExecutionYear getLastEnrolmentExecutionYear() {
	return getSortedEnrolmentsExecutionYears().last();
    }

    public Collection<ExecutionPeriod> getEnrolmentsExecutionPeriods() {
	final Collection<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();

	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		result.add(enrolment.getExecutionPeriod());
	    }
	}

	return result;
    }

    public SortedSet<ExecutionPeriod> getSortedEnrolmentsExecutionPeriods() {
	final SortedSet<ExecutionPeriod> result = new TreeSet<ExecutionPeriod>(
		ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
	result.addAll(getEnrolmentsExecutionPeriods());

	return result;
    }

    public StudentCurricularPlan getStudentCurricularPlan(final ExecutionYear executionYear) {
	return executionYear == null ? getStudentCurricularPlan(new YearMonthDay())
		: getStudentCurricularPlan(executionYear.getEndDateYearMonthDay());
    }

    public StudentCurricularPlan getStudentCurricularPlan(final YearMonthDay date) {
	StudentCurricularPlan result = null;
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    final YearMonthDay startDate = studentCurricularPlan.getStartDateYearMonthDay();
	    if (!startDate.isAfter(date)
		    && (result == null || startDate.isAfter(result.getStartDateYearMonthDay()))) {
		result = studentCurricularPlan;
	    }
	}
	return result;
    }

    public StudentCurricularPlan getStudentCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
		return studentCurricularPlan;
	    }
	}
	return null;
    }

    @Override
    public YearMonthDay getStartDate() {
	return super.getStartDate() != null ? super.getStartDate() : getStudentCandidacy()
		.getActiveCandidacySituation().getSituationDate().toYearMonthDay();
    }

    public boolean isCustomEnrolmentModel(final ExecutionYear executionYear) {
	return getEnrolmentModelForExecutionYear(executionYear) == EnrolmentModel.CUSTOM;
    }

    public boolean isCustomEnrolmentModel() {
	return isCustomEnrolmentModel(ExecutionYear.readCurrentExecutionYear());
    }

    public boolean isCompleteEnrolmentModel(final ExecutionYear executionYear) {
	return getEnrolmentModelForExecutionYear(executionYear) == EnrolmentModel.COMPLETE;
    }

    public boolean isCompleteEnrolmentModel() {
	return isCompleteEnrolmentModel(ExecutionYear.readCurrentExecutionYear());
    }

    public BigDecimal getTotalEctsCredits(final ExecutionYear executionYear) {
	BigDecimal totalEctsCredits = BigDecimal.ZERO;
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    for (final Enrolment enrolment : studentCurricularPlan
		    .getEnrolmentsByExecutionYear(executionYear)) {
		totalEctsCredits = totalEctsCredits.add(BigDecimal.valueOf(enrolment
			.getCurricularCourse().getEctsCredits()));
	    }
	}

	return totalEctsCredits;
    }

    public DegreeCurricularPlan getActiveDegreeCurricularPlan() {
	return getActiveStudentCurricularPlan().getDegreeCurricularPlan();
    }

    private boolean hasAnyNotPayedGratuityEvents() {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.hasAnyNotPayedGratuityEvents()) {
		return true;
	    }
	}

	return false;
    }

    private boolean hasAnyNotPayedGratuityEventsForPreviousYears() {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.hasAnyNotPayedGratuityEventsForPreviousYears()) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasToPayGratuityOrInsurance() {
	if (getInterruptedStudies()) {
	    return false;
	}

	return getRegistrationAgreement().equals(RegistrationAgreement.NORMAL);

    }

    public boolean getInterruptedStudies() {
	return getActiveStateType().equals(RegistrationStateType.INTERRUPTED);
    }

    public RegistrationState getActiveState() {
	return hasAnyRegistrationStates() ? Collections.max(getRegistrationStates(),
		RegistrationState.DATE_COMPARATOR) : null;
    }

    public RegistrationStateType getActiveStateType() {
	final RegistrationState activeState = getActiveState();
	return activeState != null ? activeState.getStateType() : RegistrationStateType.REGISTERED;
    }

    public RegistrationState getStateInDate(DateTime dateTime) {

	List<RegistrationState> sortedRegistrationStates = new ArrayList<RegistrationState>(
		getRegistrationStates());
	Collections.sort(sortedRegistrationStates, RegistrationState.DATE_COMPARATOR);

	for (ListIterator<RegistrationState> iterator = sortedRegistrationStates
		.listIterator(sortedRegistrationStates.size()); iterator.hasPrevious();) {

	    RegistrationState registrationState = iterator.previous();
	    if (!dateTime.isBefore(registrationState.getStateDate())) {
		return registrationState;
	    }
	}

	return null;
    }

    public boolean isInRegisteredState() {
	return getActiveStateType().equals(RegistrationStateType.REGISTERED);
    }

    public boolean isInRegisteredState(ExecutionYear executionYear) {
	final Set<RegistrationStateType> registrationStatesTypes = getRegistrationStatesTypes(executionYear);

	return (registrationStatesTypes.contains(RegistrationStateType.REGISTERED)
		|| hasAnyEnrolmentsIn(executionYear) || registrationStatesTypes
		.contains(RegistrationStateType.MOBILITY))
		&& (!registrationStatesTypes.contains(RegistrationStateType.CANCELED)
			&& !registrationStatesTypes.contains(RegistrationStateType.INTERRUPTED)
			&& !registrationStatesTypes.contains(RegistrationStateType.INTERNAL_ABANDON) && !registrationStatesTypes
			.contains(RegistrationStateType.EXTERNAL_ABANDON));
    }

    public Set<RegistrationState> getRegistrationStates(final ExecutionYear executionYear) {
	final Set<RegistrationState> result = new HashSet<RegistrationState>();

	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(
		getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, RegistrationState.DATE_COMPARATOR);

	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates
		.listIterator(sortedRegistrationsStates.size()); iter.hasPrevious();) {
	    RegistrationState state = (RegistrationState) iter.previous();

	    if (state.getStateDate().isAfter(
		    executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight())) {
		continue;
	    }

	    result.add(state);

	    if (state.getStateDate().isBefore(
		    executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight())) {
		break;
	    }

	}

	return result;
    }

    public RegistrationState getLastRegistrationState(final ExecutionYear executionYear) {
	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(
		getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, RegistrationState.DATE_COMPARATOR);

	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates
		.listIterator(sortedRegistrationsStates.size()); iter.hasPrevious();) {
	    RegistrationState state = (RegistrationState) iter.previous();
	    if (state.getStateDate().isAfter(
		    executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight())) {
		continue;
	    }
	    return state;
	}

	return null;
    }

    public Set<RegistrationStateType> getRegistrationStatesTypes(final ExecutionYear executionYear) {
	final Set<RegistrationStateType> result = new HashSet<RegistrationStateType>();

	for (final RegistrationState registrationState : getRegistrationStates(executionYear)) {
	    result.add(registrationState.getStateType());
	}

	return result;
    }

    public boolean hasDegreeDiplomaRequest() {
	for (final DocumentRequest documentRequest : this.getDocumentRequests()) {
	    if (documentRequest.isDegreeDiploma()) {
		return true;
	    }
	}
	return false;
    }

    public Set<DocumentRequest> getSucessfullyFinishedDocumentRequestsBy(ExecutionYear executionYear,
	    DocumentRequestType documentRequestType, boolean collectDocumentsMarkedAsFreeProcessed) {

	final Set<DocumentRequest> result = new HashSet<DocumentRequest>();

	for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
	    if (academicServiceRequest instanceof DocumentRequest) {
		final DocumentRequest documentRequest = (DocumentRequest) academicServiceRequest;
		if (documentRequest.getDocumentRequestType() == documentRequestType
			&& documentRequest.finishedSuccessfully()
			&& executionYear.containsDate(documentRequest.getCreationDate())
			&& (!collectDocumentsMarkedAsFreeProcessed || documentRequest.isFreeProcessed())) {

		    result.add((DocumentRequest) academicServiceRequest);
		}
	    }
	}
	return result;
    }

    public Collection<? extends AcademicServiceRequest> getAcademicServiceRequests(
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

    public Collection<AcademicServiceRequest> getNewAcademicServiceRequests() {
	return (Collection<AcademicServiceRequest>) getAcademicServiceRequests(AcademicServiceRequestSituationType.NEW);
    }

    public Collection<AcademicServiceRequest> getProcessingAcademicServiceRequests() {
	return (Collection<AcademicServiceRequest>) getAcademicServiceRequests(AcademicServiceRequestSituationType.PROCESSING);
    }

    public Collection<AcademicServiceRequest> getConcludedAcademicServiceRequests() {
	return (Collection<AcademicServiceRequest>) getAcademicServiceRequests(AcademicServiceRequestSituationType.CONCLUDED);
    }

    public Collection<AcademicServiceRequest> getHistoricalAcademicServiceRequests() {
	final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

	for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
	    if (academicServiceRequest.isHistorical()) {
		result.add(academicServiceRequest);
	    }
	}

	return result;
    }

    public boolean getFlunked() {
	return getActiveStateType().equals(RegistrationStateType.FLUNKED);
    }

    public boolean isInactive() {
	return getActiveStateType().isInactive();
    }

    public void addApprovedEnrolments(final Collection<Enrolment> enrolments) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    studentCurricularPlan.addApprovedEnrolments(enrolments);
	}
    }

    public Campus getCampus() {
	return getLastStudentCurricularPlanExceptPast().getLastCampus();
    }

    public String getIstUniversity() {
	return getCampus().getName();
    }

    @Override
    public void setStudentCandidacy(StudentCandidacy studentCandidacy) {
	if (hasStudentCandidacy()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.student.Registration.studentCandidacy.cannot.be.modified");
	}

	super.setStudentCandidacy(studentCandidacy);
    }

    @Override
    public void removeStudentCandidacy() {
	super.removeStudentCandidacy();
    }

    @Override
    public Boolean getPayedTuition() {
	return super.getPayedTuition() != null && super.getPayedTuition()
		&& !hasAnyNotPayedGratuityEventsForPreviousYears();
    }

    public Boolean getPayedOldTuition() {
	return super.getPayedTuition() != null && super.getPayedTuition();
    }

    public boolean getHasGratuityDebtsCurrently() {
	return hasGratuityDebtsCurrently();
    }

    public boolean hasGratuityDebtsCurrently() {
	return !super.getPayedTuition() || hasAnyNotPayedGratuityEvents();
    }

    public Attends readAttendByExecutionCourse(ExecutionCourse executionCourse) {
	return getStudent().readAttendByExecutionCourse(executionCourse);
    }

}
