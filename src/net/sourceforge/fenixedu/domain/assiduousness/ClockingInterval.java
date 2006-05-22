package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

import org.joda.time.Interval;

public class ClockingInterval {

    private AssiduousnessRecord clockIn;
    private AssiduousnessRecord clockOut;

    public ClockingInterval(AssiduousnessRecord clockIn, AssiduousnessRecord clockOut) {
        super();
        setClockIn(clockIn);
        setClockOut(clockOut);
    }

    public AssiduousnessRecord getClockIn() {
        return clockIn;
    }

    public void setClockIn(AssiduousnessRecord newClockIn) {
        clockIn = newClockIn;
    }

    public AssiduousnessRecord getClockOut() {
        return clockOut;
    }

    public void setClockOut(AssiduousnessRecord newClockOut) {
        clockOut = newClockOut;
    }

    public Interval toInterval() {
        return (new Interval(getClockIn().getDate(), getClockOut().getDate()));
    }

    // Converts one clocking interval to TimePoint
    public List<TimePoint> toTimePoint(AttributeType attribute) {
        List<TimePoint> timePointList = new ArrayList<TimePoint>();
        timePointList.add(new TimePoint(getClockIn().getDate().toTimeOfDay(), attribute));
        timePointList.add(new TimePoint(getClockOut().getDate().toTimeOfDay(), attribute));
        return timePointList;
    }

    // Converts list of clocking intervals to a TimePoint list
    // Gives a different attribute (from WORKED_ATTRIBUTES) to each pair of points
    public static List<TimePoint> toTimePoint(List<ClockingInterval> clockingIntervalList) {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        Iterator<AttributeType> attributesIterator = DomainConstants.WORKED_ATTRIBUTES.getAttributes()
                .iterator(); // to iterate over the WORKED attributes; gross but works...
        for (ClockingInterval clockingInterval : clockingIntervalList) {
            if (attributesIterator.hasNext()) {
                AttributeType attribute = attributesIterator.next();
                // TODO encapsulate this somehow
                List<TimePoint> clockingIntervalToTimePoint = clockingInterval.toTimePoint(attribute);
                pointList.addAll(clockingIntervalToTimePoint);
            }
        }
        return pointList;
    }

    public String toString() {
        return new String(getClockIn().toString() + " - " + getClockOut().toString());
    }

}
