/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.List;

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

        IContract contract = getCurrentContract();

        if (contract != null && contract.getWorkingUnit() != null) {

            IUnit unit = contract.getWorkingUnit();
            return getUnitDepartment(unit);
        }
        return null;
    }

    public IDepartment getDepartmentMailingPlace() {

        IContract contract = getCurrentContract();

        if (contract != null && contract.getMailingUnit() != null) {

            IUnit unit = contract.getMailingUnit();
            return getUnitDepartment(unit);
        }
        return null;
    }

    public IContract getCurrentContract() {

        List<IContract> contracts = this.getContracts();
        for (IContract contract : contracts) {
            if (contract.getEndDate() == null
                    || contract.getEndDate().after(Calendar.getInstance().getTime()))
                return contract;
        }
        return null;
    }

    private IDepartment getUnitDepartment(IUnit unit) {
        if (unit.getParentUnit() != null) {
            while (unit.getParentUnit() != null) {
                unit = unit.getParentUnit();
            }            
        }
        return unit.getDepartment();
    }
}
