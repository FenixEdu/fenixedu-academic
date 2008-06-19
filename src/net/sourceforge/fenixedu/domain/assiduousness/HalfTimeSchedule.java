package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.TimeOfDay;

/**
 * @author velouria@velouria.org
 * 
 */
public class HalfTimeSchedule extends HalfTimeSchedule_Base {

    public static final Duration normalHalfTimeWorkDayDuration = new Duration(14400000); // 4h

    public static final LocalTime splitWorkDayHour = new LocalTime(12, 0, 0);

    public HalfTimeSchedule(String acronym, ScheduleClockingType scheduleClockingType, LocalDate beginValidDate,
	    LocalDate endValidDate, TimeOfDay dayTime, Duration dayTimeDuration, TimeOfDay clockingTime,
	    Duration clockingTimeDuration, WorkPeriod normalWorkPeriod, WorkPeriod fixedWorkPeriod, DateTime lastModifiedDate,
	    Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAcronym(acronym);
	setScheduleClockingType(scheduleClockingType);
	setWorkTime(dayTime);
	setWorkTimeDuration(dayTimeDuration);
	setClockingTime(clockingTime);
	setClockingTimeDuration(clockingTimeDuration);
	setNormalWorkPeriod(normalWorkPeriod);
	setFixedWorkPeriod(fixedWorkPeriod);
	setBeginValidDate(beginValidDate);
	setEndValidDate(endValidDate);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    // TODO NAo tem trabalho extraordinario

    // Returns the schedule Attributes
    public Attributes getAttributes() {
	Attributes attributes = new Attributes(AttributeType.NORMAL_WORK_PERIOD_1);
	if (definedFixedPeriod()) {
	    attributes.addAttribute(AttributeType.FIXED_PERIOD_1);
	}
	return attributes;
    }

    public Boolean isMorningSchedule() {
	LocalDate today = new LocalDate();
	Duration beforeDuration = Duration.ZERO;
	Interval beforeInterval = getNormalWorkPeriod().getFirstPeriodInterval().toInterval(today.toDateTimeAtStartOfDay())
		.overlap(new Interval(today.toDateTimeAtStartOfDay(), today.toDateTime(splitWorkDayHour)));
	if (beforeInterval != null) {
	    beforeDuration = beforeInterval.toDuration();
	}

	Duration afterDuration = Duration.ZERO;
	Interval afterInterval = getNormalWorkPeriod().getFirstPeriodInterval().toInterval(today.toDateTimeAtStartOfDay())
		.overlap(new Interval(today.toDateTime(splitWorkDayHour), today.plusDays(1).toDateTimeAtStartOfDay()));
	if (afterInterval != null) {
	    afterDuration = afterInterval.toDuration();
	}
	return beforeDuration.isLongerThan(afterDuration);
    }
}
