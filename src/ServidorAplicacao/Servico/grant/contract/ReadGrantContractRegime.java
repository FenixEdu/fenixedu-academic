/*
 * Created on Jan 29, 2004
 *
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContractRegime;
import Dominio.grant.contract.IGrantContractRegime;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * João Simas Nuno Barbosa
 */
public class ReadGrantContractRegime extends ReadDomainObjectService {
    public ReadGrantContractRegime() {
    }

    protected Class getDomainObjectClass() {
        return GrantContractRegime.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractRegime();
    }

    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return InfoGrantContractRegimeWithTeacherAndContract
                .newInfoFromDomain((IGrantContractRegime) domainObject);
    }
}