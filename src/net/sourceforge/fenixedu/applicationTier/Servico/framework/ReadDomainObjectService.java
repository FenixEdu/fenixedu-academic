/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 */
public abstract class ReadDomainObjectService extends Service {

	public InfoObject run(Integer objectId) throws FenixServiceException{
		DomainObject domainObject = readDomainObject(objectId);
		InfoObject infoObject = null;

		if (domainObject != null) {
			infoObject = newInfoFromDomain(domainObject);
		}

		return infoObject;
	}

	protected abstract DomainObject readDomainObject(final Integer idInternal);

	protected abstract InfoObject newInfoFromDomain(DomainObject domainObject);

}