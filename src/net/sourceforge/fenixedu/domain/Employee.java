/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;

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
            return getEmployeeUnitDepartment(contract.getWorkingUnit());
        }
        return null;
    }

    public IDepartment getDepartmentMailingPlace() {

        IContract contract = getCurrentContract();
        if (contract != null && contract.getMailingUnit() != null) {
            return getEmployeeUnitDepartment(contract.getMailingUnit());
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

    private IDepartment getEmployeeUnitDepartment(IUnit unit) {

        List<IUnit> allTopUnits = unit.getTopUnits();
        if (!allTopUnits.isEmpty()) {
            for (IUnit topUnit : allTopUnits) {
                if (topUnit.getType() != null && topUnit.getType().equals(UnitType.DEPARTMENT)
                        && topUnit.getDepartment() != null
                        && topUnit.getDepartment().getCurrentActiveWorkingEmployees().contains(this)) {
                    return topUnit.getDepartment();
                }
            }
        } else if (unit.getType() != null && unit.getType().equals(UnitType.DEPARTMENT)) {
            return unit.getDepartment();
        }
        return null;
    }
}
