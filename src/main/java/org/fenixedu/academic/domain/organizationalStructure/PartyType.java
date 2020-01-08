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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

public class PartyType extends PartyType_Base {

    public PartyType(final PartyTypeEnum partyTypeEnum) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setType(partyTypeEnum);
    }

    @Override
    public void setType(final PartyTypeEnum type) {
        if (type == null) {
            throw new DomainException("error.PartyType.empty.type");
        }
        super.setType(type);
    }

    protected Collection<PartyType> getAllowedChildPartyTypes(final Boolean managedByUser) {

        final Set<PartyType> result = new HashSet<PartyType>();

        for (final ConnectionRule connectionRule : getAllowedChildConnectionRulesSet()) {
            if (managedByUser != null && connectionRule.getManagedByUser() != managedByUser.booleanValue()) {
                continue;
            }

            result.add(connectionRule.getAllowedChildPartyType());
        }

        return result;
    }

    public Collection<AccountabilityType> getAllowedAccountabilityTypesFor(final PartyType childPartyType) {
        final Set<AccountabilityType> result = new HashSet<AccountabilityType>();

        for (final ConnectionRule connectionRule : getAllowedChildConnectionRulesSet()) {
            if (connectionRule.isValid(this, childPartyType)) {
                result.add(connectionRule.getAccountabilityType());
            }
        }

        return result;
    }

    public static PartyType readPartyTypeByType(final PartyTypeEnum partyTypeEnum) {
        for (final PartyType partyType : Bennu.getInstance().getPartyTypesSet()) {
            if (partyType.getType() == partyTypeEnum) {
                return partyType;
            }
        }
        return null;
    }

    public static Set<Party> getPartiesSet(final PartyTypeEnum partyTypeEnum) {
        final PartyType partyType = readPartyTypeByType(partyTypeEnum);
        return partyType == null ? Collections.EMPTY_SET : partyType.getPartiesSet();
    }

    public void delete() {
        if (!getPartiesSet().isEmpty()) {
            throw new DomainException("error.PartyType.cannotDelete.hasAssociatedParties");
        }
        
        setRootDomainObject(null);
        getAllowedChildConnectionRulesSet().forEach(cr -> cr.delete());
        getAllowedParentConnectionRulesSet().forEach(cr -> cr.delete());
        
        super.deleteDomainObject();
    }

}
