/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.groups.Group;

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

        groups.add(RoleType.PEDAGOGICAL_COUNCIL.actualGroup());

        return groups;
    }

    public static PedagogicalCouncilUnit getPedagogicalCouncilUnit() {
        final Set<Party> parties = PartyType.getPartiesSet(PartyTypeEnum.PEDAGOGICAL_COUNCIL);
        return parties.isEmpty() ? null : (PedagogicalCouncilUnit) parties.iterator().next();
    }

}
