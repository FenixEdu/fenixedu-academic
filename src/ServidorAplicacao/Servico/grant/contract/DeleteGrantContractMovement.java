/*
 * Created on Jul 4, 2004
 */
package ServidorAplicacao.Servico.grant.contract;

import Dominio.grant.contract.GrantContractMovement;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author pica
 * @author barbosa
 */
public class DeleteGrantContractMovement extends DeleteDomainObjectService {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return GrantContractMovement.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {

        return sp.getIPersistentGrantContractMovement();
    }

}