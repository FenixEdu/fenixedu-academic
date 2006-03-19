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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;

public class Department extends Department_Base {

    public Department() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public List<Employee> getCurrentActiveWorkingEmployees() {

        Unit departmentUnit = this.getDepartmentUnit();
        Set<Employee> employees = new HashSet<Employee>();
        Date currentDate = Calendar.getInstance().getTime();

        if (departmentUnit != null) {
            readAndSaveEmployees(departmentUnit, employees, currentDate);
        }
        return new ArrayList<Employee>(employees);
    }

    private void readAndSaveEmployees(Unit unit, Set<Employee> employees, Date currentDate) {
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

    public List<Employee> getWorkingEmployees(Date begin, Date end) {

        Unit departmentUnit = this.getDepartmentUnit();
        Set<Employee> employees = new HashSet<Employee>();

        if (departmentUnit != null) {
            readAndSaveEmployees(departmentUnit, employees, begin, end);
        }
        return new ArrayList<Employee>(employees);
    }

    private void readAndSaveEmployees(Unit unit, Set<Employee> employees, Date begin, Date end) {
        for (Contract contract : unit.getWorkingContracts(begin, end)) {
            employees.add(contract.getEmployee());
        }
        for (Unit subUnit : unit.getSubUnits()) {
            readAndSaveEmployees(subUnit, employees, begin, end);
        }
    }

    public List<Teacher> getCurrentTeachers() {
        Date currentDate = Calendar.getInstance().getTime();
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = this.getCurrentActiveWorkingEmployees();

        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null) {
                TeacherLegalRegimen legalRegimen = teacher
                        .getLastLegalRegimenWithoutSpecialSituations();
                if (legalRegimen != null && legalRegimen.isActive(currentDate)) {
                    teachers.add(teacher);
                }
            }
        }
        return teachers;
    }

    public List<Teacher> getTeachersHistoric() {
        Unit unit = this.getDepartmentUnit();
        Set<Employee> allEmployees = new HashSet<Employee>();
        List<Teacher> allTeachers = new ArrayList<Teacher>();
        if (unit != null) {
            for (Contract contract : unit.getWorkingContracts()) {
                allEmployees.add(contract.getEmployee());
            }
            for (Unit subUnit : unit.getSubUnits()) {
                for (Contract contract : subUnit.getWorkingContracts()) {
                    allEmployees.add(contract.getEmployee());
                }
            }
            addTeachers(allTeachers, new ArrayList(allEmployees));
        }
        return allTeachers;
    }

    public List<Teacher> getTeachers(Date begin, Date end) {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = this.getWorkingEmployees(begin, end);
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null
                    && !teacher.getAllLegalRegimensWithoutSpecialSituations(begin,
                            end).isEmpty()) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public Teacher getTeacherByPeriod(Integer teacherNumber, Date begin, Date end) {
        for (Employee employee : getWorkingEmployees(begin, end)) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null
                    && teacher.getTeacherNumber().equals(teacherNumber)
                    && !teacher.getAllLegalRegimensWithoutSpecialSituations(begin,
                            end).isEmpty()) {
                return teacher;
            }
        }
        return null;
    }

    private void addTeachers(List<Teacher> teachers, List<Employee> employees) {
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null)
                teachers.add(teacher);
        }
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

    public List<TeacherPersonalExpectation> getTeachersPersonalExpectationsByExecutionYear(
            ExecutionYear executionYear) {
        List<Teacher> teachersFromDepartment = getTeachers(executionYear.getBeginDate(), executionYear
                .getEndDate());
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

    public String retrieveAcronym() {
        final int begin = this.getRealName().indexOf("(");
        final int end = this.getRealName().indexOf(")");
        return this.getRealName().substring(begin + 1, end);
    }

}
