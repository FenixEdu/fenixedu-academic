package net.sourceforge.fenixedu.domain.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.student.StudentStatuteBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.MasterDegreeInsurancePaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class Student extends Student_Base {

    public final static Comparator<Student> NUMBER_COMPARATOR = new BeanComparator("number");

    public Student(Person person, Integer number) {
	super();
	setPerson(person);
	if (number == null) {
	    number = Student.generateStudentNumber();
	}
	setNumber(number);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Student(Person person) {
	this(person, Student.generateStudentNumber());
    }

    @Override
    public void setNumber(Integer number) {
	Student student = readStudentByNumber(number);
	if (student != null && student != this) {
	    throw new DomainException("error.already.exists.a.student.with.the.specified.number");
	}
	super.setNumber(number);
    }

    public static Student readStudentByNumber(Integer studentNumber) {
	for (Student student : RootDomainObject.getInstance().getStudents()) {
	    if (student.getNumber().equals(studentNumber)) {
		return student;
	    }
	}
	return null;
    }

    public Collection<Registration> getRegistrationsByDegreeType(DegreeType degreeType) {
	List<Registration> result = new ArrayList<Registration>();
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType)) {
		result.add(registration);
	    }
	}
	return result;
    }

    public Registration getActiveRegistrationByDegreeType(DegreeType degreeType) {
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType) && registration.isActive()) {
		return registration;
	    }
	}
	return null;
    }

    public boolean hasActiveRegistrationForDegreeType(final DegreeType degreeType,
	    final ExecutionYear executionYear) {
	for (final Registration registration : getRegistrations()) {
	    if (registration.hasAnyEnrolmentsIn(executionYear)
		    && registration.getDegreeType() == degreeType) {
		return true;
	    }
	}
	return false;
    }

    public static Integer generateStudentNumber() {
	Integer nextNumber = 0;
	for (Student student : RootDomainObject.getInstance().getStudents()) {
	    if (student.getNumber() < 100000 && student.getNumber() > nextNumber) {
		nextNumber = student.getNumber();
	    }
	}
	return nextNumber + 1;
    }

    public StudentDataByExecutionYear getActualExecutionYearStudentData() {
	for (StudentDataByExecutionYear studentData : getStudentDataByExecutionYear()) {
	    if (studentData.getExecutionYear().isCurrent()) {
		return studentData;
	    }
	}
	return null;
    }

    public StudentDataByExecutionYear getStudentDataByExecutionYear(ExecutionYear executionYear) {
	for (StudentDataByExecutionYear studentData : getStudentDataByExecutionYear()) {
	    if (studentData.getExecutionYear().equals(executionYear)) {
		return studentData;
	    }
	}
	return null;
    }

    public ResidenceCandidacies getResidenceCandidacyForCurrentExecutionYear() {
	if (getActualExecutionYearStudentData() == null) {
	    return null;
	}
	return getActualExecutionYearStudentData().getResidenceCandidacy();
    }

    public void setResidenceCandidacyForCurrentExecutionYear(String observations) {
	createCurrentYearStudentData();
	getActualExecutionYearStudentData()
		.setResidenceCandidacy(new ResidenceCandidacies(observations));
    }

    public void setResidenceCandidacy(ResidenceCandidacies residenceCandidacy) {
	ExecutionYear executionYear = ExecutionYear.getExecutionYearByDate(residenceCandidacy
		.getCreationDateDateTime().toYearMonthDay());
	StudentDataByExecutionYear studentData = getStudentDataByExecutionYear(executionYear);
	if (studentData == null) {
	    studentData = createStudentDataForExecutionYear(executionYear);
	}
	studentData.setResidenceCandidacy(residenceCandidacy);
    }

    public boolean getWorkingStudentForCurrentExecutionYear() {
	if (getActualExecutionYearStudentData() == null) {
	    return false;
	}
	return getActualExecutionYearStudentData().getWorkingStudent();
    }

    public void setWorkingStudentForCurrentExecutionYear() {
	createCurrentYearStudentData();
	getActualExecutionYearStudentData().setWorkingStudent(true);
    }

    public StudentPersonalDataAuthorizationChoice getPersonalDataAuthorizationForCurrentExecutionYear() {
	if (getActualExecutionYearStudentData() == null) {
	    return null;
	}
	return getActualExecutionYearStudentData().getPersonalDataAuthorization();
    }

    public void setPersonalDataAuthorizationForCurrentExecutionYear(
	    StudentPersonalDataAuthorizationChoice personalDataAuthorization) {
	createCurrentYearStudentData();
	getActualExecutionYearStudentData().setPersonalDataAuthorization(personalDataAuthorization);
    }

    public void setPersonalDataAuthorizationForExecutionYear(
	    StudentPersonalDataAuthorizationChoice personalDataAuthorization, ExecutionYear executionYear) {
	StudentDataByExecutionYear studentData = getStudentDataByExecutionYear(executionYear);
	if (studentData == null) {
	    studentData = createStudentDataForExecutionYear(executionYear);
	}
	studentData.setPersonalDataAuthorization(personalDataAuthorization);
    }

    private void createCurrentYearStudentData() {
	if (getActualExecutionYearStudentData() == null) {
	    new StudentDataByExecutionYear(this);
	}
    }

    private StudentDataByExecutionYear createStudentDataForExecutionYear(ExecutionYear executionYear) {
	if (getStudentDataByExecutionYear(executionYear) == null) {
	    return new StudentDataByExecutionYear(this, executionYear);
	}
	return getStudentDataByExecutionYear(executionYear);
    }

    public DegreeType getMostSignificantDegreeType() {
	if (isStudentOfDegreeType(DegreeType.MASTER_DEGREE))
	    return DegreeType.MASTER_DEGREE;
	if (isStudentOfDegreeType(DegreeType.DEGREE))
	    return DegreeType.DEGREE;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_SPECIALIZATION_DEGREE))
	    return DegreeType.BOLONHA_SPECIALIZATION_DEGREE;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA))
	    return DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_PHD_PROGRAM))
	    return DegreeType.BOLONHA_PHD_PROGRAM;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_MASTER_DEGREE))
	    return DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE))
	    return DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_DEGREE))
	    return DegreeType.BOLONHA_DEGREE;
	return null;
    }

    private boolean isStudentOfDegreeType(DegreeType degreeType) {
	for (Registration registration : getRegistrationsByDegreeType(degreeType)) {
	    if (registration.isActive()) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasActiveRegistrationForOffice(Unit office) {
	Set<Registration> registrations = getRegistrationsSet();
	for (Registration registration : registrations) {
	    if (registration.isActiveForOffice(office)) {
		return true;
	    }
	}
	return false;
    }

    // Convenience method for invocation as bean.
    public boolean getHasActiveRegistrationForOffice() {
	Unit workingPlace = AccessControl.getPerson().getEmployee().getCurrentWorkingPlace();
	return hasActiveRegistrationForOffice(workingPlace);
    }

    public boolean hasRegistrationForOffice(final AdministrativeOffice administrativeOffice) {
	Set<Registration> registrations = getRegistrationsSet();
	for (Registration registration : registrations) {
	    if (registration.isForOffice(administrativeOffice)) {
		return true;
	    }
	}
	return false;
    }

    public boolean getHasRegistrationForOffice() {
	Unit workingPlace = AccessControl.getPerson().getEmployee().getCurrentWorkingPlace();
	return hasRegistrationForOffice(workingPlace.getAdministrativeOffice());
    }

    public boolean attends(ExecutionCourse executionCourse) {
	for (final Registration registration : getRegistrationsSet()) {
	    if (registration.attends(executionCourse)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyActiveRegistration() {
	for (final Registration registration : getRegistrationsSet()) {
	    if (registration.isActive()) {
		return true;
	    }
	}

	return false;
    }

    public void delete() {

	if (getRegistrationsCount() > 0) {
	    throw new DomainException("error.person.cannot.be.deleted");
	}

	if (getStudentDataByExecutionYearCount() > 0) {
	    throw new DomainException("error.person.cannot.be.deleted");
	}

	for (; hasAnyExternalEnrolments(); getExternalEnrolments().get(0).delete())
	    ;

	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public List<PaymentCode> getPaymentCodesBy(final PaymentCodeType paymentCodeType) {
	final List<PaymentCode> result = new ArrayList<PaymentCode>();
	for (final PaymentCode paymentCode : getPaymentCodesSet()) {
	    if (paymentCode.getType() == paymentCodeType) {
		result.add(paymentCode);
	    }
	}

	return result;
    }

    public PaymentCode getAvailablePaymentCodeBy(final PaymentCodeType paymentCodeType) {
	for (final PaymentCode paymentCode : getPaymentCodesSet()) {
	    if (paymentCode.isAvailableForReuse() && paymentCode.getType() == paymentCodeType) {
		return paymentCode;
	    }
	}

	return null;
    }

    public PaymentCode getPaymentCodeBy(final String code) {
	for (final PaymentCode paymentCode : getPaymentCodesSet()) {
	    if (paymentCode.getCode().equals(code)) {
		return paymentCode;
	    }
	}

	return null;
    }

    // TODO: This should be removed when master degree payments start using
    // Events and Posting Rules for payments
    public MasterDegreeInsurancePaymentCode calculateMasterDegreeInsurancePaymentCode(
	    final ExecutionYear executionYear) {
	if (!hasMasterDegreeInsurancePaymentCodeFor(executionYear)) {
	    return createMasterDegreeInsurancePaymentCode(executionYear);
	} else {
	    final MasterDegreeInsurancePaymentCode masterDegreeInsurancePaymentCode = getMasterDegreeInsurancePaymentCodeFor(executionYear);
	    final Money insuranceAmount = new Money(executionYear.getInsuranceValue()
		    .getAnnualValueBigDecimal());
	    masterDegreeInsurancePaymentCode.update(new YearMonthDay(),
		    calculateMasterDegreeInsurancePaymentCodeEndDate(executionYear), insuranceAmount,
		    insuranceAmount);

	    return masterDegreeInsurancePaymentCode;
	}
    }

    private MasterDegreeInsurancePaymentCode createMasterDegreeInsurancePaymentCode(
	    final ExecutionYear executionYear) {
	final Money insuranceAmount = new Money(executionYear.getInsuranceValue()
		.getAnnualValueBigDecimal());
	return MasterDegreeInsurancePaymentCode.create(new YearMonthDay(),
		calculateMasterDegreeInsurancePaymentCodeEndDate(executionYear), insuranceAmount,
		insuranceAmount, this, executionYear);
    }

    private YearMonthDay calculateMasterDegreeInsurancePaymentCodeEndDate(
	    final ExecutionYear executionYear) {
	final YearMonthDay insuranceEndDate = executionYear.getInsuranceValue().getEndDateYearMonthDay();
	final YearMonthDay now = new YearMonthDay();

	if (now.isAfter(insuranceEndDate)) {
	    return now.plusMonths(1);
	} else {
	    return insuranceEndDate;
	}
    }

    private MasterDegreeInsurancePaymentCode getMasterDegreeInsurancePaymentCodeFor(
	    final ExecutionYear executionYear) {
	for (final PaymentCode paymentCode : getPaymentCodesSet()) {
	    if (paymentCode instanceof MasterDegreeInsurancePaymentCode) {
		final MasterDegreeInsurancePaymentCode masterDegreeInsurancePaymentCode = ((MasterDegreeInsurancePaymentCode) paymentCode);
		if (masterDegreeInsurancePaymentCode.getExecutionYear() == executionYear) {
		    return masterDegreeInsurancePaymentCode;
		}
	    }
	}

	return null;
    }

    private boolean hasMasterDegreeInsurancePaymentCodeFor(final ExecutionYear executionYear) {
	return getMasterDegreeInsurancePaymentCodeFor(executionYear) != null;
    }

    // TODO: this method should be refactored as soon as possible
    public boolean hasToPayMasterDegreeInsuranceFor(final ExecutionYear executionYear) {
	for (final Registration registration : getRegistrationsByDegreeType(DegreeType.MASTER_DEGREE)) {
	    if (!registration.isActive()) {
		continue;
	    }

	    if (!registration.hasToPayMasterDegreeInsurance(executionYear)) {
		return false;
	    }

	}

	return true;
    }

    public Set<ExecutionPeriod> getEnroledExecutionPeriods() {
	Set<ExecutionPeriod> result = new TreeSet<ExecutionPeriod>(
		ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
	for (Registration registration : getRegistrations()) {
	    result.addAll(registration.getEnrolmentsExecutionPeriods());
	}
	return result;
    }

    public Collection<StudentStatuteBean> getCurrentStatutes() {
	return getStatutes(ExecutionPeriod.readActualExecutionPeriod());
    }

    private Collection<StudentStatuteBean> getStatutes(ExecutionPeriod executionPeriod) {
	List<StudentStatuteBean> result = new ArrayList<StudentStatuteBean>();
	for (final StudentStatute statute : getStudentStatutesSet()) {
	    if (statute.isValidInExecutionPeriod(executionPeriod)) {
		result.add(new StudentStatuteBean(statute, executionPeriod));
	    }
	}

	if (isHandicapped()) {
	    result.add(new StudentStatuteBean(StudentStatuteType.HANDICAPPED, executionPeriod));
	}

	return result;
    }

    public Collection<StudentStatuteBean> getAllStatutes() {
	List<StudentStatuteBean> result = new ArrayList<StudentStatuteBean>();
	for (StudentStatute statute : getStudentStatutes()) {
	    result.add(new StudentStatuteBean(statute));
	}

	if (isHandicapped()) {
	    result.add(new StudentStatuteBean(StudentStatuteType.HANDICAPPED));
	}

	return result;
    }

    public Collection<StudentStatuteBean> getAllStatutesSplittedByExecutionPeriod() {
	List<StudentStatuteBean> result = new ArrayList<StudentStatuteBean>();
	for (ExecutionPeriod executionPeriod : getEnroledExecutionPeriods()) {
	    result.addAll(getStatutes(executionPeriod));
	}
	return result;
    }

    public void addApprovedEnrolments(final Collection<Enrolment> enrolments) {
	for (final Registration registration : getRegistrationsSet()) {
	    registration.addApprovedEnrolments(enrolments);
	}
    }

    public Set<Enrolment> getApprovedEnrolments() {
	final Set<Enrolment> enrolments = new HashSet<Enrolment>();
	addApprovedEnrolments(enrolments);
	return enrolments;
    }

    public boolean isHandicapped() {
	for (Registration registration : getRegistrationsSet()) {
	    if (registration.getIngression() != null
		    && registration.getIngressionEnum().equals(Ingression.CNA07)) {
		return true;
	    }
	}
	return false;
    }

    public boolean getHasAnyBolonhaRegistration() {
	for (final Registration registration : getRegistrationsSet()) {
	    if (registration.getDegreeType().isBolonhaType()) {
		return true;
	    }
	}
	return false;
    }

    public Collection<StudentCurricularPlan> getAllStudentCurricularPlans() {
	Set<StudentCurricularPlan> result = new HashSet<StudentCurricularPlan>();
	for (Registration registration : getRegistrationsSet()) {
	    result.addAll(registration.getStudentCurricularPlansSet());
	}
	return result;
    }

    public Set<DistributedTest> getDistributedTestsByExecutionCourse(ExecutionCourse executionCourse) {
	Set<DistributedTest> result = new HashSet<DistributedTest>();
	for (final Registration registration : getRegistrationsSet()) {
	    for (StudentTestQuestion studentTestQuestion : registration.getStudentTestsQuestions()) {
		if (studentTestQuestion.getDistributedTest().getTestScope().getClassName().equals(
			ExecutionCourse.class.getName())
			&& studentTestQuestion.getDistributedTest().getTestScope().getKeyClass().equals(
				executionCourse.getIdInternal())) {
		    result.add(studentTestQuestion.getDistributedTest());
		}
	    }
	}
	return result;
    }

    public int countDistributedTestsByExecutionCourse(final ExecutionCourse executionCourse) {
	return getDistributedTestsByExecutionCourse(executionCourse).size();
    }

    public Attends readAttendByExecutionCourse(ExecutionCourse executionCourse) {
	for (final Registration registration : getRegistrationsSet()) {
	    for (Attends attend : registration.getAssociatedAttends()) {
		if (attend.getDisciplinaExecucao().equals(executionCourse)) {
		    return attend;
		}
	    }
	}
	return null;
    }
    
    public SortedSet<ExternalEnrolment> getSortedExternalEnrolments() {
	final SortedSet<ExternalEnrolment> result = new TreeSet<ExternalEnrolment>(ExternalEnrolment.COMPARATOR_BY_NAME);
	result.addAll(getExternalEnrolmentsSet());
        return result;
    }

}
