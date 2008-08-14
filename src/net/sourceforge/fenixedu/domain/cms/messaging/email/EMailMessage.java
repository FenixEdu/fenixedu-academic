/**
 * 
 */

package net.sourceforge.fenixedu.domain.cms.messaging.email;

import java.util.Collection;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 13:31:57,8/Fev/2006
 * @version $Id$
 */
public class EMailMessage {
    private String subject;

    private String text;

    private Collection<byte[]> attachments;

    public Collection<byte[]> getAttachments() {
	return attachments;
    }

    public void setAttachments(Collection<byte[]> attachments) {
	this.attachments = attachments;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public void addAttachment(byte[]... attachments) {
	for (byte[] attachment : attachments) {
	    this.attachments.add(attachment);
	}
    }

    public MimeMessage getMimeMessage(Session session) {
	MimeMessage mimeMessage = new MimeMessage(session);
	if (this.attachments.size() == 0) {
	    // TODO: no attachment
	} else {
	    // TODO: add attachments
	}

	return mimeMessage;
    }
}
