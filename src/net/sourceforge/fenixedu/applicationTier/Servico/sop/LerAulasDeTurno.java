/*
 * LerAulasDeTurno.java
 *
 * Created on 28 de Outubro de 2002, 22:23
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAulasDeTurno
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeTurno implements IService {

    public List run(ShiftKey shiftKey) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurnoPersistente persistentShift = sp.getITurnoPersistente();

        final Shift shift = persistentShift.readByNameAndExecutionCourse(shiftKey.getShiftName(), shiftKey
                .getInfoExecutionCourse().getIdInternal());
        final List<Lesson> aulas = shift.getAssociatedLessons();

        List<InfoLesson> infoAulas = new ArrayList<InfoLesson>();
        for (Lesson elem : aulas) {
            InfoLesson infoLesson = InfoLesson.newInfoFromDomain(elem);

            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoLesson.setInfoShift(infoShift);

            infoAulas.add(infoLesson);
        }
        return infoAulas;
    }

}
