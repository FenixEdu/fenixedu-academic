/*
 * ReadExamsByExecutionCourse.java
 * 
 * Created on 2003/05/26
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseAndExams;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoViewAllExams;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Season;
import Util.TipoCurso;

public class ReadExamsSortedByExecutionDegreeAndCurricularYear implements IServico
{

    private static ReadExamsSortedByExecutionDegreeAndCurricularYear _servico =
        new ReadExamsSortedByExecutionDegreeAndCurricularYear();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadExamsSortedByExecutionDegreeAndCurricularYear getService()
    {
        return _servico;
    }

    /**
	 * The actor of this class.
	 */
    private ReadExamsSortedByExecutionDegreeAndCurricularYear()
    {
    }

    /**
	 * Devolve o nome do servico
	 */
    public final String getNome()
    {
        return "ReadExamsSortedByExecutionDegreeAndCurricularYear";
    }

    public List run(InfoExecutionPeriod infoExecutionPeriod)
    {

        List infoViewAllExamsList = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();

            IExecutionPeriod executionPeriod =
                Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            IExecutionYear executionYear = executionPeriod.getExecutionYear();

            List executionDegrees =
                executionDegreeDAO.readByExecutionYearAndDegreeType(
                    executionYear,
                    new TipoCurso(TipoCurso.LICENCIATURA));

            for (int k = 0; k < executionDegrees.size(); k++)
            {
                InfoExecutionDegree infoExecutionDegree =
                    (InfoExecutionDegree) Cloner.get(
                    (ICursoExecucao) executionDegrees.get(k));

                for (int curricularYear = 1; curricularYear <= 5; curricularYear++)
                {
                    InfoViewAllExams infoViewAllExams =
                        new InfoViewAllExams(infoExecutionDegree, new Integer(curricularYear), null);

                    ArrayList infoExecutionCourseAndExamsList = new ArrayList();

                    ICursoExecucao executionDegree = (ICursoExecucao) executionDegrees.get(k);

                    List executionCourses =
                        sp
                            .getIPersistentExecutionCourse()
                            .readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                            new Integer(curricularYear),
                            executionPeriod,
                            executionDegree);

                    for (int i = 0; i < executionCourses.size(); i++)
                    {
                        IExecutionCourse executionCourse = (IExecutionCourse) executionCourses.get(i);

                        InfoExecutionCourseAndExams infoExecutionCourseAndExams =
                            new InfoExecutionCourseAndExams();

                        infoExecutionCourseAndExams.setInfoExecutionCourse(
                            (InfoExecutionCourse) Cloner.get(executionCourse));

                        for (int j = 0; j < executionCourse.getAssociatedExams().size(); j++)
                        {
                            IExam exam = (IExam) executionCourse.getAssociatedExams().get(j);
                            if (exam.getSeason().getseason().intValue() == Season.SEASON1)
                            {
                                infoExecutionCourseAndExams.setInfoExam1(
                                    Cloner.copyIExam2InfoExam(exam));
                            }
                            else if (exam.getSeason().getseason().intValue() == Season.SEASON2)
                            {
                                infoExecutionCourseAndExams.setInfoExam2(
                                    Cloner.copyIExam2InfoExam(exam));
                            }
                        }

                        // Number of students attending execution course.
                        // TODO : In this context should we realy count the
                        // number of
                        //        students attending the course or just the ones from
                        //        the indicated degree????
                        infoExecutionCourseAndExams.setNumberStudentesAttendingCourse(
                            sp.getIFrequentaPersistente().countStudentsAttendingExecutionCourse(
                                executionCourse));

                        infoExecutionCourseAndExamsList.add(infoExecutionCourseAndExams);
                    }

                    if (infoExecutionCourseAndExamsList != null
                        && infoExecutionCourseAndExamsList.isEmpty())
                    {
                        infoViewAllExams.setInfoExecutionCourseAndExamsList(null);
                    }
                    else
                    {
                        infoViewAllExams.setInfoExecutionCourseAndExamsList(
                            infoExecutionCourseAndExamsList);
                    }
                    infoViewAllExamsList.add(infoViewAllExams);

                }
            }

        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }

        return infoViewAllExamsList;
    }
}