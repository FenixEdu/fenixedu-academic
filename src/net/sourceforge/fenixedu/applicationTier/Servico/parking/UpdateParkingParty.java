package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.parking.ParkingDocument;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;

public class UpdateParkingParty extends Service {

    public void run(final Integer parkingRequestID, final ParkingRequestState parkingRequestState,
            final Integer cardCode, final Integer groupId, final String note) {
        ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(parkingRequestID);
        if (parkingRequestState == ParkingRequestState.ACCEPTED) {
            parkingRequest.getParkingParty().setAuthorized(true);
            parkingRequest.getParkingParty().setCardNumber(cardCode);
            parkingRequest.getParkingParty().setDriverLicenseDocumentState(
                    parkingRequest.getDriverLicenseDocumentState());

            parkingRequest.getParkingParty().setFirstCarMake(parkingRequest.getFirstCarMake());
            parkingRequest.getParkingParty().setFirstCarPlateNumber(
                    parkingRequest.getFirstCarPlateNumber());
            parkingRequest.getParkingParty().setFirstCarDeclarationDocumentState(
                    parkingRequest.getFirstCarDeclarationDocumentState());
            parkingRequest.getParkingParty().setFirstCarInsuranceDocumentState(
                    parkingRequest.getFirstCarInsuranceDocumentState());
            parkingRequest.getParkingParty().setFirstCarOwnerIdDocumentState(
                    parkingRequest.getFirstCarOwnerIdDocumentState());
            parkingRequest.getParkingParty().setFirstCarPropertyRegistryDocumentState(
                    parkingRequest.getFirstCarPropertyRegistryDocumentState());

            parkingRequest.getParkingParty().setSecondCarMake(parkingRequest.getSecondCarMake());
            parkingRequest.getParkingParty().setSecondCarPlateNumber(
                    parkingRequest.getSecondCarPlateNumber());
            parkingRequest.getParkingParty().setSecondCarDeclarationDocumentState(
                    parkingRequest.getSecondCarDeclarationDocumentState());
            parkingRequest.getParkingParty().setSecondCarInsuranceDocumentState(
                    parkingRequest.getSecondCarInsuranceDocumentState());
            parkingRequest.getParkingParty().setSecondCarOwnerIdDocumentState(
                    parkingRequest.getSecondCarOwnerIdDocumentState());
            parkingRequest.getParkingParty().setSecondCarPropertyRegistryDocumentState(
                    parkingRequest.getSecondCarPropertyRegistryDocumentState());

            ParkingGroup parkingGroup = rootDomainObject.readParkingGroupByOID(groupId);
            parkingRequest.getParkingParty().setParkingGroup(parkingGroup);
            for (ParkingDocument parkingDocument : parkingRequest.getParkingDocuments()) {
                parkingDocument.setParkingParty(parkingRequest.getParkingParty());
            }
        }
        parkingRequest.setParkingRequestState(parkingRequestState);
        parkingRequest.setNote(note);
    }
}