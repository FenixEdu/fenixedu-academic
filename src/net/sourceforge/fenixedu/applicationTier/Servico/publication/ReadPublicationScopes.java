/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class ReadPublicationScopes implements IServico {
    private static ReadPublicationScopes service = new ReadPublicationScopes();

    /**
     *  
     */
    private ReadPublicationScopes() {

    }

    public static ReadPublicationScopes getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadPublicationScopes";
    }

    public List run(String user, int publicationTypeId) throws FenixServiceException {
        List scopeList = new ArrayList();

        scopeList.add(PublicationConstants.SCOPE_LOCAL);
        scopeList.add(PublicationConstants.SCOPE_NACIONAL);
        scopeList.add(PublicationConstants.SCOPE_INTERNACIONAL);

        return scopeList;
    }
}