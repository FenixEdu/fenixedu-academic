/**
 * Copyright © 2014 Instituto Superior Técnico
 *
 * This file is part of Fenix Parking.
 *
 * Fenix Parking is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fenix Parking is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.parking.dto;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class SearchParkingPartyBean implements Serializable {

    private Party party;

    private String partyName;

    private String carPlateNumber;

    private Long parkingCardNumber;

    public SearchParkingPartyBean() {

    }

    public SearchParkingPartyBean(Party party, String carPlateNumber, Long parkingCardNumber) {
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
