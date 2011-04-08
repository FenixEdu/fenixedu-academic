package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface ILibraryManagement {
    public String convertLibraryNumbers(String username, String password, MessageContext context, String source)
	    throws NotAuthorizedException;
}
