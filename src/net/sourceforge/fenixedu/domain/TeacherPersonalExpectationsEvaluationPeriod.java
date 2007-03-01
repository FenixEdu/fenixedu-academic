package net.sourceforge.fenixedu.domain;

import org.joda.time.YearMonthDay;

public class TeacherPersonalExpectationsEvaluationPeriod extends TeacherPersonalExpectationsEvaluationPeriod_Base {
    
    public TeacherPersonalExpectationsEvaluationPeriod(Department department, ExecutionYear executionYear, YearMonthDay start, YearMonthDay end) {	
	super();
	super.init(department, executionYear, start, end);
    }    
}
