/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Tânia Pousão
 */
public class Employee extends Employee_Base {

    public String toString() {
        String result = "[Dominio.Employee ";
        result += ", employeeNumber=" + getEmployeeNumber();
        result += ", person=" + getPerson();
        result += "]";
        return result;
    }

    public IDepartment getDepartmentWorkingPlace() {

        IUnit unit = this.getWorkingUnit();

        if (unit != null) {

            if (unit.getParentUnit() == null) {
                return unit.getDepartment();

            } else {
                while (unit.getParentUnit() != null) {
                    unit = unit.getParentUnit();
                }
                return unit.getDepartment();
            }
        }
        return null;
    }

    public IDepartment getDepartmentMailingPlace() {

        IUnit unit = this.getMailingUnit();
        if (unit != null) {
            
            if (unit.getParentUnit() == null) {
                return unit.getDepartment();
            
            } else {
                while (unit.getParentUnit() != null) {
                    unit = unit.getParentUnit();
                }
                return unit.getDepartment();
            }
        }
        return null;
    }
}
