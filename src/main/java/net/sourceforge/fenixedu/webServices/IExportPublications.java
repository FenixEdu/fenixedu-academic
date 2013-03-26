package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IExportPublications {
    public byte[] harverst(String username, String password, MessageContext context) throws NotAuthorizedException;

    public byte[] fetchFile(String username, String password, String storageId, MessageContext context)
            throws NotAuthorizedException;

    public String getFilename(String username, String password, String storageId, MessageContext context)
            throws NotAuthorizedException;

    public String getFilePermissions(String username, String password, String storageId, MessageContext context)
            throws NotAuthorizedException;
}
