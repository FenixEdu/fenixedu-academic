package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

//import net.sourceforge.fenixedu.domain.Employee_Base.Body;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ClockingState;
import net.sourceforge.fenixedu.domain.assiduousness.util.IntervalUtilities;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

public class Clocking extends Clocking_Base {

//    protected Clocking(Body body) {
//        super(body);
//    }
    
    public Clocking() {
        super();
    }

   
    public Clocking(Employee employee, Card card, ClockUnit clockUnit, ClockingState clockingState, DateTime date) {
        this.setDate(date);
        this.setEmployee(employee);
        this.setCard(card);
        this.setClockUnit(clockUnit);
        this.setState(clockingState);
    }

    
    // Creates an interval from this object date to clockingOut date;
    public Interval createClockingInterval(Clocking clockingOut) {
        return new Interval(this.getDate(), clockingOut.getDate());
    }
    
    // Converts a clocking to a TimePoint (beware that the year-month-day are lost in the convertion).
    public TimePoint toTimePoint(AttributeType attribute) {
        return new TimePoint(this.getDate().toTimeOfDay(), attribute);
    }    
        
}
