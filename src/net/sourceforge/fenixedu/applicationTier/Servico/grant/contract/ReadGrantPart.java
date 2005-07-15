/*
 * Created on Jan 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * João Simas Nuno Barbosa
 */
public class ReadGrantPart extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantPart.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantPart();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity
                .newInfoFromDomain((IGrantPart) domainObject);
    }

}
