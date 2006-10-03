/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.Category;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author T�nia Pous�o
 */
public class Employee extends Employee_Base {

    public Employee(Person person, Integer employeeNumber, Boolean active) {
        super();
        checkParameters(person, employeeNumber);        
        setEmployeeNumber(employeeNumber);        
        setCreationDate(new DateTime());
        setPerson(person);        
        setActive(active);
        setAntiquity(new Date(System.currentTimeMillis()));
        setWorkingHours(0); 
        setAssiduousness(null);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    public void setEmployeeNumber(Integer employeeNumber) {
        checkEmployeeNumber(employeeNumber);
        super.setEmployeeNumber(employeeNumber);
    }

    private void checkParameters(Person person, Integer employeeNumber) {
        if(person == null) {
            throw new DomainException("error.employee.no.person");
        }
        if(employeeNumber == null) {
            throw new DomainException("error.employee.no.employeeNumber");
        }         
    }

    private void checkEmployeeNumber(Integer employeeNumber) {
        Employee employee = readByNumber(employeeNumber);
        if(employee != null && !employee.equals(this)) {
            throw new DomainException("error.employee.already.exists.one.employee.with.same.number");
        }
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

    public Unit getCurrentWorkingPlace() {
        Contract contract = getCurrentContract();
        if (contract != null) {
            return contract.getWorkingUnit();
        }
        return null;
    }

    public Unit getLastWorkingPlace() {
        Contract contract = getLastContract();
        if (contract != null) {
            return contract.getWorkingUnit();
        }
        return null;
    }
    
    public Unit getLastWorkingPlaceByPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        SortedSet<Contract> contracts = new TreeSet<Contract>(Contract.CONTRACT_COMPARATOR_BY_BEGIN_DATE);        
        contracts.addAll(getContractsByPeriod(beginDate, endDate));
        return (!contracts.isEmpty()) ? contracts.last().getWorkingUnit() : null;    
    }

    public List<Unit> getWorkingPlacesByPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        List<Unit> units = new ArrayList<Unit>();
        for (Contract contract : getContractsByPeriod(beginDate, endDate)) {
            units.add(contract.getWorkingUnit());
        }
        return units;
    }

    public Contract getCurrentContract() {
        List<Contract> contracts = this.getContracts();
        for (Contract contract : contracts) {
            if (contract.isActive(new YearMonthDay()))
                return contract;
        }
        return null;
    }

    public Contract getLastContract() {
        SortedSet<Contract> contracts = new TreeSet<Contract>(Contract.CONTRACT_COMPARATOR_BY_BEGIN_DATE);        
        contracts.addAll(getContracts());
        return (!contracts.isEmpty()) ? contracts.last() : null;  
    }

    public List<Contract> getContractsByPeriod(YearMonthDay begin, YearMonthDay end) {
        List<Contract> contracts = new ArrayList<Contract>();
        for (Contract contract : getContracts()) {
            if (contract.belongsToPeriod(begin, end)) {
                contracts.add(contract);
            }
        }
        return contracts;
    }

    private Department getEmployeeDepartmentUnit(Unit unit, boolean onlyActiveEmployees) {
        List<Unit> parentUnits = unit.getParentUnits();
        if (unitDepartment(unit, onlyActiveEmployees)) {
            return unit.getDepartment();
        } else if (!parentUnits.isEmpty()) {
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

    private boolean unitDepartment(Unit unit, boolean onlyActiveEmployees) {
        if (unit.getType() != null
                && unit.getType().equals(PartyTypeEnum.DEPARTMENT)
                && unit.getDepartment() != null
                && (!onlyActiveEmployees || unit.getDepartment().getAllCurrentActiveWorkingEmployees()
                        .contains(this))) {
            return true;
        }
        return false;
    }

    public static Employee readByNumber(final Integer employeeNumber) {
        for (final Employee employee : RootDomainObject.getInstance().getEmployees()) {
            if (employee.getEmployeeNumber() != null && 
                    employee.getEmployeeNumber().equals(employeeNumber)) {
                return employee;
            }
        }
        return null;
    }
    
    public Department getLastDepartmentWorkingPlace(YearMonthDay begin, YearMonthDay end) {
    	Unit unit = getLastWorkingPlaceByPeriod(begin, end);
    	Unit departmentUnit = (unit != null) ? unit.getDepartmentUnit() : null;
    	return (departmentUnit != null) ? departmentUnit.getDepartment() : null;    	    	
    }

}
