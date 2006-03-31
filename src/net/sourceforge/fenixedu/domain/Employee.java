/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

/**
 * 
 * @author T�nia Pous�o
 */
public class Employee extends Employee_Base {

    public Employee() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
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

    public List<Contract> getContractsByPeriod(Date begin, Date end){
        List<Contract> contracts = new ArrayList<Contract>();
        for (Contract contract : getContracts()) {
            if(contract.belongsToPeriod(begin,end)){
                contracts.add(contract);
            }
        }
        return contracts;
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
                && unit.getType().equals(PartyTypeEnum.DEPARTMENT)
                && unit.getDepartment() != null
                && (!onlyActiveEmployees || unit.getDepartment()
                        .getCurrentActiveWorkingEmployees().contains(this))){
            return true;
        }
        return false;
    }    
    
    public static Employee readByNumber(final Integer employeeNumber) {
        for (final Employee employee : RootDomainObject.getInstance().getEmployees()) {
            if (employee.getEmployeeNumber().equals(employeeNumber)) {
                return employee;
            }
        }
        return null;
    }
}
