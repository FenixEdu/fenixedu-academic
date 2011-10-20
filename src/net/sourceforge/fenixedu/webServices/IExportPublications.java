package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IExportPublications {
    public byte[] harverst(MessageContext context) throws NotAuthorizedException;
}
