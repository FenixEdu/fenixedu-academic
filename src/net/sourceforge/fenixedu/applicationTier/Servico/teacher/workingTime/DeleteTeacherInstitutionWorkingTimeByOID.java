package net.sourceforge.fenixedu.applicationTier.Servico.teacher.workingTime;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

public class DeleteTeacherInstitutionWorkingTimeByOID extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return TeacherInstitutionWorkTime.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
        return persistentSupport.getIPersistentTeacherInstitutionWorkingTime();
    }
	
	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
	    persistentObject.deleteByOID(getDomainObjectClass(), domainObject.getIdInternal());
	}

}
