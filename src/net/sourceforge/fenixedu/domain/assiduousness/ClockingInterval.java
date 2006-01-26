package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

import org.joda.time.Interval;


public class ClockingInterval {

    private Clocking clockIn;
    private Clocking clockOut;
    
    
    public ClockingInterval(Clocking clockIn, Clocking clockOut) {
        super();
        this.setClockIn(clockIn);
        this.setClockOut(clockOut);
    }

    
    public Clocking getClockIn() {
        return clockIn;
    }


    public void setClockIn(Clocking clockIn) {
        this.clockIn = clockIn;
    }


    public Clocking getClockOut() {
        return clockOut;
    }


    public void setClockOut(Clocking clockOut) {
        this.clockOut = clockOut;
    }
    
    public Interval toInterval() {
        return (new Interval(this.getClockIn().getDate(), this.getClockOut().getDate()));
    }
    
    // Converts one clocking interval to TimePoint
    public List<TimePoint> toTimePoint(AttributeType attribute) {
        List<TimePoint> timePointList = new ArrayList<TimePoint>();
        timePointList.add(new TimePoint(this.getClockIn().getDate().toTimeOfDay(), attribute));
        timePointList.add(new TimePoint(this.getClockOut().getDate().toTimeOfDay(), attribute));
        return timePointList;
    }
    
    // Converts list of clocking intervals to a TimePoint list
    // Gives a different attribute (from WORKED_ATTRIBUTES) to each pair of points
    public static List<TimePoint> toTimePoint(List<ClockingInterval> clockingIntervalList) {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        Iterator<AttributeType> attributesIterator = DomainConstants.WORKED_ATTRIBUTES.getAttributes().iterator(); // to iterate over the WORKED attributes; gross but works...
        for (ClockingInterval clockingInterval: clockingIntervalList) {
            if (attributesIterator.hasNext()) {
                AttributeType attribute = attributesIterator.next();
                // TODO encapsulate this shit somehow
                List<TimePoint> clockingIntervalToTimePoint = clockingInterval.toTimePoint(attribute);
                pointList.addAll(clockingIntervalToTimePoint);
            }
        }
        return pointList;
    }    
    
    public String toString() {
        return new String(this.getClockIn().toString() + " - " + this.getClockOut().toString());
    }
    
}
