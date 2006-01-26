/*
 * Created on Mar 24, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;

/**
 * @author velouria
 *
 * Horario Normal
 */

// TODO test all methods
public class NormalWorkPeriod extends NormalWorkPeriod_Base {

    public NormalWorkPeriod(TimeInterval normalWorkPeriod1, TimeInterval normalWorkPeriod2){
        setNormalWorkPeriod1(normalWorkPeriod1);
        setNormalWorkPeriod2(normalWorkPeriod2);
    }
    
    public NormalWorkPeriod(TimeInterval normalWorkPeriod1){
        setNormalWorkPeriod1(normalWorkPeriod1);
    }

    public boolean definedNormalWorkPeriod2() {
        return (getNormalWorkPeriod2() != null);
    }
    
    public boolean definedNormalWorkPeriod1() {
        return (getNormalWorkPeriod1() != null);
    }
  
    public Duration getNormalWorkPeriod1Duration() {
        return getNormalWorkPeriod1().getDuration();
    }
    
    public Duration getNormalWorkPeriod2Duration() {
       if (this.definedNormalWorkPeriod2()) {
            return getNormalWorkPeriod2().getDuration();
        } else {
            return Duration.ZERO;
        }
    }
    
    public Duration getTotalNormalWorkPeriodDuration() {
        if (getNormalWorkPeriod2() != null) {
            return this.getNormalWorkPeriod1Duration().plus(getNormalWorkPeriod2Duration());
        } else {
            return this.getNormalWorkPeriod1Duration();
        }   
    }   
       
    // Returns a list with the start and end points of both Normal Work Periods if defined
    public List<TimePoint> toTimePoints() {
        List<TimePoint> pointList = new ArrayList();
        if (this.definedNormalWorkPeriod1()) {
            pointList.add(this.getNormalWorkPeriod1().startPointToTimePoint(this.getNormalWorkPeriod1Attribute()));
            pointList.add(this.getNormalWorkPeriod1().endPointToTimePoint(this.getNormalWorkPeriod1Attribute()));
        }
        if (this.definedNormalWorkPeriod2()) {
            pointList.add(this.getNormalWorkPeriod2().startPointToTimePoint(this.getNormalWorkPeriod2Attribute()));
            pointList.add(this.getNormalWorkPeriod2().endPointToTimePoint(this.getNormalWorkPeriod2Attribute()));
        }
        return pointList;
    }
    
    public AttributeType getNormalWorkPeriod1Attribute() {
        return AttributeType.NWP1;
    }

    public AttributeType getNormalWorkPeriod2Attribute() {
        return AttributeType.NWP2;
    }
    
    public String getName() {
        return this.getNormalWorkPeriod1().getStartTime().toString()  + "/" + this.getNormalWorkPeriod2().getEndTime().toString();
    }

    
}
