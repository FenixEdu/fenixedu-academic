package net.sourceforge.fenixedu.domain.vigilancy;

import org.joda.time.YearMonthDay;
import org.testng.Assert;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;

import net.sourceforge.fenixedu.domain.TestNGBase;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

public class ConvokeTest extends TestNGBase {

    @Test(alwaysRun=true) 
    @ExpectedExceptions({DomainException.class})
    public void setActiveLogicTestingModifiedAnOldActive() {
       Convoke convoke = new Convoke();
       WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
       
       convoke.setWrittenEvaluation(writtenEvaluation);
       writtenEvaluation.setDayDateYearMonthDay(new YearMonthDay(2006,4,1));
       
       convoke.setActive(false);
       Assert.assertTrue(convoke.getActive());
    }
    
    @Test(alwaysRun=true) 
    @ExpectedExceptions({DomainException.class})
    public void setActiveLogicTestingModificationAnActive24HoursBefore() {
        Convoke convoke = new Convoke();
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        
        YearMonthDay currentDate = new YearMonthDay();
        YearMonthDay date = currentDate.plusDays(1);
        convoke.setWrittenEvaluation(writtenEvaluation);
        writtenEvaluation.setDayDateYearMonthDay(date);
        
        convoke.setActive(false);
        Assert.assertTrue(convoke.getActive());
    }
    
    @Test(alwaysRun=true)
    public void setActiveLogicTestingCorrectModification() {
        Convoke convoke = new Convoke();
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        
        YearMonthDay currentDate = new YearMonthDay();
        YearMonthDay date = currentDate.plusDays(3);
        convoke.setWrittenEvaluation(writtenEvaluation);
        writtenEvaluation.setDayDateYearMonthDay(date);
        
        convoke.setActive(false);
        Assert.assertFalse(convoke.getActive());
     }
    
    @Test(alwaysRun=true) 
    public void setAttendedLogicTestingCorrectUse() {
        Convoke convoke = new Convoke();
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        YearMonthDay currentDate = new YearMonthDay();
        
        convoke.setWrittenEvaluation(writtenEvaluation);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusDays(2));
        
        convoke.setAttendedToConvoke(true);
        Assert.assertTrue(convoke.getAttendedToConvoke());
    }
    
    @Test(alwaysRun=true)
    @ExpectedExceptions({DomainException.class})
    public void setAttendedLogicTestingIncorrectUSe() {
        Convoke convoke = new Convoke();
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        YearMonthDay currentDate = new YearMonthDay();
        
        convoke.setWrittenEvaluation(writtenEvaluation);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(2));
        
        convoke.setAttendedToConvoke(true);
        Assert.assertFalse(convoke.getAttendedToConvoke());
    }
    
    @Test(alwaysRun=true)
    public void pointsSystem() {
        
        
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        YearMonthDay currentDate = new YearMonthDay();
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusDays(4));
        writtenEvaluation.setBeginningDateHourMinuteSecond(new HourMinuteSecond());
        writtenEvaluation.setEndDateHourMinuteSecond(new HourMinuteSecond());
        Convoke convoke = new Convoke(writtenEvaluation);
        
        Assert.assertEquals(convoke.getPoints(),0);
        convoke.setActive(false);
        
        Assert.assertEquals(convoke.getPoints(),0);
        
        convoke.setActive(true);
        convoke.setConfirmed(true);
        writtenEvaluation.setDayDateYearMonthDay(currentDate.minusDays(1));
        convoke.setAttendedToConvoke(false);
        
        Assert.assertEquals(convoke.getPoints(),-2);
        
        convoke.setAttendedToConvoke(true);
        
        Assert.assertEquals(convoke.getPoints(),1);

    }

    @Test(alwaysRun=true) 
    @ExpectedExceptions({DomainException.class})
    public void pointSystemBehaviourWhenNotAttachedToWrittenEvaluation() {
        Convoke convoke = new Convoke();
        
        convoke.getPoints();
    }

}
