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
