/*
 * LerAulasDeSalaEmSemestre.java Created on 29 de Outubro de 2002, 15:44
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAulasDeSalaEmSemestre.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeSalaEmSemestre implements IService {

    public List run(InfoExecutionPeriod infoExecutionPeriod, InfoRoom infoRoom, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        
        if (executionPeriodId == null) {
            executionPeriodId = infoExecutionPeriod.getIdInternal();
        }

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final List<ILesson> lessonList = sp.getIAulaPersistente().readByRoomAndExecutionPeriod(infoRoom.getIdInternal(), executionPeriodId);

        List<InfoLesson> infoAulas = new ArrayList<InfoLesson>();
        for (ILesson elem : lessonList) {
            if (elem.getShift() == null) {
                continue;
            }

            InfoLesson infoLesson = InfoLesson.newInfoFromDomain(elem);
            
            InfoShift infoShift = InfoShift.newInfoFromDomain(elem.getShift());
            infoLesson.setInfoShift(infoShift);

            infoAulas.add(infoLesson);
        }
        
        return infoAulas;
    }

}
