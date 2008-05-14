package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;

public class TSDVirtualTeacher extends TSDVirtualTeacher_Base {

    private TSDVirtualTeacher() {
    	super();
    }

    public TSDVirtualTeacher(Category category, String name, Integer requiredHours) {
		this();
	
		if (category == null || name == null || requiredHours == null) {
			throw new DomainException("Parameters.required");
		}
			
		this.setCategory(category);
		this.setName(name);
		this.setRequiredHours(requiredHours);
		this.setExtraCreditsName("");
		this.setExtraCreditsValue(0d);
		this.setUsingExtraCredits(false);
    }

    public String getName() {
    	return super.getName();
    }

    public Double getRealHoursByShiftTypeAndExecutionCourses(ShiftType shiftType, List<ExecutionCourse> executionCourseList) {
    	Double theoreticalHoursLastYear = 0.0;
    	return theoreticalHoursLastYear;
    }
  
    public Integer getRequiredHours(final List<ExecutionSemester> executionPeriodList) {
    	return super.getRequiredHours();
    }

    public void delete() {
		super.delete();
    }

    public Double getServiceExemptionCredits(List<ExecutionSemester> executionPeriodList) {
    	Double serviceExemptionCredits = 0d;
		return serviceExemptionCredits;
    }

    public Double getManagementFunctionsCredits(List<ExecutionSemester> executionPeriodList) {
    	Double managementFunctionsCredits = 0d;
		return managementFunctionsCredits;
    }

    public Double getRequiredTeachingHours(List<ExecutionSemester> executionPeriodList) {
    	return getRequiredHours(executionPeriodList) - (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0.0);
    }

    public Integer getTeacherNumber() {
		return null;
    }

    public List<TeacherServiceExemption> getServiceExemptions(List<ExecutionSemester> executionPeriodList) {
    	List<TeacherServiceExemption> teacherServiceExemptionList = new ArrayList<TeacherServiceExemption>();
		return teacherServiceExemptionList;
    }

    public List<PersonFunction> getManagementFunctions(List<ExecutionSemester> executionPeriodList) {
    	List<PersonFunction> personFunctionList = new ArrayList<PersonFunction>();
		return personFunctionList;
    }

    public String getEmailUserId() {
	    return getAcronym();
	}

	public String getShortName() {
		return this.getName();
	}
	
	public String getDistinctName() {
		return getShortName();
	}
    
}
