package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.ICurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteCurricularCourseEquivalency implements IService {

    public void run(final Integer curricularCourseEquivalencyID) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentCurricularCourseEquivalence persistentCurricularCourseEquivalence = persistentSupport.getIPersistentCurricularCourseEquivalence();

        final ICurricularCourseEquivalence curricularCourseEquivalence = (ICurricularCourseEquivalence) persistentCurricularCourseEquivalence.readByOID(CurricularCourseEquivalence.class, curricularCourseEquivalencyID);
        curricularCourseEquivalence.delete();
    }

}
