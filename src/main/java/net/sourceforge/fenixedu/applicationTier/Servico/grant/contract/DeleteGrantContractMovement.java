package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import pt.ist.fenixframework.DomainObject;

public class DeleteGrantContractMovement extends DeleteDomainObjectService {

    @Override
    protected void deleteDomainObject(DomainObject domainObject) {
        GrantContractMovement grantContractMovement = (GrantContractMovement) domainObject;
        grantContractMovement.delete();
    }

    @Override
    protected DomainObject readDomainObject(Integer idInternal) {
        return rootDomainObject.readGrantContractByOID(idInternal);
    }

}
