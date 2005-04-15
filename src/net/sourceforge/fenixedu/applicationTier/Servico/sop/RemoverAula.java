package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviï¿½o RemoverAula
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

    // FIXME : O serviço nao devolve False quando a aula nao existe!...

    public Object run(InfoLesson infoLesson, InfoShift infoShift) {
        boolean result = false;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            ILesson lesson = Cloner.copyInfoLesson2Lesson(infoLesson);

            sp.getIAulaPersistente().delete(lesson);
            //      sp.getITurnoAulaPersistente().delete(shift,
            // infoLesson.getDiaSemana(),
            //                                           infoLesson.getInicio(), infoLesson.getFim(), room);
            result = true;
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return new Boolean(result);
    }

}