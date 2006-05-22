package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;

public class Clocking extends Clocking_Base {

    public Clocking(Assiduousness assiduousness, ClockUnit clockUnit, DateTime date) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setDate(date);
        setAssiduousness(assiduousness);
        setClockUnit(clockUnit);
        setOjbConcreteClass(Clocking.class.getName());
    }
    
    // Converts a clocking to a TimePoint (the year-month-day are lost in the convertion).
    // the seconds are 00 (in Teleponto application only the hours and the minutes of each clocking are used to calculate the work done by the employee) 
    public TimePoint toTimePoint(AttributeType attribute) {
        return new TimePoint(new TimeOfDay(getDate().getHourOfDay(), getDate().getMinuteOfHour()), attribute);
    }

}
