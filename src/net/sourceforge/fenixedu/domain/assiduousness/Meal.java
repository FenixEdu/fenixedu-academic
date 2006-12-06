/*
 * Created on Mar 24, 2005
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

import org.joda.time.Duration;
import org.joda.time.TimeOfDay;

/**
 * @author velouria
 * 
 */
public class Meal extends Meal_Base {

    public Meal(TimeOfDay mealBeginTime, TimeOfDay mealEndTime, Duration mandatoryMealDiscount,
            Duration minimumMealBreakInterval) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setBeginMealBreak(mealBeginTime);
        setEndMealBreak(mealEndTime);
        setMinimumMealBreakInterval(minimumMealBreakInterval);
        setMandatoryMealDiscount(mandatoryMealDiscount);
    }

    public TimeInterval getMealBreak() {
        return new TimeInterval(getBeginMealBreak(), getEndMealBreak(), false);
    }

    public boolean definedMealBreak() {
        return (getMealBreak() != null);
    }

    public Duration getMealBreakDuration() {
        return getMealBreak().getDuration();
    }

    public AttributeType getAttributeType() {
        return AttributeType.MEAL;
    }

    // Returns a list with the start and end points of the Meal break (if defined)
    public List<TimePoint> toTimePoints() {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        if (definedMealBreak()) {
            pointList.add(getMealBreak().startPointToTimePoint(AttributeType.MEAL));
            pointList.add(getMealBreak().endPointToTimePoint(AttributeType.MEAL));
        }
        return pointList;
    }

    public Duration calculateMealDiscount(Duration lunchBreak) {
        if (lunchBreak.isShorterThan(getMandatoryMealDiscount())
                || lunchBreak.isEqual(getMandatoryMealDiscount())) {
            return getMandatoryMealDiscount().minus(lunchBreak);
        } else {
            return Duration.ZERO;
        }
    }

    // Returns the lunch end if the employee had lunch in the beginning of its meal break
    public TimeOfDay getLunchEnd() {
        return getBeginMealBreak().plus(getMandatoryMealDiscount().toPeriod());
    }

    public Duration countMealBreakTime(Clocking clockingIn, Clocking clockingOut) {
        return TimeInterval.countDurationFromClockings(clockingIn, clockingOut, this.getMealBreak());
    }

    public TimeOfDay getEndOfMealBreakMinusMealDiscount() {
        return getMealBreak().getEndTime().toDateTimeToday().minus(getMandatoryMealDiscount())
                .toTimeOfDay();
    }

    public TimeInterval getEndOfMealBreakMinusDiscountInterval() {
        return new TimeInterval(getEndOfMealBreakMinusMealDiscount(), getEndMealBreak(), false);
    }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        return !hasAnyWorkScheduleTypes();
    }

    public boolean equivalent(TimeOfDay beginMeal, TimeOfDay endMeal, Duration minium, Duration maxium) {
        if ((getBeginMealBreak().equals(beginMeal) && getEndMealBreak().equals(endMeal))
                && ((getMinimumMealBreakInterval() == null && minium == null) || (getMinimumMealBreakInterval() != null
                        && minium != null && getMinimumMealBreakInterval().equals(minium)))
                && ((getMandatoryMealDiscount() == null && maxium == null) || (getMandatoryMealDiscount() != null
                        && maxium != null && getMandatoryMealDiscount().equals(maxium)))) {
            return true;
        }
        return false;
    }
}
