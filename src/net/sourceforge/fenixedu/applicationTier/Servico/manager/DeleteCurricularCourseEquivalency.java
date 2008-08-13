package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteCurricularCourseEquivalency extends Service {

    public void run(final Integer curricularCourseEquivalencyID) {
        final CurricularCourseEquivalence curricularCourseEquivalence = rootDomainObject.readCurricularCourseEquivalenceByOID(curricularCourseEquivalencyID);
        curricularCourseEquivalence.delete();
    }

}
