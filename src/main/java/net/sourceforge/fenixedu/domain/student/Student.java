package net.sourceforge.fenixedu.domain.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseInquiriesRegistryDTO;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
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
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.MasterDegreeInsurancePaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmationEvent;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithInvocationResult;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryExecutionPeriod;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryTemplate;
import net.sourceforge.fenixedu.domain.log.CurriculumLineLog;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.predicates.StudentPredicates;
import net.sourceforge.fenixedu.util.InvocationResult;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.Atomic;

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
        setRootDomainObject(Bennu.getInstance());
    }

    public static Student createStudentWithCustomNumber(Person person, Integer number) {
        if (number == null) {
            return new Student(person, null);
        }

        if (readStudentByNumber(number) != null) {
            throw new DomainException("error.custom.student.creation.student.number.already.set");
        }

        if (number >= Student.generateStudentNumber()) {
            throw new DomainException("error.custom.student.creation.student.number.higher.than.generated");
        }

        Student student = new Student(person, number);
        student.setNumber(number);

        return student;
    }

    public Student(Person person) {
        this(person, null);
    }

    public static Student readStudentByNumber(final Integer number) {
        for (final StudentNumber studentNumber : Bennu.getInstance().getStudentNumbersSet()) {
            if (studentNumber.getNumber().equals(number)) {
                return studentNumber.getStudent();
            }
        }
        return null;
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

    public List<Registration> getConcludedRegistrations() {
        final List<Registration> result = new ArrayList<Registration>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isConcluded()) {
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

    public Registration getLastConcludedRegistration() {
        List<Registration> concludedRegistrations = getConcludedRegistrations();
        return concludedRegistrations.isEmpty() ? null : (Registration) Collections.max(concludedRegistrations,
                Registration.COMPARATOR_BY_START_DATE);
    }

    public Registration getLastRegistration() {
        Collection<Registration> activeRegistrations = getRegistrationsSet();
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

    public boolean hasSpecialSeasonEnrolments(ExecutionYear executionYear) {
        for (Registration registration : getRegistrationsSet()) {
            if ((executionYear.isAfter(registration.getStartExecutionYear()))
                    && (registration.getStudentCurricularPlan(executionYear).isEnroledInSpecialSeason(executionYear))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSpecialSeasonEnrolments(ExecutionSemester executionSemester) {
        for (Registration registration : getRegistrationsSet()) {
            if ((executionSemester.getExecutionYear().isAfter(registration.getStartExecutionYear()))
                    && (registration.getStudentCurricularPlan(executionSemester).isEnroledInSpecialSeason(executionSemester))) {
                return true;
            }
        }
        return false;
    }

    public static Integer generateStudentNumber() {
        int nextNumber = 0;
        for (final StudentNumber studentNumber : Bennu.getInstance().getStudentNumbersSet()) {
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
        ExecutionYear executionYear =
                ExecutionYear.getExecutionYearByDate(residenceCandidacy.getCreationDateDateTime().toYearMonthDay());
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

    public StudentPersonalDataAuthorizationChoice getPersonalDataAuthorization() {
        return getActivePersonalDataAuthorization() == null ? null : getActivePersonalDataAuthorization()
                .getAuthorizationChoice();
    }

    public void setPersonalDataAuthorization(StudentPersonalDataAuthorizationChoice authorization) {
        new StudentDataShareAuthorization(this, authorization);
    }

    public void setStudentPersonalDataStudentsAssociationAuthorization(StudentPersonalDataAuthorizationChoice authorization) {
        new StudentDataShareStudentsAssociationAuthorization(this, authorization);
    }

    public StudentDataShareStudentsAssociationAuthorization getStudentPersonalDataStudentsAssociationAuthorization() {
        for (StudentDataShareAuthorization shareAuthorization : getStudentDataShareAuthorizationSet()) {
            if (shareAuthorization instanceof StudentDataShareStudentsAssociationAuthorization) {
                return (StudentDataShareStudentsAssociationAuthorization) shareAuthorization;
            }
        }
        return null;
    }

    public boolean hasFilledAuthorizationInformationInCurrentExecutionYear() {
        return getActivePersonalDataAuthorization() != null
                && getActivePersonalDataAuthorization().getSince().isAfter(
                        getCurrentExecutionYearDate().getBeginDateYearMonthDay().toDateTimeAtMidnight());
    }

    private ExecutionYear getCurrentExecutionYearDate() {
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (currentExecutionYear.getBeginDateYearMonthDay().isAfter(new LocalDate())) {
            return currentExecutionYear.getPreviousExecutionYear();
        }
        return currentExecutionYear;
    }

    public StudentDataShareAuthorization getActivePersonalDataAuthorization() {
        return getPersonalDataAuthorizationAt(new DateTime());
    }

    public StudentDataShareAuthorization getPersonalDataAuthorizationAt(DateTime when) {
        StudentDataShareAuthorization target = null;
        for (StudentDataShareAuthorization authorization : getStudentDataShareAuthorizationSet()) {
            if (authorization.isStudentDataShareAuthorization() && authorization.getSince().isBefore(when)) {
                if (target == null || authorization.getSince().isAfter(target.getSince())) {
                    target = authorization;
                }
            }
        }
        return target;
    }

    public Boolean hasPersonalDataAuthorizationForProfessionalPurposesAt() {
        StudentDataShareAuthorization authorization = getPersonalDataAuthorizationAt(new DateTime());
        return authorization != null
                && (authorization.getAuthorizationChoice().equals(StudentPersonalDataAuthorizationChoice.PROFESSIONAL_ENDS)
                        || authorization.getAuthorizationChoice().equals(StudentPersonalDataAuthorizationChoice.ALL_ENDS) || authorization
                        .getAuthorizationChoice().equals(StudentPersonalDataAuthorizationChoice.SEVERAL_ENDS));
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
        // if (isStudentOfDegreeType(DegreeType.MASTER_DEGREE))
        // return DegreeType.MASTER_DEGREE;
        // if (isStudentOfDegreeType(DegreeType.DEGREE))
        // return DegreeType.DEGREE;
        if (isStudentOfDegreeType(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)) {
            return DegreeType.BOLONHA_SPECIALIZATION_DEGREE;
        }
        if (isStudentOfDegreeType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
            return DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
        }
        if (isStudentOfDegreeType(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)) {
            return DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
        }
        if (isStudentOfDegreeType(DegreeType.BOLONHA_MASTER_DEGREE)) {
            return DegreeType.BOLONHA_MASTER_DEGREE;
        }
        if (isStudentOfDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
            return DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
        }
        if (isStudentOfDegreeType(DegreeType.BOLONHA_DEGREE)) {
            return DegreeType.BOLONHA_DEGREE;
        }
        return null;
    }

    public boolean isWorkingStudent() {
        for (StudentStatute statute : getStudentStatutes()) {
            if (statute.getStatuteType() == StudentStatuteType.WORKING_STUDENT) {
                return true;
            }
        }
        return false;
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

    public boolean hasActiveRegistrationForOffice(Unit office) {
        Set<Registration> registrations = getRegistrationsSet();
        for (Registration registration : registrations) {
            if (registration.isActiveForOffice(office)) {
                return true;
            }
        }
        return false;
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

        for (; hasAnyStudentDataByExecutionYear(); getStudentDataByExecutionYear().iterator().next().delete()) {
            ;
        }
        for (; !getRegistrations().isEmpty(); getRegistrations().iterator().next().delete()) {
            ;
        }
        for (; hasAnyVotes(); getVotes().iterator().next().delete()) {
            ;
        }

        getElectedElections().clear();
        getDelegateElections().clear();

        setNumber(null);

        setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    // TODO: This should be removed when master degree payments start using
    // Events and Posting Rules for payments
    public MasterDegreeInsurancePaymentCode calculateMasterDegreeInsurancePaymentCode(final ExecutionYear executionYear) {
        if (!hasMasterDegreeInsurancePaymentCodeFor(executionYear)) {
            return createMasterDegreeInsurancePaymentCode(executionYear);
        } else {
            final MasterDegreeInsurancePaymentCode masterDegreeInsurancePaymentCode =
                    getMasterDegreeInsurancePaymentCodeFor(executionYear);
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
        for (final PaymentCode paymentCode : getPerson().getPaymentCodesSet()) {
            if (paymentCode instanceof MasterDegreeInsurancePaymentCode) {
                final MasterDegreeInsurancePaymentCode masterDegreeInsurancePaymentCode =
                        ((MasterDegreeInsurancePaymentCode) paymentCode);
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

    public boolean isSenior(ExecutionYear executionYear) {
        for (StudentStatute statute : getStudentStatutes()) {
            if (statute.isValidOn(executionYear) && statute.getStatuteType() == StudentStatuteType.SENIOR) {
                return true;
            }
        }
        return false;
    }

    public boolean isSeniorForCurrentExecutionYear() {
        return isSenior(ExecutionYear.readCurrentExecutionYear());
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
                if (studentTestQuestion.getDistributedTest().getTestScope().getExecutionCourse().equals(executionCourse)) {
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

    public Set<Enrolment> getDissertationEnrolments() {
        final Set<Enrolment> result = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_REVERSE_EXECUTION_PERIOD_AND_NAME_AND_ID);
        for (final Registration registration : getRegistrations()) {
            for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
                result.addAll(studentCurricularPlan.getDissertationEnrolments());
            }
        }
        return result;
    }

    final public Enrolment getDissertationEnrolment() {
        return getDissertationEnrolment(null);
    }

    final public TreeSet<Enrolment> getDissertationEnrolments(DegreeCurricularPlan degreeCurricularPlan) {
        final TreeSet<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
        for (final Registration registration : getRegistrations()) {
            enrolments.addAll(registration.getDissertationEnrolments(degreeCurricularPlan));
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

    @Atomic
    public void setSpentTimeInPeriodForInquiry(List<CurricularCourseInquiriesRegistryDTO> courses, Integer weeklySpentHours,
            ExecutionSemester executionSemester) {
        check(this, RolePredicates.STUDENT_PREDICATE);

        if (!StudentInquiryRegistry.checkTotalPercentageDistribution(courses)) {
            throw new DomainException("error.weeklyHoursSpentPercentage.is.not.100.percent");
        }
        if (!StudentInquiryRegistry.checkTotalStudyDaysSpentInExamsSeason(courses)) {
            throw new DomainException("error.studyDaysSpentInExamsSeason.exceedsMaxDaysLimit");
        }

        StudentInquiryExecutionPeriod studentInquiryExecutionPeriod = getStudentInquiryExecutionPeriod(executionSemester);
        if (studentInquiryExecutionPeriod != null && studentInquiryExecutionPeriod.getWeeklyHoursSpentInClassesSeason() != 0) {
            return;
        }

        if (studentInquiryExecutionPeriod == null) {
            studentInquiryExecutionPeriod = new StudentInquiryExecutionPeriod(this, executionSemester);
        }
        studentInquiryExecutionPeriod.setWeeklyHoursSpentInClassesSeason(weeklySpentHours);

        for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
            StudentInquiryRegistry inquiryRegistry = curricularCourseInquiriesRegistryDTO.getInquiryRegistry();
            inquiryRegistry.setStudyDaysSpentInExamsSeason(curricularCourseInquiriesRegistryDTO.getStudyDaysSpentInExamsSeason());
            inquiryRegistry.setWeeklyHoursSpentPercentage(curricularCourseInquiriesRegistryDTO.getWeeklyHoursSpentPercentage());
            inquiryRegistry.setAttendenceClassesPercentage(curricularCourseInquiriesRegistryDTO.getAttendenceClassesPercentage());
            inquiryRegistry.setEstimatedECTS(curricularCourseInquiriesRegistryDTO.getCalculatedECTSCredits());
        }
    }

    public StudentInquiryExecutionPeriod getStudentInquiryExecutionPeriod(ExecutionSemester executionSemester) {
        for (final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod : getStudentsInquiriesExecutionPeriodsSet()) {
            if (studentInquiryExecutionPeriod.getExecutionPeriod() == executionSemester) {
                return studentInquiryExecutionPeriod;
            }
        }
        return null;
    }

    public boolean isWeeklySpentHoursSubmittedForOpenInquiry() {
        StudentInquiryTemplate inquiryTemplate = StudentInquiryTemplate.getCurrentTemplate();
        return inquiryTemplate == null ? false : isWeeklySpentHoursSubmittedForOpenInquiry(inquiryTemplate.getExecutionPeriod());
    }

    public boolean isWeeklySpentHoursSubmittedForOpenInquiry(ExecutionSemester executionSemester) {
        for (final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod : getStudentsInquiriesExecutionPeriods()) {
            if (studentInquiryExecutionPeriod.getExecutionPeriod() == executionSemester) {
                return studentInquiryExecutionPeriod.getWeeklyHoursSpentInClassesSeason() != null;
            }
        }
        return false;
    }

    public StudentInquiryExecutionPeriod getOpenStudentInquiryExecutionPeriod() {
        StudentInquiryTemplate inquiryTemplate = StudentInquiryTemplate.getCurrentTemplate();
        return inquiryTemplate == null ? null : getStudentInquiryExecutionPeriod(inquiryTemplate.getExecutionPeriod());
    }

    @Atomic
    public Collection<StudentInquiryRegistry> retrieveAndCreateMissingInquiryRegistriesForPeriod(
            ExecutionSemester executionSemester) {
        check(this, RolePredicates.STUDENT_PREDICATE);
        final Map<ExecutionCourse, StudentInquiryRegistry> coursesToAnswer =
                getExistingStudentInquiryRegistryMap(executionSemester);

        for (Registration registration : getRegistrations()) {
            if (!registration.isAvailableDegreeTypeForInquiries()) {
                continue;
            }
            for (final Enrolment enrolment : registration.getEnrolments(executionSemester)) {
                createMissingInquiryRegistry(executionSemester, coursesToAnswer, registration, enrolment, false);
            }

            for (final Enrolment enrolment : getPreviousAnnualEnrolmentsForInquiries(executionSemester, registration)) {
                createMissingInquiryRegistry(executionSemester, coursesToAnswer, registration, enrolment, true);
            }
        }
        return coursesToAnswer.values();
    }

    private void createMissingInquiryRegistry(final ExecutionSemester executionSemester,
            final Map<ExecutionCourse, StudentInquiryRegistry> coursesToAnswer, Registration registration,
            final Enrolment enrolment, boolean isAnnual) {
        ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
        if (isAnnual) {
            executionCourse = getQUCExecutionCourseForAnnualCC(executionSemester, enrolment);
        }
        if (executionCourse != null && !coursesToAnswer.containsKey(executionCourse)) {
            coursesToAnswer
                    .put(executionCourse,
                            new StudentInquiryRegistry(executionCourse, executionSemester, enrolment.getCurricularCourse(),
                                    registration));
        }
    }

    private Map<ExecutionCourse, StudentInquiryRegistry> getExistingStudentInquiryRegistryMap(ExecutionSemester executionSemester) {
        final Map<ExecutionCourse, StudentInquiryRegistry> coursesToAnswer =
                new HashMap<ExecutionCourse, StudentInquiryRegistry>();
        for (Registration registration : getRegistrations()) {
            if (!registration.isAvailableDegreeTypeForInquiries()) {
                continue;
            }
            for (final StudentInquiryRegistry studentInquiryRegistry : registration.getStudentsInquiryRegistries()) {
                if (studentInquiryRegistry.getExecutionPeriod() == executionSemester) {
                    coursesToAnswer.put(studentInquiryRegistry.getExecutionCourse(), studentInquiryRegistry);
                }
            }
        }
        return coursesToAnswer;
    }

    private List<Enrolment> getPreviousAnnualEnrolmentsForInquiries(ExecutionSemester executionSemester, Registration registration) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        if (executionSemester.getPreviousExecutionPeriod().getExecutionYear() == executionSemester.getExecutionYear()) {
            for (final Enrolment enrolment : registration.getEnrolments(executionSemester.getPreviousExecutionPeriod())) {
                if (enrolment.getCurricularCourse().isAnual()) {
                    result.add(enrolment);
                }
            }
        }
        return result;
    }

    public Collection<String> getInquiriesCoursesNamesToRespond(ExecutionSemester executionSemester) {
        final Map<ExecutionCourse, String> coursesToAnswer = new HashMap<ExecutionCourse, String>();
        final Set<ExecutionCourse> coursesAnswered = new HashSet<ExecutionCourse>();

        for (Registration registration : getRegistrations()) {

            if (!registration.isAvailableDegreeTypeForInquiries()) {
                continue;
            }
            for (final StudentInquiryRegistry inquiryRegistry : registration.getStudentsInquiryRegistries()) {
                if (inquiryRegistry.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                    if (inquiryRegistry.isOpenToAnswer() || inquiryRegistry.isToAnswerLater()) {
                        coursesToAnswer
                                .put(inquiryRegistry.getExecutionCourse(), inquiryRegistry.getCurricularCourse().getName());
                    } else {
                        coursesAnswered.add(inquiryRegistry.getExecutionCourse());
                    }
                }
            }
            for (final Enrolment enrolment : registration.getEnrolments(executionSemester)) {
                final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
                if (executionCourse != null && !coursesAnswered.contains(executionCourse)) {
                    coursesToAnswer.put(executionCourse, enrolment.getCurricularCourse().getName());
                }
            }
            for (final Enrolment enrolment : getPreviousAnnualEnrolmentsForInquiries(executionSemester, registration)) {
                ExecutionCourse executionCourse = getQUCExecutionCourseForAnnualCC(executionSemester, enrolment);
                if (executionCourse != null && !coursesAnswered.contains(executionCourse)) {
                    coursesToAnswer.put(executionCourse, enrolment.getCurricularCourse().getName());
                }
            }
        }
        return coursesToAnswer.values();
    }

    public boolean hasInquiriesToRespond() {
        StudentInquiryTemplate currentTemplate = StudentInquiryTemplate.getCurrentTemplate();
        if (currentTemplate == null) {
            return false;
        }

        final ExecutionSemester executionSemester = currentTemplate.getExecutionPeriod();

        final Set<CurricularCourse> inquiryCurricularCourses = new HashSet<CurricularCourse>();
        // first collect all studentInquiryRegistries from all registrations
        for (Registration registration : getRegistrations()) {
            if (!registration.isAvailableDegreeTypeForInquiries()) {
                continue;
            }
            for (final StudentInquiryRegistry inquiriesRegistry : registration.getStudentsInquiryRegistries()) {
                if (inquiriesRegistry.getExecutionPeriod() == executionSemester) {
                    if (inquiriesRegistry.isOpenToAnswer()) {
                        return true;
                    } else {
                        inquiryCurricularCourses.add(inquiriesRegistry.getCurricularCourse());
                    }
                }
            }
        }

        for (Registration registration : getRegistrations()) {
            if (!registration.isAvailableDegreeTypeForInquiries()) {
                continue;
            }
            for (Enrolment enrolment : registration.getEnrolments(executionSemester)) {
                final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
                if (executionCourse != null && !inquiryCurricularCourses.contains(enrolment.getCurricularCourse())) {
                    return true;
                }
            }

            for (final Enrolment enrolment : getPreviousAnnualEnrolmentsForInquiries(executionSemester, registration)) {
                ExecutionCourse executionCourse = getQUCExecutionCourseForAnnualCC(executionSemester, enrolment);
                if (executionCourse != null && !inquiryCurricularCourses.contains(enrolment.getCurricularCourse())) {
                    return true;
                }
            }
        }
        return false;
    }

    private ExecutionCourse getQUCExecutionCourseForAnnualCC(final ExecutionSemester executionSemester, final Enrolment enrolment) {
        ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
        if (executionCourse == null) { // some annual courses only have one
            // execution in the 1st semester
            executionCourse = enrolment.getExecutionCourseFor(executionSemester.getPreviousExecutionPeriod());
        }
        return executionCourse;
    }

    public boolean hasYearDelegateInquiriesToAnswer() {
        DelegateInquiryTemplate currentTemplate = DelegateInquiryTemplate.getCurrentTemplate();
        if (currentTemplate == null) {
            return false;
        }
        final ExecutionSemester executionSemester = currentTemplate.getExecutionPeriod();

        for (Delegate delegate : getDelegates()) {
            if (delegate instanceof YearDelegate) {
                if (delegate.isActiveForFirstExecutionYear(executionSemester.getExecutionYear())) {
                    PersonFunction lastYearDelegatePersonFunction =
                            delegate.getDegree()
                                    .getUnit()
                                    .getLastYearDelegatePersonFunctionByExecutionYearAndCurricularYear(
                                            executionSemester.getExecutionYear(), ((YearDelegate) delegate).getCurricularYear());
                    if (lastYearDelegatePersonFunction.getDelegate() == delegate) {
                        if (((YearDelegate) delegate).hasInquiriesToAnswer(executionSemester)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Collection<Registration> getAllRegistrations() {
        return Collections.unmodifiableCollection(super.getRegistrationsSet());
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

    public boolean hasTransitionRegistrations() {
        for (final Registration registration : super.getRegistrationsSet()) {
            if (registration.isTransition()) {
                return true;
            }
        }

        return false;
    }

    public List<Registration> getTransitionRegistrations() {
        check(this, StudentPredicates.checkIfLoggedPersonIsStudentOwnerOrManager);
        final List<Registration> result = new ArrayList<Registration>();
        for (final Registration registration : super.getRegistrationsSet()) {
            if (registration.isTransition()) {
                result.add(registration);
            }
        }
        return result;
    }

    public List<Registration> getTransitionRegistrationsForDegreeCurricularPlansManagedByCoordinator(final Person coordinator) {
        check(this, StudentPredicates.checkIfLoggedPersonIsCoordinator);
        final List<Registration> result = new ArrayList<Registration>();
        for (final Registration registration : super.getRegistrationsSet()) {
            if (registration.isTransition()
                    && coordinator.isCoordinatorFor(registration.getLastDegreeCurricularPlan(),
                            ExecutionYear.readCurrentExecutionYear())) {
                result.add(registration);
            }
        }
        return result;
    }

    public List<Registration> getTransitedRegistrations() {
        check(this, StudentPredicates.checkIfLoggedPersonIsStudentOwnerOrManager);
        List<Registration> result = new ArrayList<Registration>();
        for (Registration registration : super.getRegistrationsSet()) {
            if (registration.isTransited()) {
                result.add(registration);
            }
        }
        return result;
    }

    private boolean isAnyTuitionInDebt(final ExecutionYear executionYear) {
        for (final Registration registration : super.getRegistrationsSet()) {
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
        for (final Registration registration : super.getRegistrationsSet()) {
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
        for (final Registration registration : super.getRegistrationsSet()) {
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
        for (final Registration registration : super.getRegistrationsSet()) {
            final RegistrationState registrationState = registration.getActiveState();
            if (registrationState != null) {
                final RegistrationStateType registrationStateType = registrationState.getStateType();
                if (registrationStateType != RegistrationStateType.TRANSITION && registrationStateType.isActive()) {
                    return true;
                }
            }
        }
        return false;
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
        PersonFunction personFunction = getDelegateFunction(executionYear);
        Degree degree = personFunction != null ? personFunction.getUnit().getDegree() : null;
        if (degree != null && degree.hasActiveDelegateFunctionForStudent(this, executionYear, delegateFunctionType)) {
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

    public PersonFunction getDelegateFunction() {
        return getDelegateFunction(ExecutionYear.readCurrentExecutionYear());
    }

    public PersonFunction getDelegateFunction(ExecutionYear executionYear) {
        PersonFunction delegateFunction = null;
        List<Registration> activeRegistrations = new ArrayList<Registration>(getActiveRegistrations());
        Collections.sort(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
        for (Registration registration : activeRegistrations) {
            delegateFunction = registration.getDegree().getMostSignificantDelegateFunctionForStudent(this, executionYear);
            if (delegateFunction != null) {
                return delegateFunction;
            }
        }
        return delegateFunction;
    }

    private List<Student> getStudentsForMasterDegreeDelegate(Degree degree, ExecutionYear executionYear) {
        final DegreeType degreeType = degree.getDegreeType();
        return (degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree.getSecondCycleStudents(executionYear) : degree
                .getAllStudents());
    }

    private List<Student> getStudentsForDegreeDelegate(Degree degree, ExecutionYear executionYear) {
        final DegreeType degreeType = degree.getDegreeType();
        return (degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree.getFirstCycleStudents(executionYear) : degree
                .getAllStudents());
    }

    private List<Student> getStudentsForYearDelegate(Degree degree, ExecutionYear executionYear) {
        final PersonFunction yearDelegateFunction =
                degree.getActiveDelegatePersonFunctionByStudentAndFunctionType(this, executionYear, FunctionType.DELEGATE_OF_YEAR);
        int curricularYear = yearDelegateFunction.getCurricularYear().getYear();
        return degree.getStudentsFromGivenCurricularYear(curricularYear, executionYear);
    }

    /*
     * If student has delegate role, get the curricular courses he is responsible for
     */
    public Set<CurricularCourse> getCurricularCoursesResponsibleForByFunctionType(FunctionType delegateFunctionType,
            ExecutionYear executionYear) {
        PersonFunction delegateFunction = getDelegateFunction(executionYear);
        return getCurricularCoursesResponsibleForByFunctionType(delegateFunction, executionYear);
    }

    public Set<CurricularCourse> getCurricularCoursesResponsibleForByFunctionType(PersonFunction delegateFunction,
            ExecutionYear executionYear) {
        if (delegateFunction != null) {
            executionYear =
                    executionYear != null ? executionYear : ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate());
            Degree degree = delegateFunction.getUnit().getDegree();
            FunctionType delegateFunctionType = delegateFunction.getFunction().getFunctionType();
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
        final PersonFunction yearDelegateFunction =
                degree.getActiveDelegatePersonFunctionByStudentAndFunctionType(this, executionYear, FunctionType.DELEGATE_OF_YEAR);
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
        final InvocationResult result =
                manager.createAdministrativeOfficeFeeAndInsuranceEvent(studentCurricularPlan, executionYear);

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

    public boolean hasAlreadyVotedForYearDelegateElection(ExecutionYear executionYear, DelegateElectionVotingPeriod votingPeriod) {
        for (DelegateElectionVotingPeriod delegateElectionVotingPeriod : getElectionsWithVotingStudentsSet()) {
            if (delegateElectionVotingPeriod.getDelegateElection() instanceof YearDelegateElection) {
                YearDelegateElection yearDelegateElection =
                        (YearDelegateElection) delegateElectionVotingPeriod.getDelegateElection();
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
                        throw new DomainException("error.found.multiple.attends.for.student.in.execution.course",
                                executionCourse.getNome(), executionCourse.getExecutionPeriod().getQualifiedName());
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

    public boolean hasAnyMissingPersonalInformation() {
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isValidForRAIDES() && registration.hasMissingPersonalInformation(currentExecutionYear)) {
                return true;
            }
        }

        for (final PhdIndividualProgramProcess phdProcess : getPerson().getPhdIndividualProgramProcessesSet()) {
            if (isValidAndActivePhdProcess(phdProcess) && phdProcess.hasMissingPersonalInformation(currentExecutionYear)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActivePhdProgramProcess() {
        for (final PhdIndividualProgramProcess phdProcess : getPerson().getPhdIndividualProgramProcessesSet()) {
            if (isValidAndActivePhdProcess(phdProcess)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidAndActivePhdProcess(PhdIndividualProgramProcess phdProcess) {
        return phdProcess.isProcessActive() && hasValidInsuranceEvent();
    }

    public boolean hasValidInsuranceEvent() {
        return getPerson().getInsuranceEventFor(ExecutionYear.readCurrentExecutionYear()) != null
                && !getPerson().getInsuranceEventFor(ExecutionYear.readCurrentExecutionYear()).isCancelled();
    }

    public List<PersonalInformationBean> getPersonalInformationsWithMissingInformation() {
        final List<PersonalInformationBean> result = new ArrayList<PersonalInformationBean>();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        for (final Registration registration : getRegistrations()) {
            if (registration.isValidForRAIDES() && registration.hasMissingPersonalInformation(currentExecutionYear)) {
                result.add(registration.getPersonalInformationBean(currentExecutionYear));
            }
        }

        for (final PhdIndividualProgramProcess phdProcess : getPerson().getPhdIndividualProgramProcesses()) {
            if (isValidAndActivePhdProcess(phdProcess) && phdProcess.hasMissingPersonalInformation(currentExecutionYear)) {
                result.add(phdProcess.getPersonalInformationBean(currentExecutionYear));
            }
        }

        Collections.sort(result, PersonalInformationBean.COMPARATOR_BY_DESCRIPTION);

        return result;
    }

    public int getNrVotesLastElection() {
        DelegateElection delegateElection =
                DelegateElection.readCurrentDelegateElectionByDegree(getLastActiveRegistration().getDegree());
        return delegateElection.getLastVotingPeriod().getNrVotesByStudent(this);
    }

    public int getTotalPercentageLastElection() {
        DelegateElection delegateElection =
                DelegateElection.readCurrentDelegateElectionByDegree(getLastActiveRegistration().getDegree());
        return delegateElection.getLastVotingPeriod().getTotalPercentageElection(this);
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

    @Atomic
    public void acceptRegistrationsFromOtherStudent(java.util.Collection<Registration> otherRegistrations) {
        Collection<Registration> registrations = super.getRegistrationsSet();
        registrations.addAll(otherRegistrations);
    }

    public boolean isEligibleForCareerWorkshopApplication() {
        /*
         * RULE TO FILTER 2nd CYCLE STUDENTS ONLY - Prior to Sep2012 for (Registration registration : getActiveRegistrations()) {
         * 
         * if (isMasterDegreeOnly(registration)) return true;
         * 
         * if (isIntegratedMasterDegree(registration)) { if (isEnroledOnSecondCycle(registration)) { return true; } } } return false;
         */
        return true;
    }

    private boolean isEnroledOnSecondCycle(Registration registration) {
        if (registration.getLastStudentCurricularPlan().getSecondCycle() == null) {
            return false;
        }

        CycleCurriculumGroup secondCycle = registration.getLastStudentCurricularPlan().getSecondCycle();
        return secondCycle.hasAnyEnrolments();
    }

    private boolean isMasterDegreeOnly(Registration registration) {
        return (registration.getDegree().getDegreeType() == DegreeType.BOLONHA_MASTER_DEGREE);
    }

    private boolean isIntegratedMasterDegree(Registration registration) {
        return (registration.getDegree().getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
    }

    private boolean hasConcludedFirstCycle(Registration registration) {
        if (registration.getLastStudentCurricularPlan().getFirstCycle() == null) {
            return false;
        }
        if (!registration.getLastStudentCurricularPlan().getFirstCycle().isConcluded()) {
            return false;
        }
        return true;
    }

    private boolean hasAnyOtherConcludedFirstCycle(Registration registration) {
        List<Registration> otherRegistrations = new ArrayList<Registration>(getAllRegistrations());
        otherRegistrations.remove(registration);
        // Coming from other school
        if (otherRegistrations.isEmpty()) {
            return true;
        }

        // Has any 1st Cycle (bologna or classic, half IM) concluded Degree
        for (Registration reg : otherRegistrations) {
            if (reg.getDegree().getDegreeType() == DegreeType.DEGREE) {
                if (reg.isConcluded()) {
                    return true;
                }
            }
            if (reg.getDegree().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
                if (reg.isConcluded()) {
                    return true;
                }
            }
            if (isIntegratedMasterDegree(reg)) {
                if (hasConcludedFirstCycle(reg)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<CareerWorkshopApplication> getCareerWorkshopApplicationsWaitingForConfirmation() {
        List<CareerWorkshopApplication> result = new ArrayList<CareerWorkshopApplication>();
        for (CareerWorkshopApplication app : getCareerWorkshopApplications()) {
            if (app.getCareerWorkshopConfirmation() != null) {
                continue;
            }
            if (!app.getCareerWorkshopApplicationEvent().isConfirmationPeriodOpened()) {
                continue;
            }
            result.add(app);
        }
        return result;
    }

    public List<CareerWorkshopConfirmationEvent> getApplicationsWaitingForConfirmation() {
        List<CareerWorkshopConfirmationEvent> result = new ArrayList<CareerWorkshopConfirmationEvent>();
        for (CareerWorkshopApplication app : getCareerWorkshopApplications()) {
            if (app.getCareerWorkshopConfirmation() != null && app.getCareerWorkshopConfirmation().getSealStamp() != null) {
                continue;
            }
            if (!app.getCareerWorkshopApplicationEvent().isConfirmationPeriodOpened()) {
                continue;
            }
            result.add(app.getCareerWorkshopApplicationEvent().getCareerWorkshopConfirmationEvent());
        }
        return result;
    }

    public List<ExecutionCourseAudit> getExecutionCourseAudits(ExecutionSemester executionSemester) {
        List<ExecutionCourseAudit> result = new ArrayList<ExecutionCourseAudit>();
        for (ExecutionCourseAudit executionCourseAudit : getExecutionCourseAudits()) {
            if (executionCourseAudit.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                result.add(executionCourseAudit);
            }
        }
        return result;
    }

    public void updateStudentRole() {
        final Person person = getPerson();
        final RoleType roleType = RoleType.STUDENT;
        if (shouldHaveStudentRole()) {
            if (!person.hasRole(roleType)) {
                person.addPersonRoleByRoleType(roleType);
            }
        } else {
            if (person.hasRole(roleType)) {
                person.removeRoleByType(roleType);
            }
        }
    }

    public boolean shouldHaveStudentRole() {
        for (final Registration registration : getRegistrationsSet()) {
            final RegistrationStateType stateType = registration.getLastStateType();
            if (stateType != null
                    && ((stateType.isActive() && stateType != RegistrationStateType.SCHOOLPARTCONCLUDED)
                            || stateType == RegistrationStateType.FLUNKED || stateType == RegistrationStateType.INTERRUPTED || stateType == RegistrationStateType.MOBILITY)) {
                return true;
            }
        }
        for (final PhdIndividualProgramProcess process : getPerson().getPhdIndividualProgramProcesses()) {
            final PhdIndividualProgramProcessState state = process.getActiveState();
            if ((state.isActive() && state != PhdIndividualProgramProcessState.CONCLUDED)
                    || state == PhdIndividualProgramProcessState.SUSPENDED || state == PhdIndividualProgramProcessState.FLUNKED) {
                return true;
            }
        }
        return false;
    }

    public String readActiveStudentInfoForJobBank() {
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        ExecutionYear previousExecutionYear = currentExecutionYear.getPreviousExecutionYear();
        Set<Registration> registrations = new HashSet<Registration>();
        LocalDate today = new LocalDate();
        for (Registration registration : getRegistrations()) {
            if (registration.isBolonha() && !registration.getDegreeType().equals(DegreeType.EMPTY)) {
                if (registration.hasAnyActiveState(currentExecutionYear)) {
                    registrations.add(registration);
                } else {
                    RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);
                    if (registrationConclusionBean.isConcluded()) {
                        YearMonthDay conclusionDate = registrationConclusionBean.getConclusionDate();
                        if (conclusionDate != null && !conclusionDate.plusYears(1).isBefore(today)) {
                            registrations.add(registration);
                        }
                    }
                }
            }
        }
        return getRegistrationsAsJSON(registrations);
    }

    public String readStudentInfoForJobBank() {
        Set<Registration> registrations = new HashSet<Registration>();
        LocalDate today = new LocalDate();
        for (Registration registration : getRegistrations()) {
            if (registration.isBolonha() && !registration.getDegreeType().equals(DegreeType.EMPTY)) {
                RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);
                if (registration.isActive() || registrationConclusionBean.isConcluded()) {
                    registrations.add(registration);
                }
            }
        }
        return getRegistrationsAsJSON(registrations);
    }

    protected String getRegistrationsAsJSON(Set<Registration> registrations) {
        JSONArray infos = new JSONArray();
        int i = 0;
        for (Registration registration : registrations) {
            JSONObject studentInfoForJobBank = new JSONObject();
            studentInfoForJobBank.put("username", registration.getPerson().getUsername());
            studentInfoForJobBank.put("hasPersonalDataAuthorization", registration.getStudent()
                    .hasPersonalDataAuthorizationForProfessionalPurposesAt().toString());
            Person person = registration.getStudent().getPerson();
            studentInfoForJobBank.put("dateOfBirth", person.getDateOfBirthYearMonthDay() == null ? null : person
                    .getDateOfBirthYearMonthDay().toString());
            studentInfoForJobBank.put("nationality", person.getCountry() == null ? null : person.getCountry().getName());
            PhysicalAddress defaultPhysicalAddress = person.getDefaultPhysicalAddress();
            studentInfoForJobBank.put("address", defaultPhysicalAddress == null ? null : defaultPhysicalAddress.getAddress());
            studentInfoForJobBank.put("area", defaultPhysicalAddress == null ? null : defaultPhysicalAddress.getArea());
            studentInfoForJobBank.put("areaCode", defaultPhysicalAddress == null ? null : defaultPhysicalAddress.getAreaCode());
            studentInfoForJobBank.put("districtSubdivisionOfResidence",
                    defaultPhysicalAddress == null ? null : defaultPhysicalAddress.getDistrictSubdivisionOfResidence());
            studentInfoForJobBank.put("mobilePhone", person.getDefaultMobilePhoneNumber());
            studentInfoForJobBank.put("phone", person.getDefaultPhoneNumber());
            studentInfoForJobBank.put("email", person.getEmailForSendingEmails());
            studentInfoForJobBank.put("remoteRegistrationOID", registration.getExternalId());
            studentInfoForJobBank.put("number", registration.getNumber().toString());
            studentInfoForJobBank.put("degreeOID", registration.getDegree().getExternalId());
            studentInfoForJobBank.put("isConcluded", String.valueOf(registration.isRegistrationConclusionProcessed()));
            studentInfoForJobBank.put("curricularYear", String.valueOf(registration.getCurricularYear()));
            for (CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
                    .getCycleCurriculumGroups()) {
                studentInfoForJobBank.put(cycleCurriculumGroup.getCycleType().name(), cycleCurriculumGroup.getAverage()
                        .toString());

            }
            infos.add(studentInfoForJobBank);
        }
        return infos.toJSONString();
    }

    public PersonalIngressionData getLatestPersonalIngressionData() {
        TreeSet<PersonalIngressionData> personalInformations =
                new TreeSet<PersonalIngressionData>(Collections.reverseOrder(PersonalIngressionData.COMPARATOR_BY_EXECUTION_YEAR));
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (PersonalIngressionData pid : getPersonalIngressionsData()) {
            if (!pid.getExecutionYear().isAfter(currentExecutionYear)) {
                personalInformations.add(pid);
            }
        }

        if (personalInformations.isEmpty()) {
            return null;
        }
        return personalInformations.iterator().next();
    }

    public PersonalIngressionData getPersonalIngressionDataByExecutionYear(final ExecutionYear executionYear) {
        for (PersonalIngressionData pid : getPersonalIngressionsData()) {
            if (pid.getExecutionYear() == executionYear) {
                return pid;
            }
        }

        return null;
    }

    public boolean hasFirstTimeCycleInquiryToRespond() {
        for (Registration registration : getActiveRegistrations()) {
            if (!registration.getDegreeType().isEmpty() && !registration.hasInquiryStudentCycleAnswer()
                    && registration.isFirstTime()) {
                if (registration.hasPhdIndividualProgramProcess()
                        && registration.getPhdIndividualProgramProcess().hasInquiryStudentCycleAnswer()) {
                    return false;
                }
                return true;
            }
        }
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (final PhdIndividualProgramProcess phdProcess : getPerson().getPhdIndividualProgramProcesses()) {
            if (!phdProcess.hasInquiryStudentCycleAnswer() && isValidAndActivePhdProcess(phdProcess)) {
                if (phdProcess.hasRegistration()) {
                    if (phdProcess.getRegistration().hasInquiryStudentCycleAnswer()) {
                        return false;
                    } else {
                        if (currentExecutionYear.containsDate(phdProcess.getWhenStartedStudies())) {
                            return true;
                        }
                    }
                } else {
                    if (currentExecutionYear.containsDate(phdProcess.getWhenStartedStudies())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Registration> getRegistrations() {
        return getRegistrationsSet();
    }

    @Deprecated
    public boolean hasAnyRegistrations() {
        return !getRegistrationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.StudentInquiryExecutionPeriod> getStudentsInquiriesExecutionPeriods() {
        return getStudentsInquiriesExecutionPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyStudentsInquiriesExecutionPeriods() {
        return !getStudentsInquiriesExecutionPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElection> getDelegateElections() {
        return getDelegateElectionsSet();
    }

    @Deprecated
    public boolean hasAnyDelegateElections() {
        return !getDelegateElectionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit> getExecutionCourseAudits() {
        return getExecutionCourseAuditsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionCourseAudits() {
        return !getExecutionCourseAuditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElectionVote> getVotes() {
        return getVotesSet();
    }

    @Deprecated
    public boolean hasAnyVotes() {
        return !getVotesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear> getStudentDataByExecutionYear() {
        return getStudentDataByExecutionYearSet();
    }

    @Deprecated
    public boolean hasAnyStudentDataByExecutionYear() {
        return !getStudentDataByExecutionYearSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization> getStudentDataShareAuthorization() {
        return getStudentDataShareAuthorizationSet();
    }

    @Deprecated
    public boolean hasAnyStudentDataShareAuthorization() {
        return !getStudentDataShareAuthorizationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod> getVotingPeriodForNewRoundElections() {
        return getVotingPeriodForNewRoundElectionsSet();
    }

    @Deprecated
    public boolean hasAnyVotingPeriodForNewRoundElections() {
        return !getVotingPeriodForNewRoundElectionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesStudentExecutionPeriod> getInquiriesStudentExecutionPeriods() {
        return getInquiriesStudentExecutionPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyInquiriesStudentExecutionPeriods() {
        return !getInquiriesStudentExecutionPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReport> getUtlScholarshipReport() {
        return getUtlScholarshipReportSet();
    }

    @Deprecated
    public boolean hasAnyUtlScholarshipReport() {
        return !getUtlScholarshipReportSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElection> getElectedElections() {
        return getElectedElectionsSet();
    }

    @Deprecated
    public boolean hasAnyElectedElections() {
        return !getElectedElectionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.StudentStatute> getStudentStatutes() {
        return getStudentStatutesSet();
    }

    @Deprecated
    public boolean hasAnyStudentStatutes() {
        return !getStudentStatutesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PersonalIngressionData> getPersonalIngressionsData() {
        return getPersonalIngressionsDataSet();
    }

    @Deprecated
    public boolean hasAnyPersonalIngressionsData() {
        return !getPersonalIngressionsDataSet().isEmpty();
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
    public java.util.Set<net.sourceforge.fenixedu.domain.residence.StudentsPerformanceReport> getReports() {
        return getReportsSet();
    }

    @Deprecated
    public boolean hasAnyReports() {
        return !getReportsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication> getCareerWorkshopApplications() {
        return getCareerWorkshopApplicationsSet();
    }

    @Deprecated
    public boolean hasAnyCareerWorkshopApplications() {
        return !getCareerWorkshopApplicationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmation> getCareerWorkshopConfirmations() {
        return getCareerWorkshopConfirmationsSet();
    }

    @Deprecated
    public boolean hasAnyCareerWorkshopConfirmations() {
        return !getCareerWorkshopConfirmationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity> getExtraCurricularActivity() {
        return getExtraCurricularActivitySet();
    }

    @Deprecated
    public boolean hasAnyExtraCurricularActivity() {
        return !getExtraCurricularActivitySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElection> getElectionsWithStudentCandidacies() {
        return getElectionsWithStudentCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyElectionsWithStudentCandidacies() {
        return !getElectionsWithStudentCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod> getElectionsWithVotingStudents() {
        return getElectionsWithVotingStudentsSet();
    }

    @Deprecated
    public boolean hasAnyElectionsWithVotingStudents() {
        return !getElectionsWithVotingStudentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasStudentNumber() {
        return getStudentNumber() != null;
    }

    @Deprecated
    public boolean hasAlumni() {
        return getAlumni() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
