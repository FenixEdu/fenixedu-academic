package net.sourceforge.fenixedu.applicationTier.Servico.credits.otherTypeCreditLine;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

public class DeleteOtherTypeCreditLineService extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return OtherTypeCreditLine.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
        return persistentSupport.getIPersistentOtherTypeCreditLine();
    }
	
	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
	    persistentObject.deleteByOID(getDomainObjectClass(), domainObject.getIdInternal());			
	}

}
