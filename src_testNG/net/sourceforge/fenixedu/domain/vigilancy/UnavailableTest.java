package net.sourceforge.fenixedu.domain.vigilancy;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.testng.Assert;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.TestNGBase;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

public class UnavailableTest extends TestNGBase {

    @Test(alwaysRun = true)
    @ExpectedExceptions( { DomainException.class })
    public void tryToAddUnavailableWithConvoke() {
        Vigilant vigilant = new Vigilant();
        VigilantGroup group = new VigilantGroup();
        
        group.addVigilants(vigilant);
        DateTime period = new DateTime();
        group.setBeginOfFirstPeriodForUnavailablePeriods(period);
        group.setEndOfFirstPeriodForUnavailablePeriods(period.plusDays(1));
        
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        writtenEvaluation.setDayDateYearMonthDay(new YearMonthDay(2006, 2, 2));
        writtenEvaluation.setBeginningDateHourMinuteSecond(new HourMinuteSecond(13, 3, 0));
        writtenEvaluation.setEndDateHourMinuteSecond(new HourMinuteSecond(15, 3, 0));
        Convoke convoke = new Convoke();
        convoke.setWrittenEvaluation(writtenEvaluation);
        vigilant.addConvokes(convoke);

        DateTime begin = new DateTime(2006, 3, 1, 1, 0, 0, 0);
        DateTime end = new DateTime(2006, 3, 2, 1, 0, 0, 0);
        UnavailablePeriod unavailablePeriod1 = new UnavailablePeriod(begin, end, "");
        try {
            unavailablePeriod1.setVigilant(vigilant);
        } catch (DomainException e) {
            Assert.fail(e.getMessage());
        }

        begin = new DateTime(2006, 2, 1, 1, 0, 0, 0);
        end = new DateTime(2006, 3, 1, 1, 0, 0, 0);
        unavailablePeriod1 = new UnavailablePeriod(begin, end, "");

        unavailablePeriod1.setVigilant(vigilant);
    }
    
    @Test(alwaysRun=true) 
    public void verifyInterval() {
        DateTime begin = new DateTime(2006,3,1,1,0,0,0);
        DateTime end = new DateTime(2006,5,1,1,0,0,0);
        UnavailablePeriod unavailablePeriod = new UnavailablePeriod(begin,end,"");
        DateTime testBegin = new DateTime(2006,1,1,1,0,0,0);
        DateTime testEnd = new DateTime(2006,1,3,1,0,0,0);
        
        Assert.assertFalse(unavailablePeriod.containsInterval(testBegin,testEnd));
        
        testBegin = new DateTime(2006,2,1,1,0,0,0);
        testEnd = new DateTime(2006,3,2,1,0,0,0);
        
        Assert.assertTrue(unavailablePeriod.containsInterval(testBegin,testEnd));
        
        testBegin = new DateTime(2006,3,2,1,0,0,0);
        testEnd = new DateTime(2006,4,2,1,0,0,0);
        
        Assert.assertTrue(unavailablePeriod.containsInterval(testBegin,testEnd));
        
        testBegin = new DateTime(2006,4,20,1,0,0,0);
        testEnd = new DateTime(2006,5,3,1,0,0,0);
        
        Assert.assertTrue(unavailablePeriod.containsInterval(testBegin,testEnd));
        
        testBegin = new DateTime(2006,6,1,1,0,0,0);
        testEnd = new DateTime(2006,7,3,1,0,0,0);
        
        Assert.assertFalse(unavailablePeriod.containsInterval(testBegin,testEnd));
    
        Assert.assertFalse(unavailablePeriod.containsDate(new DateTime(2005,6,1,1,0,0,0)));
        Assert.assertTrue(unavailablePeriod.containsDate(new DateTime(2006,4,3,1,0,0,0)));
        Assert.assertFalse(unavailablePeriod.containsDate(new DateTime(2006,6,1,1,0,0,0)));
    }

}
