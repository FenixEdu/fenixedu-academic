package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IAlumniManagement {

    public abstract String validateAlumniIdentity(String username, String password, String requestOID, String requestUUID,
            MessageContext context) throws NotAuthorizedException;

}