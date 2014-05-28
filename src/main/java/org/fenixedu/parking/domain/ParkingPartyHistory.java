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
package org.fenixedu.parking.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ParkingPartyHistory extends ParkingPartyHistory_Base {

    public ParkingPartyHistory(ParkingParty parkingParty, Boolean onlineRequest) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setParty(parkingParty.getParty());
        setCardStartDate(parkingParty.getCardStartDate());
        setCardEndDate(parkingParty.getCardEndDate());
        setCardNumber(parkingParty.getCardNumber());
        setParkingGroup(parkingParty.getParkingGroup());
        setPhdNumber(parkingParty.getPhdNumber());
        setNotes(parkingParty.getNotes());
        setRequestedAs(parkingParty.getRequestedAs());
        setUsedNumber(parkingParty.getUsedNumber());
        setHistoryDate(new DateTime());
        setOnlineRequest(onlineRequest);
    }

    @Deprecated
    public boolean hasNotes() {
        return getNotes() != null;
    }

    @Deprecated
    public boolean hasCardNumber() {
        return getCardNumber() != null;
    }

    @Deprecated
    public boolean hasOnlineRequest() {
        return getOnlineRequest() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRequestedAs() {
        return getRequestedAs() != null;
    }

    @Deprecated
    public boolean hasPhdNumber() {
        return getPhdNumber() != null;
    }

    @Deprecated
    public boolean hasHistoryDate() {
        return getHistoryDate() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasCardStartDate() {
        return getCardStartDate() != null;
    }

    @Deprecated
    public boolean hasUsedNumber() {
        return getUsedNumber() != null;
    }

    @Deprecated
    public boolean hasParkingGroup() {
        return getParkingGroup() != null;
    }

    @Deprecated
    public boolean hasCardEndDate() {
        return getCardEndDate() != null;
    }

}
