/*
 * Created on 2004/11/17
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID implements IService {

    public InfoExecutionCourse run(final Integer oid) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = sp.getIPersistentObject();
        final ITurnoPersistente persistentShift = sp.getITurnoPersistente();

        InfoExecutionCourse infoExecutionCourse = null;

        IExecutionCourse executionCourse = (IExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, oid);

        if (executionCourse != null) {
            infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);

            infoExecutionCourse.setInfoExecutionPeriod(InfoExecutionPeriod
                    .newInfoFromDomain(executionCourse.getExecutionPeriod()));

            infoExecutionCourse.setAssociatedInfoCurricularCourses(new ArrayList());
            for (Iterator iterator = executionCourse.getAssociatedCurricularCourses().iterator(); iterator
                    .hasNext();) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
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
            List shifts = persistentShift.readByExecutionCourse(executionCourse);
            for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
                IShift shift = (IShift) iterator.next();
                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

                infoShift.setInfoLessons(new ArrayList());
                List lessons = shift.getAssociatedLessons();
                for (int i = 0; i < lessons.size(); i++) {
                    ILesson lesson = (ILesson) lessons.get(i);
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