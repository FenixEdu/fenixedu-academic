/**
 * Oct 4, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.parking.ParkingDocument;
import net.sourceforge.fenixedu.domain.parking.ParkingDocumentType;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

/**
 * @author Ricardo Rodrigues
 *
 */

public class DeleteParkingPartyCar extends Service {

    public void run(Integer parkingPartyID, boolean deleteFirstCar, boolean deleteSecondCar) {

        if (deleteFirstCar && deleteSecondCar) {
            throw new DomainException("error.parkingParty.no.cars");
        }

        ParkingParty parkingParty = rootDomainObject.readParkingPartyByOID(parkingPartyID);

        if (!parkingParty.getHasSecondCar() && deleteFirstCar) {
            throw new DomainException("error.parkingParty.no.cars");
        }

        if (deleteFirstCar) {
            moveSecondCarToFirstCar(parkingParty);
            parkingParty.deleteSecondCar();
        } else if (deleteSecondCar) {
            parkingParty.deleteSecondCar();
        }
    }

    private void moveSecondCarToFirstCar(ParkingParty parkingParty) {
        parkingParty.setFirstCarMake(parkingParty.getSecondCarMake());
        parkingParty.setFirstCarPlateNumber(parkingParty.getSecondCarPlateNumber());
        parkingParty.setFirstCarDeclarationDocumentState(parkingParty
                .getSecondCarDeclarationDocumentState());
        parkingParty
                .setFirstCarInsuranceDocumentState(parkingParty.getSecondCarInsuranceDocumentState());
        parkingParty.setFirstCarOwnerIdDocumentState(parkingParty.getSecondCarOwnerIdDocumentState());
        parkingParty.setFirstCarPropertyRegistryDocumentState(parkingParty
                .getSecondCarPropertyRegistryDocumentState());        
        
        deleteFirstCarFile(parkingParty,ParkingDocumentType.FIRST_CAR_INSURANCE);
        ParkingDocument parkingDocument = parkingParty
                .getParkingDocument(ParkingDocumentType.SECOND_CAR_INSURANCE);
        if (parkingDocument != null) {            
            parkingDocument.setParkingDocumentType(ParkingDocumentType.FIRST_CAR_INSURANCE);            
        }
        deleteFirstCarFile(parkingParty,ParkingDocumentType.FIRST_CAR_OWNER_ID);
        parkingDocument = parkingParty.getParkingDocument(ParkingDocumentType.SECOND_CAR_OWNER_ID);
        if (parkingDocument != null) {
            parkingDocument.setParkingDocumentType(ParkingDocumentType.FIRST_CAR_OWNER_ID);
        }
        deleteFirstCarFile(parkingParty,ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER);
        parkingDocument = parkingParty.getParkingDocument(ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER);
        if (parkingDocument != null) {
            parkingDocument.setParkingDocumentType(ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER);
        }
        deleteFirstCarFile(parkingParty,ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION);
        parkingDocument = parkingParty.getParkingDocument(ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION);
        if (parkingDocument != null) {
            parkingDocument.setParkingDocumentType(ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION);
        }
    }

    private void deleteFirstCarFile(ParkingParty parkingParty, ParkingDocumentType documentType) {
        ParkingDocument parkingDocument = parkingParty.getParkingDocument(documentType);
        if (parkingDocument != null) {
            String externalIdentifier = parkingDocument.getParkingFile()
                    .getExternalStorageIdentification();
            parkingDocument.delete();
            if (externalIdentifier != null) {
                FileManagerFactory.getFileManager().deleteFile(externalIdentifier);
            }
        }        
    }
}
