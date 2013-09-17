package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class NewParkingDocument extends NewParkingDocument_Base {

    public NewParkingDocument() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public NewParkingDocument(NewParkingDocumentType parkingDocumentType, ParkingFile parkingFile, Vehicle vehicle) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParkingDocumentType(parkingDocumentType);
        setParkingFile(parkingFile);
        setVehicle(vehicle);
    }

    public NewParkingDocument(NewParkingDocumentType parkingDocumentType, ParkingFile parkingFile, ParkingRequest parkingRequest) {
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

    public NewParkingDocument copyParkingDocument(final Vehicle vehicle) {
        final ParkingFile parkingFile =
                new ParkingFile(getFilePath(vehicle), getParkingFile().getFilename(), getParkingFile().getFilename(),
                        getParkingFile().getContents(), getParkingFile().getPermittedGroup());
        return new NewParkingDocument(getParkingDocumentType(), parkingFile, vehicle);
    }

    protected VirtualPath getFilePath(final Vehicle vehicle) {
        Party party = vehicle.getParkingParty().getParty();
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("ParkingFiles", "Parking Files"));
        filePath.addNode(new VirtualPathNode("Party" + party.getExternalId(), party.getName()));
        filePath.addNode(new VirtualPathNode("V" + vehicle.getExternalId(), "Vehicle ID"));
        return filePath;
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
    public boolean hasRootDomainObject() {
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
