/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;

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
            
            return getUnitDepartment(contract.getWorkingUnit());
        }
        return null;
    }

    public IDepartment getDepartmentMailingPlace() {

        IContract contract = getCurrentContract();

        if (contract != null && contract.getMailingUnit() != null) {

            return getUnitDepartment(contract.getMailingUnit());
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
