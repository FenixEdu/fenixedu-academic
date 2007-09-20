package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;

public class WeeklyWorkLoad extends WeeklyWorkLoad_Base implements Comparable<WeeklyWorkLoad> {

    public WeeklyWorkLoad(final Attends attends, final Integer weekOffset, final Integer contact, final Integer autonomousStudy,
	    final Integer other) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());

	if (attends == null || weekOffset == null) {
	    throw new NullPointerException();
	}

	setAttends(attends);
	setContact(contact);
	setAutonomousStudy(autonomousStudy);
	setOther(other);
	setWeekOffset(weekOffset);
    }

    public int getTotal() {
	final int contact = getContact() != null ? getContact() : 0;
	final int autonomousStudy = getAutonomousStudy() != null ? getAutonomousStudy() : 0;
	final int other = getOther() != null ? getOther() : 0;
	return contact + autonomousStudy + other;
    }

    public int compareTo(final WeeklyWorkLoad weeklyWorkLoad) {
	if (weeklyWorkLoad == null) {
	    throw new NullPointerException("Cannot compare weekly work load with null");
	}
	if (getAttends() != weeklyWorkLoad.getAttends()) {
	    throw new IllegalArgumentException("Cannot compare weekly work loads of different attends.");
	}

	return getWeekOffset().compareTo(weeklyWorkLoad.getWeekOffset());
    }

    public Interval getInterval() {
	final DateTime beginningOfSemester = new DateTime(getAttends().getBegginingOfLessonPeriod());
	final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
	final DateTime start = firstMonday.withFieldAdded(DurationFieldType.weeks(), getWeekOffset().intValue());
	final DateTime end = start.plusWeeks(1);
	return new Interval(start, end);
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void delete() {
	removeAttends();
	removeRootDomainObject();

	super.deleteDomainObject();

    }

}
