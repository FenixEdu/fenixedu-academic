/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/04/04
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.Season;

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
                            IDegree tempDegree = ((ICurricularCourse) tempAssociatedCurricularCourses
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