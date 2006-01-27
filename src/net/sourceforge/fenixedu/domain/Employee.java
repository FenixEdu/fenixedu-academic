/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
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

    public Department getCurrentDepartmentWorkingPlace() {

        Contract contract = getCurrentContract();
        if (contract != null && contract.getWorkingUnit() != null) {
            return getEmployeeDepartmentUnit(contract.getWorkingUnit(), true);
        }
        return null;
    }

    public Department getLastDepartmentWorkingPlace() {
        Contract contract = getLastContract();
        if (contract != null && contract.getWorkingUnit() != null) {
            return getEmployeeDepartmentUnit(contract.getWorkingUnit(), false);
        }
        return null;
    }

    public Contract getCurrentContract() {
        List<Contract> contracts = this.getContracts();
        for (Contract contract : contracts) {
            if (contract.isActive(Calendar.getInstance().getTime()))
                return contract;
        }
        return null;
    }

    public Contract getLastContract() {
        Date date = null;
        Contract contractToReturn = null;
        for (Contract contract : this.getContracts()) {
            if (contract.isActive(Calendar.getInstance().getTime())) {
                return contract;
            } else if (date == null || date.before(contract.getEndDate())) {
                date = contract.getEndDate();
                contractToReturn = contract;
            }
        }
        return contractToReturn;
    }

    private Department getEmployeeDepartmentUnit(Unit unit, boolean onlyActiveEmployees) {
        List<Unit> parentUnits = unit.getParentUnits();
        if (unitDepartment(unit, onlyActiveEmployees)) {
            return unit.getDepartment();
        }else if (!parentUnits.isEmpty()) {
            for (Unit parentUnit : parentUnits) {
                if (unitDepartment(parentUnit, onlyActiveEmployees)) {
                    return parentUnit.getDepartment();
                } else if (parentUnit.hasAnyParentUnits()) {
                    Department department = getEmployeeDepartmentUnit(parentUnit, onlyActiveEmployees);
                    if (department != null) {
                        return department;
                    }
                }
            }
        }
        return null;
    }
    
    private boolean unitDepartment(Unit unit, boolean onlyActiveEmployees){
        if (unit.getType() != null
                && unit.getType().equals(UnitType.DEPARTMENT)
                && unit.getDepartment() != null
                && (!onlyActiveEmployees || unit.getDepartment()
                        .getCurrentActiveWorkingEmployees().contains(this))){
            return true;
        }
        return false;
    }    
}
