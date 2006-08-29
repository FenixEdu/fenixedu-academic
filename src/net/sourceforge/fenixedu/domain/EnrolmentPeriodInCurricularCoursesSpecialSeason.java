package net.sourceforge.fenixedu.domain;

import java.util.Date;

import org.joda.time.DateTime;

public class EnrolmentPeriodInCurricularCoursesSpecialSeason extends EnrolmentPeriodInCurricularCoursesSpecialSeason_Base {
    
    public EnrolmentPeriodInCurricularCoursesSpecialSeason() {
        super();
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason(final DegreeCurricularPlan degreeCurricularPlan, 
            final ExecutionPeriod executionPeriod, final Date startDate, final Date endDate) {
    	super();
        setOjbConcreteClass(EnrolmentPeriodInCurricularCoursesSpecialSeason.class.getName());
        setDegreeCurricularPlan(degreeCurricularPlan);
        setExecutionPeriod(executionPeriod);
        setStartDate(startDate);
        setEndDate(endDate);
    }
    
    public boolean containsDate(DateTime date) {
		return !(getStartDateDateTime().isAfter(date) || getEndDateDateTime().isBefore(date));
	}

}
