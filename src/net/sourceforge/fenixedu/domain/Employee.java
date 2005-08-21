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

    public ICostCenter findWorkingCostCenter() {
        for (final IEmployeeHistoric employeeHistoric : getHistoricList()) {
            if (employeeHistoric.getWorkingPlaceCostCenter() != null) {
                return employeeHistoric.getWorkingPlaceCostCenter();
            }
        }
        return null;
    }

}
