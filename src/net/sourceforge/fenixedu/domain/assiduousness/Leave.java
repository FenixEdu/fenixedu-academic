package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.joda.time.Interval;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

public class Leave extends Leave_Base {
    
    public Leave() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public DateTime getEndDate() {
    		return getStartDate().plus(getDuration());
    	
    }
    public Interval getTotalInterval() {
    		return new Interval(getStartDate(), getDuration());
    }
    
    // Check if the Leave occured in a particular date
    public boolean occuredInDate(YearMonthDay date) {
    		return ((getStartDate().toYearMonthDay().isAfter(date) || getStartDate().toYearMonthDay().isEqual(date))
    			&& (getEndDate().toYearMonthDay().isBefore(date) || getEndDate().toYearMonthDay().isEqual(date))); 
    }
    
    // Converts a Leave interval to TimePoint
    public List<TimePoint> toTimePoints(AttributeType attribute) {
        List<TimePoint> timePointList = new ArrayList<TimePoint>();
        timePointList.add(new TimePoint(getStartDate().toTimeOfDay(), attribute));
        timePointList.add(new TimePoint((getStartDate().plus(getDuration())).toTimeOfDay(), attribute));
        return timePointList;
    }
    
   	public static void plotListInTimeline(List<Leave> leaveList, Iterator<AttributeType> attributesIt, Timeline timeline) {
   		List<TimePoint> pointList = new ArrayList<TimePoint>();
   		for (Leave leave: leaveList) {
   			pointList.addAll(leave.toTimePoints(attributesIt.next()));
   		}
   		timeline.plotList(pointList);
    }
    	
}
