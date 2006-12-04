package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class NewParkingDocument extends NewParkingDocument_Base {

    public NewParkingDocument() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public NewParkingDocument(NewParkingDocumentType parkingDocumentType, ParkingFile parkingFile,
            Vehicle vehicle) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParkingDocumentType(parkingDocumentType);
        setParkingFile(parkingFile);
        setVehicle(vehicle);
    }

    public NewParkingDocument(NewParkingDocumentType parkingDocumentType, ParkingFile parkingFile,
            ParkingRequest parkingRequest) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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

}
