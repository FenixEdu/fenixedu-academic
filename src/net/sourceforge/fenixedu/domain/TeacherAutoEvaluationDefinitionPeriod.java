package net.sourceforge.fenixedu.domain;

import org.joda.time.YearMonthDay;

public class TeacherAutoEvaluationDefinitionPeriod extends TeacherAutoEvaluationDefinitionPeriod_Base {   

    public TeacherAutoEvaluationDefinitionPeriod(Department department, ExecutionYear executionYear, YearMonthDay startDate, YearMonthDay endDate) {
	super();
	super.init(department, executionYear, startDate, endDate);
    }    
}
