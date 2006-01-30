package net.sourceforge.fenixedu.domain.assiduousness;

import org.joda.time.DateTime;
import org.joda.time.Interval;

//import net.sourceforge.fenixedu.domain.Employee_Base.Body;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ClockingState;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

public class Clocking extends Clocking_Base {

    public Clocking() {
        super();
    }

   
    public Clocking(Employee employee, Card card, ClockUnit clockUnit, ClockingState clockingState, DateTime date) {
        setDate(date);
        setEmployee(employee);
        setCard(card);
        setClockUnit(clockUnit);
        setState(clockingState);
    }

    
    // Creates an interval from this object date to clockingOut date;
    public Interval createClockingInterval(Clocking clockingOut) {
        return new Interval(getDate(), clockingOut.getDate());
    }
    
    // Converts a clocking to a TimePoint (beware that the year-month-day are lost in the convertion).
    public TimePoint toTimePoint(AttributeType attribute) {
        return new TimePoint(getDate().toTimeOfDay(), attribute);
    }    
        
}
