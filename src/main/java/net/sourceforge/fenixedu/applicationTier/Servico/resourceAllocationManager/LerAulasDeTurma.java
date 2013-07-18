package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;

public class LerAulasDeTurma {

    @Service
    public static List<InfoLesson> run(InfoClass infoClass) {
        SchoolClass schoolClass = RootDomainObject.getInstance().readSchoolClassByOID(infoClass.getIdInternal());

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