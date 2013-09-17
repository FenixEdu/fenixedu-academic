package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Sender;

import org.joda.time.DateTime;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class RenewParkingCards {

    @Atomic
    public static void run(List<ParkingParty> parkingParties, DateTime newEndDate, ParkingGroup newParkingGroup, String emailText) {
        check(RolePredicates.PARKING_MANAGER_PREDICATE);
        DateTime newBeginDate = new DateTime();
        for (ParkingParty parkingParty : parkingParties) {
            parkingParty.renewParkingCard(newBeginDate, newEndDate, newParkingGroup);
            ParkingRequest parkingRequest = parkingParty.getLastRequest();
            if (parkingRequest != null && parkingRequest.getParkingRequestState() == ParkingRequestState.PENDING) {
                parkingRequest.setParkingRequestState(ParkingRequestState.ACCEPTED);
                parkingRequest.setNote(emailText);
            }
            String email = null;
            EmailAddress defaultEmailAddress = parkingParty.getParty().getDefaultEmailAddress();
            if (defaultEmailAddress != null) {
                email = defaultEmailAddress.getValue();
            }

            if (emailText != null && emailText.trim().length() != 0 && email != null) {
                ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", Language.getLocale());
                Sender sender = RootDomainObject.getInstance().getSystemSender();
                ConcreteReplyTo replyTo = new ConcreteReplyTo(bundle.getString("label.fromAddress"));
                new Message(sender, replyTo.asCollection(), Collections.EMPTY_LIST, bundle.getString("label.subject"), emailText,
                        email);
            }
        }
    }
}