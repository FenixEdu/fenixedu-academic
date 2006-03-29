package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

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

	protected void doBeforeDelete(DomainObject domainObject) throws Exception { }

	protected void doAfterDelete(DomainObject domainObject) { }

	protected boolean canDelete(DomainObject newDomainObject) {
		return true;
	}

	protected abstract Class getDomainObjectClass();

	protected abstract IPersistentObject getIPersistentObject() throws ExcepcaoPersistencia;

	protected abstract void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia;

}
