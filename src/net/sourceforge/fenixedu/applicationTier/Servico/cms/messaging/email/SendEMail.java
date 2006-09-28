/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.email;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailSender;
import net.sourceforge.fenixedu.domain.cms.messaging.email.SendMailReport;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailSender.SenderNotAllowed;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.RecipientType;

;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 8:11:44,17/Fev/2006
 * @version $Id$
 */
public class SendEMail extends Service {

    static public class SendEMailParameters {
	public EMailAddress from;

	public String subject;

	public String message;

	public IGroup[] toRecipients;

	public IGroup[] allowedSenders;

	public EMailAddress[] copyTo;

	public boolean copyToSender;
    }

    public SendMailReport run(SendEMailParameters parameters) throws SenderNotAllowed {
	EMailSender sender = new EMailSender(parameters.allowedSenders);
	sender.addRecipient(RecipientType.BCC, parameters.toRecipients);
	sender.addRecipient(RecipientType.BCC, parameters.copyTo);

	EMailMessage message = new EMailMessage();
	message.setSubject(parameters.subject);
	message.setText(parameters.message);

	sender.setMessage(message);
	return sender.send(parameters.from, parameters.copyToSender);

    }
}
