package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;

public abstract class DeleteDomainObjectService extends Service {

    public void run(Integer objectId) throws Exception {
	DomainObject domainObject = readDomainObject(objectId);

	if ((domainObject == null) || !canDelete(domainObject)) {
	    throw new NonExistingServiceException("The object does not exist");
	}
	doBeforeDelete(domainObject);
	deleteDomainObject(domainObject);
	doAfterDelete(domainObject);
    }

    protected void doBeforeDelete(DomainObject domainObject) throws Exception {
    }

    protected void doAfterDelete(DomainObject domainObject) {
    }

    protected boolean canDelete(DomainObject newDomainObject) {
	return true;
    }

    protected abstract DomainObject readDomainObject(final Integer idInternal);

    protected abstract void deleteDomainObject(DomainObject domainObject);
}
