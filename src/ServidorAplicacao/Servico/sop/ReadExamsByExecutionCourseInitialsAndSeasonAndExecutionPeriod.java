/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/04/04
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Season;

public class ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod implements IServico {

    private static ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod _servico = new ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod();

    /**
     * The singleton access method of this class.
     */
    public static ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod";
    }

    public InfoViewExamByDayAndShift run(String executionCourseInitials, Season season,
            InfoExecutionPeriod infoExecutionPeriod) {
        InfoViewExamByDayAndShift infoViewExamByDayAndShift = new InfoViewExamByDayAndShift();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            IExecutionCourse executionCourse = sp.getIPersistentExecutionCourse()
                    .readByExecutionCourseInitialsAndExecutionPeriod(executionCourseInitials,
                            executionPeriod);

            for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++) {
                IExam exam = (IExam) executionCourse.getAssociatedExams().get(i);
                if (exam.getSeason().equals(season)) {
                    infoViewExamByDayAndShift.setInfoExam(Cloner.copyIExam2InfoExam(exam));

                    List infoExecutionCourses = new ArrayList();
                    List infoDegrees = new ArrayList();
                    for (int j = 0; j < exam.getAssociatedExecutionCourses().size(); j++) {
                        IExecutionCourse tempExecutionCourse = (IExecutionCourse) exam
                                .getAssociatedExecutionCourses().get(j);
                        infoExecutionCourses.add(Cloner.get(tempExecutionCourse));

                        // prepare degrees associated with exam
                        List tempAssociatedCurricularCourses = executionCourse
                                .getAssociatedCurricularCourses();
                        for (int k = 0; k < tempAssociatedCurricularCourses.size(); k++) {
                            ICurso tempDegree = ((ICurricularCourse) tempAssociatedCurricularCourses
                                    .get(k)).getDegreeCurricularPlan().getDegree();
                            infoDegrees.add(Cloner.copyIDegree2InfoDegree(tempDegree));
                        }
                    }
                    infoViewExamByDayAndShift.setInfoExecutionCourses(infoExecutionCourses);
                    infoViewExamByDayAndShift.setInfoDegrees(infoDegrees);
                }
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoViewExamByDayAndShift;
    }
}