/*
 * Created on Jan 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;

/**
 * João Simas Nuno Barbosa
 */
public class ReadGrantContractRegime extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
	return InfoGrantContractRegimeWithTeacherAndContract.newInfoFromDomain((GrantContractRegime) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
	return rootDomainObject.readGrantContractRegimeByOID(idInternal);
    }

}
