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

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseAndExams;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.Season;

public class ReadExamsByExecutionDegreeAndCurricularYearPublic implements IServico {

    private static ReadExamsByExecutionDegreeAndCurricularYearPublic _servico = new ReadExamsByExecutionDegreeAndCurricularYearPublic();

    /**
     * The singleton access method of this class.
     */
    public static ReadExamsByExecutionDegreeAndCurricularYearPublic getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadExamsByExecutionDegreeAndCurricularYearPublic() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadExamsByExecutionDegreeAndCurricularYearPublic";
    }

    public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod) {
        List infoExecutionCourseAndExamsList = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            List executionCourses = sp.getIPersistentExecutionCourse()
                    .readByExecutionPeriodAndExecutionDegree(executionPeriod, executionDegree);

            for (int i = 0; i < executionCourses.size(); i++) {
                IExecutionCourse executionCourse = (IExecutionCourse) executionCourses.get(i);

                InfoExecutionCourseAndExams infoExecutionCourseAndExams = new InfoExecutionCourseAndExams();

                infoExecutionCourseAndExams.setInfoExecutionCourse((InfoExecutionCourse) Cloner
                        .get(executionCourse));

                for (int j = 0; j < executionCourse.getAssociatedExams().size(); j++) {
                    IExam exam = (IExam) executionCourse.getAssociatedExams().get(j);
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
                infoExecutionCourseAndExams.setNumberStudentesAttendingCourse(sp
                        .getIFrequentaPersistente().countStudentsAttendingExecutionCourse(
                                executionCourse));

                infoExecutionCourseAndExamsList.add(infoExecutionCourseAndExams);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoExecutionCourseAndExamsList;
    }
}