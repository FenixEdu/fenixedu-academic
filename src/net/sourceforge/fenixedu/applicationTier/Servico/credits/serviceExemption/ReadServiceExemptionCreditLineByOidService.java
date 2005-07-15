/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.serviceExemption;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.credits.IServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadServiceExemptionCreditLineByOidService extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return ServiceExemptionCreditLine.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentServiceExemptionCreditLine();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return Cloner
                .copyIServiceExemptionCreditLine2InfoServiceExemptionCreditLine((IServiceExemptionCreditLine) domainObject);
    }

}