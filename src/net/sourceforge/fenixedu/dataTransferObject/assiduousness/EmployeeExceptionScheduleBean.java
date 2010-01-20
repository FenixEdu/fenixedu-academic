package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;

import org.joda.time.LocalDate;

public class EmployeeExceptionScheduleBean extends PageContainerBean {

    Employee employee;

    Employee modifiedBy;

    Schedule schedule;

    LocalDate beginDate;

    LocalDate endDate;

    boolean onlyChangeDates = Boolean.FALSE;

    List<EmployeeWorkWeekScheduleBean> employeeWorkWeekScheduleList = new ArrayList<EmployeeWorkWeekScheduleBean>();

    public EmployeeExceptionScheduleBean(Schedule schedule, Employee modifiedBy) {
	setModifiedBy(modifiedBy);
	setEmployee(schedule.getAssiduousness().getEmployee());
	setSchedule(schedule);
	setBeginDate(schedule.getBeginDate());
	setEndDate(schedule.getEndDate());
    }

    public EmployeeExceptionScheduleBean(Employee employee, Employee modifiedBy) {
	setModifiedBy(modifiedBy);
	setEmployee(employee);
    }

    public void setEmployee(Employee employee) {
	if (employee != null) {
	    this.employee = employee;
	} else {
	    this.employee = null;
	}
    }

    public Employee getEmployee() {
	return employee;
    }

    public List<EmployeeWorkWeekScheduleBean> getEmployeeWorkWeekScheduleList() {
	return employeeWorkWeekScheduleList;
    }

    public Employee getModifiedBy() {
	return modifiedBy;
    }

    public void setModifiedBy(Employee modifiedBy) {
	if (modifiedBy != null) {
	    this.modifiedBy = modifiedBy;
	} else {
	    this.modifiedBy = null;
	}
    }

    public LocalDate getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
	this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public Schedule getSchedule() {
	return schedule;
    }

    public void setSchedule(Schedule schedule) {
	if (schedule != null) {
	    this.schedule = schedule;
	}
    }

    public boolean getOnlyChangeDates() {
	return onlyChangeDates;
    }

    public void setOnlyChangeDates(boolean changeDates) {
	this.onlyChangeDates = changeDates;
    }
}
