package net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.AlumniRequestType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AlumniNotificationService extends FenixService {

    static ResourceBundle getAlumniBundle() {
        return ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());
    }

    static ResourceBundle getGlobalBundle() {
        return ResourceBundle.getBundle("resources.GlobalResources", Language.getLocale());
    }

    static ResourceBundle getManagerBundle() {
        return ResourceBundle.getBundle("resources.ManagerResources", Language.getLocale());
    }

    private static void sendEmail(final Collection<Recipient> recipients, final String subject, final String body,
            final String bccs) {
        SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();
        new Message(systemSender, systemSender.getConcreteReplyTos(), recipients, subject, body, bccs);
    }

    private static List<Recipient> getAlumniRecipients(Alumni alumni) {
        return Collections.singletonList(Recipient.newInstance(new PersonGroup(alumni.getStudent().getPerson())));
    }

    protected static void sendPublicAccessMail(final Alumni alumni, final String alumniEmail) {

        final String subject = getAlumniBundle().getString("alumni.public.registration.mail.subject");
        final Person person = alumni.getStudent().getPerson();
        final String body =
                MessageFormat.format(getAlumniBundle().getString("alumni.public.registration.url"), person.getFirstAndLastName(),
                        getRegisterConclusionURL(alumni));

        sendEmail(Collections.EMPTY_LIST, subject, body, alumniEmail);
    }

    public static String getRegisterConclusionURL(final Alumni alumni) {
        final String fenixURL = getGlobalBundle().getString("fenix.url");
        return MessageFormat.format(getAlumniBundle().getString("alumni.public.registration.conclusion.url"), fenixURL, alumni
                .getIdInternal().toString(), alumni.getUrlRequestToken());
    }

    protected static void sendIdentityCheckEmail(AlumniIdentityCheckRequest request, Boolean approval) {

        final String subject = getManagerBundle().getString("alumni.identity.request.mail.subject");

        String body;
        if (approval) {
            body =
                    MessageFormat.format(getManagerBundle().getString("alumni.identity.request.confirm.identity"), request
                            .getAlumni().getStudent().getPerson().getFirstAndLastName());

            switch (request.getRequestType()) {

            case IDENTITY_CHECK: // legacy behavior
                body += getManagerBundle().getString("alumni.identity.request.curriculum.access");
                break;
            case PASSWORD_REQUEST:
                body +=
                        MessageFormat.format(getManagerBundle().getString("alumni.identity.request.password.request"), request
                                .getAlumni().getLoginUsername(), String.valueOf(request.getOID()), request.getRequestToken()
                                .toString());
                break;
            case STUDENT_NUMBER_RECOVERY:
                body +=
                        MessageFormat.format(getManagerBundle().getString("alumni.identity.request.student.number.info"), request
                                .getAlumni().getStudent().getNumber().toString());
                break;

            default:
                return;
            }
        } else {
            body =
                    MessageFormat.format(getManagerBundle().getString("alumni.identity.request.refuse.identity"), request
                            .getAlumni().getStudent().getPerson().getFirstAndLastName());
        }

        body = body + " " + request.getComment();
        if (!approval && request.getRequestType().equals(AlumniRequestType.PASSWORD_REQUEST)) {
            body +=
                    "\n"
                            + MessageFormat.format(getManagerBundle()
                                    .getString("alumni.identity.request.password.request.refuse"),
                                    getRegisterConclusionURL(request.getAlumni()));
        }

        sendEmail(Collections.EMPTY_LIST, subject, body, request.getContactEmail());
    }

    protected static void sendRegistrationSuccessMail(final Alumni alumni) {

        final String subject = getAlumniBundle().getString("alumni.public.success.mail.subject");
        final String body =
                MessageFormat.format(getAlumniBundle().getString("alumni.public.username.login.url"), alumni.getStudent()
                        .getPerson().getFirstAndLastName(), alumni.getLoginUsername());

        sendEmail(getAlumniRecipients(alumni), subject, body, null);
    }

}