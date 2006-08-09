package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
            final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                    .newInfoFromDomain(executionCourse);
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

            final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            final InfoExecutionPeriod infoExecutionPeriod2 = InfoExecutionPeriod
                    .newInfoFromDomain(executionPeriod);
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod2);

            final List<Lesson> lessons = shift.getAssociatedLessons();

            final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(lessons.size());
            infoShift.setInfoLessons(infoLessons);
            for (Lesson lesson : lessons) {
                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                infoLessons.add(infoLesson);
                infoLessonList.add(infoLesson);
            }
        }

        return infoLessonList;
    }

}