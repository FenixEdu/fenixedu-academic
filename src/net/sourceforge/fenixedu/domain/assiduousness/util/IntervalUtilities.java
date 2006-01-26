package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Interval;

public class IntervalUtilities {

    // Convertes an interval to a list with 2 time points.
    public static List<TimePoint> toTimePointList(Interval interval, AttributeType attribute) {
        List<TimePoint> timePointList = new ArrayList();
        timePointList.add(new TimePoint(interval.getStart().toTimeOfDay(), attribute));
        timePointList.add(new TimePoint(interval.getEnd().toTimeOfDay(), attribute));
        return timePointList;
    }

    
    
}
