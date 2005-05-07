package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o RemoverAula
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class RemoverAula implements IService {

    public Object run(final InfoLesson infoLesson, final InfoShift infoShift)
            throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ILesson lesson = Cloner.copyInfoLesson2Lesson(infoLesson);

        DeleteLessons.deleteLesson(sp, lesson.getIdInternal());

        return new Boolean(true);
    }

}