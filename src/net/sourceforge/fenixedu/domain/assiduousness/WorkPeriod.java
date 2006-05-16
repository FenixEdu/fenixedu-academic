package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

import org.joda.time.Duration;
import org.joda.time.TimeOfDay;

public class WorkPeriod extends WorkPeriod_Base {

    public WorkPeriod(TimeOfDay firstPeriod, Duration firstPeriodDuration, TimeOfDay secondPeriod,
            Duration secondPeriodDuration) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setFirstPeriod(firstPeriod);
        setFirstPeriodDuration(firstPeriodDuration);
        setSecondPeriod(secondPeriod);
        setSecondPeriodDuration(secondPeriodDuration);
    }

    public TimeInterval getFirstPeriodInterval() {
        return new TimeInterval(getFirstPeriod(), getFirstPeriodDuration());
    }

    public TimeInterval getSecondPeriodInterval() {
        if (getSecondPeriod() == null || getSecondPeriodDuration() == null) {
            return null;
        }
        return new TimeInterval(getSecondPeriod(), getSecondPeriodDuration());
    }

    // Returns a list with the start and end points of both Normal Work Periods if defined
    public List<TimePoint> toTimePoints(AttributeType type1, AttributeType type2) {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        if (getFirstPeriodInterval() != null) {
            pointList.add(getFirstPeriodInterval().startPointToTimePoint(type1));
            pointList.add(getFirstPeriodInterval().endPointToTimePoint(type1));
        }
        if (getSecondPeriodInterval() != null) {
            pointList.add(getSecondPeriodInterval().startPointToTimePoint(type2));
            pointList.add(getSecondPeriodInterval().endPointToTimePoint(type2));
        }
        return pointList;
    }

    public Duration getWorkPeriodDuration() {
        if (getSecondPeriodInterval() != null) {
            return (getFirstPeriodInterval().getDuration())
                    .plus(getSecondPeriodInterval().getDuration());
        } else {
            return getFirstPeriodInterval().getDuration();
        }
    }

    public TimeOfDay getEndFirstPeriod() {
        return getFirstPeriod().plus(getFirstPeriodDuration().toPeriod());
    }

    public TimeOfDay getEndSecondPeriod() {
        if (getSecondPeriod() == null || getSecondPeriodDuration() == null) {
            return null;
        }
        return getSecondPeriod().plus(getSecondPeriodDuration().toPeriod());
    }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        return !(hasAnyFixedWorkScheduleTypes() || hasAnyNormalWorkScheduleTypes());
    }
    
    public boolean isSecondWorkPeriodDefined() {
        if (getSecondPeriod() != null) {
            return true;
        } else {
            return false;
        }
    }
    
}
        