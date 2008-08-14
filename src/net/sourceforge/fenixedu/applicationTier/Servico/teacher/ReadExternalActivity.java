/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadExternalActivity extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
	InfoExternalActivity infoExternalActivity = InfoExternalActivity.newInfoFromDomain((ExternalActivity) domainObject);
	return infoExternalActivity;
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
	return rootDomainObject.readExternalActivityByOID(idInternal);
    }

}
