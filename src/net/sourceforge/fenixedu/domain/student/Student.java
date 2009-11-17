package net.sourceforge.fenixedu.domain.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

import net.sourceforge.fenixedu.applicationTier.utils.GeneratePasswordBase;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.student.StudentStatuteBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.MasterDegreeInsurancePaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithInvocationResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesStudentExecutionPeriod;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.inquiries.teacher.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.log.CurriculumLineLog;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.InvocationResult;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.pstm.Transaction;

public class Student extends Student_Base {

    public final static Comparator<Student> NAME_COMPARATOR = new Comparator<Student>() {

	@Override
	public int compare(Student o1, Student o2) {
	    return o1.getPerson().getName().compareTo(o2.getPerson().getName());
	}

    };

    public final static Comparator<Student> NUMBER_COMPARATOR = new Comparator<Student>() {

	@Override
	public int compare(Student o1, Student o2) {
	    return o1.getNumber().compareTo(o2.getNumber());
	}

    };

    public Student(Person person, Integer number) {
	super();
	setPerson(person);
	if (number == null || readStudentByNumber(number) != null) {
	    number = Student.generateStudentNumber();
	}
	setNumber(number);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static Student createStudentWithCustomNumber(Person person, Integer number) {
	if (number == null)
	    return new Student(person, null);

	if (readStudentByNumber(number) != null) {
	    throw new DomainException("error.custom.student.creation.student.number.already.set");
	}

	if (number >= Student.generateStudentNumber())
	    throw new DomainException("error.custom.student.creation.student.number.higher.than.generated");

	Student student = new Student(person, number);
	student.setNumber(number);

	return student;
    }

    public Student(Person person) {
	this(person, null);
    }

    public static Student readStudentByNumber(final Integer number) {
	for (final StudentNumber studentNumber : RootDomainObject.getInstance().getStudentNumbers()) {
	    if (studentNumber.getNumber().equals(number)) {
		return studentNumber.getStudent();
	    }
	}
	return null;
    }


    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static Student readStudentByNumberOther(Integer number) {
	// For performance reasons...
	PreparedStatement stmt = null;
	try {
	    final Connection connection = Transaction.getCurrentJdbcConnection();
	    stmt = connection.prepareStatement("SELECT ID_INTERNAL FROM STUDENT WHERE NUMBER = ?");

	    stmt.setInt(1, number);
	    final ResultSet resultSet = stmt.executeQuery();
	    if (resultSet.next()) {
		return RootDomainObject.getInstance().readStudentByOID(resultSet.getInt(1));
	    }

	    return null;
	} catch (SQLException e) {
	    throw new Error(e);
	} finally {
	    if (stmt != null) {
		try {
		    stmt.close();
		} catch (SQLException e) {
		    throw new Error(e);
		}
	    }
	}
    }



    public String getName() {
	return getPerson().getName();
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

    public boolean hasAnyRegistration(final DegreeType degreeType) {
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType)) {
		return true;
	    }
	}
	return false;
    }

    public Registration readRegistrationByDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	for (final Registration registration : this.getRegistrations()) {
	    StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(degreeCurricularPlan);
	    if (studentCurricularPlan != null) {
		return registration;
	    }
	}
	return null;
    }

    public Registration readRegistrationByDegree(Degree degree) {
	for (final Registration registration : this.getRegistrations()) {
	    if (registration.getDegree() == degree) {
		return registration;
	    }
	}
	return null;
    }

    public Collection<Registration> getRegistrationsByDegreeTypeAndExecutionPeriod(DegreeType degreeType,
	    ExecutionSemester executionSemester) {
	List<Registration> result = new ArrayList<Registration>();
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType)
		    && registration.hasStudentCurricularPlanInExecutionPeriod(executionSemester)) {
		result.add(registration);
	    }
	}
	return result;
    }

    public Collection<Registration> getRegistrationsByDegreeTypes(DegreeType... degreeTypes) {
	List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
	List<Registration> result = new ArrayList<Registration>();
	for (Registration registration : getRegistrations()) {
	    if (degreeTypesList.contains(registration.getDegreeType())) {
		result.add(registration);
	    }
	}
	return result;
    }

    @Deprecated
    public Registration getActiveRegistrationByDegreeType(DegreeType degreeType) {
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType) && registration.isActive()) {
		return registration;
	    }
	}
	return null;
    }

    public List<Registration> getActiveRegistrations() {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : getRegistrationsSet()) {
	    if (registration.isActive()) {
		result.add(registration);
	    }
	}
	return result;
    }

    public List<Registration> getActiveRegistrationsIn(final ExecutionSemester executionSemester) {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : getRegistrations()) {
	    if (registration.hasActiveLastState(executionSemester)) {
		result.add(registration);
	    }
	}
	return result;
    }

    public Registration getLastActiveRegistration() {
	List<Registration> activeRegistrations = getActiveRegistrations();
	return activeRegistrations.isEmpty() ? null : (Registration) Collections.max(activeRegistrations,
		Registration.COMPARATOR_BY_START_DATE);
    }

    public Registration getLastRegistrationForDegreeType(final DegreeType degreeType) {
	Collection<Registration> registrations = getRegistrationsByDegreeType(degreeType);
	return registrations.isEmpty() ? null : (Registration) Collections.max(registrations, new BeanComparator("startDate"));
    }

    public boolean hasActiveRegistrationForDegreeType(final DegreeType degreeType, final ExecutionYear executionYear) {
	for (final Registration registration : getRegistrations()) {
	    if (registration.hasAnyEnrolmentsIn(executionYear) && registration.getDegreeType() == degreeType) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyRegistrationInState(final RegistrationStateType stateType) {
	for (final Registration registration : getRegistrations()) {
	    if (registration.getActiveStateType() == stateType) {
		return true;
	    }
	}
	return false;
    }

    public static Integer generateStudentNumber() {
	int nextNumber = 0;
	for (final StudentNumber studentNumber : RootDomainObject.getInstance().getStudentNumbers()) {
	    if (studentNumber.getNumber().intValue() < 100000 && studentNumber.getNumber().intValue() > nextNumber) {
		nextNumber = studentNumber.getNumber().intValue();
	    }
	}
	return Integer.valueOf(nextNumber + 1);
    }

    public ResidenceCandidacies getResidenceCandidacyForCurrentExecutionYear() {
	if (getActualExecutionYearStudentData() == null) {
	    return null;
	}
	return getActualExecutionYearStudentData().getResidenceCandidacy();
    }

    public void setResidenceCandidacyForCurrentExecutionYear(String observations) {
	createCurrentYearStudentData();
	getActualExecutionYearStudentData().setResidenceCandidacy(new ResidenceCandidacies(observations));
    }

    public void setResidenceCandidacy(ResidenceCandidacies residenceCandidacy) {
	ExecutionYear executionYear = ExecutionYear.getExecutionYearByDate(residenceCandidacy.getCreationDateDateTime()
		.toYearMonthDay());
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
	return (getActualExecutionYearStudentData() == null) ? null : getActualExecutionYearStudentData()
		.getPersonalDataAuthorization();
    }

    public boolean hasPersonDataAuthorizationChoiseForCurrentExecutionYear() {
	return getPersonalDataAuthorizationForCurrentExecutionYear() != null;
    }

    public void setPersonalDataAuthorizationForCurrentExecutionYear(
	    final StudentPersonalDataAuthorizationChoice personalDataAuthorization) {
	createCurrentYearStudentData();
	getActualExecutionYearStudentData().setPersonalDataAuthorization(personalDataAuthorization);
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

    public StudentDataByExecutionYear getActualExecutionYearStudentData() {
	for (final StudentDataByExecutionYear studentData : getStudentDataByExecutionYear()) {
	    if (studentData.getExecutionYear().isCurrent()) {
		return studentData;
	    }
	}
	return null;
    }

    public StudentDataByExecutionYear getStudentDataByExecutionYear(final ExecutionYear executionYear) {
	for (StudentDataByExecutionYear studentData : getStudentDataByExecutionYear()) {
	    if (studentData.getExecutionYear().equals(executionYear)) {
		return studentData;
	    }
	}
	return null;
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
	if (isStudentOfDegreeType(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA))
	    return DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_MASTER_DEGREE))
	    return DegreeType.BOLONHA_MASTER_DEGREE;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE))
	    return DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
	if (isStudentOfDegreeType(DegreeType.BOLONHA_DEGREE))
	    return DegreeType.BOLONHA_DEGREE;
	return null;
    }

    private boolean isStudentOfDegreeType(DegreeType degreeType) {
	for (Registration registration : getRegistrationsByDegreeType(degreeType)) {
	    if (registration.isActive()) {
		StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
		if (scp != null) {
		    return true;
		}
	    }
	}
	return false;
    }

    public List<Registration> getRegistrationsFor(final AdministrativeOffice administrativeOffice) {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : getRegistrations()) {
	    if (registration.isForOffice(administrativeOffice)) {
		result.add(registration);
	    }
	}
	return result;
    }

    public List<Registration> getRegistrationsFor(final AdministrativeOfficeType administrativeOfficeType) {
	return getRegistrationsFor(AdministrativeOffice.readByAdministrativeOfficeType(administrativeOfficeType));
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
	return hasRegistrationForOffice(AccessControl.getPerson().getEmployee().getAdministrativeOffice());
    }

    public boolean getHasRegistrationForOfficeOrEmptyRegistrations() {
	return !hasAnyRegistrations()
		|| hasRegistrationForOffice(AccessControl.getPerson().getEmployee().getAdministrativeOffice());
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

	for (; hasAnyStudentDataByExecutionYear(); getStudentDataByExecutionYear().get(0).delete())
	    ;
	for (; !getRegistrations().isEmpty(); getRegistrations().get(0).delete())
	    ;
	for (; hasAnyVotes(); getVotes().get(0).delete())
	    ;

	getElectedElections().clear();
	getDelegateElections().clear();

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

    private Set<PaymentCode> getPaymentCodesSet() {
	return getPerson().getPaymentCodesSet();
    }

    // TODO: This should be removed when master degree payments start using
    // Events and Posting Rules for payments
    public MasterDegreeInsurancePaymentCode calculateMasterDegreeInsurancePaymentCode(final ExecutionYear executionYear) {
	if (!hasMasterDegreeInsurancePaymentCodeFor(executionYear)) {
	    return createMasterDegreeInsurancePaymentCode(executionYear);
	} else {
	    final MasterDegreeInsurancePaymentCode masterDegreeInsurancePaymentCode = getMasterDegreeInsurancePaymentCodeFor(executionYear);
	    final Money insuranceAmount = new Money(executionYear.getInsuranceValue().getAnnualValueBigDecimal());
	    masterDegreeInsurancePaymentCode.update(new YearMonthDay(),
		    calculateMasterDegreeInsurancePaymentCodeEndDate(executionYear), insuranceAmount, insuranceAmount);

	    return masterDegreeInsurancePaymentCode;
	}
    }

    private MasterDegreeInsurancePaymentCode createMasterDegreeInsurancePaymentCode(final ExecutionYear executionYear) {
	final Money insuranceAmount = new Money(executionYear.getInsuranceValue().getAnnualValueBigDecimal());
	return MasterDegreeInsurancePaymentCode.create(new YearMonthDay(),
		calculateMasterDegreeInsurancePaymentCodeEndDate(executionYear), insuranceAmount, insuranceAmount, this,
		executionYear);
    }

    private YearMonthDay calculateMasterDegreeInsurancePaymentCodeEndDate(final ExecutionYear executionYear) {
	final YearMonthDay insuranceEndDate = executionYear.getInsuranceValue().getEndDateYearMonthDay();
	final YearMonthDay now = new YearMonthDay();

	if (now.isAfter(insuranceEndDate)) {
	    final YearMonthDay nextMonth = now.plusMonths(1);
	    return new YearMonthDay(nextMonth.getYear(), nextMonth.getMonthOfYear(), 1).minusDays(1);
	} else {
	    return insuranceEndDate;
	}
    }

    private MasterDegreeInsurancePaymentCode getMasterDegreeInsurancePaymentCodeFor(final ExecutionYear executionYear) {
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
	    if (!registration.isActive() || registration.getActiveStudentCurricularPlan() == null) {
		continue;
	    }

	    if (!registration.hasToPayMasterDegreeInsurance(executionYear)) {
		return false;
	    }

	}

	return true;
    }

    public Set<ExecutionSemester> getEnroledExecutionPeriods() {
	Set<ExecutionSemester> result = new TreeSet<ExecutionSemester>(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
	for (Registration registration : getRegistrations()) {
	    result.addAll(registration.getEnrolmentsExecutionPeriods());
	}
	return result;
    }

    public Collection<StudentStatuteBean> getCurrentStatutes() {
	return getStatutes(ExecutionSemester.readActualExecutionSemester());
    }

    public Collection<StudentStatuteBean> getStatutes(final ExecutionSemester executionSemester) {
	final List<StudentStatuteBean> result = new ArrayList<StudentStatuteBean>();
	for (final StudentStatute statute : getStudentStatutesSet()) {
	    if (statute.isValidInExecutionPeriod(executionSemester)) {
		result.add(new StudentStatuteBean(statute, executionSemester));
	    }
	}

	if (isHandicapped()) {
	    result.add(new StudentStatuteBean(StudentStatuteType.HANDICAPPED, executionSemester));
	}

	return result;
    }

    public Collection<StudentStatuteType> getStatutesTypesValidOnAnyExecutionSemesterFor(final ExecutionYear executionYear) {
	final Collection<StudentStatuteType> result = new ArrayList<StudentStatuteType>();
	for (final StudentStatute statute : getStudentStatutesSet()) {
	    if (statute.isValidOnAnyExecutionPeriodFor(executionYear)) {
		result.add(statute.getStatuteType());
	    }
	}

	if (isHandicapped()) {
	    result.add(StudentStatuteType.HANDICAPPED);
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
	for (ExecutionSemester executionSemester : getEnroledExecutionPeriods()) {
	    result.addAll(getStatutes(executionSemester));
	}
	return result;
    }

    public void addApprovedEnrolments(final Collection<Enrolment> enrolments) {
	for (final Registration registration : getRegistrationsSet()) {
	    registration.addApprovedEnrolments(enrolments);
	}
    }

    public Set<Enrolment> getApprovedEnrolments() {
	final Set<Enrolment> aprovedEnrolments = new HashSet<Enrolment>();
	for (final Registration registration : getRegistrationsSet()) {
	    aprovedEnrolments.addAll(registration.getApprovedEnrolments());
	}
	return aprovedEnrolments;
    }

    public List<Enrolment> getApprovedEnrolments(final AdministrativeOffice administrativeOffice) {
	final List<Enrolment> aprovedEnrolments = new ArrayList<Enrolment>();
	for (final Registration registration : getRegistrationsFor(administrativeOffice)) {
	    aprovedEnrolments.addAll(registration.getApprovedEnrolments());
	}
	return aprovedEnrolments;
    }

    public Set<Enrolment> getDismissalApprovedEnrolments() {
	Set<Enrolment> aprovedEnrolments = new HashSet<Enrolment>();
	for (Registration registration : getRegistrationsSet()) {
	    for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
		aprovedEnrolments.addAll(studentCurricularPlan.getDismissalApprovedEnrolments());
	    }
	}
	return aprovedEnrolments;
    }

    public boolean isHandicapped() {
	for (Registration registration : getRegistrationsSet()) {
	    if (registration.getIngression() != null && registration.getIngression().equals(Ingression.CNA07)) {
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
	final Set<StudentCurricularPlan> result = new HashSet<StudentCurricularPlan>();
	for (final Registration registration : getRegistrationsSet()) {
	    result.addAll(registration.getStudentCurricularPlansSet());
	}
	return result;
    }

    public Map<Registration, Set<DistributedTest>> getDistributedTestsByExecutionCourse(ExecutionCourse executionCourse) {
	Map<Registration, Set<DistributedTest>> result = new HashMap<Registration, Set<DistributedTest>>();
	for (final Registration registration : getRegistrationsSet()) {
	    for (StudentTestQuestion studentTestQuestion : registration.getStudentTestsQuestions()) {
		if (studentTestQuestion.getDistributedTest().getTestScope().getClassName()
			.equals(ExecutionCourse.class.getName())
			&& studentTestQuestion.getDistributedTest().getTestScope().getKeyClass().equals(
				executionCourse.getIdInternal())) {
		    Set<DistributedTest> tests = result.get(registration);
		    if (tests == null) {
			tests = new HashSet<DistributedTest>();
		    }
		    tests.add(studentTestQuestion.getDistributedTest());
		    result.put(registration, tests);
		}
	    }
	}
	return result;
    }

    public int countDistributedTestsByExecutionCourse(final ExecutionCourse executionCourse) {
	return getDistributedTestsByExecutionCourse(executionCourse).size();
    }

    public Attends readAttendByExecutionCourse(final ExecutionCourse executionCourse) {
	for (final Registration registration : getRegistrationsSet()) {
	    Attends attends = registration.readRegistrationAttendByExecutionCourse(executionCourse);
	    if (attends != null) {
		return attends;
	    }
	}
	return null;
    }

    public SortedSet<Attends> getAttendsForExecutionPeriod(ExecutionSemester executionSemester) {
	SortedSet<Attends> attends = new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
	for (Registration registration : getRegistrations()) {
	    attends.addAll(registration.getAttendsForExecutionPeriod(executionSemester));
	}
	return attends;
    }

    public List<Registration> getRegistrationsToEnrolByStudent() {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : getRegistrations()) {
	    if (registration.isEnrolmentByStudentAllowed()) {
		result.add(registration);
	    }
	}

	return result;

    }

    public List<Registration> getRegistrationsToEnrolInShiftByStudent() {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : getRegistrations()) {
	    if (registration.isEnrolmentByStudentInShiftsAllowed()) {
		result.add(registration);
	    }
	}

	return result;
    }

    public boolean isCurrentlyEnroled(DegreeCurricularPlan degreeCurricularPlan) {
	for (Registration registration : getRegistrations()) {
	    final RegistrationState registrationState = registration.getActiveState();
	    if (!registration.isActive() && registrationState.getStateType() != RegistrationStateType.TRANSITED) {
		continue;
	    }

	    StudentCurricularPlan lastStudentCurricularPlan = registration.getLastStudentCurricularPlan();
	    if (lastStudentCurricularPlan == null) {
		continue;
	    }

	    if (lastStudentCurricularPlan.getDegreeCurricularPlan() != degreeCurricularPlan) {
		continue;
	    }

	    return true;
	}

	return false;
    }

    final public Enrolment getDissertationEnrolment() {
	return getDissertationEnrolment(null);
    }

    final public TreeSet<Enrolment> getDissertationEnrolments(DegreeCurricularPlan degreeCurricularPlan) {
	final TreeSet<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
	for (final Registration registration : getRegistrations()) {
	    final Enrolment dissertationEnrolment = registration.getDissertationEnrolment(degreeCurricularPlan);
	    if (dissertationEnrolment != null) {
		enrolments.add(dissertationEnrolment);
	    }
	}
	return enrolments;
    }

    final public Enrolment getDissertationEnrolment(DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear) {
	TreeSet<Enrolment> enrolments = getDissertationEnrolments(degreeCurricularPlan);
	CollectionUtils.filter(enrolments, new Predicate() {

	    @Override
	    public boolean evaluate(Object enrolment) {
		return ((Enrolment) enrolment).getExecutionYear().equals(executionYear);
	    }
	});
	return enrolments.isEmpty() ? null : enrolments.last();
    }

    final public Enrolment getDissertationEnrolment(DegreeCurricularPlan degreeCurricularPlan) {
	TreeSet<Enrolment> enrolments = getDissertationEnrolments(degreeCurricularPlan);
	return enrolments.isEmpty() ? null : enrolments.last();
    }

    public boolean doesNotWantToRespondToInquiries() {
	for (final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod : getInquiriesStudentExecutionPeriodsSet()) {
	    if (inquiriesStudentExecutionPeriod.getExecutionPeriod().getState().equals(PeriodState.CURRENT)
		    && inquiriesStudentExecutionPeriod.getDontWantToRespond().booleanValue()) {
		return true;
	    }
	}
	return false;
    }

    public boolean isWeeklySpentHoursSubmittedForCurrentPeriod() {
	return isWeeklySpentHoursSubmittedForPeriod(ExecutionSemester.readActualExecutionSemester());
    }

    public boolean isWeeklySpentHoursSubmittedForOpenInquiriesResponsePeriod() {
	final InquiryResponsePeriod openPeriod = InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.STUDENT);
	return openPeriod == null ? false : isWeeklySpentHoursSubmittedForPeriod(openPeriod.getExecutionPeriod());
    }

    public boolean isWeeklySpentHoursSubmittedForPeriod(ExecutionSemester executionSemester) {
	for (final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod : getInquiriesStudentExecutionPeriodsSet()) {
	    if (inquiriesStudentExecutionPeriod.getExecutionPeriod() == executionSemester) {
		return inquiriesStudentExecutionPeriod.getWeeklyHoursSpentInClassesSeason() != null;
	    }
	}
	return false;
    }

    public InquiriesStudentExecutionPeriod getCurrentInquiriesStudentExecutionPeriod() {
	return getInquiriesStudentExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
    }

    public InquiriesStudentExecutionPeriod getOpenInquiriesStudentExecutionPeriod() {
	final InquiryResponsePeriod openPeriod = InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.STUDENT);
	return openPeriod == null ? null : getInquiriesStudentExecutionPeriod(openPeriod.getExecutionPeriod());
    }

    public InquiriesStudentExecutionPeriod getInquiriesStudentExecutionPeriod(ExecutionSemester executionSemester) {
	for (final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod : getInquiriesStudentExecutionPeriodsSet()) {
	    if (inquiriesStudentExecutionPeriod.getExecutionPeriod() == executionSemester) {
		return inquiriesStudentExecutionPeriod;
	    }
	}
	return null;
    }

    /**
     * -> Temporary overrides due migrations - Filter 'InTransition'
     * registrations -> Do not use this method to add new registrations directly
     * (use {@link addRegistrations} method)
     */
    @Override
    public List<Registration> getRegistrations() {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : super.getRegistrations()) {
	    if (!registration.isTransition()) {
		result.add(registration);
	    }
	}
	return Collections.unmodifiableList(result);
    }

    public Collection<Registration> getAllRegistrations() {
	return Collections.unmodifiableCollection(super.getRegistrations());
    }

    /**
     * -> Temporary overrides due migrations - Filter 'InTransition'
     * registrations -> Do not use this method to add new registrations directly
     * (use {@link addRegistrations} method)
     */
    @Override
    public Set<Registration> getRegistrationsSet() {
	final Set<Registration> result = new HashSet<Registration>();
	for (final Registration registration : super.getRegistrationsSet()) {
	    if (!registration.isTransition()) {
		result.add(registration);
	    }
	}
	return Collections.unmodifiableSet(result);
    }

    @Override
    public Iterator<Registration> getRegistrationsIterator() {
	return getRegistrationsSet().iterator();
    }

    @Override
    public int getRegistrationsCount() {
	return getRegistrations().size();
    }

    public boolean hasTransitionRegistrations() {
	for (final Registration registration : super.getRegistrations()) {
	    if (registration.isTransition()) {
		return true;
	    }
	}

	return false;
    }

    @Checked("StudentPredicates.checkIfLoggedPersonIsStudentOwnerOrManager")
    public List<Registration> getTransitionRegistrations() {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : super.getRegistrations()) {
	    if (registration.isTransition()) {
		result.add(registration);
	    }
	}
	return result;
    }

    @Checked("StudentPredicates.checkIfLoggedPersonIsCoordinator")
    public List<Registration> getTransitionRegistrationsForDegreeCurricularPlansManagedByCoordinator(final Person coordinator) {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : super.getRegistrations()) {
	    if (registration.isTransition()
		    && coordinator.isCoordinatorFor(registration.getLastDegreeCurricularPlan(), ExecutionYear
			    .readCurrentExecutionYear())) {
		result.add(registration);
	    }
	}
	return result;
    }

    @Checked("StudentPredicates.checkIfLoggedPersonIsStudentOwnerOrManager")
    public List<Registration> getTransitedRegistrations() {
	List<Registration> result = new ArrayList<Registration>();
	for (Registration registration : super.getRegistrations()) {
	    if (registration.isTransited()) {
		result.add(registration);
	    }
	}
	return result;
    }

    private boolean isAnyTuitionInDebt(final ExecutionYear executionYear) {
	for (final Registration registration : super.getRegistrations()) {
	    if (registration.hasAnyNotPayedGratuityEventsForPreviousYears(executionYear)) {
		return true;
	    }
	}

	return false;
    }

    public boolean isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt() {
	return isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt(ExecutionYear.readCurrentExecutionYear());
    }

    /**
     * Check if there is any debt until given execution year (exclusive)
     * 
     * @param executionYear
     */
    public boolean isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt(final ExecutionYear executionYear) {
	return isAnyTuitionInDebt(executionYear) || isAnyAdministrativeOfficeFeeAndInsuranceInDebtUntil(executionYear);
    }

    /**
     * Check if there is any debt until given execution year (exclusive)
     * 
     * @param executionYear
     */
    private boolean isAnyAdministrativeOfficeFeeAndInsuranceInDebtUntil(final ExecutionYear executionYear) {
	for (final Event event : getPerson().getEvents()) {
	    if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent
		    && ((AdministrativeOfficeFeeAndInsuranceEvent) event).getExecutionYear().isBefore(executionYear)
		    && event.isOpen()) {
		return true;
	    }
	}

	return false;
    }

    public List<Registration> getRegistrationsFor(final DegreeCurricularPlan degreeCurricularPlan) {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : super.getRegistrations()) {
	    for (final DegreeCurricularPlan degreeCurricularPlanToTest : registration.getDegreeCurricularPlans()) {
		if (degreeCurricularPlanToTest.equals(degreeCurricularPlan)) {
		    result.add(registration);
		    break;
		}
	    }
	}
	return result;
    }

    public boolean hasRegistrationFor(final DegreeCurricularPlan degreeCurricularPlan) {
	return !getRegistrationsFor(degreeCurricularPlan).isEmpty();
    }

    public Registration getMostRecentRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
	final List<Registration> registrations = getRegistrationsFor(degreeCurricularPlan);
	return registrations.isEmpty() ? null : Collections.max(registrations, Registration.COMPARATOR_BY_START_DATE);
    }

    public List<Registration> getRegistrationsFor(final Degree degree) {
	final List<Registration> result = new ArrayList<Registration>();
	for (final Registration registration : super.getRegistrations()) {
	    if (registration.getDegree() == degree) {
		result.add(registration);
	    }
	}
	return result;
    }

    public boolean hasRegistrationFor(final Degree degree) {
	return !getRegistrationsFor(degree).isEmpty();
    }

    public Registration getActiveRegistrationFor(final DegreeCurricularPlan degreeCurricularPlan) {
	for (final Registration registration : getActiveRegistrations()) {
	    if (registration.getLastDegreeCurricularPlan() == degreeCurricularPlan) {
		return registration;
	    }
	}

	return null;
    }

    public boolean hasActiveRegistrationFor(final DegreeCurricularPlan degreeCurricularPlan) {
	return getActiveRegistrationFor(degreeCurricularPlan) != null;
    }

    public Registration getActiveRegistrationFor(final Degree degree) {
	for (final Registration registration : getActiveRegistrations()) {
	    if (registration.getLastDegree() == degree) {
		return registration;
	    }
	}

	return null;
    }

    public boolean hasActiveRegistrationFor(final Degree degree) {
	return getActiveRegistrationFor(degree) != null;
    }

    public boolean hasActiveRegistrations() {
	return getActiveRegistrations().size() > 0;
    }

    public Registration getTransitionRegistrationFor(DegreeCurricularPlan degreeCurricularPlan) {
	for (final Registration registration : getTransitionRegistrations()) {
	    if (registration.getLastDegreeCurricularPlan() == degreeCurricularPlan) {
		return registration;
	    }
	}

	return null;
    }

    public DelegateElection getLastElectedDelegateElection() {
	List<DelegateElection> elections = new ArrayList<DelegateElection>(getElectedElections());
	return (elections.isEmpty() ? null : Collections
		.max(elections, DelegateElection.ELECTION_COMPARATOR_BY_VOTING_START_DATE));
    }

    /*
     * ACTIVE DELEGATE FUNCTIONS OWNED BY STUDENT
     */
    public List<PersonFunction> getAllActiveDelegateFunctions() {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	List<PersonFunction> result = new ArrayList<PersonFunction>();
	for (FunctionType delegateFunctionType : FunctionType.getAllDelegateFunctionTypes()) {
	    Set<Function> functions = Function.readAllActiveFunctionsByType(delegateFunctionType);
	    for (Function function : functions) {
		for (PersonFunction personFunction : function.getActivePersonFunctionsStartingIn(currentExecutionYear)) {
		    if (personFunction.getPerson().equals(this.getPerson())) {
			result.add(personFunction);
		    }
		}
	    }

	}
	return result;
    }

    public List<PersonFunction> getAllActiveDelegateFunctions(FunctionType functionType) {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	List<PersonFunction> result = new ArrayList<PersonFunction>();
	Set<Function> functions = Function.readAllActiveFunctionsByType(functionType);
	for (Function function : functions) {
	    for (PersonFunction personFunction : function.getActivePersonFunctionsStartingIn(currentExecutionYear)) {
		if (personFunction.getPerson().equals(this.getPerson())) {
		    result.add(personFunction);
		}
	    }

	}
	return result;
    }

    public boolean hasActiveDelegateFunction(FunctionType functionType) {
	List<PersonFunction> personFunctions = getAllActiveDelegateFunctions(functionType);
	return !personFunctions.isEmpty();
    }

    public boolean hasAnyActiveDelegateFunction() {
	for (FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
	    if (hasActiveDelegateFunction(functionType)) {
		return true;
	    }
	}
	return false;
    }

    /*
     * ALL DELEGATE FUNCTIONS (ACTIVE AND PAST) OWNED BY STUDENT
     */
    public List<PersonFunction> getAllDelegateFunctions() {
	List<PersonFunction> result = new ArrayList<PersonFunction>();
	for (FunctionType delegateFunctionType : FunctionType.getAllDelegateFunctionTypes()) {
	    Set<Function> functions = Function.readAllFunctionsByType(delegateFunctionType);
	    for (Function function : functions) {
		for (PersonFunction personFunction : function.getPersonFunctions()) {
		    if (personFunction.getPerson().equals(this.getPerson())) {
			result.add(personFunction);
		    }
		}
	    }
	}
	return result;
    }

    /*
     * If student has delegate role, get the students he is responsible for
     */
    public List<Student> getStudentsResponsibleForGivenFunctionType(FunctionType delegateFunctionType, ExecutionYear executionYear) {
	final Degree degree = getLastActiveRegistration().getDegree();
	if (degree.hasActiveDelegateFunctionForStudent(this, executionYear, delegateFunctionType)) {
	    switch (delegateFunctionType) {
	    case DELEGATE_OF_GGAE:
		return degree.getAllStudents();
	    case DELEGATE_OF_INTEGRATED_MASTER_DEGREE:
		return degree.getAllStudents();
	    case DELEGATE_OF_MASTER_DEGREE:
		return getStudentsForMasterDegreeDelegate(degree, executionYear);
	    case DELEGATE_OF_DEGREE:
		return getStudentsForDegreeDelegate(degree, executionYear);
	    case DELEGATE_OF_YEAR:
		return getStudentsForYearDelegate(degree, executionYear);
	    }
	}

	return new ArrayList<Student>();
    }

    private List<Student> getStudentsForMasterDegreeDelegate(Degree degree, ExecutionYear executionYear) {
	final DegreeType degreeType = degree.getDegreeType();
	return (degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree.getSecondCycleStudents(executionYear)
		: degree.getAllStudents());
    }

    private List<Student> getStudentsForDegreeDelegate(Degree degree, ExecutionYear executionYear) {
	final DegreeType degreeType = degree.getDegreeType();
	return (degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree.getFirstCycleStudents(executionYear)
		: degree.getAllStudents());
    }

    private List<Student> getStudentsForYearDelegate(Degree degree, ExecutionYear executionYear) {
	final PersonFunction yearDelegateFunction = degree.getActiveDelegatePersonFunctionByStudentAndFunctionType(this,
		executionYear, FunctionType.DELEGATE_OF_YEAR);
	int curricularYear = yearDelegateFunction.getCurricularYear().getYear();
	return degree.getStudentsFromGivenCurricularYear(curricularYear, executionYear);
    }

    /*
     * If student has delegate role, get the curricular courses he is
     * responsible for
     */
    public Set<CurricularCourse> getCurricularCoursesResponsibleForByFunctionType(FunctionType delegateFunctionType,
	    ExecutionYear executionYear) {
	final Degree degree = getLastActiveRegistration().getDegree();
	final PersonFunction delegateFunction = degree.getActiveDelegatePersonFunctionByStudentAndFunctionType(this,
		executionYear, delegateFunctionType);
	if (delegateFunction != null) {
	    executionYear = executionYear != null ? executionYear : ExecutionYear.getExecutionYearByDate(delegateFunction
		    .getBeginDate());
	    if (degree.hasActiveDelegateFunctionForStudent(this, executionYear, delegateFunctionType)) {
		switch (delegateFunctionType) {
		case DELEGATE_OF_GGAE:
		    return degree.getAllCurricularCourses(executionYear);
		case DELEGATE_OF_INTEGRATED_MASTER_DEGREE:
		    return degree.getAllCurricularCourses(executionYear);
		case DELEGATE_OF_MASTER_DEGREE:
		    return getCurricularCoursesForMasterDegreeDelegate(degree, executionYear);
		case DELEGATE_OF_DEGREE:
		    return getCurricularCoursesForDegreeDelegate(degree, executionYear);
		case DELEGATE_OF_YEAR:
		    return getCurricularCoursesForYearDelegate(degree, executionYear);
		}
	    }
	}
	return new HashSet<CurricularCourse>();
    }

    private Set<CurricularCourse> getCurricularCoursesForMasterDegreeDelegate(Degree degree, ExecutionYear executionYear) {
	final DegreeType degreeType = degree.getDegreeType();
	return (degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree
		.getSecondCycleCurricularCourses(executionYear) : degree.getAllCurricularCourses(executionYear));
    }

    private Set<CurricularCourse> getCurricularCoursesForDegreeDelegate(Degree degree, ExecutionYear executionYear) {
	final DegreeType degreeType = degree.getDegreeType();
	return (degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree
		.getFirstCycleCurricularCourses(executionYear) : degree.getAllCurricularCourses(executionYear));
    }

    private Set<CurricularCourse> getCurricularCoursesForYearDelegate(Degree degree, ExecutionYear executionYear) {
	final PersonFunction yearDelegateFunction = degree.getActiveDelegatePersonFunctionByStudentAndFunctionType(this,
		executionYear, FunctionType.DELEGATE_OF_YEAR);
	int curricularYear = yearDelegateFunction.getCurricularYear().getYear();
	return degree.getCurricularCoursesFromGivenCurricularYear(curricularYear, executionYear);
    }

    public Collection<Delegate> getDelegates() {
	Collection<Delegate> result = new ArrayList<Delegate>();
	for (Registration registration : getRegistrations()) {
	    for (Delegate delegate : registration.getDelegates()) {
		result.add(delegate);
	    }
	}
	return result;
    }

    public boolean hasYearDelegateInquiriesToAnswer() {

	if (!InquiryResponsePeriod.hasOpenPeriod(InquiryResponsePeriodType.DELEGATE)) {
	    return false;
	}

	final ExecutionSemester executionSemester = InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.DELEGATE)
		.getExecutionPeriod();

	for (Delegate delegate : getDelegates()) {
	    if (delegate instanceof YearDelegate) {
		if (delegate.isActiveForFirstExecutionYear(executionSemester.getExecutionYear())) {
		    if (!((YearDelegate) delegate).getNotAnsweredInquiriesExecutionCourses(executionSemester).isEmpty()) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public boolean isGrantOwner(final ExecutionYear executionYear) {
	for (final StudentStatute studentStatute : getStudentStatutesSet()) {
	    if (studentStatute.isGrantOwnerStatute() && studentStatute.isValidOn(executionYear)) {
		return true;
	    }
	}

	return false;
    }

    public SortedSet<ExternalEnrolment> getSortedExternalEnrolments() {
	final SortedSet<ExternalEnrolment> result = new TreeSet<ExternalEnrolment>(ExternalEnrolment.COMPARATOR_BY_NAME);
	for (final Registration registration : getRegistrationsSet()) {
	    result.addAll(registration.getExternalEnrolmentsSet());
	}
	return result;
    }

    public Collection<? extends Forum> getForuns(ExecutionSemester executionSemester) {
	final Collection<Forum> res = new HashSet<Forum>();
	for (Registration registration : getRegistrationsSet()) {
	    for (Attends attends : registration.getAssociatedAttendsSet()) {
		if (attends.getExecutionPeriod() == executionSemester) {
		    res.addAll(attends.getExecutionCourse().getForuns());
		}
	    }
	}
	return res;
    }

    public void createGratuityEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
	final AccountingEventsManager manager = new AccountingEventsManager();
	final InvocationResult result = manager.createGratuityEvent(studentCurricularPlan, executionYear);

	if (!result.isSuccess()) {
	    throw new DomainExceptionWithInvocationResult(result);
	}
    }

    public void createAdministrativeOfficeFeeEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear) {
	final AccountingEventsManager manager = new AccountingEventsManager();
	final InvocationResult result = manager.createAdministrativeOfficeFeeAndInsuranceEvent(studentCurricularPlan,
		executionYear);

	if (!result.isSuccess()) {
	    throw new DomainExceptionWithInvocationResult(result);
	}

    }

    public void createEnrolmentOutOfPeriodEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester, final Integer numberOfDelayDays) {
	new AccountingEventsManager()
		.createEnrolmentOutOfPeriodEvent(studentCurricularPlan, executionSemester, numberOfDelayDays);
    }

    public void createInsuranceEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
	final AccountingEventsManager manager = new AccountingEventsManager();
	final InvocationResult result = manager.createInsuranceEvent(studentCurricularPlan, executionYear);

	if (!result.isSuccess()) {
	    throw new DomainExceptionWithInvocationResult(result);
	}
    }

    public Collection<CurriculumLineLog> getCurriculumLineLogs(final ExecutionSemester executionSemester) {
	final Collection<CurriculumLineLog> res = new HashSet<CurriculumLineLog>();
	for (final Registration registration : getRegistrationsSet()) {
	    res.addAll(registration.getCurriculumLineLogs(executionSemester));
	}
	return res;
    }

    public boolean hasActiveStatuteInPeriod(StudentStatuteType studentStatuteType, ExecutionSemester executionSemester) {
	for (StudentStatute studentStatute : getStudentStatutesSet()) {
	    if (studentStatute.getStatuteType() == studentStatuteType
		    && studentStatute.isValidInExecutionPeriod(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasEnrolments(final Enrolment enrolment) {
	if (enrolment == null) {
	    return false;
	}
	for (final Registration registration : getRegistrationsSet()) {
	    if (registration.hasEnrolments(enrolment)) {
		return true;
	    }
	}
	return false;
    }

    public Collection<InquiriesRegistry> getOrCreateInquiriesRegistriesForPeriod(ExecutionSemester executionSemester) {
	final Map<ExecutionCourse, InquiriesRegistry> coursesToAnswer = new HashMap<ExecutionCourse, InquiriesRegistry>();

	for (Registration registration : getRegistrations()) {

	    // TODO: Chack degree types and response period!
	    if (!registration.isAvailableDegreeTypeForInquiries()) {
		continue;
	    }

	    for (final InquiriesRegistry inquiriesRegistry : registration.getAssociatedInquiriesRegistries()) {
		if (inquiriesRegistry.getExecutionCourse().getExecutionPeriod() == executionSemester) {
		    coursesToAnswer.put(inquiriesRegistry.getExecutionCourse(), inquiriesRegistry);
		}
	    }

	    for (final Enrolment enrolment : registration.getEnrolments(executionSemester)) {
		createNewInquiriesRegistryIfDoesntExist(executionSemester, coursesToAnswer, registration, enrolment);
	    }

	    for (final Enrolment enrolment : registration.getEnrolments(executionSemester.getPreviousExecutionPeriod())) {
		if (enrolment.getCurricularCourse().isAnual()) {
		    createNewInquiriesRegistryIfDoesntExist(executionSemester, coursesToAnswer, registration, enrolment);
		}
	    }
	}
	return coursesToAnswer.values();
    }

    private void createNewInquiriesRegistryIfDoesntExist(ExecutionSemester executionSemester,
	    final Map<ExecutionCourse, InquiriesRegistry> coursesToAnswer, Registration registration, final Enrolment enrolment) {
	final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
	if (executionCourse != null && !coursesToAnswer.containsKey(executionCourse)) {
	    coursesToAnswer.put(executionCourse, new InquiriesRegistry(executionCourse, enrolment.getCurricularCourse(),
		    executionSemester, registration));
	}
    }

    public Collection<String> getInquiriesCoursesNamesToRespond(ExecutionSemester executionSemester) {
	final Map<ExecutionCourse, String> coursesToAnswer = new HashMap<ExecutionCourse, String>();
	final Set<ExecutionCourse> coursesAnswered = new HashSet<ExecutionCourse>();

	for (Registration registration : getRegistrations()) {

	    if (!registration.isAvailableDegreeTypeForInquiries()) {
		continue;
	    }

	    for (final InquiriesRegistry inquiriesRegistry : registration.getAssociatedInquiriesRegistries()) {
		if (inquiriesRegistry.getExecutionCourse().getExecutionPeriod() == executionSemester) {
		    if (inquiriesRegistry.isOpenToAnswer() || inquiriesRegistry.isToAnswerLater()) {
			coursesToAnswer.put(inquiriesRegistry.getExecutionCourse(), inquiriesRegistry.getCurricularCourse()
				.getName());
		    } else {
			coursesAnswered.add(inquiriesRegistry.getExecutionCourse());
		    }
		}
	    }

	    for (final Enrolment enrolment : registration.getEnrolments(executionSemester)) {
		final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
		if (executionCourse != null && !coursesAnswered.contains(executionCourse)) {
		    coursesToAnswer.put(executionCourse, enrolment.getCurricularCourse().getName());
		}
	    }

	    for (final Enrolment enrolment : registration.getEnrolments(executionSemester.getPreviousExecutionPeriod())) {
		if (enrolment.getCurricularCourse().isAnual()) {
		    final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
		    if (executionCourse != null && !coursesAnswered.contains(executionCourse)) {
			coursesToAnswer.put(executionCourse, enrolment.getCurricularCourse().getName());
		    }
		}
	    }
	}
	return coursesToAnswer.values();
    }

    public boolean hasInquiriesToRespond() {
	if (!InquiryResponsePeriod.hasOpenPeriod(InquiryResponsePeriodType.STUDENT)) {
	    return false;
	}

	final ExecutionSemester executionSemester = InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.STUDENT)
		.getExecutionPeriod();

	for (Registration registration : getRegistrations()) {
	    if (!registration.isAvailableDegreeTypeForInquiries()) {
		continue;
	    }

	    final Set<CurricularCourse> inquiriesCurricularCourses = new HashSet<CurricularCourse>();
	    for (final InquiriesRegistry inquiriesRegistry : registration.getAssociatedInquiriesRegistriesSet()) {
		if (inquiriesRegistry.getExecutionCourse().getExecutionPeriod() == executionSemester) {
		    if (inquiriesRegistry.isOpenToAnswer()) {
			return true;
		    } else {
			inquiriesCurricularCourses.add(inquiriesRegistry.getCurricularCourse());
		    }
		}
	    }

	    for (Enrolment enrolment : registration.getEnrolments(executionSemester)) {
		final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
		if (executionCourse != null && !inquiriesCurricularCourses.contains(enrolment.getCurricularCourse())) {
		    return true;
		}
	    }

	    for (final Enrolment enrolment : registration.getEnrolments(executionSemester.getPreviousExecutionPeriod())) {
		if (enrolment.getCurricularCourse().isAnual()) {
		    final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
		    if (executionCourse != null && !inquiriesCurricularCourses.contains(enrolment.getCurricularCourse())) {
			return true;
		    }
		}
	    }

	}

	return false;
    }

    public boolean learnsAt(final Campus campus) {
	for (final Registration registration : getActiveRegistrations()) {
	    if (registration.getCampus() == campus) {
		return true;
	    }
	}
	return false;
    }

    public List<Tutorship> getTutorships() {
	List<Tutorship> tutorships = new ArrayList<Tutorship>();
	for (Registration registration : getActiveRegistrations()) {
	    for (StudentCurricularPlan curricularPlan : registration.getStudentCurricularPlans()) {
		tutorships.addAll(curricularPlan.getTutorships());
	    }
	}
	return tutorships;
    }

    public List<ExecutionYear> getTutorshipsExecutionYears() {
	HashSet<ExecutionYear> coveredYears = new HashSet<ExecutionYear>();
	for (Tutorship tutorship : getTutorships()) {
	    coveredYears.addAll(tutorship.getCoveredExecutionYears());
	}
	return new ArrayList<ExecutionYear>(coveredYears);
    }

    public List<Tutorship> getActiveTutorships() {
	List<Tutorship> tutorships = new ArrayList<Tutorship>();
	for (Tutorship tutorship : getTutorships()) {
	    if (tutorship.isActive()) {
		tutorships.add(tutorship);
	    }
	}
	return tutorships;
    }

    public ExecutionYear getFirstRegistrationExecutionYear() {

	ExecutionYear firstYear = null;
	for (Registration registration : getRegistrations()) {
	    if (firstYear == null) {
		firstYear = registration.getStartExecutionYear();
		continue;
	    }

	    if (registration.getStartExecutionYear().isBefore(firstYear)) {
		firstYear = registration.getStartExecutionYear();
	    }
	}
	return firstYear;
    }

    public boolean hasAlreadyVotedForYearDelegateElection(ExecutionYear executionYear) {
	for (DelegateElection delegateElection : getElectionsWithVotingStudentsSet()) {
	    if (delegateElection instanceof YearDelegateElection) {
		YearDelegateElection yearDelegateElection = (YearDelegateElection) delegateElection;
		if (yearDelegateElection.getExecutionYear() == executionYear) {
		    return true;
		}
	    }
	}
	return false;
    }

    public Collection<? extends AcademicServiceRequest> getAcademicServiceRequests(
	    final Class<? extends AcademicServiceRequest> clazz) {
	final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();
	for (final Registration registration : getRegistrations()) {
	    result.addAll(registration.getAcademicServiceRequests(clazz));
	}
	return result;
    }

    public Collection<ExecutionYear> getEnrolmentsExecutionYears() {
	Set<ExecutionYear> executionYears = new HashSet<ExecutionYear>();
	for (final Registration registration : getRegistrationsSet()) {
	    executionYears.addAll(registration.getEnrolmentsExecutionYears());
	}
	return executionYears;
    }

    public boolean getActiveAlumni() {
	return hasAlumni();
    }

    public Attends getAttends(final ExecutionCourse executionCourse) {
	Attends result = null;

	for (final Registration registration : getRegistrationsSet()) {
	    for (final Attends attends : registration.getAssociatedAttendsSet()) {
		if (attends.isFor(executionCourse)) {
		    if (result != null) {
			throw new DomainException("error.found.multiple.attends.for.student.in.execution.course", executionCourse
				.getNome(), executionCourse.getExecutionPeriod().getQualifiedName());
		    }
		    result = attends;
		}
	    }
	}

	return result;
    }

    public boolean hasAttends(final ExecutionCourse executionCourse) {
	for (final Registration registration : getRegistrationsSet()) {
	    for (final Attends attends : registration.getAssociatedAttendsSet()) {
		if (attends.getExecutionCourse() == executionCourse) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean hasAnyRegistrationWithMissingCandidacyInformation() {
	for (final Registration registration : getRegistrations()) {
	    if (registration.hasMissingCandidacyInformation(ExecutionYear.readCurrentExecutionYear())) {
		return true;
	    }
	}

	return false;
    }

    public SortedSet<Registration> getRegistrationsWithMissingCandidacyInformation() {
	final SortedSet<Registration> result = new TreeSet<Registration>(Registration.COMPARATOR_BY_START_DATE);
	for (final Registration registration : getRegistrations()) {
	    if (registration.hasMissingCandidacyInformation(ExecutionYear.readCurrentExecutionYear())) {
		result.add(registration);
	    }
	}

	return result;
    }

    public void generateExportInformationPassword() {
	setExportInformationPassword(new GeneratePasswordBase().generatePassword(getPerson()));
    }

    public void deleteExportInformationPassword() {
	setExportInformationPassword(null);
    }

    @Override
    public void setNumber(final Integer number) {
	super.setNumber(number);

	if (hasStudentNumber()) {
	    if (number != null) {
		getStudentNumber().setNumber(number);
	    } else {
		getStudentNumber().delete();
	    }
	} else if (number != null) {
	    new StudentNumber(this);
	}
    }
}
