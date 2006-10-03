package net.sourceforge.fenixedu.dataTransferObject.parking;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class SearchPartyBean implements Serializable {    

    private DomainReference<Party> party;

    private String partyName;
    
    private String carPlateNumber;

    public SearchPartyBean() {

    }

    public SearchPartyBean(Party party, String carPlateNumber) {
        setParty(party);
        setPartyName(party.getName());
        setCarPlateNumber(carPlateNumber);
    }

    public Party getParty() {
        return party == null ? null : party.getObject();
    }

    public void setParty(Party party) {
        if (party != null) {
            this.party = new DomainReference<Party>(party);
        }else {
            this.party = null;
        }
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

}
