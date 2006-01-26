/*
 * Created on Mar 24, 2005
 */
package net.sourceforge.fenixedu.domain.assiduousness;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

import org.joda.time.Duration;

/**
 * @author velouria
 *
 */
public class Meal extends Meal_Base {

    public Meal(TimeInterval mealBreak) {
        this.setMealBreak(mealBreak);
        this.setMinimumMealBreakInterval(DomainConstants.MINIMUM_BREAK_INTERVAL);
        this.setMandatoryMealDiscount(DomainConstants.MANDATORY_MEAL_DISCOUNT);
    }
    
    public Meal(TimeInterval mealBreak, Duration minimumMealInterval, Duration mandatoryMealDiscount) {
        setMealBreak(mealBreak);
        setMandatoryMealDiscount(mandatoryMealDiscount);
        setMinimumMealBreakInterval(minimumMealInterval);
    }
    
    public boolean definedMealBreak() {
        return (this.getMealBreak() != null);
    }
    
    public Duration getMealBreakDuration() {
        return this.getMealBreak().getDuration();
    }
    
    public AttributeType getAttributeType() {
        return AttributeType.MEAL;
    }
    

   // Returns a list with the start and end points of the Meal break (if defined)
    public List<TimePoint> toTimePoints() {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        if (this.definedMealBreak()) {
            pointList.add(this.getMealBreak().startPointToTimePoint(AttributeType.MEAL));
            pointList.add(this.getMealBreak().endPointToTimePoint(AttributeType.MEAL));
        }
        return pointList;
    }    

    // Calcula o desconto a efectuar
    // Se funcionario almocar em mais do que 1 hora nao desconta nada
    // senao devolve o que falta para que a hora de almoco seja 1 hora.
    public Duration calculateMealDiscount(Duration lunchBreak) {
        if (lunchBreak.isLongerThan(this.getMandatoryMealDiscount())) {
            return Duration.ZERO;
        } else {
            return (this.getMandatoryMealDiscount().minus(lunchBreak));
        }
    }
    
    
//    public Duration countMealBreakTime(Clocking clockingIn, Clocking clockingOut) {
//        return TimeInterval.countDurationFromClockings(clockingIn, clockingOut, this.getMealBreak());
//    }
        

}
