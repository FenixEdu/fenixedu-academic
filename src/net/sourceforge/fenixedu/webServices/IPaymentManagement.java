package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IPaymentManagement {

    public String generatePaymentTicket(String username, String password, String source, MessageContext context)
	    throws NotAuthorizedException;

}
