/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/03/29
 */

package net.sourceforge.fenixedu.applicationTier.Servico.publico;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseAndExams;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.Season;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExamsByExecutionDegreeAndCurricularYearPublic implements IService {

    public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod)
            throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        List<IExecutionCourse> executionCourses = sp.getIPersistentExecutionCourse()
                .readByExecutionPeriodAndExecutionDegree(infoExecutionPeriod.getIdInternal(),
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getName(),
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());

        List<InfoExecutionCourseAndExams> infoExecutionCourseAndExamsList = new ArrayList<InfoExecutionCourseAndExams>();
        for (IExecutionCourse executionCourse : executionCourses) {
            InfoExecutionCourseAndExams infoExecutionCourseAndExams = new InfoExecutionCourseAndExams();

            infoExecutionCourseAndExams.setInfoExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));

            for (IExam exam : executionCourse.getAssociatedExams()) {
                if (exam.getSeason().getseason().intValue() == Season.SEASON1) {
                    infoExecutionCourseAndExams.setInfoExam1(Cloner.copyIExam2InfoExam(exam));
                } else if (exam.getSeason().getseason().intValue() == Season.SEASON2) {
                    infoExecutionCourseAndExams.setInfoExam2(Cloner.copyIExam2InfoExam(exam));
                }
            }

            // Number of students attending execution course.
            // TODO : In this context should we realy count the number of
            //        students attending the course or just the ones from
            //        the indicated degree????
            infoExecutionCourseAndExams.setNumberStudentesAttendingCourse(sp.getIFrequentaPersistente()
                    .countStudentsAttendingExecutionCourse(executionCourse));

            infoExecutionCourseAndExamsList.add(infoExecutionCourseAndExams);
        }

        return infoExecutionCourseAndExamsList;
    }

}
