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

    public List getWorkingEmployees() {

        IUnit unit = this.getUnit();
        List employees = new ArrayList();

        if (unit != null) {

            for (IContract contract : unit.getWorkingContracts()) {
                IEmployee employee = contract.getEmployee();
                if (employee.getActive().booleanValue()
                        && contract.equals(employee.getCurrentContract())) {
                    employees.add(employee);
                }
            }
            for (IUnit subUnit : unit.getAssociatedUnits()) {
                for (IContract contract : subUnit.getWorkingContracts()) {
                    IEmployee employee = contract.getEmployee();
                    if (employee.getActive().booleanValue()
                            && contract.equals(employee.getCurrentContract())) {
                        employees.add(employee);
                    }
                }
            }
        }
        return employees;
    }

    public List getTeachers() {

        List teachers = new ArrayList();
        List<IEmployee> employees = this.getWorkingEmployees();

        for (IEmployee employee : employees) {
            ITeacher teacher = employee.getPerson().getTeacher();
            if (teacher != null)
                teachers.add(teacher);
        }
        return teachers;
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

}
