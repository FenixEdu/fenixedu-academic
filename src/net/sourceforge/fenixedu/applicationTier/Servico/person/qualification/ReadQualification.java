/*
 * Created on 12/Nov/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualificationWithPersonAndCountry;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Qualification;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualification extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
	return InfoQualificationWithPersonAndCountry.newInfoFromDomain((Qualification) domainObject);
    }

    protected ISiteComponent getISiteComponent(InfoObject infoObject) {
	return (InfoQualification) infoObject;
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
	return rootDomainObject.readQualificationByOID(idInternal);
    }

}
