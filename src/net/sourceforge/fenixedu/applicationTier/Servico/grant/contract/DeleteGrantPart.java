package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public class DeleteGrantPart extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantPart.class;
    }

    protected IPersistentObject getIPersistentObject() {
        return persistentSupport.getIPersistentGrantPart();
    }

	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
	    GrantPart grantPart = (GrantPart) domainObject;
        grantPart.delete();
	}

}
