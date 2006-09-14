package net.sourceforge.fenixedu.domain.vigilancy;



import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.TestNGBase;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;

import net.sourceforge.fenixedu.domain.exceptions.DomainException; 
import net.sourceforge.fenixedu.util.HourMinuteSecond;

public class VigilantTest extends TestNGBase {

    @Test(alwaysRun=true)
    public void removeVigilantTestWillWork() {
        Vigilant vigilant = new Vigilant();
        vigilant.delete();
    }
    
    @Test(alwaysRun=true)
    @ExpectedExceptions({DomainException.class})
    public void removeVigilantTestThatWillFail() {
       Vigilant vigilant = new Vigilant();
       vigilant.addConvokes(new Convoke());
       vigilant.delete();
    }
      
    @Test(alwaysRun=true)
    public void vigilantAvailability() {
        Vigilant vigilant = new Vigilant();
        DateTime unavailableBegin, unavailableEnd, testBegin, testEnd;
        
        unavailableBegin = new DateTime(2006,2,1,2,0,0,0);
        unavailableEnd = new DateTime(2006,3,1,2,0,0,0);
        UnavailablePeriod unavailablePeriod = new UnavailablePeriod(unavailableBegin,unavailableEnd,"test");
        vigilant.addUnavailablePeriods(unavailablePeriod);
        unavailableBegin = new DateTime(2006,4,2,1,0,0,0);
        unavailableEnd = new DateTime(2006,4,30,1,0,0,0);
        unavailablePeriod = new UnavailablePeriod(unavailableBegin,unavailableEnd,"test2");
        vigilant.addUnavailablePeriods(unavailablePeriod);
        
        
        testBegin = new DateTime(2005,2,1,2,0,0,0);
        testEnd= new DateTime(2005,3,1,2,0,0,0);
        
        Assert.assertTrue(vigilant.isAvailableOnDate(testBegin,testEnd));
        
        testBegin = new DateTime(2006,1,30,2,0,0,0);
        testEnd = new DateTime(2006,2,2,2,0,0,0);
        
        Assert.assertFalse(vigilant.isAvailableOnDate(testBegin,testEnd));
        
        testBegin = new DateTime(2006,2,4,1,0,0,0);
        testEnd = new DateTime(2006,2,6,1,0,0,0);
        
        Assert.assertFalse(vigilant.isAvailableOnDate(testBegin,testEnd));
        
        testBegin = new DateTime(2006,2,20,1,0,0,0);
        testEnd = new DateTime(2006,3,4,1,0,0,0);
        
        Assert.assertFalse(vigilant.isAvailableOnDate(testBegin,testEnd));
        
        testBegin = new DateTime(2006,3,20,1,0,0,0);
        testEnd = new DateTime(2006,4,1,1,0,0,0);
        
        Assert.assertTrue(vigilant.isAvailableOnDate(testBegin,testEnd));
        
        testBegin = new DateTime(2006,4,5,1,0,0,0);
        testEnd = new DateTime(2006,4,10,1,0,0,0);
        
        Assert.assertFalse(vigilant.isAvailableOnDate(testBegin,testEnd));
        
    }
    
    
    @Test(alwaysRun=true) 
    public void pointsSystemCurrentVigilant() {
        Vigilant vigilant = new Vigilant();
        Assert.assertEquals(vigilant.getPoints(),new Integer(0));

        YearMonthDay currentDate = new YearMonthDay();
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(4));
        writtenEvaluation.setBeginningDateHourMinuteSecond(new HourMinuteSecond());
        writtenEvaluation.setEndDateHourMinuteSecond(new HourMinuteSecond());
        Convoke convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        Assert.assertEquals(vigilant.getPoints(),new Integer(0));
        
        convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusMonths(1));
        convoke.setAttendedToConvoke(true);
        
        vigilant.addConvokes(convoke);
        Assert.assertEquals(vigilant.getPoints(),new Integer(1));
        
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(4));
        convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusMonths(1));
        convoke.setAttendedToConvoke(false);
        
        vigilant.addConvokes(convoke);
        Assert.assertEquals(vigilant.getPoints(),new Integer(-1));

    }

    @Test(alwaysRun=true)
    public void pointSystemInGivenExecutionYear() {
        Person person = new Person();
        Vigilant vigilant = new Vigilant(person);
        ExecutionYear year = new ExecutionYear();
        ExecutionYear year2 = new ExecutionYear();
        vigilant.setExecutionYear(year);
        
        YearMonthDay currentDate = new YearMonthDay();
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(4));
        writtenEvaluation.setBeginningDateHourMinuteSecond(new HourMinuteSecond());
        writtenEvaluation.setEndDateHourMinuteSecond(new HourMinuteSecond());
        Convoke convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        
        
        convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusMonths(1));
        convoke.setAttendedToConvoke(true);
        
        vigilant.addConvokes(convoke);
        
        
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(4));
        convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusMonths(1));
        convoke.setAttendedToConvoke(false);
        
        vigilant.addConvokes(convoke);
        
        
        Assert.assertEquals(vigilant.getPointsInExecutionYear(year2),new Integer(0));   
        Assert.assertEquals(vigilant.getPointsInExecutionYear(year), new Integer(-1));
    }
    
    @Test(alwaysRun=true) 
    public void pointSystemForTotalPoints() {
        Person person = new Person();
        Vigilant vigilant = new Vigilant(person);
        ExecutionYear year = new ExecutionYear();
        ExecutionYear year2 = new ExecutionYear();
        Vigilant vigilant2 = new Vigilant(person);
        vigilant.setExecutionYear(year);
        vigilant2.setExecutionYear(year2);
        
        YearMonthDay currentDate = new YearMonthDay();
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(4));
        writtenEvaluation.setBeginningDateHourMinuteSecond(new HourMinuteSecond());
        writtenEvaluation.setEndDateHourMinuteSecond(new HourMinuteSecond());
        
        Convoke convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        
        convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusMonths(1));
        convoke.setAttendedToConvoke(true);
        
        vigilant.addConvokes(convoke);
        
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(4));
        convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusMonths(1));
        convoke.setAttendedToConvoke(true);
        vigilant2.addConvokes(convoke);
        
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(4));
        convoke = new Convoke(writtenEvaluation);
        convoke.setConfirmed(true);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusMonths(1));
        convoke.setAttendedToConvoke(false);
        
        vigilant.addConvokes(convoke);
        
        Assert.assertEquals(vigilant.getPointsInExecutionYear(year), new Integer(-1));
        Assert.assertEquals(vigilant.getPointsInExecutionYear(year2), new Integer(1));
        Assert.assertEquals(vigilant.getTotalPoints(),new Integer(0));
        
    }
 
}
