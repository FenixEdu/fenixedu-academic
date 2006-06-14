package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerAulasDeSalaEmSemestre extends Service {

    public List run(InfoExecutionPeriod infoExecutionPeriod, InfoRoom infoRoom, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        if (executionPeriodId == null) {
            executionPeriodId = infoExecutionPeriod.getIdInternal();
        }

    	final OldRoom room = (OldRoom) rootDomainObject.readSpaceByOID(infoRoom.getIdInternal());
    	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);

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
