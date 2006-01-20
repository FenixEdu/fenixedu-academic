/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 */
public abstract class DeleteDomainObjectService extends Service {
	public void run(Integer objectId) throws Exception {
		DomainObject domainObject = persistentObject.readByOID(getDomainObjectClass(), objectId);

		if ((domainObject == null) || !canDelete(domainObject)) {
			throw new NonExistingServiceException("The object does not exist");
		}
		doBeforeDelete(domainObject);
		deleteDomainObject(domainObject);
		doAfterDelete(domainObject);
	}

	/**
	 * @param domainObject
	 * @param sp
	 */
	protected void doBeforeDelete(DomainObject domainObject) throws Exception {
	}

	/**
	 * @param domainObject
	 */
	protected void doAfterDelete(DomainObject domainObject) {

	}

	/**
	 * By default returns true
	 * 
	 * @param newDomainObject
	 * @return
	 */
	protected boolean canDelete(DomainObject newDomainObject) {
		return true;
	}

	/**
	 * This is the class in witch the broker will read and delete the
	 * DomainObject
	 * 
	 * @return
	 */
	protected abstract Class getDomainObjectClass();

	/**
	 * @param sp
	 * @return
	 */
	protected abstract IPersistentObject getIPersistentObject(ISuportePersistente sp)
			throws ExcepcaoPersistencia;

	protected abstract void deleteDomainObject(DomainObject domainObject);

}