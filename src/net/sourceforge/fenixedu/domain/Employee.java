/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
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

    public List<Contract> getContractsByContractType(ContractType contractType, YearMonthDay begin,
	    YearMonthDay end) {

	final List<Contract> contracts = new ArrayList();
	for (final Contract accountability : (Collection<Contract>) getPerson()
		.getParentAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class)) {
	    if (accountability.getContractType().equals(contractType)
		    && accountability.belongsToPeriod(begin, end)) {
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
	YearMonthDay date = null, current = new YearMonthDay();
	Contract contractToReturn = null;
	for (Contract contract : getContractsByContractType(type)) {
	    if (!contract.getBeginDate().isAfter(current)) {
		if (contract.isActive(current)) {
		    return contract;
		} else if (date == null || contract.getBeginDate().isAfter(date)) {
		    date = contract.getBeginDate();
		    contractToReturn = contract;
		}
	    }
	}
	return contractToReturn;

    }

    public Contract getLastContractByContractType(ContractType contractType, YearMonthDay begin,
	    YearMonthDay end) {
	YearMonthDay date = null, current = new YearMonthDay();
	Contract contractToReturn = null;
	for (Contract contract : getContractsByContractType(contractType, begin, end)) {
	    if (!contract.getBeginDate().isAfter(current)) {
		if (contract.isActive(current)) {
		    return contract;
		} else if (date == null || contract.getBeginDate().isAfter(date)) {
		    date = contract.getBeginDate();
		    contractToReturn = contract;
		}
	    }
	}
	return contractToReturn;
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

    public Unit getCurrentMailingPlace() {
	Contract contract = getCurrentContractByContractType(ContractType.MAILING);
	return (contract != null) ? contract.getMailingUnit() : null;
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
	    return campus.iterator().next();
	}

	return null;
    }

    public void delete() {
	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public AdministrativeOffice getAdministrativeOffice() {
	AdministrativeOffice administrativeOffice = getCurrentWorkingPlace() == null ? null : getCurrentWorkingPlace().getAdministrativeOffice();

	if (administrativeOffice == null) {
	    for (PersonFunction personFunction : getPerson().getActivePersonFunctions()) {
		if (personFunction.getFunction().getFunctionType().equals(
			FunctionType.ASSIDUOUSNESS_RESPONSIBLE)
			&& personFunction.getUnit().getAdministrativeOffice() != null) {
		    administrativeOffice = personFunction.getUnit().getAdministrativeOffice();
		}
	    }
	}

	return administrativeOffice;
    }

    public boolean isAdministrativeOfficeEmployee() {
	return getAdministrativeOffice() != null;
    }

    private RoleType getRoleType() {
	return RoleType.EMPLOYEE;
    }
    
    public String getRoleLoginAlias() {
	final List<LoginAlias> roleLoginAlias = getPerson().getLoginIdentification().getRoleLoginAlias(getRoleType());
	
	if (roleLoginAlias.isEmpty() || roleLoginAlias.size() > 1) {
	    return "F" + getEmployeeNumber();
	} else {
	    return roleLoginAlias.get(0).getAlias(); 
	}
    }
}
