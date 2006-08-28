/*
 * Department.java
 * 
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

public class Department extends Department_Base {

    public Department() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public List<Employee> getAllCurrentActiveWorkingEmployees() {
        Unit departmentUnit = this.getDepartmentUnit();        
        return (departmentUnit != null) ? departmentUnit.getAllCurrentActiveWorkingEmployees() : new ArrayList<Employee>();
    }

    public List<Employee> getAllWorkingEmployees(YearMonthDay begin, YearMonthDay end) {
        Unit departmentUnit = this.getDepartmentUnit();
        return (departmentUnit != null) ? departmentUnit.getAllWorkingEmployees(begin, end) : new ArrayList<Employee>();
    }
    
    public List<Employee> getAllWorkingEmployees() {
        Unit departmentUnit = this.getDepartmentUnit();
        return (departmentUnit != null) ? departmentUnit.getAllWorkingEmployees() : new ArrayList<Employee>();
    }

    public List<Teacher> getAllCurrentTeachers() {
        Unit departmentUnit = this.getDepartmentUnit();
        return (departmentUnit != null) ? departmentUnit.getAllCurrentTeachers() : new ArrayList<Teacher>();
    }

    public List<Teacher> getAllTeachers() {
        Unit departmentUnit = this.getDepartmentUnit();
        return (departmentUnit != null) ? departmentUnit.getAllTeachers() : new ArrayList<Teacher>();
    }

    public List<Teacher> getAllTeachers(YearMonthDay begin, YearMonthDay end) {
        Unit departmentUnit = this.getDepartmentUnit();
        return (departmentUnit != null) ? departmentUnit.getAllTeachers(begin, end) : new ArrayList<Teacher>();
    }

    public Teacher getTeacherByPeriod(Integer teacherNumber, YearMonthDay begin, YearMonthDay end) {
        for (Teacher teacher : getAllTeachers(begin, end)) {            
            if (teacher.getTeacherNumber().equals(teacherNumber)) {
                return teacher;
            }
        }
        return null;
    }

    public void createTeacherExpectationDefinitionPeriod(ExecutionYear executionYear, Date startDate,
            Date endDate) {

        TeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = new TeacherExpectationDefinitionPeriod(
                executionYear, startDate, endDate);

        checkIfCanCreateExpectationDefinitionPeriod(executionYear);
        this.getTeacherExpectationDefinitionPeriods().add(teacherExpectationDefinitionPeriod);

    }

    private void checkIfCanCreateExpectationDefinitionPeriod(ExecutionYear executionYear) {

        for (TeacherExpectationDefinitionPeriod expectationDefinitionPeriod : this
                .getTeacherExpectationDefinitionPeriods()) {
            if (expectationDefinitionPeriod.getExecutionYear().equals(executionYear)) {
                throw new DomainException("error.exception.expectationPeriodForYearAlreadyExists");
            }
        }

    }

    public TeacherExpectationDefinitionPeriod readTeacherExpectationDefinitionPeriodByExecutionYear(
            ExecutionYear executionYear) {
        TeacherExpectationDefinitionPeriod result = null;

        for (TeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod : this
                .getTeacherExpectationDefinitionPeriods()) {
            if (teacherExpectationDefinitionPeriod.getExecutionYear().equals(executionYear)) {
                result = teacherExpectationDefinitionPeriod;
                break;
            }
        }

        return result;

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
        List<Teacher> teachersFromDepartment = getAllTeachers(executionYear.getBeginDateYearMonthDay(), executionYear
                .getEndDateYearMonthDay());
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

    public List<Teacher> getTeachers(YearMonthDay begin, YearMonthDay end) {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = this.getWorkingEmployees(begin, end);
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null
                    && !teacher.getAllLegalRegimensWithoutEndSituations(begin,
                            end).isEmpty()) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    @SuppressWarnings("unchecked")
	public List<TeacherServiceDistribution> getTeacherServiceDistributionsByExecutionPeriods(final List<ExecutionPeriod> executionPeriodList) {
    	return (List<TeacherServiceDistribution>) CollectionUtils.select(getTeacherServiceDistributions(),
    			new Predicate() {
					public boolean evaluate(Object arg0) {
						TeacherServiceDistribution teacherServiceDistribution = (TeacherServiceDistribution) arg0;
						return !CollectionUtils.intersection(teacherServiceDistribution.getExecutionPeriods(), executionPeriodList).isEmpty();
					}
    	});
    }
    
    public List<TeacherServiceDistribution> getTeacherServiceDistributionsByExecutionPeriod(final ExecutionPeriod executionPeriod) {
    	List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
    	executionPeriodList.add(executionPeriod);
    	return getTeacherServiceDistributionsByExecutionPeriods(executionPeriodList);
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributionsByExecutionYear(final ExecutionYear executionYear) {
    	return getTeacherServiceDistributionsByExecutionPeriods(executionYear.getExecutionPeriods());
    }

    
    private void readAndSaveEmployees(Unit unit, Set<Employee> employees, YearMonthDay currentDate) {
        for (Contract contract : unit.getWorkingContracts()) {
            Employee employee = contract.getEmployee();
            if (employee.getActive().booleanValue() && contract.isActive(currentDate)) {
                employees.add(employee);
            }
        }
        for (Unit subUnit : unit.getSubUnits()) {
            readAndSaveEmployees(subUnit, employees, currentDate);
        }
    }
    
    public List<Employee> getWorkingEmployees(YearMonthDay begin, YearMonthDay end) {

        Unit departmentUnit = this.getDepartmentUnit();
        Set<Employee> employees = new HashSet<Employee>();

        if (departmentUnit != null) {
            readAndSaveEmployees(departmentUnit, employees, begin, end);
        }
        return new ArrayList<Employee>(employees);
    }


    private void readAndSaveEmployees(Unit unit, Set<Employee> employees, YearMonthDay begin, YearMonthDay end) {
        for (Contract contract : unit.getWorkingContracts(begin, end)) {
            employees.add(contract.getEmployee());
        }
        for (Unit subUnit : unit.getSubUnits()) {
            readAndSaveEmployees(subUnit, employees, begin, end);
        }
    }
    
	public List<Employee> getCurrentActiveWorkingEmployees() {

        Unit departmentUnit = this.getDepartmentUnit();
        Set<Employee> employees = new HashSet<Employee>();
        YearMonthDay currentDate = new YearMonthDay();

        if (departmentUnit != null) {
            readAndSaveEmployees(departmentUnit, employees, currentDate);
        }
        return new ArrayList<Employee>(employees);
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
}
