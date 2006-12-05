/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.util.ContractType;

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
	if (person == null) {
	    throw new DomainException("error.employee.no.person");
	}
	if (employeeNumber == null) {
	    throw new DomainException("error.employee.no.employeeNumber");
	}
    }

    private void checkEmployeeNumber(Integer employeeNumber) {
	Employee employee = readByNumber(employeeNumber);
	if (employee != null && !employee.equals(this)) {
	    throw new DomainException("error.employee.already.exists.one.employee.with.same.number");
	}
    }

    public Collection<Contract> getContracts() {
	return (Collection<Contract>) getPerson().getParentAccountabilities(
		AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class);
    }

    public List<Contract> getContractsByContractType(ContractType contractType) {
	final List<Contract> contracts = new ArrayList();
	for (final Contract accountability : (Collection<Contract>) getPerson()
		.getParentAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class)) {
	    if (accountability.getContractType().equals(contractType)) {
		contracts.add(accountability);
	    }
	}
	return contracts;
    }

    public Contract getCurrentContractByContractType(ContractType contractType) {
	YearMonthDay current = new YearMonthDay();
	for (final Contract accountability : (Collection<Contract>) getPerson()
		.getParentAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class)) {
	    if (accountability.getContractType().equals(contractType)
		    && accountability.isActive(current)) {
		return accountability;
	    }
	}
	return null;
    }

    public Contract getLastContractByContractType(ContractType type) {
	SortedSet<Contract> contracts = new TreeSet<Contract>(Contract.CONTRACT_COMPARATOR_BY_BEGIN_DATE);
	contracts.addAll(getContractsByContractType(type));
	return (!contracts.isEmpty()) ? contracts.last() : null;
    }

    public Contract getLastContractByContractType(ContractType contractType, YearMonthDay begin,
	    YearMonthDay end) {
	final SortedSet<Contract> contracts = new TreeSet<Contract>(
		Contract.CONTRACT_COMPARATOR_BY_BEGIN_DATE);
	for (final Contract accountability : (Collection<Contract>) getPerson()
		.getParentAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class)) {
	    if (accountability.getContractType().equals(contractType)
		    && accountability.belongsToPeriod(begin, end)) {
		contracts.add(accountability);
	    }
	}
	return (!contracts.isEmpty()) ? contracts.last() : null;
    }

    public List<Contract> getWorkingContracts() {
	return getContractsByContractType(ContractType.WORKING);
    }

    public List<Contract> getWorkingContracts(YearMonthDay begin, YearMonthDay end) {
	final List<Contract> contracts = new ArrayList();
	for (final Contract accountability : (Collection<Contract>) getPerson()
		.getParentAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class)) {
	    if (accountability.getContractType().equals(ContractType.WORKING)
		    && accountability.belongsToPeriod(begin, end)) {
		contracts.add(accountability);
	    }
	}
	return contracts;
    }

    public Contract getCurrentWorkingContract() {
	return getCurrentContractByContractType(ContractType.WORKING);
    }

    public Contract getLastWorkingContract() {
	return getLastContractByContractType(ContractType.WORKING);
    }

    public Unit getCurrentWorkingPlace() {
	Contract contract = getCurrentWorkingContract();
	return (contract != null) ? contract.getWorkingUnit() : null;
    }

    public Unit getLastWorkingPlace() {
	Contract contract = getLastWorkingContract();
	return (contract != null) ? contract.getWorkingUnit() : null;
    }

    public Unit getLastWorkingPlace(YearMonthDay beginDate, YearMonthDay endDate) {
	Contract lastContract = getLastContractByContractType(ContractType.WORKING, beginDate, endDate);
	return lastContract != null ? lastContract.getWorkingUnit() : null;
    }

    public List<Unit> getWorkingPlaces(YearMonthDay beginDate, YearMonthDay endDate) {
	List<Unit> units = new ArrayList<Unit>();
	for (final Contract accountability : (Collection<Contract>) getPerson()
		.getParentAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class)) {
	    if (accountability.getContractType().equals(ContractType.WORKING)
		    && accountability.belongsToPeriod(beginDate, endDate)) {
		units.add(accountability.getWorkingUnit());
	    }
	}
	return units;
    }

    public Department getCurrentDepartmentWorkingPlace() {
	Contract contract = getCurrentWorkingContract();
	return (contract != null && contract.getWorkingUnit() != null) ? getEmployeeDepartmentUnit(
		contract.getWorkingUnit(), true) : null;
    }

    public Department getLastDepartmentWorkingPlace() {
	Contract contract = getLastContractByContractType(ContractType.WORKING);
	return (contract != null && contract.getWorkingUnit() != null) ? getEmployeeDepartmentUnit(
		contract.getWorkingUnit(), false) : null;
    }

    public Department getLastDepartmentWorkingPlace(YearMonthDay begin, YearMonthDay end) {
	Unit unit = getLastWorkingPlace(begin, end);
	Unit departmentUnit = (unit != null) ? unit.getDepartmentUnit() : null;
	return (departmentUnit != null) ? departmentUnit.getDepartment() : null;
    }

    private Department getEmployeeDepartmentUnit(Unit unit, boolean onlyActiveEmployees) {
	Collection<Unit> parentUnits = unit.getParentUnits();
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
	return (unit.getType() != null && unit.getType().equals(PartyTypeEnum.DEPARTMENT)
		&& unit.getDepartment() != null && (!onlyActiveEmployees || unit.getDepartment()
		.getAllCurrentActiveWorkingEmployees().contains(this)));
    }

    public static Employee readByNumber(final Integer employeeNumber) {
	for (final Employee employee : RootDomainObject.getInstance().getEmployees()) {
	    if (employee.getEmployeeNumber().equals(employeeNumber)) {
		return employee;
	    }
	}
	return null;
    }
    
    public Campus getCurrentCampus() {
	final YearMonthDay now = new YearMonthDay();
	
	final List<Campus> campus = getAssiduousness().getCampusForInterval(now, now);
	if (campus.size() > 1) {
	    throw new DomainException("Employee.with.more.than.one.campus.for.same.day");
	} else if (!campus.isEmpty()) {
	    campus.iterator().next();
	}
	
	return null; 
    }
    
}
