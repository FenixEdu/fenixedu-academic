/*
 * Created on Jul 04, 2004
 * Note: it is 40 past midnight! In less than 24 hours, Portugal will be the next European Champion! :)
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantContractMovementWithContract;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContractMovement;
import Dominio.grant.contract.IGrantContractMovement;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * Barbosa Pica
 */
public class ReadGrantContractMovement extends ReadDomainObjectService {
    public ReadGrantContractMovement() {
    }

    protected Class getDomainObjectClass() {
        return GrantContractMovement.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractMovement();
    }

    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return InfoGrantContractMovementWithContract
                .newInfoFromDomain((IGrantContractMovement) domainObject);
    }
}