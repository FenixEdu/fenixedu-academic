/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import pt.ist.fenixframework.DomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 */
public abstract class ReadDomainObjectService extends FenixService {

    protected InfoObject run(Integer objectId) {
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