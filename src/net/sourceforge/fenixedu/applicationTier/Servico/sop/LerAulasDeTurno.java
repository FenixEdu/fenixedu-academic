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
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeTurno implements IService {

    public List run(ShiftKey shiftKey) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurnoPersistente persistentShift = sp.getITurnoPersistente();

        final IShift shift = persistentShift.readByNameAndExecutionCourse(shiftKey.getShiftName(), shiftKey
                .getInfoExecutionCourse().getIdInternal());
        final List<ILesson> aulas = shift.getAssociatedLessons();

        List<InfoLesson> infoAulas = new ArrayList<InfoLesson>();
        for (ILesson elem : aulas) {
            InfoLesson infoLesson = InfoLesson.newInfoFromDomain(elem);

            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoLesson.setInfoShift(infoShift);

            infoAulas.add(infoLesson);
        }
        return infoAulas;
    }

}
