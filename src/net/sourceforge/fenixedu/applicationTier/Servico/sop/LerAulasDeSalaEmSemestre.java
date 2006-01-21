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

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerAulasDeSalaEmSemestre extends Service {

    public List run(InfoExecutionPeriod infoExecutionPeriod, InfoRoom infoRoom, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        if (executionPeriodId == null) {
            executionPeriodId = infoExecutionPeriod.getIdInternal();
        }

    	final Room room = (Room) persistentObject.readByOID(Room.class, infoRoom.getIdInternal());
    	final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class, executionPeriodId);

        final List<Lesson> lessonList = room.findLessonsForExecutionPeriod(executionPeriod);
        List<InfoLesson> infoAulas = new ArrayList<InfoLesson>();
        for (Lesson elem : lessonList) {
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
