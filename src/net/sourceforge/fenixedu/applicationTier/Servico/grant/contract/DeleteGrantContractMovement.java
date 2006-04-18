package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteGrantContractMovement extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantContractMovement.class;
    }

	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
        GrantContractMovement grantContractMovement = (GrantContractMovement) domainObject;
        grantContractMovement.delete();
	}

}
