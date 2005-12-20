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
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;

public class Department extends Department_Base {

    public String toString() {
        String result = "[DEPARTAMENT";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getCode();
        result += ", nome=" + getName();
        result += "]";
        return result;
    }

    public List<IEmployee> getCurrentActiveWorkingEmployees() {
        IUnit unit = this.getUnit();
        List<IEmployee> employees = new ArrayList<IEmployee>();
        Date currentDate = Calendar.getInstance().getTime();

        if (unit != null) {
            for (IContract contract : unit.getWorkingContracts()) {
                IEmployee employee = contract.getEmployee();
                if (employee.getActive().booleanValue() && contract.isActive(currentDate)) {
                    employees.add(employee);
                }
            }
            for (IUnit subUnit : unit.getSubUnits()) {
                for (IContract contract : subUnit.getWorkingContracts()) {
                    IEmployee employee = contract.getEmployee();
                    if (employee.getActive().booleanValue() && contract.isActive(currentDate)) {
                        employees.add(employee);
                    }
                }
            }
        }
        return employees;
    }

    public List<ITeacher> getCurrentTeachers() {
        List<ITeacher> teachers = new ArrayList<ITeacher>();
        List<IEmployee> employees = this.getCurrentActiveWorkingEmployees();

        addTeachers(teachers, employees);
        return teachers;
    }

    public List<ITeacher> getTeachersHistoric() {
        IUnit unit = this.getUnit();
        Set<IEmployee> allEmployees = new HashSet<IEmployee>();
        List<ITeacher> allTeachers = new ArrayList<ITeacher>();
        if (unit != null) {
            for (IContract contract : unit.getWorkingContracts()) {
                allEmployees.add(contract.getEmployee());
            }
            addTeachers(allTeachers, new ArrayList(allEmployees));
        }
        return allTeachers;
    }

    public List<IEmployee> getWorkingEmployees(Date begin, Date end) {
        IUnit unit = this.getUnit();
        Set<IEmployee> employees = new HashSet<IEmployee>();

        if (unit != null) {
            for (IContract contract : unit.getWorkingContracts(begin, end)) {
                employees.add(contract.getEmployee());
            }
            for (IUnit subUnit : unit.getSubUnits()) {
                for (IContract contract : subUnit.getWorkingContracts(begin, end)) {
                    employees.add(contract.getEmployee());
                }
            }
        }
        return new ArrayList<IEmployee>(employees);
    }

    public List<ITeacher> getTeachers(Date begin, Date end) {
        List teachers = new ArrayList();
        addTeachers(teachers, this.getWorkingEmployees(begin, end));
        return teachers;
    }

    public ITeacher getTeacherByPeriod(Integer teacherNumber, Date begin, Date end) {
        for (IEmployee employee : getWorkingEmployees(begin, end)) {
            ITeacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && teacher.getTeacherNumber().equals(teacherNumber)) {
                return teacher;
            }
        }
        return null;
    }

    private void addTeachers(List<ITeacher> teachers, List<IEmployee> employees) {
        for (IEmployee employee : employees) {
            ITeacher teacher = employee.getPerson().getTeacher();
            if (teacher != null)
                teachers.add(teacher);
        }
    }

    public void createTeacherExpectationDefinitionPeriod(IExecutionYear executionYear, Date startDate,
            Date endDate) {

        ITeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = new TeacherExpectationDefinitionPeriod(
                executionYear, startDate, endDate);

        checkIfCanCreateExpectationDefinitionPeriod(executionYear);

        this.getTeacherExpectationDefinitionPeriods().add(teacherExpectationDefinitionPeriod);

    }

    private void checkIfCanCreateExpectationDefinitionPeriod(IExecutionYear executionYear) {

        for (ITeacherExpectationDefinitionPeriod expectationDefinitionPeriod : this
                .getTeacherExpectationDefinitionPeriods()) {
            if (expectationDefinitionPeriod.getExecutionYear().equals(executionYear)) {
                throw new DomainException("error.exception.expectationPeriodForYearAlreadyExists");
            }
        }

    }

    public ITeacherExpectationDefinitionPeriod readTeacherExpectationDefinitionPeriodByExecutionYear(
            IExecutionYear executionYear) {
        ITeacherExpectationDefinitionPeriod result = null;

        for (ITeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod : this
                .getTeacherExpectationDefinitionPeriods()) {
            if (teacherExpectationDefinitionPeriod.getExecutionYear().equals(executionYear)) {
                result = teacherExpectationDefinitionPeriod;
                break;
            }
        }

        return result;

    }

    public List<ICompetenceCourse> getCompetenceCoursesByExecutionYear(IExecutionYear executionYear) {
        List<ICompetenceCourse> competenceCourses = this.getCompetenceCourses();
        List<ICompetenceCourse> competenceCoursesByExecutionYear = new ArrayList<ICompetenceCourse>();
        for (ICompetenceCourse competenceCourse : competenceCourses) {
            if (competenceCourse.hasActiveScopesInExecutionYear(executionYear)) {
                competenceCoursesByExecutionYear.add(competenceCourse);
            }

        }
        return competenceCoursesByExecutionYear;
    }
}
