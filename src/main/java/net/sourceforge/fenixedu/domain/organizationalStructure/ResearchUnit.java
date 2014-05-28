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
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.email.ResearchUnitBasedSender;
import net.sourceforge.fenixedu.domain.util.email.UnitBasedSender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

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
    public boolean isResearchUnit() {
        return true;
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    private static void checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(ResearchUnit researchUnit) {
        PartyType type = PartyType.readPartyTypeByType(PartyTypeEnum.RESEARCH_UNIT);
        for (Party party : type.getParties()) {
            ResearchUnit unit = (ResearchUnit) party;
            if (!unit.equals(researchUnit) && unit.isResearchUnit() && researchUnit.getName().equalsIgnoreCase(unit.getName())
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

    @Override
    public Collection<Person> getPossibleGroupMembers() {
        HashSet<Person> people = (HashSet<Person>) super.getPossibleGroupMembers();
        YearMonthDay today = new YearMonthDay();
        for (Accountability accountability : getChildsSet()) {
            if (accountability instanceof ResearchContract
                    && (accountability.getEndDate() == null || accountability.getEndDate().isAfter(today))) {
                people.add(((ResearchContract) accountability).getPerson());
            }
        }
        return people;
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
        return getPublicationCollaborators().contains(person);
    }

    public boolean isCurrentUserAbleToInsertOthersPublications() {
        return isUserAbleToInsertOthersPublications(AccessControl.getPerson());
    }

    public void setPublicationCollaborators(List<Person> collaborators) {
        getPublicationCollaborators().clear();
        getPublicationCollaborators().addAll(collaborators);
    }

    @Override
    protected ResearchUnitSite createSite() {
        return new ResearchUnitSite(this);
    }

    @Override
    @Atomic
    public UnitBasedSender getOneUnitBasedSender() {
        if (hasAnyUnitBasedSender()) {
            return getUnitBasedSender().iterator().next();
        } else {
            return ResearchUnitBasedSender.newInstance(this);
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getPublicationCollaborators() {
        return getPublicationCollaboratorsSet();
    }

    @Deprecated
    public boolean hasAnyPublicationCollaborators() {
        return !getPublicationCollaboratorsSet().isEmpty();
    }

}
