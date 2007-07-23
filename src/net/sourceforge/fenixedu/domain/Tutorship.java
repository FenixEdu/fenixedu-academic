package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class Tutorship extends Tutorship_Base {

	public static final Comparator<Tutorship> TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER = new BeanComparator(
			"studentCurricularPlan.registration.number");

	public static final Comparator<Tutorship> TUTORSHIP_COMPARATOR_BY_ENTRY_YEAR = new BeanComparator(
			"studentCurricularPlan.registration.startDate");

	public static Comparator<Tutorship> TUTORSHIP_END_DATE_COMPARATOR = new Comparator<Tutorship>() {
		public int compare(Tutorship t1, Tutorship t2) {
			if (t1.getEndDate() == null) {
				return -1;
			}
			else if(t2.getEndDate() == null) {
				return 1;
			}
			else {
				return (t1.getEndDate().isBefore(t2.getEndDate()) ? -1 : 
					(t1.getEndDate().isAfter(t2.getEndDate()) ? 1 : 
						(t1.getIdInternal() <= t2.getIdInternal()) ? -1 : 1));
			}
		}
	};

	public static Comparator<Tutorship> TUTORSHIP_START_DATE_COMPARATOR = new Comparator<Tutorship>() {
		public int compare(Tutorship t1, Tutorship t2) {
			if (t1.getStartDate() == null) {
				return -1;
			}
			else if(t2.getStartDate() == null) {
				return 1;
			}
			else {
				return (t1.getStartDate().isBefore(t2.getStartDate()) ? -1 : 
					(t1.getStartDate().isAfter(t2.getStartDate()) ? 1 : 
						(t1.getIdInternal() <= t2.getIdInternal()) ? -1 : 1));
			}
		}
	};

	// Tutorship maximum period, in years
	public static final int TUTORSHIP_MAX_PERIOD = 2;

	public Tutorship(Teacher teacher, Partial tutorshipStartDate,
			Partial tutorshipEndDate) {
		super();

		setRootDomainObject(RootDomainObject.getInstance());
		setTeacher(teacher);
		setStartDate(tutorshipStartDate);
		setEndDate(tutorshipEndDate);
	}

	public void delete() {
		removeStudentCurricularPlan();
		removeTeacher();
		removeRootDomainObject();
		super.deleteDomainObject();
	}

	public boolean hasEndDate() {
		if (getEndDate() != null)
			return true;
		return false;
	}

	public boolean isActive() {
		if (!this.hasEndDate())
			return false;

		YearMonthDay currentYearMonthDay = new YearMonthDay();
		Partial currentDate = new Partial(new DateTimeFieldType[] {
				DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() },
				new int[] { currentYearMonthDay.year().get(),
						currentYearMonthDay.monthOfYear().get() });

		if (getEndDate().isAfter(currentDate)) {
			return true;
		}

		return false;
	}

	public static int getLastPossibleTutorshipYear() {
		YearMonthDay currentDate = new YearMonthDay();
		return currentDate.getYear() + TUTORSHIP_MAX_PERIOD;
	}

	public Registration getStudent() {
		return this.getStudentCurricularPlan().getRegistration();
	}
	
	public boolean belongsToAnotherTeacher() {
		Student student = this.getStudentCurricularPlan().getRegistration().getStudent();
		Registration registration = student.getLastActiveRegistration();
		
		if(registration == null)
			return false;
		
		Tutorship lastTutorship = registration.getActiveStudentCurricularPlan().getLastTutorship();
		
		if(lastTutorship != null && !lastTutorship.equals(this))
			return true;
		
		return false;
	}
}
