/*
 * Created on 22/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantType extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
	return InfoGrantType.newInfoFromDomain((GrantType) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
	return rootDomainObject.readGrantTypeByOID(idInternal);
    }

}
