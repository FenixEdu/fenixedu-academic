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

public class NewParkingDocument extends NewParkingDocument_Base {

    public NewParkingDocument() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public NewParkingDocument(NewParkingDocumentType parkingDocumentType, ParkingFile parkingFile, Vehicle vehicle) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setParkingDocumentType(parkingDocumentType);
        setParkingFile(parkingFile);
        setVehicle(vehicle);
    }

    public NewParkingDocument(NewParkingDocumentType parkingDocumentType, ParkingFile parkingFile, ParkingRequest parkingRequest) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setParkingDocumentType(parkingDocumentType);
        setParkingFile(parkingFile);
        setParkingRequest(parkingRequest);
    }

    public void delete() {
        setRootDomainObject(null);
        setParkingParty(null);
        setParkingRequest(null);
        setVehicle(null);
        getParkingFile().delete();
        deleteDomainObject();
    }

    public NewParkingDocument copyParkingDocument(final Vehicle vehicle) {
        final ParkingFile parkingFile =
                new ParkingFile(getParkingFile().getFilename(), getParkingFile().getFilename(), getParkingFile().getContent(),
                        getParkingFile().getPermittedGroup());
        return new NewParkingDocument(getParkingDocumentType(), parkingFile, vehicle);
    }
}
