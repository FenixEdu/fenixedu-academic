package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.util.EmailAddressList;

import org.joda.time.YearMonthDay;

public class ConcludeAcademicServiceRequest extends Service {

    public void run(final AcademicServiceRequest academicServiceRequest, final Boolean sendEmailToStudent,
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

    private void sendEmail(final AcademicServiceRequest academicServiceRequest) {

	final ResourceBundle globalBundle = ResourceBundle.getBundle("resources.GlobalResources");
	final ResourceBundle appBundle = ResourceBundle.getBundle("resources.ApplicationResources");

	final Email email = new Email();

	email.setFromName(globalBundle.getString("degreeAdminOffice.name"));
	email.setFromAddress(globalBundle.getString("degreeAdminOffice.mail"));
	email.setToAddresses(new EmailAddressList(academicServiceRequest.getPerson().getEmail()));
	email.setSubject(academicServiceRequest.getDescription());

	String body = appBundle.getString("mail.academicServiceRequest.concluded.message1");
	body += " " + academicServiceRequest.getServiceRequestNumberYear();
	body += " " + appBundle.getString("mail.academicServiceRequest.concluded.message2");
	body += " '" + academicServiceRequest.getDescription();
	body += "' " + appBundle.getString("mail.academicServiceRequest.concluded.message3");
	body += "\n\n" + appBundle.getString("mail.academicServiceRequest.concluded.message4");

	email.setBody(body);
    }
}
