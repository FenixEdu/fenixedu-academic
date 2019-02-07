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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.log.CurriculumLineLog;
import org.fenixedu.academic.domain.messaging.Forum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.dto.student.StudentStatuteBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.Atomic;

public class Student extends Student_Base {

    public final static Comparator<Student> NAME_COMPARATOR = new Comparator<Student>() {

        @Override
        public int compare(final Student o1, final Student o2) {
            return o1.getPerson().getName().compareTo(o2.getPerson().getName());
        }

    };

    public final static Comparator<Student> NUMBER_COMPARATOR = new Comparator<Student>() {

        @Override
        public int compare(final Student o1, final Student o2) {
            return o1.getNumber().compareTo(o2.getNumber());
        }

    };

    public Student(final Person person, Integer number) {
        super();
        setPerson(person);
        if (number == null || readStudentByNumber(number) != null) {
            number = Student.generateStudentNumber();
        }
        setNumber(number);
        setRootDomainObject(Bennu.getInstance());
    }

    public static Student createStudentWithCustomNumber(final Person person, final Integer number) {
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

    public Student(final Person person) {
        this(person, null);
    }

    public static Student readStudentByNumber(final Integer number) {
        return Bennu.getInstance().getStudentNumbersSet().stream().filter(sn -> sn.getNumber().equals(number))
                .map(StudentNumber::getStudent).findAny().orElse(null);
    }

    public String getName() {
        return getPerson().getName();
    }

    public Collection<Registration> getRegistrationsMatchingDegreeType(final java.util.function.Predicate<DegreeType> predicate) {
        List<Registration> result = new ArrayList<>();
        for (Registration registration : getRegistrationsSet()) {
            if (predicate.test(registration.getDegreeType())) {
                result.add(registration);
            }
        }
        return result;
    }

    /**
     * @deprecated method is never used... delete it
     */
    @Deprecated
    public boolean hasAnyRegistration(final DegreeType degreeType) {
        for (Registration registration : getRegistrationsSet()) {
            if (registration.getDegreeType().equals(degreeType)) {
                return true;
            }
        }
        return false;
    }

    public Registration readRegistrationByDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
        return getRegistrationStream().filter(r -> r.getStudentCurricularPlan(degreeCurricularPlan) != null).findAny()
                .orElse(null);
    }

    public Registration readRegistrationByDegree(final Degree degree) {
        for (final Registration registration : this.getRegistrationsSet()) {
            if (registration.getDegree() == degree) {
                return registration;
            }
        }
        return null;
    }

    public Collection<Registration> getRegistrationsByDegreeTypeAndExecutionPeriod(final DegreeType degreeType,
            final ExecutionSemester executionSemester) {
        List<Registration> result = new ArrayList<>();
        for (Registration registration : getRegistrationsSet()) {
            if (registration.getDegreeType().equals(degreeType)
                    && registration.hasStudentCurricularPlanInExecutionPeriod(executionSemester)) {
                result.add(registration);
            }
        }
        return result;
    }

    public Collection<Registration> getRegistrationsByDegreeTypes(final DegreeType... degreeTypes) {
        List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
        List<Registration> result = new ArrayList<>();
        for (Registration registration : getRegistrationsSet()) {
            if (degreeTypesList.contains(registration.getDegreeType())) {
                result.add(registration);
            }
        }
        return result;
    }

    public Stream<Registration> getActiveRegistrationStream() {
        return getRegistrationStream().filter(r -> r.isActive());
    }

    /**
     * @deprecated use getActiveRegistrationStream instead;
     */
    @Deprecated
    public List<Registration> getActiveRegistrations() {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isActive()) {
                result.add(registration);
            }
        }
        return result;
    }

    public List<Registration> getConcludedRegistrations() {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isRegistrationConclusionProcessed()) {
                result.add(registration);
            }
        }
        return result;
    }

    public List<Registration> getActiveRegistrationsIn(final ExecutionSemester executionSemester) {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.hasActiveLastState(executionSemester)) {
                result.add(registration);
            }
        }
        return result;
    }

    public Registration getLastActiveRegistration() {
        List<Registration> activeRegistrations = getActiveRegistrations();
        return activeRegistrations
                .isEmpty() ? null : (Registration) Collections.max(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
    }

    public Registration getLastConcludedRegistration() {
        List<Registration> concludedRegistrations = getConcludedRegistrations();
        return concludedRegistrations
                .isEmpty() ? null : (Registration) Collections.max(concludedRegistrations, Registration.COMPARATOR_BY_START_DATE);
    }

    public Registration getLastRegistration() {
        Collection<Registration> activeRegistrations = getRegistrationsSet();
        return activeRegistrations
                .isEmpty() ? null : (Registration) Collections.max(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
    }

    /**
     * @deprecated method is never used... delete it
     */
    @Deprecated
    public boolean hasActiveRegistrationForDegreeType(final DegreeType degreeType, final ExecutionYear executionYear) {
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.hasAnyEnrolmentsIn(executionYear) && registration.getDegreeType() == degreeType) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyRegistrationInState(final RegistrationStateType stateType) {
        return getRegistrationStream().anyMatch(r -> r.getActiveStateType() == stateType);
    }

    /**
     * @deprecated method is never used... delete it
     */
    @Deprecated
    public boolean hasSpecialSeasonEnrolments(final ExecutionYear executionYear) {
        for (Registration registration : getRegistrationsSet()) {
            if (registration.getStudentCurricularPlan(executionYear) == null) {
                continue;
            }

            if (executionYear.isAfter(registration.getStartExecutionYear())
                    && registration.getStudentCurricularPlan(executionYear).isEnroledInSpecialSeason(executionYear)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSpecialSeasonEnrolments(final ExecutionSemester executionSemester) {
        return getRegistrationStream().anyMatch(r -> executionSemester.getExecutionYear().isAfter(r.getStartExecutionYear())
                && r.getStudentCurricularPlan(executionSemester).isEnroledInSpecialSeason(executionSemester));
    }

    public static Integer generateStudentNumber() {
        int max = 0;
        for (final StudentNumber studentNumber : Bennu.getInstance().getStudentNumbersSet()) {
            int n = studentNumber.getNumber().intValue();
            if (n > max) {
                max = n;
            }
        }
        return Integer.valueOf(max + 1);
    }

    public boolean isWorkingStudent() {
        for (StudentStatute statute : getStudentStatutesSet()) {
            if (statute.getType().isWorkingStudentStatute()) {
                return true;
            }
        }
        return false;
    }

    public List<Registration> getRegistrationsFor(final AdministrativeOffice administrativeOffice) {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isForOffice(administrativeOffice)) {
                result.add(registration);
            }
        }
        return result;
    }

    public boolean hasActiveRegistrationForOffice(final Unit office) {
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

    public boolean attends(final ExecutionCourse executionCourse) {
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
        for (; !getRegistrationsSet().isEmpty(); getRegistrationsSet().iterator().next().delete()) {
            ;
        }

        setNumber(null);

        setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public Set<ExecutionSemester> getEnroledExecutionPeriods() {
        Set<ExecutionSemester> result = new TreeSet<>(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
        for (Registration registration : getRegistrationsSet()) {
            result.addAll(registration.getEnrolmentsExecutionPeriods());
        }
        return result;
    }

    public Collection<StudentStatuteBean> getCurrentStatutes() {
        return getStatutesValidOnAnyExecutionSemesterFor(ExecutionYear.readCurrentExecutionYear());
    }

    public Collection<StudentStatuteBean> getStatutes(final ExecutionSemester executionSemester) {
        final List<StudentStatuteBean> result = new ArrayList<>();
        for (final StudentStatute statute : getStudentStatutesSet()) {
            if (statute.isValidInExecutionPeriod(executionSemester)) {
                result.add(new StudentStatuteBean(statute, executionSemester));
            }
        }

        if (isHandicapped()) {
            result.add(new StudentStatuteBean(StatuteType.findHandicappedStatuteType().orElse(null), executionSemester));
        }

        return result;
    }

    public Collection<StatuteType> getStatutesTypesValidOnAnyExecutionSemesterFor(final ExecutionYear executionYear) {
        return getStatutesValidOnAnyExecutionSemesterFor(executionYear).stream().map(bean -> bean.getStatuteType())
                .collect(Collectors.toList());
    }

    public Collection<StudentStatuteBean> getStatutesValidOnAnyExecutionSemesterFor(final ExecutionYear executionYear) {
        final Collection<StudentStatuteBean> result = new ArrayList<>();
        for (final StudentStatute statute : getStudentStatutesSet()) {
            if (statute.isValidOnAnyExecutionPeriodFor(executionYear)) {
                result.add(new StudentStatuteBean(statute));
            }
        }

        if (isHandicapped()) {
            result.add(new StudentStatuteBean(StatuteType.findHandicappedStatuteType().orElse(null)));
        }

        return result;
    }

    public Collection<StudentStatuteBean> getAllStatutes() {
        List<StudentStatuteBean> result = new ArrayList<>();
        for (StudentStatute statute : getStudentStatutesSet()) {
            result.add(new StudentStatuteBean(statute));
        }

        if (isHandicapped()) {
            result.add(new StudentStatuteBean(StatuteType.findHandicappedStatuteType().orElse(null)));
        }

        return result;
    }

    public Collection<StudentStatuteBean> getAllStatutesSplittedByExecutionPeriod() {
        List<StudentStatuteBean> result = new ArrayList<>();
        for (ExecutionSemester executionSemester : getEnroledExecutionPeriods()) {
            result.addAll(getStatutes(executionSemester));
        }
        return result;
    }

    public boolean isSenior(final ExecutionYear executionYear) {
        for (StudentStatute statute : getStudentStatutesSet()) {
            if (statute.isValidOn(executionYear) && statute.getType().isSeniorStatute()) {
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
        final Set<Enrolment> aprovedEnrolments = new HashSet<>();
        for (final Registration registration : getRegistrationsSet()) {
            aprovedEnrolments.addAll(registration.getApprovedEnrolments());
        }
        return aprovedEnrolments;
    }

    public List<Enrolment> getApprovedEnrolments(final AdministrativeOffice administrativeOffice) {
        final List<Enrolment> aprovedEnrolments = new ArrayList<>();
        for (final Registration registration : getRegistrationsFor(administrativeOffice)) {
            aprovedEnrolments.addAll(registration.getApprovedEnrolments());
        }
        return aprovedEnrolments;
    }

    public Set<Enrolment> getDismissalApprovedEnrolments() {
        Set<Enrolment> aprovedEnrolments = new HashSet<>();
        for (Registration registration : getRegistrationsSet()) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                aprovedEnrolments.addAll(studentCurricularPlan.getDismissalApprovedEnrolments());
            }
        }
        return aprovedEnrolments;
    }

    public boolean isHandicapped() {
        for (Registration registration : getRegistrationsSet()) {
            if (registration.getIngressionType() != null && registration.getIngressionType().isHandicappedContingent()) {
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
        final Set<StudentCurricularPlan> result = new HashSet<>();
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

    public SortedSet<Attends> getAttendsForExecutionPeriod(final ExecutionSemester executionSemester) {
        SortedSet<Attends> attends = new TreeSet<>(Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        for (Registration registration : getRegistrationsSet()) {
            attends.addAll(registration.getAttendsForExecutionPeriod(executionSemester));
        }
        return attends;
    }

    public List<Registration> getRegistrationsToEnrolByStudent() {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isEnrolmentByStudentAllowed()) {
                result.add(registration);
            }
        }

        return result;

    }

    public List<Registration> getRegistrationsToEnrolInShiftByStudent() {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isEnrolmentByStudentInShiftsAllowed()) {
                result.add(registration);
            }
        }

        return result;
    }

    public boolean isCurrentlyEnroled(final DegreeCurricularPlan degreeCurricularPlan) {
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
        final Set<Enrolment> result = new TreeSet<>(Enrolment.COMPARATOR_BY_REVERSE_EXECUTION_PERIOD_AND_NAME_AND_ID);
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

    final public TreeSet<Enrolment> getDissertationEnrolments(final DegreeCurricularPlan degreeCurricularPlan) {
        final TreeSet<Enrolment> enrolments = new TreeSet<>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
        for (final Registration registration : getRegistrationsSet()) {
            enrolments.addAll(registration.getDissertationEnrolments(degreeCurricularPlan));
        }
        return enrolments;
    }

    final public Enrolment getDissertationEnrolment(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear) {
        TreeSet<Enrolment> enrolments = getDissertationEnrolments(degreeCurricularPlan);
        CollectionUtils.filter(enrolments, new Predicate() {

            @Override
            public boolean evaluate(final Object enrolment) {
                return ((Enrolment) enrolment).getExecutionYear().equals(executionYear);
            }
        });
        return enrolments.isEmpty() ? null : enrolments.last();
    }

    final public Enrolment getDissertationEnrolment(final DegreeCurricularPlan degreeCurricularPlan) {
        TreeSet<Enrolment> enrolments = getDissertationEnrolments(degreeCurricularPlan);
        return enrolments.isEmpty() ? null : enrolments.last();
    }

    public Collection<Registration> getAllRegistrations() {
        return Collections.unmodifiableCollection(super.getRegistrationsSet());
    }

    public Stream<Registration> getRegistrationStream() {
        return super.getRegistrationsSet().stream().filter(r -> !r.isTransition());
    }

    /**
     * -> Temporary overrides due migrations - Filter 'InTransition'
     * registrations -> Do not use this method to add new registrations directly
     * (use {@link addRegistrations} method)
     */
    @Override
    public Set<Registration> getRegistrationsSet() {
        final Set<Registration> result = new HashSet<>();
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
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : super.getRegistrationsSet()) {
            if (registration.isTransition()) {
                result.add(registration);
            }
        }
        return result;
    }

    public List<Registration> getTransitionRegistrationsForDegreeCurricularPlansManagedByCoordinator(final Person coordinator) {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : super.getRegistrationsSet()) {
            if (registration.isTransition() && coordinator.isCoordinatorFor(registration.getLastDegreeCurricularPlan(),
                    ExecutionYear.readCurrentExecutionYear())) {
                result.add(registration);
            }
        }
        return result;
    }

    public List<Registration> getTransitedRegistrations() {
        List<Registration> result = new ArrayList<>();
        for (Registration registration : super.getRegistrationsSet()) {
            if (registration.isTransited()) {
                result.add(registration);
            }
        }
        return result;
    }

    public List<Registration> getRegistrationsFor(final DegreeCurricularPlan degreeCurricularPlan) {
        final List<Registration> result = new ArrayList<>();
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
        final List<Registration> result = new ArrayList<>();
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
        return getActiveRegistrationStream().filter(r -> r.getLastDegreeCurricularPlan() == degreeCurricularPlan).findAny()
                .orElse(null);
    }

    public boolean hasActiveRegistrationFor(final DegreeCurricularPlan degreeCurricularPlan) {
        return getActiveRegistrationFor(degreeCurricularPlan) != null;
    }

    public Registration getActiveRegistrationFor(final Degree degree) {
        return getActiveRegistrationStream().filter(r -> r.getLastDegree() == degree).findAny().orElse(null);
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

    public Registration getTransitionRegistrationFor(final DegreeCurricularPlan degreeCurricularPlan) {
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
        final SortedSet<ExternalEnrolment> result = new TreeSet<>(ExternalEnrolment.COMPARATOR_BY_NAME);
        for (final Registration registration : getRegistrationsSet()) {
            result.addAll(registration.getExternalEnrolmentsSet());
        }
        return result;
    }

    public Collection<? extends Forum> getForuns(final ExecutionSemester executionSemester) {
        final Collection<Forum> res = new HashSet<>();
        for (Registration registration : getRegistrationsSet()) {
            for (Attends attends : registration.getAssociatedAttendsSet()) {
                if (attends.getExecutionPeriod() == executionSemester) {
                    res.addAll(attends.getExecutionCourse().getForuns());
                }
            }
        }
        return res;
    }

    public Collection<CurriculumLineLog> getCurriculumLineLogs(final ExecutionSemester executionSemester) {
        final Collection<CurriculumLineLog> res = new HashSet<>();
        for (final Registration registration : getRegistrationsSet()) {
            res.addAll(registration.getCurriculumLineLogs(executionSemester));
        }
        return res;
    }

    public boolean hasActiveStatuteInPeriod(final StatuteType statuteType, final ExecutionSemester executionSemester) {
        for (StudentStatute studentStatute : getStudentStatutesSet()) {
            if (studentStatute.getType() == statuteType && studentStatute.isValidInExecutionPeriod(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWorkingStudentStatuteInPeriod(final ExecutionSemester executionSemester) {
        for (StudentStatute studentStatute : getStudentStatutesSet()) {
            if (studentStatute.getType().isWorkingStudentStatute()
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
        return getActiveRegistrationStream().anyMatch(r -> r.getCampus() == campus);
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

    public Collection<ExecutionYear> getEnrolmentsExecutionYears() {
        Set<ExecutionYear> executionYears = new HashSet<>();
        for (final Registration registration : getRegistrationsSet()) {
            executionYears.addAll(registration.getEnrolmentsExecutionYears());
        }
        return executionYears;
    }

    public boolean getActiveAlumni() {
        return getAlumni() != null;
    }

    public Attends getAttends(final ExecutionCourse executionCourse) {
        return getRegistrationStream().flatMap(r -> r.getAssociatedAttendsSet().stream()).filter(a -> a.isFor(executionCourse))
                .findAny().orElse(null);
    }

    public boolean hasAttends(final ExecutionCourse executionCourse) {
        return getRegistrationStream().flatMap(r -> r.getAssociatedAttendsSet().stream()).anyMatch(a -> a.isFor(executionCourse));
    }

    public boolean hasAnyMissingPersonalInformation() {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (getRegistrationStream()
                .anyMatch(r -> r.isValidForRAIDES() && r.hasMissingPersonalInformation(currentExecutionYear))) {
            return true;
        }

        return false;
    }

    public List<PersonalInformationBean> getPersonalInformationsWithMissingInformation() {
        final List<PersonalInformationBean> result = new ArrayList<>();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        for (final Registration registration : getRegistrationsSet()) {
            if (registration.isValidForRAIDES() && registration.hasMissingPersonalInformation(currentExecutionYear)) {
                result.add(registration.getPersonalInformationBean(currentExecutionYear));
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
    public void acceptRegistrationsFromOtherStudent(final java.util.Collection<Registration> otherRegistrations) {
        Collection<Registration> registrations = super.getRegistrationsSet();
        registrations.addAll(otherRegistrations);
    }

    public void updateStudentRole() {
        if (shouldHaveStudentRole()) {
            getPerson().ensureOpenUserAccount();
        }
    }

    public boolean shouldHaveStudentRole() {
        final boolean activeRegistration = getRegistrationStream().map(r -> r.getLastStateType()).filter(st -> st != null)
                .anyMatch(st -> st.isActive() && st != RegistrationStateType.SCHOOLPARTCONCLUDED
                        || st == RegistrationStateType.FLUNKED || st == RegistrationStateType.INTERRUPTED
                        || st == RegistrationStateType.MOBILITY);
        if (activeRegistration) {
            return true;
        }

        return false;
    }

    public PersonalIngressionData getLatestPersonalIngressionData() {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        final Comparator<PersonalIngressionData> comparator =
                Collections.reverseOrder(PersonalIngressionData.COMPARATOR_BY_EXECUTION_YEAR);
        return getPersonalIngressionsDataSet().stream().filter(pid -> !pid.getExecutionYear().isAfter(currentExecutionYear))
                .sorted(comparator).findFirst().orElse(null);
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
