package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.util.EmailAddressList;

public class ConcludeAcademicServiceRequest extends Service {

    public void run(final AcademicServiceRequest academicServiceRequest, final Boolean sendEmailToStudent) {
	if (!academicServiceRequest.isConcluded()) {
	    academicServiceRequest.conclude();
	    
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
	
	String body = appBundle.getString("message.academicServiceRequest.concluded.mail1");
	body += " " + academicServiceRequest.getDescription();
	body += " " + appBundle.getString("message.academicServiceRequest.concluded.mail2");
	body += "\n" + appBundle.getString("message.academicServiceRequest.concluded.mail3");
	
	email.setBody(body);
    }
}
