/*
 * Created on Jul 04, 2004
 * Note: it is 40 past midnight! In less than 24 hours, Portugal will be the next European Champion! :)
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovementWithContract;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * Barbosa Pica
 */
public class ReadGrantContractMovement extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantContractMovement.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractMovement();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return InfoGrantContractMovementWithContract
                .newInfoFromDomain((IGrantContractMovement) domainObject);
    }

}
