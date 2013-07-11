/*
 * Created on 12/Nov/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Filtro.person.ReadQualificationAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualificationWithPersonAndCountry;
import net.sourceforge.fenixedu.domain.Qualification;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualification extends ReadDomainObjectService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoQualificationWithPersonAndCountry.newInfoFromDomain((Qualification) domainObject);
    }

    protected ISiteComponent getISiteComponent(InfoObject infoObject) {
        return (InfoQualification) infoObject;
    }

    @Override
    protected DomainObject readDomainObject(final String externalId) {
        return FenixFramework.getDomainObject(externalId);
    }

    // Service Invokers migrated from Berserk

    private static final ReadQualification serviceInstance = new ReadQualification();

    @Atomic
    public static InfoObject runReadQualification(String externalId) throws NotAuthorizedException {
        ReadQualificationAuthorizationFilter.instance.execute(externalId);
        return serviceInstance.run(externalId);
    }

}