/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import constants.publication.PublicationConstants;

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