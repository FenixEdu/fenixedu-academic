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
package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class SearchPartyBean implements Serializable {

    private Party party;

    private String partyName;

    public SearchPartyBean() {

    }

    public SearchPartyBean(Party party) {
        setParty(party);
        setPartyName(party.getName());
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        if (party != null) {
            this.party = party;
        } else {
            this.party = null;
        }
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
}
