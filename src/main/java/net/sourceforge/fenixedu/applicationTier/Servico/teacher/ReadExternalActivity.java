/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.teacher.ExternalActivityTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadExternalActivity extends ReadDomainObjectService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        InfoExternalActivity infoExternalActivity = InfoExternalActivity.newInfoFromDomain((ExternalActivity) domainObject);
        return infoExternalActivity;
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
        return rootDomainObject.readExternalActivityByOID(idInternal);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExternalActivity serviceInstance = new ReadExternalActivity();

    @Service
    public static InfoObject runReadExternalActivity(Integer idInternal) throws NotAuthorizedException {
        ExternalActivityTeacherAuthorizationFilter.instance.execute();
        return serviceInstance.run(idInternal);
    }

}