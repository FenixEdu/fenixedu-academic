/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.workingTime;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;

/**
 * @author jpvl
 */
public class ReadTeacherInstitutionWorkingTimeByOID extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return TeacherInstitutionWorkTime.class;
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoTeacherInstitutionWorkTime.newInfoFromDomain((TeacherInstitutionWorkTime) domainObject);
    }

}
