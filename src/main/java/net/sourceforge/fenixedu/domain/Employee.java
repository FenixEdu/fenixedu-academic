/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Tânia Pousão
 */
public class Employee extends Employee_Base {

    public Employee(Person person, Integer employeeNumber) {
        super();
        setEmployeeNumber(employeeNumber);
        setCreationDate(new DateTime());
        setPerson(person);
        setWorkingHours(0);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        super.setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    public void setEmployeeNumber(Integer employeeNumber) {
        if (employeeNumber == null) {
            throw new DomainException("error.employee.no.employeeNumber");
        }
        checkEmployeeNumber(employeeNumber);
        super.setEmployeeNumber(employeeNumber);
    }

    @Override
    public void setPerson(Person person) {
        if (person == null) {
            throw new DomainException("error.employee.no.person");
        }
        if (person.hasEmployee()) {
            throw new DomainException("error.employee.person.already.has.employee");
        }
        super.setPerson(person);
    }

    private void checkEmployeeNumber(Integer employeeNumber) {
        Employee employee = readByNumber(employeeNumber);
        if (employee != null && !employee.equals(this)) {
            throw new DomainException("error.employee.already.exists.one.employee.with.same.number", employeeNumber.toString());
        }
    }

    public Collection<Contract> getContractsByContractType(AccountabilityTypeEnum contractType) {
        return (Collection<Contract>) getPerson().getParentAccountabilities(contractType, EmployeeContract.class);
    }

    public List<Contract> getContractsByContractType(AccountabilityTypeEnum contractType, YearMonthDay begin, YearMonthDay end) {
        final List<Contract> contracts = new ArrayList<Contract>();
        for (final Contract accountability : getContractsByContractType(contractType)) {
            if (accountability.belongsToPeriod(begin, end)) {
                contracts.add(accountability);
            }
        }
        return contracts;
    }

    public Contract getCurrentContractByContractType(AccountabilityTypeEnum contractType) {
        YearMonthDay current = new YearMonthDay();
        for (final Contract accountability : getContractsByContractType(contractType)) {
            if (accountability.isActive(current)) {
                return accountability;
            }
        }
        return null;
    }

    public Contract getLastContractByContractType(AccountabilityTypeEnum contractType) {
        YearMonthDay date = null, current = new YearMonthDay();
        Contract contractToReturn = null;
        for (Contract contract : getContractsByContractType(contractType)) {
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

    public Contract getLastContractByContractType(AccountabilityTypeEnum contractType, YearMonthDay begin, YearMonthDay end) {
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
        List<Contract> workingContracts = new ArrayList<Contract>();
        workingContracts.addAll(getContractsByContractType(AccountabilityTypeEnum.WORKING_CONTRACT));
        return workingContracts;
    }

    public List<Contract> getMailingContracts() {
        List<Contract> mailingContracts = new ArrayList<Contract>();
        mailingContracts.addAll(getContractsByContractType(AccountabilityTypeEnum.MAILING_CONTRACT));
        return mailingContracts;
    }

    public List<Contract> getWorkingContracts(YearMonthDay begin, YearMonthDay end) {
        final List<Contract> contracts = new ArrayList<Contract>();
        for (final Contract accountability : getContractsByContractType(AccountabilityTypeEnum.WORKING_CONTRACT)) {
            if (accountability.belongsToPeriod(begin, end)) {
                contracts.add(accountability);
            }
        }
        return contracts;
    }

    public Contract getCurrentWorkingContract() {
        return getCurrentContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT);
    }

    public Contract getLastWorkingContract() {
        return getLastContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT);
    }

    public Unit getCurrentWorkingPlace() {
        Contract contract = getCurrentWorkingContract();
        return (contract != null) ? contract.getWorkingUnit() : null;
    }

    public List<Unit> getCurrentWorkingPlacePath() {
        Contract contract = getCurrentWorkingContract();
        return (List<Unit>) (contract != null ? contract.getWorkingUnit().getParentUnitsPath() : Collections.emptyList());
    }

    public Unit getLastWorkingPlace() {
        Contract contract = getLastWorkingContract();
        return (contract != null) ? contract.getWorkingUnit() : null;
    }

    public Unit getCurrentMailingPlace() {
        Contract contract = getCurrentContractByContractType(AccountabilityTypeEnum.MAILING_CONTRACT);
        return (contract != null) ? contract.getMailingUnit() : null;
    }

    public Unit getLastWorkingPlace(YearMonthDay beginDate, YearMonthDay endDate) {
        Contract lastContract = getLastContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT, beginDate, endDate);
        return lastContract != null ? lastContract.getWorkingUnit() : null;
    }

    public List<Unit> getWorkingPlaces(YearMonthDay beginDate, YearMonthDay endDate) {
        List<Unit> units = new ArrayList<Unit>();
        for (final Contract contract : getContractsByContractType(AccountabilityTypeEnum.WORKING_CONTRACT)) {
            if (contract.belongsToPeriod(beginDate, endDate)) {
                units.add(contract.getWorkingUnit());
            }
        }
        return units;
    }

    public Department getCurrentDepartmentWorkingPlace() {
        Contract contract = getCurrentWorkingContract();
        return (contract != null && contract.getWorkingUnit() != null) ? getEmployeeDepartmentUnit(contract.getWorkingUnit(),
                true) : null;
    }

    public Department getLastDepartmentWorkingPlace() {
        Contract contract = getLastContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT);
        return (contract != null && contract.getWorkingUnit() != null) ? getEmployeeDepartmentUnit(contract.getWorkingUnit(),
                false) : null;
    }

    public Department getLastDepartmentWorkingPlace(YearMonthDay begin, YearMonthDay end) {
        Unit unit = getLastWorkingPlace(begin, end);
        DepartmentUnit departmentUnit = (unit != null) ? unit.getDepartmentUnit() : null;
        return (departmentUnit != null) ? departmentUnit.getDepartment() : null;
    }

    private Department getEmployeeDepartmentUnit(Unit unit, boolean onlyActiveEmployees) {
        Collection<Unit> parentUnits = unit.getParentUnits();
        if (unitDepartment(unit, onlyActiveEmployees)) {
            return ((DepartmentUnit) unit).getDepartment();
        } else if (!parentUnits.isEmpty()) {
            for (Unit parentUnit : parentUnits) {
                if (unitDepartment(parentUnit, onlyActiveEmployees)) {
                    return ((DepartmentUnit) parentUnit).getDepartment();
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
        return (unit.isDepartmentUnit() && ((DepartmentUnit) unit).getDepartment() != null && (!onlyActiveEmployees || ((DepartmentUnit) unit)
                .getDepartment().hasCurrentActiveWorkingEmployee(this)));
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
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalData();
            if (giafProfessionalData != null && giafProfessionalData.isActive()) {
                return giafProfessionalData.getCampus();
            }
        }
        return null;
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

    public Unit getCurrentSectionOrScientificArea() {
        return getSectionOrScientificArea(getCurrentWorkingPlace());
    }

    private Unit getSectionOrScientificArea(Unit unit) {
        if (unit == null) {
            return null;
        }

        if (unit.isScientificAreaUnit() || unit.isSectionUnit()) {
            return unit;
        }

        for (Unit parent : unit.getParentUnits()) {
            Unit parentUnit = getSectionOrScientificArea(parent);

            if (parentUnit != null) {
                return parentUnit;
            }
        }

        return null;
    }

    public ProfessionalCategory getCategory() {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        ProfessionalCategory professionalCategory = null;
        if (personProfessionalData != null) {
            professionalCategory =
                    personProfessionalData.getProfessionalCategoryByCategoryType(CategoryType.EMPLOYEE, new LocalDate());
        }
        return professionalCategory;
    }

    public PersonContractSituation getCurrentEmployeeContractSituation() {
        return getPerson().getPersonProfessionalData() != null ? getPerson().getPersonProfessionalData()
                .getCurrentPersonContractSituationByCategoryType(CategoryType.EMPLOYEE) : null;
    }

    public boolean isActive() {
        PersonContractSituation currentEmployeeContractSituation = getCurrentEmployeeContractSituation();
        return currentEmployeeContractSituation != null;
    }

    public boolean isUnitCoordinator() {
        return getCurrentWorkingPlace().getActiveUnitCoordinator() == getPerson();
    }

    public static Integer getNextEmployeeNumber() {
        final int max = findMaxEmployeeNumber();
        return new Integer(max + 1);
    }

    private static int findMaxEmployeeNumber() {
        int max = 0;
        for (final Employee employee : RootDomainObject.getInstance().getEmployeesSet()) {
            max = Math.max(max, employee.getEmployeeNumber().intValue());
        }
        return max;
    }

    public boolean hasMultipleDepartments() {
        List<Department> departments = RootDomainObject.getInstance().getDepartments();
        int count = 0;
        final int several = 2;
        for (Department department : departments) {
            if (department.hasAssociatedPersons(getPerson())) {
                count++;
            }
        }
        return count >= several ? true : false;
    }

    @Atomic
    public void assignPermission(final Department department) {
        this.getPerson().getManageableDepartmentCredits().add(department);
        this.getPerson().addPersonRoleByRoleType(RoleType.DEPARTMENT_CREDITS_MANAGER);
        this.getPerson().addPersonRoleByRoleType(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
    }

    @Atomic
    public void removePermission(Department department) {
        if (!this.hasMultipleDepartments()) {
            this.getPerson().removeRoleByType(RoleType.DEPARTMENT_CREDITS_MANAGER);
            this.getPerson().removeRoleByType(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        }
        this.getPerson().getManageableDepartmentCredits().remove(department);
    }

}
