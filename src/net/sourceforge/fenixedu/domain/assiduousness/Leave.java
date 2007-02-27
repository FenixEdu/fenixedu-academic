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

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.Partial;
import org.joda.time.Seconds;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class Leave extends Leave_Base {

    public static final Comparator<Leave> COMPARATORY_BY_DATE = new BeanComparator("date");

    public Leave(Assiduousness assiduousness, DateTime date, Duration dateDuration,
            JustificationMotive justificationMotive, WorkWeek aplicableWeekDays, String notes,
            DateTime lastModificationDate, Employee modifiedBy, Integer oracleSequence) {
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
        setOjbConcreteClass(Leave.class.getName());
    }

    public Leave(Assiduousness assiduousness, DateTime date, Duration dateDuration,
            JustificationMotive justificationMotive, WorkWeek aplicableWeekDays, String notes,
            DateTime lastModificationDate, Employee modifiedBy) {
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
        setOjbConcreteClass(Leave.class.getName());
    }

    public void modify(DateTime date, Duration dateDuration, JustificationMotive justificationMotive,
            WorkWeek aplicableWeekDays, String notes, Employee modifiedBy) {
        setDate(date);
        setJustificationMotive(justificationMotive);
        setAplicableWeekDays(aplicableWeekDays);
        setNotes(notes);
        setDuration(dateDuration);
        setLastModifiedDate(new DateTime());
        setModifiedBy(modifiedBy);
        setOracleSequence(0);
        setOjbConcreteClass(Leave.class.getName());
    }

    public DateTime getEndDate() {
        Days days = Days.standardDaysIn(getDuration().toPeriod());
        Minutes minutes = Minutes.standardMinutesIn(getDuration().toPeriod());
        Seconds seconds = Seconds.standardSecondsIn(getDuration().toPeriod());
        DateTime dateTime = getDate().plusDays(days.getDays()).plusMinutes(
                minutes.getMinutes() - days.getDays() * 1440).plusSeconds(
                seconds.getSeconds() - minutes.getMinutes() * 60);
        return dateTime;//getDate().plus(getDuration());//dateTime;
    }

    public TimeOfDay getEndTimeOfDay() {
        if (getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)
                || getJustificationMotive().getJustificationType().equals(
                        JustificationType.MULTIPLE_MONTH_BALANCE)) {
            return null;
        }
        return getEndDate().toTimeOfDay();
    }

    public YearMonthDay getEndYearMonthDay() {
        if (getJustificationMotive().getJustificationType().equals(JustificationType.BALANCE)) {
            return null;
        }
        return getEndDate().toYearMonthDay();
    }

    public Partial getPartialEndDate() {
        Partial p = new Partial();
        YearMonthDay y = getEndYearMonthDay();
        if (y != null) {
            for (int i = 0; i < y.getFields().length; i++) {
                p = p.with(y.getFieldType(i), y.getValue(i));
            }
        }
        TimeOfDay t = getEndTimeOfDay();
        if (t != null) {
            for (int i = 0; i < t.getFields().length; i++) {
                p = p.with(t.getFieldType(i), t.getValue(i));
            }
        }
        return p;
    }

    public Interval getTotalInterval() {
        return new Interval(getDate().getMillis(), getEndDate().getMillis());
    }

    // Check if the Leave occured in a particular date
    public boolean occuredInDate(YearMonthDay date) {
        return ((getDate().toYearMonthDay().isBefore(date) || getDate().toYearMonthDay().isEqual(date)) && (getEndDate()
                .toYearMonthDay().isAfter(date) || getEndDate().toYearMonthDay().isEqual(date)));
    }

    // Converts a Leave interval to TimePoint
    public List<TimePoint> toTimePoints(AttributeType attribute) {
        List<TimePoint> timePointList = new ArrayList<TimePoint>();
        EnumSet<AttributeType> attributesToAdd = EnumSet.of(attribute, AttributeType.JUSTIFICATION);
        timePointList.add(new TimePoint(getDate().toTimeOfDay(), new Attributes(attributesToAdd)));
        timePointList.add(new TimePoint((getDate().plus(getDuration())).toTimeOfDay(), new Attributes(
                attributesToAdd)));
        return timePointList;
    }

    public static void plotListInTimeline(List<Leave> leaveList, Iterator<AttributeType> attributesIt,
            Timeline timeline) {
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
    public boolean justificationForDay(YearMonthDay day) {
        DateTime dayAtMidnight = day.toDateTimeAtMidnight();
        if (getDate().equals(getEndDate()) && dayAtMidnight.equals(getDate())) {
            return true;
        }
        Interval justificationInterval = getTotalInterval();
        if (justificationInterval.contains(dayAtMidnight)) {
            return true;
        }
        return false;
    }

}
