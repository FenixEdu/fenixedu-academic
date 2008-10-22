package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface ISendMail {

    public void sendMail(String fromName, String fromEmail, String userId, String[] cc, String[] bcc, String topic,
	    String message, MessageContext context) throws NotAuthorizedException;
}
