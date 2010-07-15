package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AdministrativeOfficeUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.SpecialSeasonRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ConcludeAcademicServiceRequest extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final AcademicServiceRequest academicServiceRequest, final Boolean sendEmailToStudent,
	    final YearMonthDay situationDate, final String justification) {
	if (!academicServiceRequest.isConcluded()) {
	    if (situationDate == null) {
		academicServiceRequest.conclude();
	    } else {
		academicServiceRequest.conclude(situationDate, justification);
	    }

	    if (sendEmailToStudent != null && sendEmailToStudent.booleanValue()) {
		sendEmail(academicServiceRequest);
	    }
	}
    }

    private static void sendEmail(final AcademicServiceRequest academicServiceRequest) {

	final ResourceBundle appBundle = ResourceBundle.getBundle("resources.ApplicationResources");

	String body = appBundle.getString("mail.academicServiceRequest.concluded.message1");
	body += " " + academicServiceRequest.getServiceRequestNumberYear();
	body += " " + appBundle.getString("mail.academicServiceRequest.concluded.message2");
	body += " '" + academicServiceRequest.getDescription();
	body += "' " + appBundle.getString("mail.academicServiceRequest.concluded.message3");

	if (academicServiceRequest.getAcademicServiceRequestType() == AcademicServiceRequestType.SPECIAL_SEASON_REQUEST) {
	    if (((SpecialSeasonRequest) academicServiceRequest).getDeferred() == null)
		throw new DomainException("special.season.request.deferment.cant.be.null");
	    if (((SpecialSeasonRequest) academicServiceRequest).getDeferred() == true) {
		body += "\n" + appBundle.getString("mail.academicServiceRequest.concluded.messageSSR4A");
	    } else {
		body += "\n" + appBundle.getString("mail.academicServiceRequest.concluded.messageSSR4B");
	    }
	    body += "\n\n" + appBundle.getString("mail.academicServiceRequest.concluded.messageSSR5");
	} else {
	    
	    body += "\n\n" + appBundle.getString("mail.academicServiceRequest.concluded.message4");
	    
	}

	final Sender sender = AdministrativeOfficeUnit.getGraduationUnit().getUnitBasedSender().get(0);
	final Recipient recipient = new Recipient(new PersonGroup(academicServiceRequest.getPerson()));
	new Message(sender, sender.getReplyTos(), recipient.asCollection(), academicServiceRequest.getDescription(), body, "");
    }
}