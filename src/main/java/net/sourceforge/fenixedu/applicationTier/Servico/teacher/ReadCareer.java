/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.teacher.CareerTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.domain.teacher.Career;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCareer extends ReadDomainObjectService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        InfoCareer infoCarrerWithInfoTeacher = InfoCareer.newInfoFromDomain((Career) domainObject);
        infoCarrerWithInfoTeacher.setInfoTeacher(InfoTeacher.newInfoFromDomain(((Career) domainObject).getTeacher()));
        return infoCarrerWithInfoTeacher;
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
        return rootDomainObject.readCareerByOID(idInternal);
    }

    // Service Invokers migrated from Berserk

    private static final ReadCareer serviceInstance = new ReadCareer();

    @Service
    public static InfoObject runReadCareer(Integer objectId) throws NotAuthorizedException {
        CareerTeacherAuthorizationFilter.instance.execute();
        return serviceInstance.run(objectId);
    }

}