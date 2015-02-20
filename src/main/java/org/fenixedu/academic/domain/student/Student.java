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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.events.AccountingEventsManager;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.paymentCodes.MasterDegreeInsurancePaymentCode;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacy.Ingression;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithInvocationResult;
import org.fenixedu.academic.domain.log.CurriculumLineLog;
import org.fenixedu.academic.domain.messaging.Forum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.dto.student.StudentStatuteBean;
import org.fenixedu.academic.predicate.StudentPredicates;
import org.fenixedu.academic.util.InvocationResult;
import org.fenixedu.academic.util.Money;
import org.fenixedu.academic.util.StudentPersonalDataAuthorizationChoice;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.UserLoginPeriod;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

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
        return Bennu.getInstance().getStudentNumbersSet().stream().filter(sn -> sn.getNumber().equals(number))
                .map(StudentNumber::getStudent).findAny().orElse(null);
    }

    public String getName() {
        return getPerson().getName();
    }

    public Collection<Registration> getRegistrationsByDegreeType(DegreeType degreeType) {
        List<Registration> result = new ArrayList<Registration>();
        for (Registration registration : getRegistrationsSet()) {
            if (registration.getDegreeType().equals(degreeType)) {
                result.add(registration);
            }
        }
        return result;
    }

    public boolean hasAnyRegistration(final DegreeType degreeType) {
        for (Registration registration : getRegistrationsSet()) {
            if (registration.getDegreeType().equals(degreeType)) {
                return true;
            }
        }
        return false;
    }

    public Registration readRegistrationByDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        for (final Registration registration : this.getRegistrationsSet()) {
            StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(degreeCurricularPlan);
            if (studentCurricularPlan != null) {
                return registration;
            }
        }
        return null;
    }

    public Registration readRegistrationByDegree(Degree degree) {
        for (final Registration registration : this.getRegistrationsSet()) {
            if (registration.getDegree() == degree) {
                return registration;
            }
        }
        return null;
    }

    public Collection<Registration> getRegistrationsByDegreeTypeAndExecutionPeriod(DegreeType degreeType,
            ExecutionSemester executionSemester) {
        List<Registration> result = new ArrayList<Registration>();
        for (Registration registration : getRegistrationsSet()) {
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
        for (Registration registration : getRegistrationsSet()) {
            if (degreeTypesList.contains(registration.getDegreeType())) {
                result.add(registration);
            }
        }
        return result;
    }

    @Deprecated
    public Registration getActiveRegistrationByDegreeType(DegreeType degreeType) {
        for (Registration registration : getRegistrationsSet()) {
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
        for (final Registration registration : getRegistrationsSet()) {
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
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.hasAnyEnrolmentsIn(executionYear) && registration.getDegreeType() == degreeType) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyRegistrationInState(final RegistrationStateType stateType) {
        for (final Registration registration : getRegistrationsSet()) {
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
        for (final StudentDataByExecutionYear studentData : getStudentDataByExecutionYearSet()) {
            if (studentData.getExecutionYear().isCurrent()) {
                return studentData;
            }
        }
        return null;
    }

    public StudentDataByExecutionYear getStudentDataByExecutionYear(final ExecutionYear executionYear) {
        for (StudentDataByExecutionYear studentData : getStudentDataByExecutionYearSet()) {
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
        for (StudentStatute statute : getStudentStatutesSet()) {
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
        for (final Registration registration : getRegistrationsSet()) {
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
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        for (; !getStudentDataByExecutionYearSet().isEmpty(); getStudentDataByExecutionYearSet().iterator().next().delete()) {
            ;
        }
        for (; !getRegistrationsSet().isEmpty(); getRegistrationsSet().iterator().next().delete()) {
            ;
        }

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
        for (Registration registration : getRegistrationsSet()) {
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
        for (StudentStatute statute : getStudentStatutesSet()) {
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
        for (StudentStatute statute : getStudentStatutesSet()) {
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
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
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
        for (Registration registration : getRegistrationsSet()) {
            attends.addAll(registration.getAttendsForExecutionPeriod(executionSemester));
        }
        return attends;
    }

    public List<Registration> getRegistrationsToEnrolByStudent() {
        final List<Registration> result = new ArrayList<Registration>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isEnrolmentByStudentAllowed()) {
                result.add(registration);
            }
        }

        return result;

    }

    public List<Registration> getRegistrationsToEnrolInShiftByStudent() {
        final List<Registration> result = new ArrayList<Registration>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isEnrolmentByStudentInShiftsAllowed()) {
                result.add(registration);
            }
        }

        return result;
    }

    public boolean isCurrentlyEnroled(DegreeCurricularPlan degreeCurricularPlan) {
        for (Registration registration : getRegistrationsSet()) {
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
        for (final Registration registration : getRegistrationsSet()) {
            for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
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
        for (final Registration registration : getRegistrationsSet()) {
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
        for (final Event event : getPerson().getEventsSet()) {
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

    public boolean learnsAt(final Space campus) {
        for (final Registration registration : getActiveRegistrations()) {
            if (registration.getCampus() == campus) {
                return true;
            }
        }
        return false;
    }

    public ExecutionYear getFirstRegistrationExecutionYear() {

        ExecutionYear firstYear = null;
        for (Registration registration : getRegistrationsSet()) {
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

    public Collection<? extends AcademicServiceRequest> getAcademicServiceRequests(
            final Class<? extends AcademicServiceRequest> clazz) {
        final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();
        for (final Registration registration : getRegistrationsSet()) {
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
        return getAlumni() != null;
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
        return FenixEduAcademicConfiguration.getConfiguration().getRaidesRequestInfo() && phdProcess.isProcessActive()
                && hasValidInsuranceEvent();
    }

    public boolean hasValidInsuranceEvent() {
        return getPerson().getInsuranceEventFor(ExecutionYear.readCurrentExecutionYear()) != null
                && !getPerson().getInsuranceEventFor(ExecutionYear.readCurrentExecutionYear()).isCancelled();
    }

    public List<PersonalInformationBean> getPersonalInformationsWithMissingInformation() {
        final List<PersonalInformationBean> result = new ArrayList<PersonalInformationBean>();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isValidForRAIDES() && registration.hasMissingPersonalInformation(currentExecutionYear)) {
                result.add(registration.getPersonalInformationBean(currentExecutionYear));
            }
        }

        for (final PhdIndividualProgramProcess phdProcess : getPerson().getPhdIndividualProgramProcessesSet()) {
            if (isValidAndActivePhdProcess(phdProcess) && phdProcess.hasMissingPersonalInformation(currentExecutionYear)) {
                result.add(phdProcess.getPersonalInformationBean(currentExecutionYear));
            }
        }

        Collections.sort(result, PersonalInformationBean.COMPARATOR_BY_DESCRIPTION);

        return result;
    }

    @Override
    public void setNumber(final Integer number) {
        super.setNumber(number);

        if (getStudentNumber() != null) {
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

    public void updateStudentRole() {
        if (shouldHaveStudentRole()) {
            UserLoginPeriod.createOpenPeriod(getPerson().getUser());
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
        for (final PhdIndividualProgramProcess process : getPerson().getPhdIndividualProgramProcessesSet()) {
            final PhdIndividualProgramProcessState state = process.getActiveState();
            if ((state.isActive() && state != PhdIndividualProgramProcessState.CONCLUDED)
                    || state == PhdIndividualProgramProcessState.SUSPENDED || state == PhdIndividualProgramProcessState.FLUNKED) {
                return true;
            }
        }
        return false;
    }

    public PersonalIngressionData getLatestPersonalIngressionData() {
        TreeSet<PersonalIngressionData> personalInformations =
                new TreeSet<PersonalIngressionData>(Collections.reverseOrder(PersonalIngressionData.COMPARATOR_BY_EXECUTION_YEAR));
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (PersonalIngressionData pid : getPersonalIngressionsDataSet()) {
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
        for (PersonalIngressionData pid : getPersonalIngressionsDataSet()) {
            if (pid.getExecutionYear() == executionYear) {
                return pid;
            }
        }

        return null;
    }

}
