/*
 * Department.java
 * 
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

public class Department extends Department_Base {

    public Department() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public List<Employee> getAllCurrentActiveWorkingEmployees() {
	Unit departmentUnit = this.getDepartmentUnit();
	return (departmentUnit != null) ? departmentUnit.getAllCurrentActiveWorkingEmployees()
		: new ArrayList<Employee>();
    }

    public List<Employee> getAllWorkingEmployees(YearMonthDay begin, YearMonthDay end) {
	Unit departmentUnit = this.getDepartmentUnit();
	return (departmentUnit != null) ? departmentUnit.getAllWorkingEmployees(begin, end)
		: new ArrayList<Employee>();
    }

    public List<Employee> getAllWorkingEmployees() {
	Unit departmentUnit = this.getDepartmentUnit();
	return (departmentUnit != null) ? departmentUnit.getAllWorkingEmployees()
		: new ArrayList<Employee>();
    }

    public List<Teacher> getAllCurrentTeachers() {
	Unit departmentUnit = this.getDepartmentUnit();
	return (departmentUnit != null) ? departmentUnit.getAllCurrentTeachers()
		: new ArrayList<Teacher>();
    }

    public List<Teacher> getAllTeachers() {
	Unit departmentUnit = this.getDepartmentUnit();
	return (departmentUnit != null) ? departmentUnit.getAllTeachers() : new ArrayList<Teacher>();
    }

    public List<Teacher> getAllTeachers(YearMonthDay begin, YearMonthDay end) {
	Unit departmentUnit = this.getDepartmentUnit();
	return (departmentUnit != null) ? departmentUnit.getAllTeachers(begin, end)
		: new ArrayList<Teacher>();
    }

    public Teacher getTeacherByPeriod(Integer teacherNumber, YearMonthDay begin, YearMonthDay end) {
	for (Teacher teacher : getAllTeachers(begin, end)) {
	    if (teacher.getTeacherNumber().equals(teacherNumber)) {
		return teacher;
	    }
	}
	return null;
    }       

    public List<CompetenceCourse> getCompetenceCoursesByExecutionYear(ExecutionYear executionYear) {
	List<CompetenceCourse> competenceCourses = this.getCompetenceCourses();
	List<CompetenceCourse> competenceCoursesByExecutionYear = new ArrayList<CompetenceCourse>();
	for (CompetenceCourse competenceCourse : competenceCourses) {
	    if (competenceCourse.hasActiveScopesInExecutionYear(executionYear)) {
		competenceCoursesByExecutionYear.add(competenceCourse);
	    }

	}
	return competenceCoursesByExecutionYear;
    }

    public List<CompetenceCourse> getCompetenceCoursesByExecutionPeriod(ExecutionPeriod executionPeriod) {
	List<CompetenceCourse> competenceCourses = this.getCompetenceCourses();
	List<CompetenceCourse> competenceCoursesByExecutionPeriod = new ArrayList<CompetenceCourse>();
	for (CompetenceCourse competenceCourse : competenceCourses) {
	    if (competenceCourse.hasActiveScopesInExecutionPeriod(executionPeriod)) {
		competenceCoursesByExecutionPeriod.add(competenceCourse);
	    }

	}
	return competenceCoursesByExecutionPeriod;
    }

    public List<TeacherPersonalExpectation> getTeachersPersonalExpectationsByExecutionYear(
	    ExecutionYear executionYear) {
	List<Teacher> teachersFromDepartment = getAllTeachers(executionYear.getBeginDateYearMonthDay(),
		executionYear.getEndDateYearMonthDay());
	List<TeacherPersonalExpectation> personalExpectations = new ArrayList<TeacherPersonalExpectation>();

	for (Teacher teacher : teachersFromDepartment) {
	    TeacherPersonalExpectation teacherPersonalExpectation = teacher
		    .getTeacherPersonalExpectationByExecutionYear(executionYear);

	    if (teacherPersonalExpectation != null) {
		personalExpectations.add(teacherPersonalExpectation);
	    }

	}

	return personalExpectations;
    }

    public String getAcronym() {
	final int begin = this.getRealName().indexOf("(");
	final int end = this.getRealName().indexOf(")");
	return this.getRealName().substring(begin + 1, end);
    }

    @SuppressWarnings("unchecked")
    public List<TeacherServiceDistribution> getTeacherServiceDistributionsByExecutionPeriods(
	    final List<ExecutionPeriod> executionPeriodList) {
	return (List<TeacherServiceDistribution>) CollectionUtils.select(
		getTeacherServiceDistributions(), new Predicate() {
		    public boolean evaluate(Object arg0) {
			TeacherServiceDistribution teacherServiceDistribution = (TeacherServiceDistribution) arg0;
			return !CollectionUtils.intersection(
				teacherServiceDistribution.getExecutionPeriods(), executionPeriodList)
				.isEmpty();
		    }
		});
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributionsByExecutionPeriod(
	    final ExecutionPeriod executionPeriod) {
	List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
	executionPeriodList.add(executionPeriod);
	return getTeacherServiceDistributionsByExecutionPeriods(executionPeriodList);
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributionsByExecutionYear(
	    final ExecutionYear executionYear) {
	return getTeacherServiceDistributionsByExecutionPeriods(executionYear.getExecutionPeriods());
    }

    public List<VigilantGroup> getVigilantGroupsForGivenExecutionYear(ExecutionYear executionYear) {

	Unit departmentUnit = this.getDepartmentUnit();
	List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
	for (Unit unit : departmentUnit.getSubUnits()) {
	    groups.addAll(unit.getVigilantGroupsForGivenExecutionYear(executionYear));
	}
	groups.addAll(departmentUnit.getVigilantGroupsForGivenExecutionYear(executionYear));
	return groups;
    }

    public List<CompetenceCourse> getBolonhaCompetenceCourses() {
	Unit departmentUnit = this.getDepartmentUnit();
	List<CompetenceCourse> courses = new ArrayList<CompetenceCourse>();
	for (Unit areaUnit : departmentUnit.getScientificAreaUnits()) {
	    for (Unit competenceCourseGroupUnit : areaUnit.getCompetenceCourseGroupUnits()) {
		courses.addAll(competenceCourseGroupUnit.getCompetenceCourses());
	    }
	}
	return courses;
    }

    public List<CompetenceCourse> getBolonhaCompetenceCourses(ExecutionPeriod period) {
	List<CompetenceCourse> courses = new ArrayList<CompetenceCourse>();
	for (CompetenceCourse course : this.getBolonhaCompetenceCourses()) {
	    if (!course.getCurricularCoursesWithActiveScopesInExecutionPeriod(period).isEmpty()) {
		courses.add(course);
	    }
	}
	return courses;
    }
    
    public TeacherAutoEvaluationDefinitionPeriod getTeacherAutoEvaluationDefinitionPeriodForExecutionYear(ExecutionYear executionYear) {
	for (TeacherAutoEvaluationDefinitionPeriod period : getTeacherAutoEvaluationDefinitionPeriods()) {
	    if (period.getExecutionYear().equals(executionYear)) {
		return period;
	    }
	}
	return null;
    }
    
    public TeacherExpectationDefinitionPeriod getTeacherExpectationDefinitionPeriodForExecutionYear(ExecutionYear executionYear) {
	for (TeacherExpectationDefinitionPeriod period : getTeacherExpectationDefinitionPeriods()) {
	    if (period.getExecutionYear().equals(executionYear)) {
		return period;
	    }
	}
	return null;
    }
    

    public TeacherPersonalExpectationsVisualizationPeriod getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(ExecutionYear executionYear) {	
	for (TeacherPersonalExpectationsVisualizationPeriod period : getTeacherPersonalExpectationsVisualizationPeriods()) {
	    if (period.getExecutionYear().equals(executionYear)) {
		return period;		
	    }
	}
	return null;
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static Department readByName(final String departmentName) {
	for (final Department department : RootDomainObject.getInstance().getDepartments()) {
	    if (department.getName().equals(departmentName)) {
		return department;
	    }
	}
	return null;
    }

    public static class DepartmentDegreeBean implements FactoryExecutor, Serializable {

	private DomainReference<Department> departmentReference;
	private DomainReference<Degree> degreeReference;

	public Department getDepartment() {
	    return departmentReference == null ? null : departmentReference.getObject();
	}

	public void setDepartment(final Department department) {
	    departmentReference = department == null ? null : new DomainReference<Department>(department);
	}

	public Degree getDegree() {
	    return degreeReference == null ? null : degreeReference.getObject();
	}

	public void setDegree(final Degree degree) {
	    degreeReference = degree == null ? null : new DomainReference<Degree>(degree);
	}

	public Object execute() {
	    final Department department = getDepartment();
	    final Degree degree = getDegree();
	    if (department != null && degree != null) {
		department.getDegreesSet().add(degree);
	    }
	    return null;
	}	
    }

	public void delete() {
        removeDepartmentUnit();
        removeRootDomainObject();
        deleteDomainObject();
    }
}