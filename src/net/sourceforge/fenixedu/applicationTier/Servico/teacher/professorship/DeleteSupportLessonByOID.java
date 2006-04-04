package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteSupportLessonByOID extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return SupportLesson.class;
    }

	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
        SupportLesson supportLesson = (SupportLesson) domainObject;
        supportLesson.delete();
	}

}
