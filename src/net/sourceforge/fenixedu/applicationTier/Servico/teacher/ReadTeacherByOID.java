/*
 * Created on Nov 28, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadTeacherByOID extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return Teacher.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
        return persistentSupport.getIPersistentTeacher();
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoTeacher.newInfoFromDomain((Teacher) domainObject);
    }

}
