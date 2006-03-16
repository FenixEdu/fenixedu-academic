/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsuranceWithContractAndPaymentEntity;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author Barbosa
 * @author Pica
 */
public class ReadGrantInsurance extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantInsurance.class;
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoGrantInsuranceWithContractAndPaymentEntity
                .newInfoFromDomain((GrantInsurance) domainObject);
    }

}
