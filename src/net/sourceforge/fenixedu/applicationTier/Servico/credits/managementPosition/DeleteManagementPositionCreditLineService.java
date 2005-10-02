/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author jpvl
 */
public class DeleteManagementPositionCreditLineService extends DeleteDomainObjectService {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return ManagementPositionCreditLine.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentManagementPositionCreditLine();
    }

    /* (non-Javadoc)
	 * @see net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService#deleteDomainObject(net.sourceforge.fenixedu.domain.IDomainObject)
	 */
	protected void deleteDomainObject(IDomainObject domainObject) {
		try{
	      ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
	      IPersistentObject persistentObject = getIPersistentObject(sp);
	      persistentObject.deleteByOID(getDomainObjectClass(), domainObject.getIdInternal());
			
		} catch (Exception e) {
			
		}
		
	}

}