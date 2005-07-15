/*
 * Created on 12/Nov/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualificationWithPersonAndCountry;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualification extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return Qualification.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        return sp.getIPersistentQualification();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return InfoQualificationWithPersonAndCountry.newInfoFromDomain((IQualification) domainObject);
    }

    protected ISiteComponent getISiteComponent(InfoObject infoObject) {
        return (InfoQualification) infoObject;
    }

}
