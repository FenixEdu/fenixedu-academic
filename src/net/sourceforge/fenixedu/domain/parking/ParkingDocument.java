package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.RootDomainObject;


public class ParkingDocument extends ParkingDocument_Base {
    
    public ParkingDocument(ParkingDocumentType parkingDocumentType, ParkingFile parkingFile, ParkingRequest parkingRequest) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParkingDocumentType(parkingDocumentType);
        setParkingRequest(parkingRequest);
        setParkingFile(parkingFile);
    }


    public void delete() {
        setRootDomainObject(null);
        setParkingRequest(null);
        getParkingFile().delete();
        deleteDomainObject();
    }
}
