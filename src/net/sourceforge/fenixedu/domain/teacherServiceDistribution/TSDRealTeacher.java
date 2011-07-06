package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;

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
	this.setProfessionalCategory(teacher.getCategory());
	this.setExtraCreditsName("");
	this.setExtraCreditsValue(0d);
	this.setUsingExtraCredits(false);
    }

    @Override
    public String getName() {
	return getTeacher().getPerson().getName();
    }

    @Override
    public Department getDepartment() {
	List<ExecutionSemester> executionSemesters = getTeacherServiceDistributions().get(0).getTSDProcessPhase().getTSDProcess()
		.getExecutionPeriods();

	for (ExecutionSemester period : executionSemesters) {
	    Department department = getTeacher().getLastWorkingDepartment(period.getBeginDateYearMonthDay(),
		    period.getEndDateYearMonthDay());
	    if (department != null) {
		return department;
	    }
	}
	return null;
    }

    @Override
    public Double getRealHoursByShiftTypeAndExecutionCourses(ShiftType shiftType, List<ExecutionCourse> executionCourseList) {
	Double hoursLastYear = 0d;
	Teacher teacher = getTeacher();

	for (ExecutionCourse executionCourse : executionCourseList) {
	    hoursLastYear += teacher.getHoursLecturedOnExecutionCourseByShiftType(executionCourse, shiftType);
	}

	return hoursLastYear;
    }

    @Override
    public Integer getRequiredHours(final List<ExecutionSemester> executionPeriodList) {
	Integer requiredHours = 0;

	for (ExecutionSemester executionSemester : executionPeriodList) {
	    requiredHours += getTeacher().getMandatoryLessonHours(executionSemester);
	}

	return requiredHours;
    }

    @Override
    public void delete() {
	removeTeacher();
	super.delete();
    }

    @Override
    public Double getServiceExemptionCredits(List<ExecutionSemester> executionPeriodList) {
	Double serviceExemptionCredits = 0d;

	for (ExecutionSemester executionSemester : executionPeriodList) {
	    serviceExemptionCredits += new Double(getTeacher().getServiceExemptionCredits(executionSemester));
	}

	return serviceExemptionCredits;
    }

    @Override
    public Double getManagementFunctionsCredits(List<ExecutionSemester> executionPeriodList) {
	Double managementFunctionsCredits = 0d;

	for (ExecutionSemester executionSemester : executionPeriodList) {
	    managementFunctionsCredits += getTeacher().getManagementFunctionsCredits(executionSemester);
	}
	return managementFunctionsCredits;
    }

    @Override
    public List<PersonContractSituation> getServiceExemptions(List<ExecutionSemester> executionPeriodList) {
	List<PersonContractSituation> teacherServiceExemptionList = new ArrayList<PersonContractSituation>();

	for (ExecutionSemester executionSemester : executionPeriodList) {
	    teacherServiceExemptionList.addAll(getTeacher().getValidTeacherServiceExemptions(executionSemester));
	}

	return teacherServiceExemptionList;
    }

    @Override
    public List<PersonFunction> getManagementFunctions(List<ExecutionSemester> executionPeriodList) {
	List<PersonFunction> personFunctionList = new ArrayList<PersonFunction>();

	for (ExecutionSemester executionSemester : executionPeriodList) {
	    List<PersonFunction> teacherFunctionList = getTeacher().getManagementFunctions(executionSemester);
	    personFunctionList.addAll(teacherFunctionList);
	}
	return personFunctionList;
    }

    @Override
    public Double getTotalHoursLecturedPlusExtraCredits(List<ExecutionSemester> executionPeriodList) {
	return getTotalHoursLectured(executionPeriodList)
		+ (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0d);
    }

    @Override
    public String getEmailUserId() {
	String email = getTeacher().getPerson().getEmail();
	String results[] = email.split("@");

	if (results.length > 0) {
	    return results[0];
	} else {
	    return getAcronym();
	}
    }

    @Override
    public String getShortName() {
	return getTeacher().getPerson().getFirstAndLastName();
    }

    @Override
    public String getDistinctName() {
	return getShortName() + "(" + getTeacher().getPerson().getIstUsername() + ")";
    }

    @Override
    public String getTeacherId() {
	return getTeacher().getPerson().getIstUsername();
    }

}
