/*
 * Created on Mar 24, 2005
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.EnumSet;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * @author velouria
 * 
 */
public class ScheduleExemption extends ScheduleExemption_Base {

    public ScheduleExemption(String acronym, Boolean mandatoryClocking, YearMonthDay beginValidDate, YearMonthDay endValidDate,
            TimeOfDay dayTime, Duration dayTimeDuration, TimeOfDay clockingTime,
            Duration clockingTimeDuration, WorkPeriod normalWorkPeriod, Meal meal,
            DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
        setAcronym(acronym);
        setMandatoryClocking(mandatoryClocking);
        setWorkTime(dayTime);
        setWorkTimeDuration(dayTimeDuration);
        setClockingTime(clockingTime);
        setClockingTimeDuration(clockingTimeDuration);
        setNormalWorkPeriod(normalWorkPeriod);
        setMeal(meal);
        setBeginValidDate(beginValidDate);
        setEndValidDate(endValidDate);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    // Exemption Schedule is now allowed to have overtime
    public Duration countOvertimeWorkDone(Clocking clockingIn, Clocking clockingOut) {
        return Duration.ZERO;
    }

    // Returns the schedule Attributes
    public Attributes getAttributes() {
        EnumSet<AttributeType> attributes = EnumSet.of(AttributeType.NORMAL_WORK_PERIOD_1,
                AttributeType.NORMAL_WORK_PERIOD_2, AttributeType.MEAL);
        return new Attributes(attributes);
    }

}
