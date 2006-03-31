/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.otherTypeCreditLine;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;

/**
 * @author jpvl
 */
public class ReadOtherTypeCreditLineByOidService extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return OtherTypeCreditLine.class;
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoOtherTypeCreditLine.newInfoFromDomain((OtherTypeCreditLine) domainObject);
    }

}
