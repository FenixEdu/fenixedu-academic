/*
 * Created on Jan 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * João Simas Nuno Barbosa
 */
public class ReadGrantContractRegime extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantContractRegime.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractRegime();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return InfoGrantContractRegimeWithTeacherAndContract
                .newInfoFromDomain((IGrantContractRegime) domainObject);
    }

}
