package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.apache.commons.beanutils.BeanComparator;
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
        setRootDomainObject(RootDomainObject.getInstance());
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
        if (hasTutorshipLog()) {
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

        Tutorship lastTutorship = registration.getActiveStudentCurricularPlan().getLastTutorship();

        if (lastTutorship != null && !lastTutorship.equals(this)) {
            return true;
        }

        return false;
    }

    public static List<Tutorship> getStudentActiveTutorships(Integer studentNumber) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship t : RootDomainObject.getInstance().getTutorships()) {
            if (t.getStudent().getNumber().equals(studentNumber) && t.isActive()) {
                tutorships.add(t);
            }
        }
        return tutorships;
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

}
