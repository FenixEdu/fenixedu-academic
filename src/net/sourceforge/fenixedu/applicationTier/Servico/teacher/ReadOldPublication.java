/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadOldPublication extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoOldPublication.newInfoFromDomain((OldPublication) domainObject);
    }

	@Override
	protected DomainObject readDomainObject(final Integer idInternal) {
		return rootDomainObject.readOldPublicationByOID(idInternal);
	}

}
