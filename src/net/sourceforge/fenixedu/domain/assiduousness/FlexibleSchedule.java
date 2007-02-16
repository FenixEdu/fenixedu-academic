/*
 * Created on Mar 9, 2005
 *
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.EnumSet;

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
 * @author velouria
 * 
 */
public class FlexibleSchedule extends FlexibleSchedule_Base {

	public FlexibleSchedule(String acronym, ScheduleClockingType scheduleClockingType,
			YearMonthDay beginValidDate, YearMonthDay endValidDate, TimeOfDay dayTime,
			Duration dayTimeDuration, TimeOfDay clockingTime, Duration clockingTimeDuration,
			WorkPeriod normalWorkPeriod, WorkPeriod fixedWorkPeriod, Meal meal,
			DateTime lastModifiedDate, Employee modifiedBy) {
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
		setMeal(meal);
		setBeginValidDate(beginValidDate);
		setEndValidDate(endValidDate);
		setLastModifiedDate(lastModifiedDate);
		setModifiedBy(modifiedBy);
	}

	// Returns the schedule Attributes
	public Attributes getAttributes() {
		EnumSet<AttributeType> attributes = EnumSet.of(AttributeType.NORMAL_WORK_PERIOD_1,
				AttributeType.NORMAL_WORK_PERIOD_2, AttributeType.FIXED_PERIOD_1,
				AttributeType.FIXED_PERIOD_2, AttributeType.MEAL);
		return new Attributes(attributes);
	}

}
