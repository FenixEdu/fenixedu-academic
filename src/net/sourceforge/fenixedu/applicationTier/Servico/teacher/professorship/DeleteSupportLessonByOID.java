/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author jpvl
 */
public class DeleteSupportLessonByOID extends DeleteDomainObjectService {
    public DeleteSupportLessonByOID() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return SupportLesson.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentSupportLesson();
    }
	
    /* (non-Javadoc)
	 * @see net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService#deleteDomainObject(net.sourceforge.fenixedu.domain.DomainObject)
	 */
	protected void deleteDomainObject(DomainObject domainObject) {
		try{
	      ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
	      IPersistentObject persistentObject = getIPersistentObject(sp);
	      persistentObject.deleteByOID(getDomainObjectClass(), domainObject.getIdInternal());
			
		} catch (Exception e) {
			
		}
		
	}

}