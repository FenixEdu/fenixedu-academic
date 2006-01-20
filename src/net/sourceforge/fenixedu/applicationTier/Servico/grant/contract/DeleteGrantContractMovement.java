/*
 * Created on Jul 4, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

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
    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {

        return persistentSupport.getIPersistentGrantContractMovement();
    }

    /* (non-Javadoc)
	 * @see net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService#deleteDomainObject(net.sourceforge.fenixedu.domain.DomainObject)
	 */
	protected void deleteDomainObject(DomainObject domainObject) {
		try{
	      persistentObject.deleteByOID(getDomainObjectClass(), domainObject.getIdInternal());
			
		} catch (Exception e) {
			
		}
		
	}

}