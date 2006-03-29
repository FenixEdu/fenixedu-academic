package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public class DeleteManagementPositionCreditLineService extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return ManagementPositionCreditLine.class;
    }

    protected IPersistentObject getIPersistentObject() {
        return persistentSupport.getIPersistentManagementPositionCreditLine();
    }

	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
        ManagementPositionCreditLine managementPositionCreditLine = (ManagementPositionCreditLine) domainObject;
        managementPositionCreditLine.delete();
	}

}
