/*
 * ReadExamsByExecutionCourse.java
 * 
 * Created on 2003/05/26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

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
import net.sourceforge.fenixedu.dataTransferObject.InfoViewAllExams;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.Season;

public class ReadExamsSortedByExecutionDegreeAndCurricularYear implements IServico {

    private static ReadExamsSortedByExecutionDegreeAndCurricularYear _servico = new ReadExamsSortedByExecutionDegreeAndCurricularYear();

    /**
     * The singleton access method of this class.
     */
    public static ReadExamsSortedByExecutionDegreeAndCurricularYear getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadExamsSortedByExecutionDegreeAndCurricularYear() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadExamsSortedByExecutionDegreeAndCurricularYear";
    }

    public List run(InfoExecutionPeriod infoExecutionPeriod) {

        List infoViewAllExamsList = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            IExecutionYear executionYear = executionPeriod.getExecutionYear();

            List executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(executionYear.getIdInternal(),
                    DegreeType.DEGREE);

            for (int k = 0; k < executionDegrees.size(); k++) {
                IExecutionDegree executionDegree = (IExecutionDegree) executionDegrees.get(k);
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);

                for (int curricularYear = 1; curricularYear <= 5; curricularYear++) {
                    InfoViewAllExams infoViewAllExams = new InfoViewAllExams(infoExecutionDegree,
                            new Integer(curricularYear), null);

                    List infoExecutionCourseAndExamsList = new ArrayList();

                    List executionCourses = sp.getIPersistentExecutionCourse()
                            .readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                                    new Integer(curricularYear), executionPeriod, executionDegree);

                    for (int i = 0; i < executionCourses.size(); i++) {
                        IExecutionCourse executionCourse = (IExecutionCourse) executionCourses.get(i);

                        InfoExecutionCourseAndExams infoExecutionCourseAndExams = new InfoExecutionCourseAndExams();

                        infoExecutionCourseAndExams.setInfoExecutionCourse((InfoExecutionCourse) Cloner
                                .get(executionCourse));

                        for (int j = 0; j < executionCourse.getAssociatedExams().size(); j++) {
                            IEvaluation evaluation = (IEvaluation) executionCourse.getAssociatedExams()
                                    .get(j);

                            if (evaluation instanceof IExam) {
                                IExam exam = (IExam) evaluation;
                                if (exam.getSeason().getseason().intValue() == Season.SEASON1) {
                                    infoExecutionCourseAndExams.setInfoExam1(Cloner
                                            .copyIExam2InfoExam(exam));
                                } else if (exam.getSeason().getseason().intValue() == Season.SEASON2) {
                                    infoExecutionCourseAndExams.setInfoExam2(Cloner
                                            .copyIExam2InfoExam(exam));
                                }
                            }
                        }

                        // Number of students attending execution course.
                        // TODO : In this context should we realy count the
                        // number of
                        //        students attending the course or just the ones from
                        //        the indicated degree????
                        infoExecutionCourseAndExams.setNumberStudentesAttendingCourse(sp
                                .getIFrequentaPersistente().countStudentsAttendingExecutionCourse(
                                        executionCourse));

                        infoExecutionCourseAndExamsList.add(infoExecutionCourseAndExams);
                    }

                    if (infoExecutionCourseAndExamsList != null
                            && infoExecutionCourseAndExamsList.isEmpty()) {
                        infoViewAllExams.setInfoExecutionCourseAndExamsList(null);
                    } else {
                        infoViewAllExams
                                .setInfoExecutionCourseAndExamsList(infoExecutionCourseAndExamsList);
                    }
                    infoViewAllExamsList.add(infoViewAllExams);

                }
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return infoViewAllExamsList;
    }
}