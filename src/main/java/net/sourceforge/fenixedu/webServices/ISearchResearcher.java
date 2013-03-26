package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.ResearcherDTO;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface ISearchResearcher {
    public ResearcherDTO[] searchByKeyword(String username, String password, String name, MessageContext context)
            throws NotAuthorizedException;

    public ResearcherDTO[] searchByName(String username, String password, String keywords, MessageContext context)
            throws NotAuthorizedException;

    public ResearcherDTO[] getAvailableResearchers(String username, String password, MessageContext context)
            throws NotAuthorizedException;

}
