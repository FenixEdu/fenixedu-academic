package net.sourceforge.fenixedu.applicationTier.Servico.teacher.workingTime;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public class DeleteTeacherInstitutionWorkingTimeByOID extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return TeacherInstitutionWorkTime.class;
    }

    protected IPersistentObject getIPersistentObject() {
        return persistentSupport.getIPersistentTeacherInstitutionWorkingTime();
    }
	
	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
        TeacherInstitutionWorkTime teacherInstitutionWorkTime = (TeacherInstitutionWorkTime) domainObject;
        teacherInstitutionWorkTime.delete();
	}

}
