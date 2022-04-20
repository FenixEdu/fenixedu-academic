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

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;

public class ConnectionRule extends ConnectionRule_Base {

    public ConnectionRule() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ConnectionRule(PartyType allowedParentPartyType, PartyType allowedChildPartyType,
            AccountabilityType accountabilityType) {
        this();
        setAllowedParentPartyType(allowedParentPartyType);
        setAllowedChildPartyType(allowedChildPartyType);
        setAccountabilityType(accountabilityType);
    }

    public void delete() {
        setAccountabilityType(null);
        setAllowedChildPartyType(null);
        setAllowedParentPartyType(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean isValid(PartyType parentType, PartyType childType) {
        return getAllowedParentPartyType() == parentType && getAllowedChildPartyType() == childType;
    }

    public static Stream<ConnectionRule> find(PartyType parentType, PartyType childType, AccountabilityType accountabilityType) {
        return Bennu.getInstance().getConnectionRulesSet().stream().filter(cr -> cr.getAllowedParentPartyType() == parentType
                && cr.getAllowedChildPartyType() == childType && cr.getAccountabilityType() == accountabilityType);
    }

}
