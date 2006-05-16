package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;

import org.joda.time.Duration;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class Leave extends Leave_Base {

    public Leave(Assiduousness assiduousness, DateTime date, Duration dateDuration,
            JustificationMotive justificationMotive, String notes, DateTime lastModificationDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setDate(date);
        setJustificationMotive(justificationMotive);
        setAssiduousness(assiduousness);
        setNotes(notes);
        setOjbConcreteClass(Leave.class.getName());
        setDuration(dateDuration);
        setLastModifiedDate(lastModificationDate);
        setModifiedBy(modifiedBy);
    }

    public DateTime getEndDate() {
        return getDate().plus(getDuration());

    }

    public Interval getTotalInterval() {
        return new Interval(getDate(), getDuration());
    }

    // Check if the Leave occured in a particular date
    public boolean occuredInDate(YearMonthDay date) {
        return ((getDate().toYearMonthDay().isAfter(date) || getDate().toYearMonthDay().isEqual(date)) && (getEndDate()
                .toYearMonthDay().isBefore(date) || getEndDate().toYearMonthDay().isEqual(date)));
    }

    // Converts a Leave interval to TimePoint
    public List<TimePoint> toTimePoints(AttributeType attribute) {
        List<TimePoint> timePointList = new ArrayList<TimePoint>();
        timePointList.add(new TimePoint(getDate().toTimeOfDay(), attribute));
        timePointList.add(new TimePoint((getDate().plus(getDuration())).toTimeOfDay(), attribute));
        return timePointList;
    }
    
   	public static void plotListInTimeline(List<Leave> leaveList, Iterator<AttributeType> attributesIt, Timeline timeline) {
   		List<TimePoint> pointList = new ArrayList<TimePoint>();
   		for (Leave leave: leaveList) {
            if (leave.getJustificationMotive().getJustificationType() == JustificationType.BALANCE) {
                pointList.addAll(leave.toTimePoints(AttributeType.BALANCE));
            } else {
                pointList.addAll(leave.toTimePoints(attributesIt.next()));
            }
   		}
   		timeline.plotList(pointList);
    }

}
