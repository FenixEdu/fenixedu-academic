/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.DomainObject;
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

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoManagementPositionCreditLine.newInfoFromDomain((ManagementPositionCreditLine) domainObject);
    }

}
