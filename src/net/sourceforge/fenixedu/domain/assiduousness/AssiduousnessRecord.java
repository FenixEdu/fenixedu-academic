package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

public class AssiduousnessRecord extends AssiduousnessRecord_Base {

	public AssiduousnessRecord() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    // Plots the pairs of clockings in the timeline
    // Converts pairs of clockings of the clockingList to clockingInterval and adds it to the pointList.
    public static void plotListInTimeline(List<AssiduousnessRecord> clockingList, Iterator<AttributeType> attributesIt, Timeline timeline) {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        Iterator<AssiduousnessRecord> clockingIt = clockingList.iterator();
        while (clockingIt.hasNext()) {
            AssiduousnessRecord clockIn = clockingIt.next();
            if (clockingIt.hasNext()) {
                AssiduousnessRecord clockOut = clockingIt.next();
                ClockingInterval clockingInterval = new ClockingInterval(clockIn, clockOut);
                pointList.addAll(clockingInterval.toTimePoint(attributesIt.next()));
            }
        }
        timeline.plotList(pointList);
    }

//    public static boolean isEven(int number) {
//        return number % 2 == 0;
//    }

}
