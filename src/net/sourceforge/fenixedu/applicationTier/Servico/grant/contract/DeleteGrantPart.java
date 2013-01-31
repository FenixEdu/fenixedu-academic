package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;

public class DeleteGrantPart extends DeleteDomainObjectService {

	@Override
	protected void deleteDomainObject(DomainObject domainObject) {
		GrantPart grantPart = (GrantPart) domainObject;
		grantPart.delete();
	}

	@Override
	protected DomainObject readDomainObject(Integer idInternal) {
		return rootDomainObject.readGrantPartByOID(idInternal);
	}

}
