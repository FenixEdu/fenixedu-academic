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

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PedagogicalCouncilSite;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.DelegatesGroup;
import net.sourceforge.fenixedu.domain.accessControl.ManagersOfUnitSiteGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.joda.time.YearMonthDay;

public class PedagogicalCouncilUnit extends PedagogicalCouncilUnit_Base {

    protected PedagogicalCouncilUnit() {
        super();
        super.setType(PartyTypeEnum.PEDAGOGICAL_COUNCIL);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    @Override
    protected List<Group> getDefaultGroups() {
        List<Group> groups = super.getDefaultGroups();

        groups.add(RoleGroup.get(RoleType.PEDAGOGICAL_COUNCIL));
        groups.add(ManagersOfUnitSiteGroup.get(getSite()));

        /* For sending mail to all degrees delegates */
        groups.add(DelegatesGroup.get(FunctionType.DELEGATE_OF_GGAE));
        groups.add(DelegatesGroup.get(FunctionType.DELEGATE_OF_YEAR));
        groups.add(DelegatesGroup.get(FunctionType.DELEGATE_OF_DEGREE));
        groups.add(DelegatesGroup.get(FunctionType.DELEGATE_OF_MASTER_DEGREE));
        groups.add(DelegatesGroup.get(FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE));

        /*
         * For sending mail to selected degrees delegates IS THIS REALLY
         * NECESSARY? IT CREATES A ENOURMOUS LIST OF GROUPS TO CHOOSE FROM
         */
        /*
         * groups.addAll(Degree.getDegreesDelegatesGroupByDegreeType(DegreeType.
         * BOLONHA_DEGREE ));
         * groups.addAll(Degree.getDegreesDelegatesGroupByDegreeType(DegreeType
         * .BOLONHA_MASTER_DEGREE));
         * groups.addAll(Degree.getDegreesDelegatesGroupByDegreeType
         * (DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
         */

        return groups;
    }

    @Override
    public Collection<Person> getPossibleGroupMembers() {
        return Role.getRoleByRoleType(RoleType.PEDAGOGICAL_COUNCIL).getAssociatedPersonsSet();
    }

    @Override
    protected PedagogicalCouncilSite createSite() {
        return new PedagogicalCouncilSite(this);
    }

    // TODO: controlo de acesso?
    public void addDelegatePersonFunction(Person person, Function delegateFunction) {
        YearMonthDay currentDate = new YearMonthDay();

        // The following restriction tries to guarantee that a new delegate is
        // elected before this person function ends
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        YearMonthDay endDate = currentExecutionYear.getEndDateYearMonthDay().plusYears(1);

        /* Check if there is another active person function with this type */
        if (delegateFunction != null) {
            List<PersonFunction> delegateFunctions = delegateFunction.getActivePersonFunctions();
            if (!delegateFunctions.isEmpty()) {
                for (PersonFunction personFunction : delegateFunctions) {
                    if (personFunction.getBeginDate().equals(currentDate)) {
                        if (personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
                            personFunction.getDelegate().delete();
                        } else {
                            personFunction.delete();
                        }
                    } else {
                        personFunction.setOccupationInterval(personFunction.getBeginDate(), currentDate.minusDays(1));
                    }
                }
            }
        }

        PersonFunction.createDelegatePersonFunction(this, person, currentDate, endDate, delegateFunction);
    }

    // TODO: controlo de acesso?
    public void removeActiveDelegatePersonFunctionFromPersonByFunction(Person person, Function function) {
        YearMonthDay today = new YearMonthDay();
        List<PersonFunction> delegatesFunctions = function.getActivePersonFunctions();
        if (!delegatesFunctions.isEmpty()) {
            for (PersonFunction personfunction : delegatesFunctions) {
                Person delegate = personfunction.getPerson();
                if (delegate.equals(person)) {
                    if (personfunction.getBeginDate().equals(today)) {
                        if (personfunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
                            personfunction.getDelegate().delete();
                        } else {
                            personfunction.delete();
                        }
                    } else {
                        personfunction.setOccupationInterval(personfunction.getBeginDate(), today.minusDays(1));
                    }
                }
            }
        }
    }

    public static PedagogicalCouncilUnit getPedagogicalCouncilUnit() {
        final Set<Party> parties = PartyType.getPartiesSet(PartyTypeEnum.PEDAGOGICAL_COUNCIL);
        return parties.isEmpty() ? null : (PedagogicalCouncilUnit) parties.iterator().next();
    }

}
