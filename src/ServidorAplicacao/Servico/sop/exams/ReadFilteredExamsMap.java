/*
 * Created on 2003/10/20
 */

package ServidorAplicacao.Servico.sop.exams;

/**
 * @author Ana & Ricardo
 *          
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadFilteredExamsMap implements IServico
{

    private static ReadFilteredExamsMap servico = new ReadFilteredExamsMap();
    /**
     * The singleton access method of this class.
     */
    public static ReadFilteredExamsMap getService()
    {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadFilteredExamsMap()
    {
    }

    /**
     * Devolve o nome do servico
     */
    public String getNome()
    {
        return "ReadFilteredExamsMap";
    }

    public InfoExamsMap run(
        InfoExecutionDegree infoExecutionDegree,
        List curricularYears,
        InfoExecutionPeriod infoExecutionPeriod)
    {

        // Object to be returned
        InfoExamsMap infoExamsMap = new InfoExamsMap();

        // Set Execution Degree
        infoExamsMap.setInfoExecutionDegree(infoExecutionDegree);

        // Set List of Curricular Years
        infoExamsMap.setCurricularYears(curricularYears);

        // Exam seasons hardcoded because this information
        // is not yet available from the database
        Calendar startSeason1 = Calendar.getInstance();
        startSeason1.set(Calendar.YEAR, 2004);
        startSeason1.set(Calendar.MONTH, Calendar.JUNE);
        startSeason1.set(Calendar.DAY_OF_MONTH, 14);
        startSeason1.set(Calendar.HOUR_OF_DAY, 0);
        startSeason1.set(Calendar.MINUTE, 0);
        startSeason1.set(Calendar.SECOND, 0);
        startSeason1.set(Calendar.MILLISECOND, 0);
        Calendar endSeason2 = Calendar.getInstance();
        endSeason2.set(Calendar.YEAR, 2004);
        endSeason2.set(Calendar.MONTH, Calendar.JULY);
        endSeason2.set(Calendar.DAY_OF_MONTH, 24);
        endSeason2.set(Calendar.HOUR_OF_DAY, 0);
        endSeason2.set(Calendar.MINUTE, 0);
        endSeason2.set(Calendar.SECOND, 0);
        endSeason2.set(Calendar.MILLISECOND, 0);

		if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla().equals("LEC"))
		{
			startSeason1.set(Calendar.DAY_OF_MONTH, 21);
			endSeason2.set(Calendar.DAY_OF_MONTH, 17);
		}
		if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla().equals("LET"))
		{
			startSeason1.set(Calendar.DAY_OF_MONTH, 21);
			endSeason2.set(Calendar.DAY_OF_MONTH, 17);
		}
		if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla().equals("LA"))
		{
			startSeason1.set(Calendar.DAY_OF_MONTH, 21);
			endSeason2.set(Calendar.DAY_OF_MONTH, 17);
		}

        // Set Exam Season info
        infoExamsMap.setStartSeason1(startSeason1);
        infoExamsMap.setEndSeason1(null);
        infoExamsMap.setStartSeason2(null);
        infoExamsMap.setEndSeason2(endSeason2);

        // Translate to execute following queries
        ICursoExecucao executionDegree =
            Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
        IExecutionPeriod executionPeriod =
            Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            // List of execution courses
            List infoExecutionCourses = new ArrayList();

            // Obtain execution courses and associated information
            // of the given execution degree for each curricular year specified
            for (int i = 0; i < curricularYears.size(); i++)
            {
                // Obtain list os execution courses
                List executionCourses =
                    sp
                        .getIPersistentExecutionCourse()
                        .readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                        (Integer) curricularYears.get(i),
                        executionPeriod,
                        executionDegree);

                // For each execution course obtain curricular courses and
                // exams
                for (int j = 0; j < executionCourses.size(); j++)
                {
                    InfoExecutionCourse infoExecutionCourse =
                        (InfoExecutionCourse) Cloner.get((IExecutionCourse) executionCourses.get(j));

                    infoExecutionCourse.setCurricularYear((Integer) curricularYears.get(i));

                    List associatedInfoCurricularCourses = new ArrayList();
                    List associatedCurricularCourses =
                        ((IExecutionCourse) executionCourses.get(j)).getAssociatedCurricularCourses();
                    // Curricular courses
                    for (int k = 0; k < associatedCurricularCourses.size(); k++)
                    {
                        InfoCurricularCourse infoCurricularCourse =
                            Cloner.copyCurricularCourse2InfoCurricularCourse(
                                (ICurricularCourse) associatedCurricularCourses.get(k));
                        associatedInfoCurricularCourses.add(infoCurricularCourse);
                    }
                    infoExecutionCourse.setAssociatedInfoCurricularCourses(
                        associatedInfoCurricularCourses);

                    List associatedInfoExams = new ArrayList();
                    List associatedExams =
                        ((IExecutionCourse) executionCourses.get(j)).getAssociatedExams();
                    // Exams
                    for (int k = 0; k < associatedExams.size(); k++)
                    {
                    	if (! (associatedExams.get(k) instanceof IExam))
						{
							continue;
						}
                        InfoExam infoExam = Cloner.copyIExam2InfoExam((IExam) associatedExams.get(k));

                        List associatedCurricularCourseScope = new ArrayList();
                        associatedCurricularCourseScope = infoExam.getAssociatedCurricularCourseScope();

                        for (int h = 0; h < associatedCurricularCourseScope.size(); h++)
                        {
                            InfoCurricularCourseScope infoCurricularCourseScope =
                                (InfoCurricularCourseScope) associatedCurricularCourseScope.get(h);

                            InfoCurricularYear infoCurricularYear =
                                infoCurricularCourseScope
                                    .getInfoCurricularSemester()
                                    .getInfoCurricularYear();

                            boolean isCurricularYearEqual =
                                infoCurricularYear.getYear().equals(curricularYears.get(i));

                            //obter o curricular plan a partir do curricular course scope
                            InfoDegreeCurricularPlan degreeCurricularPlanFromScope =
                                infoCurricularCourseScope
                                    .getInfoCurricularCourse()
                                    .getInfoDegreeCurricularPlan();

                            //obter o curricular plan a partir do info degree
                            InfoDegreeCurricularPlan infoDegreeCurricularPlan =
                                infoExecutionDegree.getInfoDegreeCurricularPlan();

                            boolean isCurricularPlanEqual =
                                degreeCurricularPlanFromScope.equals(infoDegreeCurricularPlan);

                            if (isCurricularYearEqual
                                && isCurricularPlanEqual
                                && !associatedInfoExams.contains(infoExam))
                            {
                                associatedInfoExams.add(infoExam);
                                break;
                            }
                        }
                    }
                    infoExecutionCourse.setAssociatedInfoExams(associatedInfoExams);

                    infoExecutionCourses.add(infoExecutionCourse);
                }
            }
            infoExamsMap.setExecutionCourses(infoExecutionCourses);

        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }

        return infoExamsMap;
    }

}