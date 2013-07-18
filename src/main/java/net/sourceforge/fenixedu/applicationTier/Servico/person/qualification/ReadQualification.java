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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;

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
    protected DomainObject readDomainObject(final Integer idInternal) {
        return RootDomainObject.getInstance().readQualificationByOID(idInternal);
    }

    // Service Invokers migrated from Berserk

    private static final ReadQualification serviceInstance = new ReadQualification();

    @Service
    public static InfoObject runReadQualification(Integer idInternal) throws NotAuthorizedException {
        ReadQualificationAuthorizationFilter.instance.execute(idInternal);
        return serviceInstance.run(idInternal);
    }

}