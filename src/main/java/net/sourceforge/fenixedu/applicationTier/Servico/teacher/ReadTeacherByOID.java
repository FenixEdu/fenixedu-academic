/*
 * Created on Nov 28, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;

/**
 * @author jpvl
 */
public class ReadTeacherByOID extends ReadDomainObjectService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoTeacher.newInfoFromDomain((Teacher) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
        return RootDomainObject.getInstance().readTeacherByOID(idInternal);
    }

    // Service Invokers migrated from Berserk

    private static final ReadTeacherByOID serviceInstance = new ReadTeacherByOID();

    @Service
    public static InfoObject runReadTeacherByOID(Integer idInternal) {
        return serviceInstance.run(idInternal);
    }

}