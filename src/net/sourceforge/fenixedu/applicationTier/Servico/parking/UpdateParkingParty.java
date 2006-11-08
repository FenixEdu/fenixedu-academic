package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class UpdateParkingParty extends Service {

    public void run(ParkingRequest parkingRequest, final ParkingRequestState parkingRequestState,
            final Long cardCode, final Integer groupId, final String note, final DateTime cardStartDate,
            final DateTime cardEndDate) {
        if (parkingRequestState == ParkingRequestState.ACCEPTED) {            
            parkingRequest.getParkingParty().setCardStartDate(cardStartDate);
            parkingRequest.getParkingParty().setCardEndDate(cardEndDate);
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

            parkingRequest.deleteFirstCarFiles();
            parkingRequest.deleteSecondCarFiles();
            parkingRequest.deleteDriverLicenseFile();
            //dont copy files to parking party
            //            for (ParkingDocument parkingDocument : parkingRequest.getParkingDocuments()) {
            //                parkingDocument.setParkingParty(parkingRequest.getParkingParty());
            //            }
        }
        parkingRequest.setParkingRequestState(parkingRequestState);
        parkingRequest.setNote(note);

        String email = ((Person) parkingRequest.getParkingParty().getParty()).getEmail();

        if (note != null && note.trim().length() != 0 && email != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            List<String> to = new ArrayList<String>();
            to.add(email);
            if (EmailSender.send(bundle.getString("label.fromName"),
                    bundle.getString("label.fromAddress"), to, null, null,
                    bundle.getString("label.subject"), note).isEmpty()) {

            }
        }
    }
}