/*
 * Created on Feb 12, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;

/**
 * Pica Barbosa
 */
public class ReadGrantPaymentEntity extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
	return InfoGrantPaymentEntity.newInfoFromDomain((GrantPaymentEntity) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
	return rootDomainObject.readGrantPaymentEntityByOID(idInternal);
    }

}
