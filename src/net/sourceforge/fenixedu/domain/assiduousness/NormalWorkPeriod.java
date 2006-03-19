/*
 * Created on Mar 24, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

import org.joda.time.Duration;

/**
 * @author velouria
 *
 * Horario Normal
 */

// TODO test all methods
public class NormalWorkPeriod extends NormalWorkPeriod_Base {

    public NormalWorkPeriod(TimeInterval normalWorkPeriod1, TimeInterval normalWorkPeriod2){
    	this(normalWorkPeriod1);
        setNormalWorkPeriod2(normalWorkPeriod2);
    }
    
    public NormalWorkPeriod(TimeInterval normalWorkPeriod1){
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
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
       if (definedNormalWorkPeriod2()) {
            return getNormalWorkPeriod2().getDuration();
        } else {
            return Duration.ZERO;
        }
    }
    
    public Duration getTotalNormalWorkPeriodDuration() {
        if (getNormalWorkPeriod2() != null) {
            return getNormalWorkPeriod1Duration().plus(getNormalWorkPeriod2Duration());
        } else {
            return getNormalWorkPeriod1Duration();
        }   
    }   
       
    // Returns a list with the start and end points of both Normal Work Periods if defined
    public List<TimePoint> toTimePoints() {
        List<TimePoint> pointList = new ArrayList();
        if (definedNormalWorkPeriod1()) {
            pointList.add(getNormalWorkPeriod1().startPointToTimePoint(getNormalWorkPeriod1Attribute()));
            pointList.add(getNormalWorkPeriod1().endPointToTimePoint(getNormalWorkPeriod1Attribute()));
        }
        if (definedNormalWorkPeriod2()) {
            pointList.add(getNormalWorkPeriod2().startPointToTimePoint(getNormalWorkPeriod2Attribute()));
            pointList.add(getNormalWorkPeriod2().endPointToTimePoint(getNormalWorkPeriod2Attribute()));
        }
        return pointList;
    }
    
    public AttributeType getNormalWorkPeriod1Attribute() {
        return AttributeType.NORMAL_WORK_PERIOD_1;
    }

    public AttributeType getNormalWorkPeriod2Attribute() {
        return AttributeType.NORMAL_WORK_PERIOD_2;
    }
    
    public String getName() {
        return getNormalWorkPeriod1().getStartTime().toString()  + "/" + getNormalWorkPeriod2().getEndTime().toString();
    }

    
}
