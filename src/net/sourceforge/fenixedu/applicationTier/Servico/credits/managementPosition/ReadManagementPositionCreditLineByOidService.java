/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.credits.IManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadManagementPositionCreditLineByOidService extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return ManagementPositionCreditLine.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentManagementPositionCreditLine();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return Cloner
                .copyIManagementPositionCreditLine2InfoManagementPositionCreditLine((IManagementPositionCreditLine) domainObject);
    }

}
