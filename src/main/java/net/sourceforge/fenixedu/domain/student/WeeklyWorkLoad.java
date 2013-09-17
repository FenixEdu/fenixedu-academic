package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;

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

    @Override
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

    public void delete() {
        check(this, RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE);
        setAttends(null);
        setRootDomainObject(null);

        super.deleteDomainObject();

    }

    @Deprecated
    public boolean hasWeekOffset() {
        return getWeekOffset() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasContact() {
        return getContact() != null;
    }

    @Deprecated
    public boolean hasOther() {
        return getOther() != null;
    }

    @Deprecated
    public boolean hasAttends() {
        return getAttends() != null;
    }

    @Deprecated
    public boolean hasAutonomousStudy() {
        return getAutonomousStudy() != null;
    }

}
