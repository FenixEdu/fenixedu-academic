package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;

public class ValuationTeacher extends ValuationTeacher_Base {

    protected ValuationTeacher() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ValuationTeacher(Teacher teacher) {
	this();

	if (teacher == null) {
	    throw new NullPointerException();
	}

	this.setTeacher(teacher);
	this.setCategory(teacher.getCategory());
	this.setExtraCreditsName("");
	this.setExtraCreditsValue(0d);
	this.setUsingExtraCredits(false);

    }

    public ValuationTeacher(Category category, String name, Integer requiredHours) {
	this();

	if (category == null || name == null || requiredHours == null) {
	    throw new NullPointerException();
	}
	this.setTeacher(null);

	this.setCategory(category);
	this.setName(name);
	this.setRequiredHours(requiredHours);
	this.setExtraCreditsName("");
	this.setExtraCreditsValue(0d);
	this.setUsingExtraCredits(false);
    }

    public Boolean isRealTeacher() {
	return getTeacher() != null;
    }

    public Boolean getIsRealTeacher() {
	return isRealTeacher();
    }

    public String getName() {
	if (isRealTeacher()) {
	    return getTeacher().getPerson().getName();
	}
	return super.getName();
    }

    public Department getDepartment() {
	if (isRealTeacher()) {

	    List<ExecutionPeriod> executionPeriods = getValuationGroupings().get(0).getValuationPhase()
		    .getTeacherServiceDistribution().getExecutionPeriods();
	    for (ExecutionPeriod period : executionPeriods) {
		Department department = getTeacher().getLastWorkingDepartment(
			period.getBeginDateYearMonthDay(), period.getEndDateYearMonthDay());
		if (department != null) {
		    return department;
		}
	    }
	    return null;
	} else {
	    return getValuationGroupings().get(0).getValuationPhase().getTeacherServiceDistribution()
		    .getDepartment();
	}
    }

    private Double getRealHoursByShiftTypeAndExecutionCourses(ShiftType shiftType,
	    List<ExecutionCourse> executionCourseList) {
	Teacher teacher = getTeacher();
	Double theoreticalHoursLastYear = 0.0;

	if (teacher != null) {
	    for (ExecutionCourse executionCourse : executionCourseList) {
		theoreticalHoursLastYear += teacher.getHoursLecturedOnExecutionCourseByShiftType(
			executionCourse, shiftType);
	    }
	}

	return theoreticalHoursLastYear;
    }

    public Double getRealTheoreticalHours(List<ExecutionCourse> executionCourseList) {
	return getRealHoursByShiftTypeAndExecutionCourses(ShiftType.TEORICA, executionCourseList);
    }

    public Double getRealPraticalHours(List<ExecutionCourse> executionCourseList) {
	return getRealHoursByShiftTypeAndExecutionCourses(ShiftType.PRATICA, executionCourseList);
    }

    public Double getRealTheoPratHours(List<ExecutionCourse> executionCourseList) {
	return getRealHoursByShiftTypeAndExecutionCourses(ShiftType.TEORICO_PRATICA, executionCourseList);
    }

    public Double getRealLaboratorialHours(List<ExecutionCourse> executionCourseList) {
	return getRealHoursByShiftTypeAndExecutionCourses(ShiftType.LABORATORIAL, executionCourseList);
    }

    private Double getTotalHoursLecturedByShiftType(ShiftType shiftType,
	    List<ExecutionPeriod> executionPeriodList) {
	Double totalHours = 0d;

	for (ExecutionPeriod executionPeriod : executionPeriodList) {
	    for (ProfessorshipValuation professorshipValuation : getActiveProfessorshipValuations()) {
		if (shiftType == ShiftType.TEORICA) {
		    totalHours += (professorshipValuation.getExecutionPeriod() == executionPeriod) ? professorshipValuation
			    .getTheoreticalHours()
			    : 0d;
		} else if (shiftType == ShiftType.PRATICA) {
		    totalHours += (professorshipValuation.getExecutionPeriod() == executionPeriod) ? professorshipValuation
			    .getPraticalHours()
			    : 0d;
		} else if (shiftType == ShiftType.TEORICO_PRATICA) {
		    totalHours += (professorshipValuation.getExecutionPeriod() == executionPeriod) ? professorshipValuation
			    .getTheoPratHours()
			    : 0d;
		} else if (shiftType == ShiftType.LABORATORIAL) {
		    totalHours += (professorshipValuation.getExecutionPeriod() == executionPeriod) ? professorshipValuation
			    .getLaboratorialHours()
			    : 0d;
		}
	    }
	}

	return totalHours;
    }

    public Double getTotalTheoreticalHoursLectured(List<ExecutionPeriod> executionPeriodList) {
	return getTotalHoursLecturedByShiftType(ShiftType.TEORICA, executionPeriodList);
    }

    public Double getTotalPraticalHoursLectured(List<ExecutionPeriod> executionPeriodList) {
	return getTotalHoursLecturedByShiftType(ShiftType.PRATICA, executionPeriodList);
    }

    public Double getTotalTheoPratHoursLectured(List<ExecutionPeriod> executionPeriodList) {
	return getTotalHoursLecturedByShiftType(ShiftType.TEORICO_PRATICA, executionPeriodList);
    }

    public Double getTotalLaboratorialHoursLectured(List<ExecutionPeriod> executionPeriodList) {
	return getTotalHoursLecturedByShiftType(ShiftType.LABORATORIAL, executionPeriodList);
    }

    public Double getTotalHoursLectured(List<ExecutionPeriod> executionPeriodList) {
	return getTotalTheoreticalHoursLectured(executionPeriodList)
		+ getTotalPraticalHoursLectured(executionPeriodList)
		+ getTotalTheoPratHoursLectured(executionPeriodList)
		+ getTotalLaboratorialHoursLectured(executionPeriodList);
    }

    public Integer getRequiredHours(final List<ExecutionPeriod> executionPeriodList) {
	Integer requiredHours = 0;

	if (isRealTeacher()) {
	    for (ExecutionPeriod executionPeriod : executionPeriodList) {
		requiredHours += getTeacher().getLessonHours(executionPeriod);
	    }
	} else {
	    requiredHours = super.getRequiredHours();
	}

	return requiredHours;
    }

    public Double getAvailability(List<ExecutionPeriod> executionPeriodList) {
	return getRequiredHours(executionPeriodList) - getTotalHoursLectured(executionPeriodList)
		- (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0.0);
    }

    public void delete() {
	for (ValuationGrouping grouping : getValuationGroupings()) {
	    removeValuationGroupings(grouping);
	}
	for (ProfessorshipValuation professorshipValuation : getProfessorshipValuations()) {
	    professorshipValuation.delete();
	}

	removeCategory();
	removeTeacher();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public String getAcronym() {
	StringBuilder sb = new StringBuilder();

	String name = getName();

	String[] nameCompounds = name.split(" ");

	for (String compound : nameCompounds) {
	    compound = compound.trim();

	    if (compound.length() > 0 && Character.isUpperCase(compound.charAt(0))) {
		sb.append(compound.charAt(0));
	    }
	}

	return sb.toString();
    }

    public Double getServiceExemptionCredits(List<ExecutionPeriod> executionPeriodList) {
	Double serviceExemptionCredits = 0d;

	if (isRealTeacher()) {
	    for (ExecutionPeriod executionPeriod : executionPeriodList) {
		serviceExemptionCredits += new Double(getTeacher().getServiceExemptionCredits(
			executionPeriod));
	    }
	}

	return serviceExemptionCredits;
    }

    public Double getManagementFunctionsCredits(List<ExecutionPeriod> executionPeriodList) {
	Double managementFunctionsCredits = 0d;

	if (isRealTeacher()) {
	    for (ExecutionPeriod executionPeriod : executionPeriodList) {
		managementFunctionsCredits += getTeacher()
			.getManagementFunctionsCredits(executionPeriod);
	    }
	}

	return managementFunctionsCredits;
    }

    public Double getRequiredTeachingHours(List<ExecutionPeriod> executionPeriodList) {
	return getRequiredHours(executionPeriodList)
		- (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0.0);
    }

    public Integer getTeacherNumber() {
	if (getIsRealTeacher()) {
	    return getTeacher().getTeacherNumber();
	} else {
	    return null;
	}
    }

    public List<TeacherServiceExemption> getServiceExemptions(List<ExecutionPeriod> executionPeriodList) {
	List<TeacherServiceExemption> teacherServiceExemptionList = new ArrayList<TeacherServiceExemption>();

	if (getIsRealTeacher()) {
	    for (ExecutionPeriod executionPeriod : executionPeriodList) {		
		teacherServiceExemptionList.addAll(getTeacher().getValidTeacherServiceExemptionsToCountInCredits(executionPeriod));
	    }
	}

	return teacherServiceExemptionList;
    }

    public List<PersonFunction> getManagementFunctions(List<ExecutionPeriod> executionPeriodList) {
	List<PersonFunction> personFunctionList = new ArrayList<PersonFunction>();

	if (getIsRealTeacher()) {
	    for (ExecutionPeriod executionPeriod : executionPeriodList) {
		List<PersonFunction> teacherFunctionList = getTeacher().getManagementFunctions(
			executionPeriod);
		personFunctionList.addAll(teacherFunctionList);
	    }
	}

	return personFunctionList;
    }

    public Double getExtraCreditsValue(List<ExecutionPeriod> executionPeriodList) {
	return getExtraCreditsValue()
		* (executionPeriodList.size() / getValuationGroupings().get(0).getValuationPhase()
			.getTeacherServiceDistribution().getExecutionPeriodsCount());
    }

    public Double getTotalHoursLecturedPlusExtraCredits(List<ExecutionPeriod> executionPeriodList) {
	return getTotalHoursLectured(executionPeriodList)
		+ (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0d);
    }

    @SuppressWarnings("unchecked")
    public List<ProfessorshipValuation> getActiveProfessorshipValuations() {
	return (List<ProfessorshipValuation>) CollectionUtils.select(getProfessorshipValuations(),
		new Predicate() {
		    public boolean evaluate(Object arg0) {
			ProfessorshipValuation professorshipValuation = (ProfessorshipValuation) arg0;
			return professorshipValuation.getIsActive();
		    }
		});
    }

    public String getEmailUserId() {
	if (getIsRealTeacher()) {
	    String email = getTeacher().getPerson().getEmail();

	    String results[] = email.split("@");

	    if (results.length > 0) {
		return results[0];
	    } else {
		return "";
	    }
	} else {
	    return getAcronym();
	}
    }

    public boolean getHavePermissionToValuate(Person person) {
	List<ValuationGrouping> associatedValuationGroupingList = getValuationGroupings();

	for (ValuationGrouping valuationGrouping : associatedValuationGroupingList) {
	    if (valuationGrouping.getHaveCoursesAndTeachersValuationPermission(person)) {
		return true;
	    }
	}

	return false;
    }
}
