package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoverAula extends Service {

    public Object run(final InfoLesson infoLesson, final InfoShift infoShift)
            throws ExcepcaoPersistencia {
        DeleteLessons.deleteLesson(persistentSupport, infoLesson.getIdInternal());
        return Boolean.TRUE;
    }

}