package net.sourceforge.fenixedu.domain;

import org.joda.time.YearMonthDay;

public class TeacherPersonalExpectationsVisualizationPeriod extends TeacherPersonalExpectationsVisualizationPeriod_Base {
       
    public TeacherPersonalExpectationsVisualizationPeriod(Department department, ExecutionYear executionYear, YearMonthDay start, YearMonthDay end) {	
	super();
	super.init(department, executionYear, start, end);
    }
}
