/*
 * Created on 2004/11/17
 * 
 *  
 */
package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import Dominio.ExecutionCourse;
import Dominio.ILesson;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IShift;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID implements IService {

    public InfoExecutionCourse run(final Integer oid) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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