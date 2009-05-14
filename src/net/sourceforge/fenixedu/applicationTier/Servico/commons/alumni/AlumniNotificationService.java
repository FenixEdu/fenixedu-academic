package net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AlumniNotificationService extends FenixService {

    private static void sendEmail(final Collection<Recipient> recipients, final String subject, final String body,
	    final String bccs) {
	RootDomainObject.getInstance().getSystemSender().newMessage(recipients, subject, body, bccs);
    }

    private static void sendEmail(final Collection<Recipient> recipients, final String subject, final String body) {
	RootDomainObject.getInstance().getSystemSender().newMessage(recipients, subject, body, "");
    }

    private static List<Recipient> getAlumniRecipients(Alumni alumni) {
	final Person person = alumni.getStudent().getPerson();
	PersonGroup group = new PersonGroup(person);
	return Recipient.createNewRecipient(Collections.singletonList(group));
    }

    protected static void sendPublicAccessMail(final Alumni alumni) {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());

	final String subject = bundle.getString("alumni.public.registration.mail.subject");
	final Person person = alumni.getStudent().getPerson();
	final String body = MessageFormat.format(bundle.getString("alumni.public.registration.url"),
		person.getFirstAndLastName(), alumni.getIdInternal().toString(), alumni.getUrlRequestToken(), ResourceBundle
			.getBundle("resources.GlobalResources").getString("fenix.url"));

	sendEmail(getAlumniRecipients(alumni), subject, body);
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

	sendEmail(Collections.EMPTY_LIST, subject, body, request.getContactEmail());
    }

    protected static void sendRegistrationSuccessMail(final Alumni alumni) {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());

	final String subject = bundle.getString("alumni.public.success.mail.subject");
	final String body = MessageFormat.format(bundle.getString("alumni.public.username.login.url"), alumni.getStudent()
		.getPerson().getFirstAndLastName(), alumni.getLoginUsername());

	sendEmail(getAlumniRecipients(alumni), subject, body);
    }

}