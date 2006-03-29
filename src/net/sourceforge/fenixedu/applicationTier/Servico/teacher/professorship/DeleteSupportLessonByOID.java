package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public class DeleteSupportLessonByOID extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return SupportLesson.class;
    }

    protected IPersistentObject getIPersistentObject() {
        return persistentSupport.getIPersistentSupportLesson();
    }
	
	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
        SupportLesson supportLesson = (SupportLesson) domainObject;
        supportLesson.delete();
	}

}
