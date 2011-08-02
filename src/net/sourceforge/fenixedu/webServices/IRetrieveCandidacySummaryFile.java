package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.webServices.RetrieveCandidacySummaryFile.SummaryFileNotAvailableException;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IRetrieveCandidacySummaryFile {
    public abstract byte[] getCandidacySummaryFile(String username, String password, String userUID, MessageContext context)
	    throws NotAuthorizedException, SummaryFileNotAvailableException;
}