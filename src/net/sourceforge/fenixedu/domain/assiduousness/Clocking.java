package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class Clocking extends Clocking_Base {

    public Clocking(Assiduousness assiduousness, ClockUnit clockUnit, DateTime date) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setDate(date);
        setAssiduousness(assiduousness);
        setClockUnit(clockUnit);
        setOjbConcreteClass(Clocking.class.getName());
    }

    // Plots the pairs of clockings in the timeline
    // Converts pairs of clockings of the clockingList to clockingInterval and
    // adds it to the pointList.
    public static void plotListInTimeline(List<Clocking> clockingList,
            Iterator<AttributeType> attributesIt, Timeline timeline) {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        Iterator<Clocking> clockingIt = clockingList.iterator();
        while (clockingIt.hasNext()) {
            Clocking clockIn = clockingIt.next();
            if (clockingIt.hasNext()) {
                Clocking clockOut = clockingIt.next();
                ClockingInterval clockingInterval = new ClockingInterval(clockIn, clockOut);
                pointList.addAll(clockingInterval.toTimePoint(attributesIt.next()));
            }
        }
        timeline.plotList(pointList);
    }

    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    // TO DELETE
    // Creates an interval from this object date to clockingOut date;
    public Interval createClockingInterval(Clocking clockingOut) {
        return new Interval(getDate(), clockingOut.getDate());
    }

    // Converts a clocking to a TimePoint (beware that the year-month-day are lost in the convertion).
    public TimePoint toTimePoint(AttributeType attribute) {
        return new TimePoint(getDate().toTimeOfDay(), attribute);
    }

}
