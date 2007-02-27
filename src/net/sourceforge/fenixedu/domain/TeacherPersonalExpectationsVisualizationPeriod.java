package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class TeacherPersonalExpectationsVisualizationPeriod extends TeacherPersonalExpectationsVisualizationPeriod_Base {
    
    private TeacherPersonalExpectationsVisualizationPeriod() {
        super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public TeacherPersonalExpectationsVisualizationPeriod(Department department, ExecutionYear executionYear, 
	    YearMonthDay start, YearMonthDay end) {
	
	this();	
	if(department != null && executionYear != null &&
		department.getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(executionYear) != null) {
	    throw new DomainException("error.TeacherPersonalExpectationsVisualizationPeriod.already.exists");
	}	
	setDepartment(department);	
	setExecutionYear(executionYear);
	setTimeInterval(start, end);
    }

    public void edit(YearMonthDay startDate, YearMonthDay endDate) {
	setTimeInterval(startDate, endDate);
    }
    
    public void setTimeInterval(YearMonthDay start, YearMonthDay end) {	 
	if (start == null) {
	    throw new DomainException("error.TeacherExpectationDefinitionPeriod.empty.startDateYearMonthDay");
	}
	if (end == null) {
	    throw new DomainException("error.TeacherExpectationDefinitionPeriod.empty.endDateYearMonthDay");
	}
	if (!end.isAfter(start)) {
	    throw new DomainException("error.begin.after.end");
	}
	super.setStartDateYearMonthDay(start);
	super.setEndDateYearMonthDay(end);
    }
    
    public Boolean isPeriodOpen() {	
	YearMonthDay now = new YearMonthDay();
        return !getStartDateYearMonthDay().isAfter(now) && !getEndDateYearMonthDay().isBefore(now) ? Boolean.TRUE : Boolean.FALSE;    
    }

    @Override
    public void setStartDateYearMonthDay(YearMonthDay startDateYearMonthDay) {
	if(startDateYearMonthDay == null) {
	    throw new DomainException("error.TeacherPersonalExpectationsVisualizationPeriod.empty.startDateYearMonthDay");
	}
	setTimeInterval(startDateYearMonthDay, getEndDateYearMonthDay());
    }   
    
    @Override
    public void setEndDateYearMonthDay(YearMonthDay endDateYearMonthDay) {
	if(endDateYearMonthDay == null) {
	    throw new DomainException("error.TeacherPersonalExpectationsVisualizationPeriod.empty.endDate");
	}
	setTimeInterval(getStartDateYearMonthDay(), endDateYearMonthDay);
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	if(executionYear == null) {
	    throw new DomainException("error.TeacherPersonalExpectationsVisualizationPeriod.empty.executionYear");
	}
	super.setExecutionYear(executionYear);
    }

    @Override
    public void setDepartment(Department department) {
	if(department == null) {
	    throw new DomainException("error.TeacherPersonalExpectationsVisualizationPeriod.empty.department");
	}
	super.setDepartment(department);
    }   
}
