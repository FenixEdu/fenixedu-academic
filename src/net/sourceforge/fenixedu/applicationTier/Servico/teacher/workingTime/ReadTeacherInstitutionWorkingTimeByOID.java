/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.workingTime;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.teacher.workTime.ITeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadTeacherInstitutionWorkingTimeByOID extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return TeacherInstitutionWorkTime.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentTeacherInstitutionWorkingTime();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return Cloner
                .copyITeacherInstitutionWorkingTime2InfoTeacherInstitutionWorkTime((ITeacherInstitutionWorkTime) domainObject);
    }

}
