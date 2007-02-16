/*
 * Created on Mar 25, 2005
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * @author velouria
 * 
 */
public class ContinuousSchedule extends ContinuousSchedule_Base {

	public ContinuousSchedule(String acronym, ScheduleClockingType scheduleClockingType,
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

	// public Duration checkNormalWorkPeriodAccordingToRules(Duration
	// normalWorkPeriodWorked) {
	// return normalWorkPeriodWorked;
	// }
}
