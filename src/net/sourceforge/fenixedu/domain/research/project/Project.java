package net.sourceforge.fenixedu.domain.research.project;

import org.joda.time.Period;

public class Project extends Project_Base {
    
    public  Project() {
        super();
    }
    
    public Period getPeriod () {
        return new Period(getStartDate(), getEndDate());
    }
    
    public int getDurationInMonths () {
        Period period = getPeriod();
        return period.getYears() * 12 + period.getMonths();
    }
    
}
