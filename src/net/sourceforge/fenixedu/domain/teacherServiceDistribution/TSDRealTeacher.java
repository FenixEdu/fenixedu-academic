package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;

public class TSDRealTeacher extends TSDRealTeacher_Base {

    private TSDRealTeacher() {
    	super();
    }

    public TSDRealTeacher(Teacher teacher) {
		this();
	
		if (teacher == null) {
			throw new DomainException("Teacher.required");
		}
	
		this.setTeacher(teacher);
		this.setCategory(teacher.getCategory());
		this.setExtraCreditsName("");
		this.setExtraCreditsValue(0d);
		this.setUsingExtraCredits(false);
    }
    
    public String getName() {
    	return getTeacher().getPerson().getName();
	}
    
    public Department getDepartment() {
	    List<ExecutionPeriod> executionPeriods = getTeacherServiceDistributions().get(0).getTSDProcessPhase()
		    .getTSDProcess().getExecutionPeriods();
	    
	    for (ExecutionPeriod period : executionPeriods) {
	    	Department department = getTeacher().getLastWorkingDepartment(
			period.getBeginDateYearMonthDay(), period.getEndDateYearMonthDay());
			if (department != null) {
			    return department;
			}
	    }
	    return null;
    }

    public Double getRealHoursByShiftTypeAndExecutionCourses(ShiftType shiftType,
	    List<ExecutionCourse> executionCourseList) {
		Double hoursLastYear = 0d;
		Teacher teacher = getTeacher();

	    for (ExecutionCourse executionCourse : executionCourseList) {
	    	hoursLastYear += teacher.getHoursLecturedOnExecutionCourseByShiftType(
			executionCourse, shiftType);
	    }
	
	    return hoursLastYear;
    }

    
    public Integer getRequiredHours(final List<ExecutionPeriod> executionPeriodList) {
    	Integer requiredHours = 0;

	    for (ExecutionPeriod executionPeriod : executionPeriodList) {
	    	requiredHours += getTeacher().getLessonHours(executionPeriod);
	    }
	
	    return requiredHours;
    }

    public void delete() {
    	removeTeacher();
    	super.delete();
    }

    public Double getServiceExemptionCredits(List<ExecutionPeriod> executionPeriodList) {
		Double serviceExemptionCredits = 0d;
	
	    for (ExecutionPeriod executionPeriod : executionPeriodList) {
			serviceExemptionCredits += new Double(getTeacher().getServiceExemptionCredits(
				executionPeriod));
		}
	
		return serviceExemptionCredits;
    }

    public Double getManagementFunctionsCredits(List<ExecutionPeriod> executionPeriodList) {
    	Double managementFunctionsCredits = 0d;
	
	    for (ExecutionPeriod executionPeriod : executionPeriodList) {
	    	managementFunctionsCredits += getTeacher()
				.getManagementFunctionsCredits(executionPeriod);
	    }
		return managementFunctionsCredits;
    }

    public Integer getTeacherNumber() {
	    return getTeacher().getTeacherNumber();
	}

    public List<TeacherServiceExemption> getServiceExemptions(List<ExecutionPeriod> executionPeriodList) {
    	List<TeacherServiceExemption> teacherServiceExemptionList = new ArrayList<TeacherServiceExemption>();

	    for (ExecutionPeriod executionPeriod : executionPeriodList) {		
	    	teacherServiceExemptionList.addAll(getTeacher().
	    			getValidTeacherServiceExemptionsToCountInCredits(executionPeriod));
		}
	
		return teacherServiceExemptionList;
    }

    public List<PersonFunction> getManagementFunctions(List<ExecutionPeriod> executionPeriodList) {
    	List<PersonFunction> personFunctionList = new ArrayList<PersonFunction>();

	    for (ExecutionPeriod executionPeriod : executionPeriodList) {
	    	List<PersonFunction> teacherFunctionList = getTeacher().getManagementFunctions(executionPeriod);
	    	personFunctionList.addAll(teacherFunctionList);
	    }
		return personFunctionList;
    }

    public Double getTotalHoursLecturedPlusExtraCredits(List<ExecutionPeriod> executionPeriodList) {
    	return getTotalHoursLectured(executionPeriodList)
			+ (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0d);
    }

    public String getEmailUserId() {
	    String email = getTeacher().getPerson().getEmail();
	    String results[] = email.split("@");

	    if (results.length > 0) {
	    	return results[0];
	    } else {
	    	return getAcronym();
	    }
    }

	public String getShortName() {
		return getTeacher().getPerson().getFirstAndLastName();
	}
	
	public String getDistinctName() {
		return getShortName() + "(" + getTeacher().getTeacherNumber() + ")";
	}

}
