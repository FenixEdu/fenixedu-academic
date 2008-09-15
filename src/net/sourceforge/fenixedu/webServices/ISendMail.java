package net.sourceforge.fenixedu.webServices;

import org.codehaus.xfire.MessageContext;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;

public interface ISendMail {

    public void sendMail(String fromName, String fromEmail, String userId, String[] cc, String[] bcc, String topic,
	    String message, MessageContext context) throws NotAuthorizedException;
}
