/*
 * Created on Jul 04, 2004
 * Note: it is 40 past midnight! In less than 24 hours, Portugal will be the next European Champion! :)
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovementWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import pt.ist.fenixframework.DomainObject;

/**
 * Barbosa Pica
 */
public class ReadGrantContractMovement extends ReadDomainObjectService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoGrantContractMovementWithContract.newInfoFromDomain((GrantContractMovement) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
        return rootDomainObject.readGrantContractMovementByOID(idInternal);
    }

}
