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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class Tutorship extends Tutorship_Base {

    public static final Comparator<Tutorship> TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER = new BeanComparator(
            "studentCurricularPlan.registration.number");

    public static final Comparator<Tutorship> TUTORSHIP_COMPARATOR_BY_ENTRY_YEAR = new BeanComparator(
            "studentCurricularPlan.registration.startDate");

    public static Comparator<Tutorship> TUTORSHIP_END_DATE_COMPARATOR = new Comparator<Tutorship>() {
        @Override
        public int compare(Tutorship t1, Tutorship t2) {
            if (t1.getEndDate() == null) {
                return -1;
            } else if (t2.getEndDate() == null) {
                return 1;
            } else {
                return (t1.getEndDate().isBefore(t2.getEndDate()) ? -1 : (t1.getEndDate().isAfter(t2.getEndDate()) ? 1 : 0));
            }
        }
    };

    public static Comparator<Tutorship> TUTORSHIP_START_DATE_COMPARATOR = new Comparator<Tutorship>() {
        @Override
        public int compare(Tutorship t1, Tutorship t2) {
            if (t1.getStartDate() == null) {
                return -1;
            } else if (t2.getStartDate() == null) {
                return 1;
            } else {
                return (t1.getStartDate().isBefore(t2.getStartDate()) ? -1 : (t1.getStartDate().isAfter(t2.getStartDate()) ? 1 : 0));
            }
        }
    };

    // Tutorship maximum period, in years
    public static final int TUTORSHIP_MAX_PERIOD = 2;

    private Tutorship(Teacher teacher, Partial tutorshipStartDate, Partial tutorshipEndDate, StudentCurricularPlan scp) {
        super();
        setRootDomainObject(Bennu.getInstance());
        for (Tutorship tutorship : scp.getTutorshipsSet()) {
            if (tutorship.getTeacher().equals(teacher) && tutorship.getEndDate().equals(tutorshipEndDate)) {
                throw new DomainException("error.tutorships.duplicatedTutorship");
            }

            if (tutorship.isActive()) {
                throw new DomainException("error.tutorships.onlyOneActiveTutorship");
            }
        }
        setTeacher(teacher);
        setStudentCurricularPlan(scp);
        setStartDate(tutorshipStartDate);
        setEndDate(tutorshipEndDate);
    }

    public static void createTutorship(Teacher teacher, StudentCurricularPlan scp, Integer endMonth, Integer endYear) {
        LocalDate currentDate = new LocalDate();

        Partial tutorshipStartDate =
                new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] {
                        currentDate.year().get(), currentDate.monthOfYear().get() });

        Partial tutorshipEndDate =
                new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] {
                        endYear, endMonth });
        Tutorship tutorship = new Tutorship(teacher, tutorshipStartDate, tutorshipEndDate, scp);

        TutorshipLog tutorshipLog = new TutorshipLog();
        if (scp.getRegistration() != null && scp.getRegistration().getStudentCandidacy() != null
                && scp.getRegistration().getStudentCandidacy().getPlacingOption() != null) {
            switch (scp.getRegistration().getStudentCandidacy().getPlacingOption()) {
            case 1: {
                tutorshipLog.setOptionNumberDegree(Option.ONE);
                break;
            }
            case 2: {
                tutorshipLog.setOptionNumberDegree(Option.TWO);
                break;
            }
            case 3: {
                tutorshipLog.setOptionNumberDegree(Option.THREE);
                break;
            }
            case 4: {
                tutorshipLog.setOptionNumberDegree(Option.FOUR);
                break;
            }
            case 5: {
                tutorshipLog.setOptionNumberDegree(Option.FIVE);
                break;
            }
            case 6: {
                tutorshipLog.setOptionNumberDegree(Option.SIX);
                break;
            }
            default: {
                tutorshipLog.setOptionNumberDegree(null);
            }
            }
        }
        tutorship.setTutorshipLog(tutorshipLog);
    }

    public void delete() {
        setStudentCurricularPlan(null);
        setTeacher(null);
        if (getTutorshipLog() != null) {
            getTutorshipLog().delete();
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean hasEndDate() {
        if (getEndDate() != null) {
            return true;
        }
        return false;
    }

    public boolean isActive() {
        if (!getStudent().isActive()) {
            return false;
        }
        if (!this.hasEndDate()) {
            return false;
        }

        YearMonthDay currentYearMonthDay = new YearMonthDay();
        Partial currentDate =
                new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] {
                        currentYearMonthDay.year().get(), currentYearMonthDay.monthOfYear().get() });

        if (getEndDate().isAfter(currentDate)) {
            return true;
        }

        return false;
    }

    public boolean isActive(AcademicInterval semester) {
        RegistrationState registrationState = getStudent().getStateInDate(semester.getEnd());
        if (registrationState != null && !registrationState.isActive()) {
            return false;
        }
        if (!this.hasEndDate()) {
            return false;
        }
        return getEndDate().toDateTime(new DateTime(0)).isAfter(semester.getEnd());
    }

    public static int getLastPossibleTutorshipYear() {
        LocalDate currentDate = new LocalDate();
        return currentDate.getYear() + TUTORSHIP_MAX_PERIOD;
    }

    public Registration getStudent() {
        return this.getStudentCurricularPlan().getRegistration();
    }

    public boolean belongsToAnotherTeacher() {
        Student student = this.getStudentCurricularPlan().getRegistration().getStudent();
        Registration registration = student.getLastActiveRegistration();

        if (registration == null) {
            return false;
        }

        Tutorship lastTutorship = getLastTutorship(registration.getActiveStudentCurricularPlan());

        if (lastTutorship != null && !lastTutorship.equals(this)) {
            return true;
        }

        return false;
    }

    public List<ExecutionYear> getCoveredExecutionYears() {
        return ExecutionYear.readExecutionYears(getStartDateExecutionYear(), getEndDateExecutionYear());
    }

    private ExecutionYear getStartDateExecutionYear() {
        int year = getStartDate().get(DateTimeFieldType.year());
        int month = getStartDate().get(DateTimeFieldType.monthOfYear());
        return ExecutionYear.readByDateTime(new LocalDate(year, month, 1).toDateTimeAtCurrentTime());
    }

    private ExecutionYear getEndDateExecutionYear() {
        int year = getEndDate().get(DateTimeFieldType.year());
        int month = getEndDate().get(DateTimeFieldType.monthOfYear());
        return ExecutionYear.readByDateTime(new LocalDate(year, month, 1).toDateTimeAtCurrentTime());
    }

    public Person getPerson() {
        return getTeacher().getPerson();
    }

    public boolean getTutorshipLogEditable() {
        DegreeCurricularPlan degreeCurricularPlan = getStudent().getLastDegreeCurricularPlan();
        return !(degreeCurricularPlan == null || getTutorshipLog() == null);
    }

    public static Tutorship getActiveTutorship(StudentCurricularPlan studentCurricularPlan) {
        Collection<Tutorship> tutorships = studentCurricularPlan.getTutorshipsSet();
        Tutorship lastTutorship = getLastTutorship(studentCurricularPlan);
        if (!tutorships.isEmpty() && lastTutorship.isActive()) {
            return lastTutorship;
        }
        return null;
    }

    private static Tutorship getLastTutorship(StudentCurricularPlan studentCurricularPlan) {
        if (!studentCurricularPlan.getTutorshipsSet().isEmpty()) {
            return Collections.max(studentCurricularPlan.getTutorshipsSet(), Tutorship.TUTORSHIP_START_DATE_COMPARATOR);
        }
        return null;
    }

    public static List<Tutorship> getTutorships(Student student) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Registration registration : student.getActiveRegistrations()) {
            for (StudentCurricularPlan curricularPlan : registration.getStudentCurricularPlansSet()) {
                tutorships.addAll(curricularPlan.getTutorshipsSet());
            }
        }
        return tutorships;
    }

    public static List<Tutorship> getActiveTutorships(Student student) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : Tutorship.getTutorships(student)) {
            if (tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public static List<Tutorship> getActiveTutorships(Teacher teacher, AcademicInterval semester) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : teacher.getTutorshipsSet()) {
            if (tutorship.isActive(semester)) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public static List<Tutorship> getActiveTutorships(Teacher teacher) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : teacher.getTutorshipsSet()) {
            if (tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public static List<Tutorship> getPastTutorships(Teacher teacher) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : teacher.getTutorshipsSet()) {
            if (!tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public static List<Teacher> getPossibleTutorsFromExecutionDegreeDepartments(ExecutionDegree executionDegree) {
        Collection<Department> departments = executionDegree.getDegree().getDepartmentsSet();

        ArrayList<Teacher> possibleTeachers = new ArrayList<Teacher>();
        for (Department department : departments) {
            for (Teacher teacher : department.getAllTeachers()) {
                if (teacher.getDepartment() != null && teacher.getDepartment().equals(department)) {
                    possibleTeachers.add(teacher);
                }
            }
        }

        return possibleTeachers;
    }

}
