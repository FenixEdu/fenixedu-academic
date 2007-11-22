package net.sourceforge.fenixedu.domain;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.PeriodState;

public class AcademicPeriod extends AcademicPeriod_Base {
    
    public AcademicPeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setState(PeriodState.NOT_OPEN);
    }
    
    public YearMonthDay getBeginDateYearMonthDay() {
	return getExecutionInterval().getBeginYearMonthDayWithoutChronology();
    }

    public YearMonthDay getEndDateYearMonthDay() {
	return getExecutionInterval().getEndYearMonthDayWithoutChronology();
    }
    
    @Override
    public void setExecutionInterval(AcademicInterval executionInterval) {
        if(executionInterval == null) {
            throw new DomainException("error.AcademicPeriod.empty.executionInterval");
        }
        super.setExecutionInterval(executionInterval);
    }
    
    @Override
    public void setState(PeriodState state) {
        if(state == null) {
            throw new DomainException("error.AcademicPeriod.empty.state");
        }
        super.setState(state);
    }  
    
    @Deprecated   
    public java.util.Date getBeginDate(){  
	YearMonthDay day = getBeginDateYearMonthDay(); 
	return (day == null) ? null : new java.util.Date(day.getYear() - 1900, day.getMonthOfYear() - 1, day.getDayOfMonth());   
    }

    @Deprecated   
    public java.util.Date getEndDate(){  
	YearMonthDay day = getEndDateYearMonthDay(); 
	return (day == null) ? null : new java.util.Date(day.getYear() - 1900, day.getMonthOfYear() - 1, day.getDayOfMonth());   
    }
}
