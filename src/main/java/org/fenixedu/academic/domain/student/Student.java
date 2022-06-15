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

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.dto.student.StudentStatuteBean;
import org.fenixedu.bennu.core.domain.Bennu;

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

    /**
     * It should be given preference to use {@link Registration#readByNumber(Integer)} instead of
     * this method. If it is not possible to use {@link Registration#readByNumber(Integer)}, then
     * the logic should be evaluated and reviewed.
     * 
     * @param number
     * @return
     */
    public static Student readStudentByNumber(final Integer number) {
        return Bennu.getInstance().getStudentNumbersSet().stream().filter(sn -> sn.getNumber().equals(number))
                .map(StudentNumber::getStudent).findAny().orElse(null);
    }

    public String getName() {
        return getPerson().getName();
    }

    public Registration readRegistrationByDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
        return getRegistrationStream().filter(r -> r.getStudentCurricularPlan(degreeCurricularPlan) != null).findAny()
                .orElse(null);
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

    public List<Registration> getActiveRegistrationsIn(final ExecutionInterval executionInterval) {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : getRegistrationsSet()) {
            if (registration.hasActiveLastState(executionInterval)) {
                result.add(registration);
            }
        }
        return result;
    }

    public Registration getLastRegistration() {
        Collection<Registration> activeRegistrations = getRegistrationsSet();
        return activeRegistrations
                .isEmpty() ? null : (Registration) Collections.max(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
    }

    public boolean hasAnyRegistrationInState(final RegistrationStateType stateType) {
        return getRegistrationStream().anyMatch(r -> r.getActiveStateType() == stateType);
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

//    public boolean attends(final ExecutionCourse executionCourse) {
//        for (final Registration registration : getRegistrationsSet()) {
//            if (registration.attends(executionCourse)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        for (; !getRegistrationsSet().isEmpty(); getRegistrationsSet().iterator().next().delete()) {
            ;
        }
        
        for (; !getPersonalIngressionsDataSet().isEmpty(); getPersonalIngressionsDataSet().iterator().next().delete()) {
            ;
        }

        setNumber(null);

        setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public Set<ExecutionInterval> getEnroledExecutionPeriods() {
        Set<ExecutionInterval> result = new TreeSet<>(ExecutionInterval.COMPARATOR_BY_BEGIN_DATE);
        for (Registration registration : getRegistrationsSet()) {
            result.addAll(registration.getEnrolmentsExecutionPeriods());
        }
        return result;
    }

    public Collection<StudentStatuteBean> getCurrentStatutes() {
        return getStatutesValidOnAnyExecutionSemesterFor(ExecutionYear.findCurrent(null));
    }

    public Collection<StudentStatuteBean> getStatutes(final ExecutionInterval executionInterval) {
        final List<StudentStatuteBean> result = new ArrayList<>();
        for (final StudentStatute statute : getStudentStatutesSet()) {
            if (statute.isValidInExecutionInterval(executionInterval)) {
                result.add(new StudentStatuteBean(statute, executionInterval));
            }
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

        return result;
    }

    public Collection<StudentStatuteBean> getAllStatutes() {
        List<StudentStatuteBean> result = new ArrayList<>();
        for (StudentStatute statute : getStudentStatutesSet()) {
            result.add(new StudentStatuteBean(statute));
        }

        return result;
    }

    public Set<Enrolment> getApprovedEnrolments() {
        final Set<Enrolment> aprovedEnrolments = new HashSet<>();
        for (final Registration registration : getRegistrationsSet()) {
            aprovedEnrolments.addAll(registration.getApprovedEnrolments());
        }
        return aprovedEnrolments;
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

    public SortedSet<Attends> getAttendsForExecutionPeriod(final ExecutionInterval executionInterval) {
        SortedSet<Attends> attends = new TreeSet<>(Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        for (Registration registration : getRegistrationsSet()) {
            attends.addAll(registration.getAttendsForExecutionPeriod(executionInterval));
        }
        return attends;
    }

    public Stream<Registration> getRegistrationStream() {
        return super.getRegistrationsSet().stream();
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

    public List<Registration> getRegistrationsFor(final Degree degree) {
        final List<Registration> result = new ArrayList<>();
        for (final Registration registration : super.getRegistrationsSet()) {
            if (registration.getDegree() == degree) {
                result.add(registration);
            }
        }
        return result;
    }

    public Registration getActiveRegistrationFor(final DegreeCurricularPlan degreeCurricularPlan) {
        return getActiveRegistrationStream().filter(r -> r.getLastDegreeCurricularPlan() == degreeCurricularPlan).findAny()
                .orElse(null);
    }

    public Registration getActiveRegistrationFor(final Degree degree) {
        return getActiveRegistrationStream().filter(r -> r.getLastDegree() == degree).findAny().orElse(null);
    }

    public boolean hasActiveRegistrations() {
        for (final Registration registration : super.getRegistrationsSet()) {
            final RegistrationState registrationState = registration.getActiveState();
            if (registrationState != null) {
                if (registrationState.getStateType().isActive()) {
                    return true;
                }
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

    public boolean hasWorkingStudentStatuteInPeriod(final ExecutionInterval executionInterval) {
        for (StudentStatute studentStatute : getStudentStatutesSet()) {
            if (studentStatute.getType().isWorkingStudentStatute()
                    && studentStatute.isValidInExecutionInterval(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    public Attends getAttends(final ExecutionCourse executionCourse) {
        return getRegistrationStream().flatMap(r -> r.getAssociatedAttendsSet().stream()).filter(a -> a.isFor(executionCourse))
                .findAny().orElse(null);
    }

    public boolean hasAttends(final ExecutionCourse executionCourse) {
        return getRegistrationStream().flatMap(r -> r.getAssociatedAttendsSet().stream()).anyMatch(a -> a.isFor(executionCourse));
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
        final ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(null);
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
