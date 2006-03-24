package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

public class DeleteGrantPart extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantPart.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
        return persistentSupport.getIPersistentGrantPart();
    }

	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
	    persistentObject.deleteByOID(getDomainObjectClass(), domainObject.getIdInternal());
	}

}
