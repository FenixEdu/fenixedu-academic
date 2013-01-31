package net.sourceforge.fenixedu.dataTransferObject.parking;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class SearchPartyBean implements Serializable {

	private Party party;

	private String partyName;

	private String carPlateNumber;

	private Long parkingCardNumber;

	public SearchPartyBean() {

	}

	public SearchPartyBean(Party party, String carPlateNumber, Long parkingCardNumber) {
		setParty(party);
		setPartyName(party.getName());
		setCarPlateNumber(carPlateNumber);
		setParkingCardNumber(parkingCardNumber);
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

	public String getCarPlateNumber() {
		return carPlateNumber;
	}

	public void setCarPlateNumber(String carPlateNumber) {
		this.carPlateNumber = carPlateNumber;
	}

	public Long getParkingCardNumber() {
		return parkingCardNumber;
	}

	public void setParkingCardNumber(Long parkingCardNumber) {
		this.parkingCardNumber = parkingCardNumber;
	}

}
