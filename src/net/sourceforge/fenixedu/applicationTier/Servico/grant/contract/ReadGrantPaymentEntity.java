/*
 * Created on Feb 12, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * Pica Barbosa
 */
public class ReadGrantPaymentEntity extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantPaymentEntity.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantPaymentEntity();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return InfoGrantPaymentEntity.newInfoFromDomain((IGrantPaymentEntity) domainObject);
    }

}
