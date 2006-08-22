package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerAulasDeTurma extends Service {

    public List run(InfoClass infoClass) throws ExcepcaoPersistencia {
        SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID( infoClass.getIdInternal());
        
        final List<Shift> shiftList = schoolClass.getAssociatedShifts();

        List<InfoLesson> infoLessonList = new ArrayList<InfoLesson>();
        for (Shift shift : shiftList) {
            for (Lesson lesson : shift.getAssociatedLessonsSet()) {
                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                infoLessonList.add(infoLesson);
            }
        }

        return infoLessonList;
    }

}