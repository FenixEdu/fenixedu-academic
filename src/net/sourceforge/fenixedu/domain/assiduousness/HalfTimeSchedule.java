package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * @author velouria@velouria.org
 * 
 */
public class HalfTimeSchedule extends HalfTimeSchedule_Base {

    public static final Duration normalHalfTimeWorkDayDuration = new Duration(12600000); // 3:30
	public HalfTimeSchedule(String acronym, ScheduleClockingType scheduleClockingType,
			YearMonthDay beginValidDate, YearMonthDay endValidDate, TimeOfDay dayTime,
			Duration dayTimeDuration, TimeOfDay clockingTime, Duration clockingTimeDuration,
			WorkPeriod normalWorkPeriod, WorkPeriod fixedWorkPeriod, DateTime lastModifiedDate,
			Employee modifiedBy) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setOjbConcreteClass(this.getClass().getName());
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
}
