package net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.util.Email;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AlumniNotificationService extends FenixService {

    private static void sendEmail(final String subject, final String body, Alumni alumni) {
	sendEmail(subject, body, alumni.getStudent().getPerson().getDefaultEmailAddress().getValue());
    }

    private static void sendEmail(final String subject, final String body, String alumniEmail) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());
	new Email(bundle.getString("alumni.public.registration.mail.sender.name"), bundle
		.getString("alumni.public.registration.mail.sender.email"), subject, body, alumniEmail);
    }

    protected static void sendPublicAccessMail(final Alumni alumni) {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());

	final String subject = bundle.getString("alumni.public.registration.mail.subject");
	final String body = MessageFormat.format(bundle.getString("alumni.public.registration.url"), alumni.getStudent()
		.getPerson().getFirstAndLastName(), alumni.getIdInternal().toString(), alumni.getUrlRequestToken(),
		ResourceBundle.getBundle("resources.GlobalResources").getString("fenix.url"));

	sendEmail(subject, body, alumni);
    }

    protected static void sendIdentityCheckEmail(AlumniIdentityCheckRequest request, Boolean approval) {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.ManagerResources", Language.getLocale());

	final String subject = bundle.getString("alumni.identity.request.mail.subject");

	String body;
	if (approval) {
	    body = MessageFormat.format(bundle.getString("alumni.identity.request.confirm.identity"), request.getAlumni()
		    .getStudent().getPerson().getFirstAndLastName());

	    switch (request.getRequestType()) {

	    case IDENTITY_CHECK: // legacy behavior
		body += bundle.getString("alumni.identity.request.curriculum.access");
		break;
	    case PASSWORD_REQUEST:
		body += MessageFormat.format(bundle.getString("alumni.identity.request.password.request"), request.getAlumni()
			.getLoginUsername(), String.valueOf(request.getOID()), request.getRequestToken().toString());
		break;
	    case STUDENT_NUMBER_RECOVERY:
		body += MessageFormat.format(bundle.getString("alumni.identity.request.student.number.info"), request.getAlumni()
			.getStudent().getNumber().toString());
		break;

	    default:
		return;
	    }
	} else {
	    body = MessageFormat.format(bundle.getString("alumni.identity.request.refuse.identity"), request.getAlumni()
		    .getStudent().getPerson().getFirstAndLastName());
	}

	body = body + " " + request.getComment();

	sendEmail(subject, body, request.getContactEmail());
    }

    protected static void sendRegistrationSuccessMail(final Alumni alumni) {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());
	final String subject = bundle.getString("alumni.public.success.mail.subject");
	final String body = MessageFormat.format(bundle.getString("alumni.public.username.login.url"), alumni.getStudent()
		.getPerson().getFirstAndLastName(), alumni.getLoginUsername());
	sendEmail(subject, body, alumni);
    }

}