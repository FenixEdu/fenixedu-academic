package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface ILibraryManagement {
    public String getLibraryCardNumberByIstUsername(String username, String password, MessageContext context, String istUsername)
	    throws NotAuthorizedException;

    public String getLibraryCardNumberByCitizenCardNumber(String username, String password, MessageContext context,
	    String citizenCardNumber)
	    throws NotAuthorizedException;
}
