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

import java.util.Collection;

import org.fenixedu.bennu.core.domain.Bennu;

public class ParkingGroup extends ParkingGroup_Base {

    public ParkingGroup(String groupName) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGroupName(groupName);
    }

    public static Collection<ParkingGroup> getAll() {
        return Bennu.getInstance().getParkingGroupsSet();
    }

    @Deprecated
    public java.util.Set<ParkingParty> getParkingParties() {
        return getParkingPartiesSet();
    }

    @Deprecated
    public boolean hasAnyParkingParties() {
        return !getParkingPartiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<ParkingPartyHistory> getParkingPartyHistories() {
        return getParkingPartyHistoriesSet();
    }

    @Deprecated
    public boolean hasAnyParkingPartyHistories() {
        return !getParkingPartyHistoriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGroupName() {
        return getGroupName() != null;
    }

    @Deprecated
    public boolean hasGroupDescription() {
        return getGroupDescription() != null;
    }

}
