package net.sourceforge.fenixedu.domain;

import org.joda.time.YearMonthDay;

public class TeacherExpectationDefinitionPeriod extends TeacherExpectationDefinitionPeriod_Base {
        
    public TeacherExpectationDefinitionPeriod(Department department, ExecutionYear executionYear, YearMonthDay startDate, YearMonthDay endDate) {
	super();
	super.init(department, executionYear, startDate, endDate);
    }     
}
