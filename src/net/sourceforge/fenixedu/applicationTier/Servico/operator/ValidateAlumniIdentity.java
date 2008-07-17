package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.Email;

import org.joda.time.DateTime;

public class ValidateAlumniIdentity extends Service {

    public void run(AlumniIdentityCheckRequest identityRequest, Boolean approval, Person operator) {

	identityRequest.setApproved(approval);
	identityRequest.setDecisionDateTime(new DateTime());
	identityRequest.setOperator(operator);
	sendEmail(identityRequest, approval);
    }

    private void sendEmail(AlumniIdentityCheckRequest request, Boolean approval) {

	final ResourceBundle managerBundle = ResourceBundle.getBundle("resources.ManagerResources", new Locale("pt"));
	String body;
	if (approval) {
	    body = MessageFormat.format(managerBundle.getString("alumni.identity.request.confirm.identity"), request.getAlumni()
		    .getStudent().getPerson().getFirstAndLastName());

	    switch (request.getRequestType()) {

	    case IDENTITY_CHECK:
		body += managerBundle.getString("alumni.identity.request.curriculum.access");
		break;

	    case STUDENT_NUMBER_RECOVERY:
		body += MessageFormat.format(managerBundle.getString("alumni.identity.request.student.number.info"), request
			.getAlumni().getStudent().getNumber().toString());
		break;

	    default:
		return;
	    }
	} else {
	    body = managerBundle.getString("alumni.identity.request.refuse.identity");
	}

	final String senderName = managerBundle.getString("alumni.identity.request.mail.sender.name");
	final String senderEmail = managerBundle.getString("alumni.identity.request.mail.sender.email");
	final String subject = managerBundle.getString("alumni.identity.request.mail.subject");

	new Email(senderName, senderEmail, new String[] {}, Collections.singleton(request.getContactEmail()),
		Collections.EMPTY_LIST, Collections.EMPTY_LIST, subject, body);

    }
}