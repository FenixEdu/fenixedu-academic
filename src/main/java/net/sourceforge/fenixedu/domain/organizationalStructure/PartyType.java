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

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule> getAllowedChildConnectionRules() {
        return getAllowedChildConnectionRulesSet();
    }

    @Deprecated
    public boolean hasAnyAllowedChildConnectionRules() {
        return !getAllowedChildConnectionRulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule> getAllowedParentConnectionRules() {
        return getAllowedParentConnectionRulesSet();
    }

    @Deprecated
    public boolean hasAnyAllowedParentConnectionRules() {
        return !getAllowedParentConnectionRulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Party> getParties() {
        return getPartiesSet();
    }

    @Deprecated
    public boolean hasAnyParties() {
        return !getPartiesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

}
