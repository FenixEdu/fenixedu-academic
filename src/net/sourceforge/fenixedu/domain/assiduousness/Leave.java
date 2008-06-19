package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Partial;

public class Leave extends Leave_Base {

    public static final Comparator<Leave> COMPARATORY_BY_DATE = new BeanComparator("date");

    public Leave(Assiduousness assiduousness, DateTime date, Duration dateDuration, JustificationMotive justificationMotive,
	    WorkWeek aplicableWeekDays, String notes, DateTime lastModificationDate, Employee modifiedBy, Integer oracleSequence) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDate(date);
	setJustificationMotive(justificationMotive);
	setAplicableWeekDays(aplicableWeekDays);
	setAssiduousness(assiduousness);
	setNotes(notes);
	setDuration(dateDuration);
	setLastModifiedDate(lastModificationDate);
	setModifiedBy(modifiedBy);
	setOracleSequence(oracleSequence);
    }

    public Leave(Assiduousness assiduousness, DateTime date, Duration dateDuration, JustificationMotive justificationMotive,
	    WorkWeek aplicableWeekDays, String notes, DateTime lastModificationDate, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDate(date);
	setJustificationMotive(justificationMotive);
	setAplicableWeekDays(aplicableWeekDays);
	setAssiduousness(assiduousness);
	setNotes(notes);
	setDuration(dateDuration);
	setLastModifiedDate(lastModificationDate);
	setModifiedBy(modifiedBy);
	setOracleSequence(0);
    }

    public void modify(DateTime date, Duration dateDuration, JustificationMotive justificationMotive, WorkWeek aplicableWeekDays,
	    String notes, Employee modifiedBy) {
	setDate(date);
	setJustificationMotive(justificationMotive);
	setAplicableWeekDays(aplicableWeekDays);
	setNotes(notes);
	setDuration(dateDuration);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
	setOracleSequence(0);
    }

    public DateTime getEndDate() {
	return getDate().plus(getDuration());
    }

    public LocalTime getEndLocalTime() {
	if (getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)
		|| getJustificationMotive().getJustificationType().equals(JustificationType.MULTIPLE_MONTH_BALANCE)) {
	    return null;
	}
	return getEndDate().toLocalTime();
    }

    public LocalDate getEndLocalDate() {
	if (getJustificationMotive().getJustificationType().equals(JustificationType.BALANCE)) {
	    return null;
	}
	return getEndDate().toLocalDate();
    }

    public Partial getPartialEndDate() {
	Partial p = new Partial();
	LocalDate y = getEndLocalDate();
	if (y != null) {
	    for (int i = 0; i < y.getFields().length; i++) {
		p = p.with(y.getFieldType(i), y.getValue(i));
	    }
	}
	LocalTime t = getEndLocalTime();
	if (t != null) {
	    for (int i = 0; i < t.getFields().length; i++) {
		p = p.with(t.getFieldType(i), t.getValue(i));
	    }
	}
	return p;
    }

    public Interval getTotalInterval() {
	return new Interval(getDate().getMillis(), getEndDate().getMillis() + 1);
    }

    // Check if the Leave occured in a particular date
    public boolean occuredInDate(LocalDate date) {
	return ((getDate().toLocalDate().isBefore(date) || getDate().toLocalDate().isEqual(date)) && (getEndDate().toLocalDate()
		.isAfter(date) || getEndDate().toLocalDate().isEqual(date)));
    }

    // Converts a Leave interval to TimePoint
    public List<TimePoint> toTimePoints(AttributeType attribute) {
	List<TimePoint> timePointList = new ArrayList<TimePoint>();
	EnumSet<AttributeType> attributesToAdd = EnumSet.of(attribute, AttributeType.JUSTIFICATION);
	timePointList.add(new TimePoint(getDate().toTimeOfDay(), new Attributes(attributesToAdd)));
	timePointList.add(new TimePoint((getDate().plus(getDuration())).toTimeOfDay(), new Attributes(attributesToAdd)));
	return timePointList;
    }

    public static void plotListInTimeline(List<Leave> leaveList, Iterator<AttributeType> attributesIt, Timeline timeline) {
	List<TimePoint> pointList = new ArrayList<TimePoint>();
	for (Leave leave : leaveList) {
	    // if (leave.getJustificationMotive().getJustificationType() ==
	    // JustificationType.BALANCE) {
	    // pointList.addAll(leave.toTimePoints(AttributeType.BALANCE));
	    // } else {
	    AttributeType at = (AttributeType) attributesIt.next();
	    pointList.addAll(leave.toTimePoints(at));
	}
	timeline.plotList(pointList);
    }

    // Returns true if the justification is for the day
    public boolean justificationForDay(LocalDate day) {
	DateTime dayAtMidnight = day.toDateTimeAtStartOfDay();
	if (getDate().equals(getEndDate()) && dayAtMidnight.equals(getDate())) {
	    return true;
	}
	Interval justificationInterval = getTotalInterval();
	if (justificationInterval.contains(dayAtMidnight)) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean isLeave() {
	return true;
    }

    public int getUtilDaysBetween(Interval interval) {
	int days = 0;
	for (LocalDate thisDay = interval.getStart().toLocalDate(); !thisDay.isAfter(interval.getEnd().toLocalDate()); thisDay = thisDay
		.plusDays(1)) {
	    WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(thisDay.toDateTimeAtStartOfDay());
	    if ((!dayOfWeek.equals(WeekDay.SATURDAY)) && (!dayOfWeek.equals(WeekDay.SUNDAY))
		    && (!getAssiduousness().isHoliday(thisDay))) {
		days++;
	    }
	}
	return days;
    }

    public int getWorkDaysBetween(Interval interval) {
	int days = 0;
	for (LocalDate thisDay = interval.getStart().toLocalDate(); !thisDay.isAfter(interval.getEnd().toLocalDate()); thisDay = thisDay
		.plusDays(1)) {
	    WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(thisDay.toDateTimeAtStartOfDay());
	    if ((!dayOfWeek.equals(WeekDay.SATURDAY)) && (!dayOfWeek.equals(WeekDay.SUNDAY))
		    && (!getAssiduousness().isHoliday(thisDay)) && occuredInDate(thisDay)) {
		days++;
	    }
	}
	return days;
    }

    public int getWorkDaysBetween(int year) {
	LocalDate begin = new LocalDate(year, 1, 1);
	LocalDate end = new LocalDate(year, 12, 31);
	Interval yearInterval = new Interval(begin.toDateTimeAtMidnight(), end.toDateTimeAtMidnight());
	Interval overlap = getTotalInterval().overlap(yearInterval);
	if (overlap != null) {
	    return getWorkDaysBetween(overlap);
	}
	return 0;
    }

}
