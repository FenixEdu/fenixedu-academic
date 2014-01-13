package net.sourceforge.fenixedu.domain.parking;

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
                new ParkingFile(getParkingFile().getFilename(), getParkingFile().getFilename(), getParkingFile().getContents(),
                        getParkingFile().getPermittedGroup());
        return new NewParkingDocument(getParkingDocumentType(), parkingFile, vehicle);
    }

    @Deprecated
    public boolean hasParkingFile() {
        return getParkingFile() != null;
    }

    @Deprecated
    public boolean hasParkingParty() {
        return getParkingParty() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasParkingRequest() {
        return getParkingRequest() != null;
    }

    @Deprecated
    public boolean hasParkingDocumentType() {
        return getParkingDocumentType() != null;
    }

    @Deprecated
    public boolean hasVehicle() {
        return getVehicle() != null;
    }

}
