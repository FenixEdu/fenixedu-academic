package net.sourceforge.fenixedu.domain.research;

import org.joda.time.Period;
import org.joda.time.PeriodType;

public class Project extends Project_Base {
    
    public  Project() {
        super();
    }
    
    public Period getPeriod(){
        return new Period(this.getStartDate(), this.getEndDate(), PeriodType.yearMonthDay());
    }
    
}
