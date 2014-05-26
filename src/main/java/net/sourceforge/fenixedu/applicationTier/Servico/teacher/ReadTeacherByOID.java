/*
 * Created on Nov 28, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 */
public class ReadTeacherByOID {

    protected InfoObject run(String objectId) {
        final String externalId = objectId;
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        InfoObject infoObject = null;

        if (domainObject != null) {
            infoObject = InfoTeacher.newInfoFromDomain((Teacher) domainObject);
        }

        return infoObject;
    }

    // Service Invokers migrated from Berserk

    private static final ReadTeacherByOID serviceInstance = new ReadTeacherByOID();

    @Atomic
    public static InfoObject runReadTeacherByOID(String externalId) {
        return serviceInstance.run(externalId);
    }

}