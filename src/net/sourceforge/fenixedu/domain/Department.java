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
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;

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
