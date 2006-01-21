/*
 * Created on 2004/11/17
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID extends Service {

    public InfoExecutionCourse run(final Integer oid) throws ExcepcaoPersistencia {
        final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

        InfoExecutionCourse infoExecutionCourse = null;

        ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, oid);

        if (executionCourse != null) {
            infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);

            infoExecutionCourse.setInfoExecutionPeriod(InfoExecutionPeriod
                    .newInfoFromDomain(executionCourse.getExecutionPeriod()));

            infoExecutionCourse.setAssociatedInfoCurricularCourses(new ArrayList());
            for (Iterator iterator = executionCourse.getAssociatedCurricularCourses().iterator(); iterator
                    .hasNext();) {
                CurricularCourse curricularCourse = (CurricularCourse) iterator.next();
                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                        .newInfoFromDomain(curricularCourse);
                infoCurricularCourse.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                        .newInfoFromDomain(curricularCourse.getDegreeCurricularPlan()));
                infoCurricularCourse.getInfoDegreeCurricularPlan().setInfoDegree(
                        InfoDegree.newInfoFromDomain(curricularCourse.getDegreeCurricularPlan()
                                .getDegree()));

                infoExecutionCourse.getAssociatedInfoCurricularCourses().add(infoCurricularCourse);
            }

            infoExecutionCourse.setAssociatedInfoShifts(new ArrayList());
            List shifts = persistentShift.readByExecutionCourse(executionCourse.getIdInternal());
            for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
                Shift shift = (Shift) iterator.next();
                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

                infoShift.setInfoLessons(new ArrayList());
                List lessons = shift.getAssociatedLessons();
                for (int i = 0; i < lessons.size(); i++) {
                    Lesson lesson = (Lesson) lessons.get(i);
                    InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                    infoLesson.setInfoSala(InfoRoom.newInfoFromDomain(lesson.getSala()));

                    infoShift.getInfoLessons().add(infoLesson);
                }

                infoExecutionCourse.getAssociatedInfoShifts().add(infoShift);
            }
        }

        return infoExecutionCourse;
    }

}