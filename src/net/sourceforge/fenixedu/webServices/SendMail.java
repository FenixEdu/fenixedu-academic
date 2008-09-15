package net.sourceforge.fenixedu.webServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletRequest;

import org.codehaus.xfire.MessageContext;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.util.HostAccessControl;
import pt.ist.fenixWebFramework.services.Service;

public class SendMail implements ISendMail {

    public void sendMail(String fromName, String fromEmail, String userId, String[] cc, String[] bcc, String topic,
	    String message, MessageContext context) throws NotAuthorizedException {
	if (!isAllowed(context)) {
	    throw new NotAuthorizedException();
	}

	Person person = Person.readPersonByIstUsername(userId);

	if (person == null) {
	    throw new UnsupportedOperationException("invalid username");
	}

	List<String> singleName = Collections.singletonList(person.getInstitutionalOrDefaultEmailAddress().getValue());
	List<String> ccList = getList(cc);
	List<String> bccList = getList(bcc);

	sendEmail(fromName, fromEmail, singleName, ccList, bccList, topic, message);
    }

    private boolean isAllowed(MessageContext context) {
	return HostAccessControl.isAllowed(this, (ServletRequest) context
		.getProperty("XFireServletController.httpServletRequest"));
    }

    private List<String> getList(String[] cc) {
	List<String> list = new ArrayList<String>();
	for (String ccAddress : cc) {
	    list.add(ccAddress);
	}
	return list;
    }

    @Service
    public void sendEmail(String fromName, String fromEmail, List<String> tos, List<String> carbonCopies,
	    List<String> blindCarbonCopies, String topic, String message) {
	new Email(fromName, fromEmail, null, tos, carbonCopies, blindCarbonCopies, topic, message);
    }
}
