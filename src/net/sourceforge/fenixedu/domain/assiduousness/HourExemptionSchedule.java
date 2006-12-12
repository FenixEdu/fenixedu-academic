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

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * @author velouria@velouria.org
 * 
 */

public class HourExemptionSchedule extends HourExemptionSchedule_Base {

    public HourExemptionSchedule(String acronym, Boolean mandatoryClocking, YearMonthDay beginValidDate, YearMonthDay endValidDate,
            TimeOfDay dayTime, Duration dayTimeDuration, TimeOfDay clockingTime,
            Duration clockingTimeDuration, WorkPeriod normalWorkPeriod, WorkPeriod fixedWorkPeriod,
            Meal meal, DateTime lastModifiedDate, Employee modifiedBy) {
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
        setFixedWorkPeriod(fixedWorkPeriod);
        setMeal(meal);
        setBeginValidDate(beginValidDate);
        setEndValidDate(endValidDate);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    // Returns the schedule Attributes
    public Attributes getAttributes() {
        EnumSet<AttributeType> attributeSet = EnumSet.of(AttributeType.NORMAL_WORK_PERIOD_1,
                AttributeType.NORMAL_WORK_PERIOD_2, AttributeType.FIXED_PERIOD_1,
                AttributeType.FIXED_PERIOD_2);
        Attributes attributes = new Attributes(attributeSet);
        // if (((Meal)getMeal()).definedMealBreak()) {
        // attributes.addAttribute(AttributeType.MEAL);
        // }
        return attributes;
    }
}