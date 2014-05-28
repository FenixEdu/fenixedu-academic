/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
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
        setRootDomainObject(Bennu.getInstance());
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
        for (final Employee employee : Bennu.getInstance().getEmployeesSet()) {
            if (employee.getEmployeeNumber().equals(employeeNumber)) {
                return employee;
            }
        }
        return null;
    }

    public Space getCurrentCampus() {
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
        for (final Employee employee : Bennu.getInstance().getEmployeesSet()) {
            max = Math.max(max, employee.getEmployeeNumber().intValue());
        }
        return max;
    }

    public boolean hasMultipleDepartments() {
        Collection<Department> departments = Bennu.getInstance().getDepartmentsSet();
        int count = 0;
        final int several = 2;
        for (Department department : departments) {
            if (department.getAssociatedPersonsSet().contains(getPerson())) {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion> getMasterDegreeThesisDataVersions() {
        return getMasterDegreeThesisDataVersionsSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeThesisDataVersions() {
        return !getMasterDegreeThesisDataVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeProofVersion> getMasterDegreeProofVersions() {
        return getMasterDegreeProofVersionsSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeProofVersions() {
        return !getMasterDegreeProofVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GratuitySituation> getPenaltyExemptionGratuitySituations() {
        return getPenaltyExemptionGratuitySituationsSet();
    }

    @Deprecated
    public boolean hasAnyPenaltyExemptionGratuitySituations() {
        return !getPenaltyExemptionGratuitySituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EmployeeHistoric> getHistoricList() {
        return getHistoricListSet();
    }

    @Deprecated
    public boolean hasAnyHistoricList() {
        return !getHistoricListSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation> getReimbursementGuideSituations() {
        return getReimbursementGuideSituationsSet();
    }

    @Deprecated
    public boolean hasAnyReimbursementGuideSituations() {
        return !getReimbursementGuideSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GratuitySituation> getGratuitySituations() {
        return getGratuitySituationsSet();
    }

    @Deprecated
    public boolean hasAnyGratuitySituations() {
        return !getGratuitySituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EmployeeHistoric> getResponsibleEmployeeAssociatedEmployeeHistorics() {
        return getResponsibleEmployeeAssociatedEmployeeHistoricsSet();
    }

    @Deprecated
    public boolean hasAnyResponsibleEmployeeAssociatedEmployeeHistorics() {
        return !getResponsibleEmployeeAssociatedEmployeeHistoricsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.StudentCurricularPlan> getStudentCurricularPlans() {
        return getStudentCurricularPlansSet();
    }

    @Deprecated
    public boolean hasAnyStudentCurricularPlans() {
        return !getStudentCurricularPlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GratuityValues> getGratuityValues() {
        return getGratuityValuesSet();
    }

    @Deprecated
    public boolean hasAnyGratuityValues() {
        return !getGratuityValuesSet().isEmpty();
    }

    @Deprecated
    public boolean hasEmployeeNumber() {
        return getEmployeeNumber() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWorkingHours() {
        return getWorkingHours() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
