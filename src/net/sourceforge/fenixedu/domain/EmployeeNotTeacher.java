/*
 * Created on 1/Mar/2004
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Tânia Pousão
 *  
 */
public class EmployeeNotTeacher extends EmployeeNotTeacher_Base {
    private IEmployee employee;

    /**
     * @return Returns the employee.
     */
    public IEmployee getEmployee() {
        return employee;
    }

    /**
     * @param employee
     *            The employee to set.
     */
    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

}