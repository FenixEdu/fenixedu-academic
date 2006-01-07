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

public class Department extends Department_Base {

    public String toString() {
        String result = "[DEPARTAMENT";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getCode();
        result += ", nome=" + getName();
        result += "]";
        return result;
    }

    public List<Employee> getCurrentActiveWorkingEmployees() {
        Unit unit = this.getUnit();
        List<Employee> employees = new ArrayList<Employee>();
        Date currentDate = Calendar.getInstance().getTime();

        if (unit != null) {
            for (Contract contract : unit.getWorkingContracts()) {
                Employee employee = contract.getEmployee();
                if (employee.getActive().booleanValue() && contract.isActive(currentDate)) {
                    employees.add(employee);
                }
            }
            for (Unit subUnit : unit.getSubUnits()) {
                for (Contract contract : subUnit.getWorkingContracts()) {
                    Employee employee = contract.getEmployee();
                    if (employee.getActive().booleanValue() && contract.isActive(currentDate)) {
                        employees.add(employee);
                    }
                }
            }
        }
        return employees;
    }

    public List<Teacher> getCurrentTeachers() {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = this.getCurrentActiveWorkingEmployees();

        addTeachers(teachers, employees);
        return teachers;
    }

    public List<Teacher> getTeachersHistoric() {
        Unit unit = this.getUnit();
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

    public List<Employee> getWorkingEmployees(Date begin, Date end) {
        Unit unit = this.getUnit();
        Set<Employee> employees = new HashSet<Employee>();

        if (unit != null) {
            for (Contract contract : unit.getWorkingContracts(begin, end)) {
                employees.add(contract.getEmployee());
            }
            for (Unit subUnit : unit.getSubUnits()) {
                for (Contract contract : subUnit.getWorkingContracts(begin, end)) {
                    employees.add(contract.getEmployee());
                }
            }
        }
        return new ArrayList<Employee>(employees);
    }

    public List<Teacher> getTeachers(Date begin, Date end) {
        List teachers = new ArrayList();
        addTeachers(teachers, this.getWorkingEmployees(begin, end));
        return teachers;
    }

    public Teacher getTeacherByPeriod(Integer teacherNumber, Date begin, Date end) {
        for (Employee employee : getWorkingEmployees(begin, end)) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && teacher.getTeacherNumber().equals(teacherNumber)) {
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
}
