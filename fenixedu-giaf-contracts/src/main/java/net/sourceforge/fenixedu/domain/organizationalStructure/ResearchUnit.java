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
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.accessControl.UnitGroup;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.util.email.ResearchUnitBasedSender;
import org.fenixedu.academic.domain.util.email.UnitBasedSender;
import org.fenixedu.academic.predicate.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ResearchUnit extends ResearchUnit_Base {

    private ResearchUnit() {
        super();
        super.setType(PartyTypeEnum.RESEARCH_UNIT);
    }

    public static ResearchUnit createNewResearchUnit(MultiLanguageString name, String unitNameCard, Integer costCenterCode,
            String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType,
            String webAddress, UnitClassification classification, Boolean canBeResponsibleOfSpaces, Space campus) {

        ResearchUnit researchUnit = new ResearchUnit();
        researchUnit.init(name, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification, null,
                canBeResponsibleOfSpaces, campus);
        researchUnit.addParentUnit(parentUnit, accountabilityType);

        checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(researchUnit);

        return researchUnit;
    }

    @Override
    public void setAcronym(String acronym) {
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("acronym.cannot.be.null");
        }
        super.setAcronym(acronym);
    }

    @Override
    public void edit(MultiLanguageString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Space campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);

        checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(this);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    private static void checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(ResearchUnit researchUnit) {
        PartyType type = PartyType.readPartyTypeByType(PartyTypeEnum.RESEARCH_UNIT);
        for (Party party : type.getPartiesSet()) {
            ResearchUnit unit = (ResearchUnit) party;
            if (!unit.equals(researchUnit) && unit instanceof ResearchUnit
                    && researchUnit.getName().equalsIgnoreCase(unit.getName())
                    && researchUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {
                throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
            }
        }

    }

    public Collection<Accountability> getResearchContracts() {
        return (Collection<Accountability>) getChildAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
    }

    public Collection<Person> getActivePeopleForContract(Class clazz) {
        AccountabilityType accountabilityType = Function.readByType(AccountabilityTypeEnum.RESEARCH_CONTRACT);
        YearMonthDay today = new YearMonthDay();
        List<Person> people = new ArrayList<Person>();
        for (Accountability accountability : getChildsSet()) {
            if (accountability.getAccountabilityType().equals(accountabilityType)
                    && (accountability.getEndDate() == null || accountability.getEndDate().isAfter(today))
                    && clazz.isAssignableFrom(accountability.getClass())) {
                people.add((Person) accountability.getChildParty());
            }
        }
        return people;
    }

    public Collection<Accountability> getActiveResearchContracts(Class clazz) {
        AccountabilityType accountabilityType = Function.readByType(AccountabilityTypeEnum.RESEARCH_CONTRACT);
        YearMonthDay today = new YearMonthDay();
        List<Accountability> accountabilities = new ArrayList<Accountability>();
        for (Accountability accountability : getChildsSet()) {
            if (accountability.getAccountabilityType().equals(accountabilityType)
                    && (accountability.getEndDate() == null || accountability.getEndDate().isAfter(today))
                    && clazz.isAssignableFrom(accountability.getClass())) {
                accountabilities.add(accountability);
            }
        }
        return accountabilities;
    }

    public Collection<Unit> getAllCurrentActiveSubUnits() {
        return this.getAllActiveSubUnits(new YearMonthDay(), AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
    }

    public Collection<Unit> getAllCurrentActiveSubUnitsOrdered() {
        SortedSet<Unit> subUnits = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        subUnits.addAll(getAllCurrentActiveSubUnits());
        return subUnits;
    }

    public Collection<Person> getResearchers() {
        return getActivePeopleForContract(ResearcherContract.class);
    }

    public Collection<Accountability> getResearcherContracts() {
        return getActiveResearchContracts(ResearcherContract.class);
    }

    public Collection<Person> getTechnicalStaff() {
        return getActivePeopleForContract(ResearchTechnicalStaffContract.class);
    }

    public Collection<Accountability> getTechnicalStaffContracts() {
        return getActiveResearchContracts(ResearchTechnicalStaffContract.class);
    }

    public Collection<Person> getScholarships() {
        return getActivePeopleForContract(ResearchScholarshipContract.class);
    }

    public Collection<Accountability> getScholarshipContracts() {
        return getActiveResearchContracts(ResearchScholarshipContract.class);
    }

    public Collection<Person> getInternships() {
        return getActivePeopleForContract(ResearchInternshipContract.class);
    }

    public Collection<Accountability> getInternshipContracts() {
        return getActiveResearchContracts(ResearchInternshipContract.class);
    }

    @Override
    public List<Group> getDefaultGroups() {
        List<Group> groups = super.getDefaultGroups();
        groups.add(UnitGroup.get(this, AccountabilityTypeEnum.RESEARCH_CONTRACT, false));
        return groups;
    }

    public boolean isUserAbleToInsertOthersPublications(Person person) {
        return getPublicationCollaboratorsSet().contains(person);
    }

    public boolean isCurrentUserAbleToInsertOthersPublications() {
        return isUserAbleToInsertOthersPublications(AccessControl.getPerson());
    }

    public void setPublicationCollaborators(List<Person> collaborators) {
        getPublicationCollaboratorsSet().clear();
        getPublicationCollaboratorsSet().addAll(collaborators);
    }

    @Override
    @Atomic
    public UnitBasedSender getOneUnitBasedSender() {
        if (!getUnitBasedSenderSet().isEmpty()) {
            return getUnitBasedSenderSet().iterator().next();
        } else {
            return ResearchUnitBasedSender.newInstance(this);
        }
    }

    public static List<ResearchUnit> getWorkingResearchUnits(Person person) {
        final List<ResearchUnit> units = new ArrayList<ResearchUnit>();
        final Collection<? extends Accountability> parentAccountabilities =
                person.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);

        final YearMonthDay currentDate = new YearMonthDay();
        for (final Accountability accountability : parentAccountabilities) {
            if (accountability.isActive(currentDate)) {
                units.add((ResearchUnit) accountability.getParentParty());
            }
        }

        return units;
    }

    public static List<ResearchUnit> getWorkingResearchUnitsAndParents(Person person) {
        final Set<ResearchUnit> baseUnits = new HashSet<ResearchUnit>();
        for (final ResearchUnit unit : ResearchUnit.getWorkingResearchUnits(person)) {
            baseUnits.add(unit);
            for (final Unit parentUnit : unit.getAllActiveParentUnits(new YearMonthDay())) {
                if (parentUnit instanceof ResearchUnit) {
                    baseUnits.add((ResearchUnit) parentUnit);
                }
            }
        }
        return new ArrayList<ResearchUnit>(baseUnits);
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = getAllWorkingEmployees();
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && teacher.isActiveContractedTeacher()) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public List<Employee> getAllWorkingEmployees() {
        Set<Employee> employees = new HashSet<Employee>();
        for (Contract contract : EmployeeContract.getWorkingContracts(this)) {
            employees.add(contract.getEmployee());
        }
        for (Unit subUnit : getSubUnits()) {
            employees.addAll(((ResearchUnit) subUnit).getAllWorkingEmployees());
        }
        return new ArrayList<Employee>(employees);
    }

}
