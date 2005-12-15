/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;
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
            return getEmployeeUnitDepartment(contract.getWorkingUnit(), true);
        }
        return null;
    }

    public IDepartment getDepartmentMailingPlace() {

        IContract contract = getCurrentContract();
        if (contract != null && contract.getMailingUnit() != null) {
            return getEmployeeUnitDepartment(contract.getMailingUnit(), true);
        }
        return null;
    }
    
    public IDepartment getLastDepartmentWorkingPlace(){
        IContract contract = getLastContract();
        if (contract != null && contract.getWorkingUnit() != null) {
            return getEmployeeUnitDepartment(contract.getWorkingUnit(), false);
        }
        return null;
    }

    public IContract getCurrentContract() {

        List<IContract> contracts = this.getContracts();
        for (IContract contract : contracts) {
            if (contract.getEndDate() == null || contract.getEndDate().after(prepareCurrentDate()))
                return contract;
        }
        return null;
    }    
    
    public IContract getLastContract() {
        Date date = null, currentDate = prepareCurrentDate();
        IContract contractToReturn = null;
        for (IContract contract : this.getContracts()) {
            if (contract.getEndDate() == null || contract.getEndDate().after(currentDate)) {
                contractToReturn = contract;
                break;
            } else if (date == null || date.before(contract.getEndDate())) {
                date = contract.getEndDate();
                contractToReturn = contract;
            }
        }
        return contractToReturn;
    }

    private IDepartment getEmployeeUnitDepartment(IUnit unit, boolean onlyActiveEmployees) {

        List<IUnit> allTopUnits = unit.getTopUnits();
        if (!allTopUnits.isEmpty()) {
            for (IUnit topUnit : allTopUnits) {
                if (topUnit.getType() != null && topUnit.getType().equals(UnitType.DEPARTMENT)
                        && topUnit.getDepartment() != null
                        && ( !onlyActiveEmployees || topUnit.getDepartment().getCurrentActiveWorkingEmployees().contains(this))) {
                    return topUnit.getDepartment();
                }
            }
        } else if (unit.getType() != null && unit.getType().equals(UnitType.DEPARTMENT)) {
            return unit.getDepartment();
        }
        return null;
    }
    
    private Date prepareCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
