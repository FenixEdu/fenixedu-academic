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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.accessControl.ManagersOfUnitSiteGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonsInFunctionGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ScientificCouncilUnit extends ScientificCouncilUnit_Base {

    private static final String COORDINATION_COMMITTEE_NAME = "Comissão Executiva";
    private static final String COORDINATION_COMMITTEE_MEMBER_NAME = "Membro";

    public ScientificCouncilUnit() {
        super();
        super.setType(PartyTypeEnum.SCIENTIFIC_COUNCIL);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    @Override
    protected List<Group> getDefaultGroups() {
        List<Group> groups = super.getDefaultGroups();

        groups.add(RoleGroup.get(RoleType.SCIENTIFIC_COUNCIL));
        groups.add(ManagersOfUnitSiteGroup.get(getSite()));

        Function function = getCoordinationCommitteeMembersFunction();
        if (function != null) {
            groups.add(PersonsInFunctionGroup.get(function));
        }

        return groups;
    }

    private Function getCoordinationCommitteeMembersFunction() {
        Unit subUnit = getSubUnitWithName(COORDINATION_COMMITTEE_NAME);

        if (subUnit == null) {
            return null;
        } else {
            return getFunctionWithName(subUnit, COORDINATION_COMMITTEE_MEMBER_NAME);
        }
    }

    private Unit getSubUnitWithName(String name) {
        for (Unit sub : getSubUnits()) {
            if (sub.getName().equals(name)) {
                return sub;
            }
        }

        return null;
    }

    private Function getFunctionWithName(Unit unit, String name) {
        for (Function function : unit.getFunctions()) {
            if (function.getName().equals(name)) {
                return function;
            }
        }

        return null;
    }

    @Override
    public Collection<Person> getPossibleGroupMembers() {
        return Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL).getAssociatedPersonsSet();
    }

    @Override
    protected ScientificCouncilSite createSite() {
        return new ScientificCouncilSite(this);
    }

    public static ScientificCouncilUnit getScientificCouncilUnit() {
        final Set<Party> parties = PartyType.getPartiesSet(PartyTypeEnum.SCIENTIFIC_COUNCIL);
        return parties.isEmpty() ? null : (ScientificCouncilUnit) parties.iterator().next();
    }

    public static ScientificCouncilUnit createScientificCouncilUnit(MultiLanguageString name, String unitNameCard,
            Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
            AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
            Boolean canBeResponsibleOfSpaces, Space campus) {
        ScientificCouncilUnit scu = new ScientificCouncilUnit();
        scu.init(name, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification, null,
                canBeResponsibleOfSpaces, campus);
        scu.addParentUnit(parentUnit, accountabilityType);
        return scu;
    }

}
