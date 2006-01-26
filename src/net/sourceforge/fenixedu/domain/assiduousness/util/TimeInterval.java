package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class TimeInterval implements Serializable {
   
    // TODO mudar para immutable
    private TimeOfDay startTime;
    private TimeOfDay endTime;
    private Boolean nextDay; // some schedules may end in the next day
   
    
    public TimeInterval(TimeOfDay startTime, TimeOfDay endTime, Boolean nextDay) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
//        this.setNextDay(nextDay);
        // never trust the data from the user :D
        if (startTime.isAfter(endTime)) {
            this.setNextDay(true);
        } else {
            this.setNextDay(nextDay);
        }
    }    

    public TimeInterval(TimeOfDay startTime, TimeOfDay endTime) {
        if (startTime.isAfter(endTime)) {
            this.setNextDay(true);
        } else {
            this.setNextDay(false);
        }
        this.setStartTime(startTime);
        this.setEndTime(endTime);
    }    
   
    
    private void setNextDay(boolean nextDay) {
        this.nextDay = nextDay;
    }
    
    private void setStartTime(TimeOfDay startTime) {
        this.startTime = startTime;
    }
    
    private void setEndTime(TimeOfDay endTime) {
        this.endTime = endTime;
    }

    public Boolean getNextDay() {
        return nextDay;
    }

    public void setNextDay(Boolean nextDay) {
        this.nextDay = nextDay;
    }

    public TimeOfDay getEndTime() {
        return endTime;
    }

    public TimeOfDay getStartTime() {
        return startTime;
    }

    // Returns the duration of the time interval
    public Duration getDuration() {
        DateTime startDate = this.getStartTime().toDateTimeToday();
        DateTime endDate = this.getEndTime().toDateTimeToday();
        if (this.getNextDay()) {
            endDate.plusDays(1);
        }
        return new Duration(startDate, endDate);
    }
    
    public long getDurationMillis() {
        return getDuration().getMillis();
    }
    
    public boolean contains(TimeOfDay timeOfDay, boolean nextDay) {
        if (nextDay == false) {
            if ((this.getStartTime().isBefore(timeOfDay) || this.getStartTime().isEqual(timeOfDay)) && (this.getEndTime().isAfter(timeOfDay) || 
                    this.getEndTime().isEqual(timeOfDay))) {
                return true;
            } else {
                return false;
            }
        } else { // nextDay == true
            if (this.getNextDay() == false) {
                return false;
            } else { // nextDay e this.nextDay
                if (this.getEndTime().isAfter(timeOfDay) || this.getEndTime().isEqual(timeOfDay)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
     


    
//    // TimeInterval e' antes de timeOfDay? Se timeinterval  nao esta definido no dia seguinte e timeofday e' no dia seguinte entao natürlich devolve true
//    public boolean timeIntervalIsBefore(TimeOfDay timeOfDay, boolean nextDay) {
//        if (nextDay && (this.getNextDay() == false)) {
//            return true;
//        } else {
//            return this.getEndTime().isBefore(timeOfDay);
//        }
//    }
//    
//    //
//    public boolean timeIntervalIsAfter(TimeOfDay timeOfDay, boolean nextDay) {
//        if (nextDay && (this.getNextDay() == false)) {
//            return false;
//        } else {
//            return this.getStartTime().isAfter(timeOfDay);
//        }
//    }
//    // Returns true if the TimeInterval contains the date.
//    public Boolean contains(DateTime date) {
//        // convertion to compare
//        TimeOfDay dateTimeOfDay = this.dateTimeToTimeOfDay(date);
//  
//        // Next day e inicio e' depois do fim (pq fim e' no dia seguinte)
//        if (this.getNextDay()) {
//            // se inicio e' antes da data e data e' antes ou igual meia-noite
//            // ou data e' depois ou igual 'a meia-noite e fim e' depois da data
//            if ((this.startIsBeforeOrEqual(dateTimeOfDay) && (dateTimeOfDay.isBefore(TimeOfDay.MIDNIGHT) || (dateTimeOfDay.isEqual(TimeOfDay.MIDNIGHT)))) ||
//                    ((dateTimeOfDay.isAfter(TimeOfDay.MIDNIGHT) || (dateTimeOfDay.isEqual(TimeOfDay.MIDNIGHT))) && this.endIsAfterOrEqual(dateTimeOfDay))) {
//                return true;
//            }
//        } else {
//            // inicio do periodo e' igual ou antes da data
//            if (this.startIsBeforeOrEqual(dateTimeOfDay) && this.endIsAfterOrEqual(dateTimeOfDay)) {
//                return true;
//            }
//        }
//        return false;
//    }   
//    
//    public Boolean endIsAfterOrEqual(TimeOfDay timeOfDay) {
//        if (this.getEndTime().isAfter(timeOfDay) || this.getEndTime().isEqual(timeOfDay)) {
//            return true;
//        } else {
//            return false;
//        }
//    }

//    public Boolean endIsBeforeOrEqual(TimeOfDay timeOfDay) {
//        if (this.getEndTime().isBefore(timeOfDay) || this.getEndTime().isEqual(timeOfDay)) {
//            return true;
//        } else {
//            return false;
//        }
//    }

//    public Boolean startIsAfterOrEqual(TimeOfDay timeOfDay) {
//        if (this.getStartTime().isAfter(timeOfDay) || this.getStartTime().isEqual(timeOfDay)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public Boolean startIsBeforeOrEqual(TimeOfDay timeOfDay) {
//        if (this.getStartTime().isBefore(timeOfDay) || this.getStartTime().isEqual(timeOfDay)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    
    //    

    
    public TimeOfDay dateTimeToTimeOfDay(DateTime date) {
        return date.toTimeOfDay();
//        return new TimeOfDay(date.getHourOfDay(), date.getMinuteOfDay(), date.getSecondOfDay());
    }
    
    
    
//    // Return true if 
//    public Boolean startIsBefore(DateTime date) {
//        TimeOfDay timeOfDay = new TimeOfDay(date.getHourOfDay(), date.getMinuteOfDay(), date.getSecondOfDay());
//        if (this.getStartTime().isBefore(timeOfDay)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public Boolean startIsAfter(DateTime date) {
//        TimeOfDay timeOfDay = new TimeOfDay(date.getHourOfDay(), date.getMinuteOfDay(), date.getSecondOfDay());
//        if (this.getStartTime().isAfter(timeOfDay)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    
    
    // Converts a TimeInterval to Interval
    // completes the YearMonthDay with the date from variable date.
    public Interval toInterval(DateTime date) {
        YearMonthDay datePartial = new YearMonthDay(date);
        if (this.getNextDay()) {
            datePartial.plusDays(1); // adds a day if the interval ends the next day
        }
        return (new Interval(datePartial.toDateTime(this.getStartTime()), datePartial.toDateTime(this.getEndTime())));
    }
    
//    // TODO change to more suitable class
//    // Overlaps the clocking interval with the working (fixed period/lunch/whatever) interval and calculates the amount of time the employee worked.
//    public static Duration countDurationFromClockings(Clocking clockIn, Clocking clockOut, TimeInterval timeInterval) {
//        
//        Interval intervalFromTimeInterval = timeInterval.toInterval(clockIn.getDate());
//        Interval clockingInterval = new Interval(clockIn.getDate(), clockOut.getDate());
//        Interval overlappedInterval = intervalFromTimeInterval.overlap(clockingInterval);
//        if (overlappedInterval == null) {
//            return new Duration(0);
//        } else {
//            return overlappedInterval.toDuration();
//        }
//    }
    
    // Converts one of the limits of the interval to a TimePoint
    public TimePoint intervalLimitToTimePoint(TimeOfDay intervalLimit, AttributeType attribute) {
        return new TimePoint(intervalLimit, attribute);
    }

    public TimePoint startPointToTimePoint(AttributeType attribute) {
        return this.intervalLimitToTimePoint(this.getStartTime(), attribute);
    }
    
    public TimePoint endPointToTimePoint(AttributeType attribute) {
        return this.intervalLimitToTimePoint(this.getEndTime(), attribute);
    }
    
    public String toString() {
        return new String(this.getStartTime() + "-" + getEndTime() + " today: " + getNextDay());
    }
    
    public boolean equals(TimeInterval timeInterval) {
        return (this.getStartTime().equals(timeInterval.getStartTime()) && (this.getEndTime().equals(timeInterval.getEndTime()))
                && (this.getNextDay().equals(timeInterval.getNextDay())));
    }

    
    public boolean isTimeIntervalBeforeTime(TimeOfDay timeOfDay, boolean nextDay) {
        if ((nextDay && this.getNextDay()) || (nextDay == false && this.getNextDay() == false)) {
            return (this.getEndTime().isBefore(timeOfDay) || this.getEndTime().isEqual(timeOfDay));
        } else if (nextDay && this.getNextDay() == false) {
            return true;
        } else if (nextDay == false && this.getNextDay()) {
            return false;
        }
        return false;
    }
    
    
    
    // TODO reminder to turn to immutable
//  /**
//  * @param regularSchedule1 The regularSchedule1 to set.
//  */
// public NormalWorkPeriod setNormalWorkPeriod1(Interval normalWorkPeriod1) {
//     if (normalWorkPeriod1.equals(this.normalWorkPeriod1)) {
//         return this;
//     } else {
//         return new NormalWorkPeriod(normalWorkPeriod1, this.getNormalWorkPeriod2());
//     }
// }
 
 

// public NormalWorkPeriod setNormalWorkPeriod2(Interval normalWorkPeriod2) {
//  if (normalWorkPeriod2.equals(this.normalWorkPeriod2)) {
//         return this;
//     } else {
//         return new NormalWorkPeriod(this.normalWorkPeriod2, getNormalWorkPeriod2());
////         NormalWorkPeriod clonedNormalWorkPeriod = this.clone();
////         clonedNormalWorkPeriod.regularSetNormalWorkPeriod2(normalWorkPeriod2);
////         return new NormalWorkPeriod(normalWorkPeriod2);
//     }
// }

}
