/*
 * Created on 31/Jan/2004
 */
package middleware.degreeCurricularPlan;

import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author jmota
 */
public class FixCurriculum
{
    public static void main(String[] args)
    {
        try
        {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            ps.iniciarTransaccao();
            IPersistentExecutionYear persistentExecutionYear = ps
                    .getIPersistentExecutionYear();
            IPersistentCurriculum persistentCurriculum = ps
                    .getIPersistentCurriculum();
            IExecutionYear executionYear = persistentExecutionYear
                    .readCurrentExecutionYear();
            ICursoExecucaoPersistente persistentExecutionDegree = ps
                    .getICursoExecucaoPersistente();
            List executionDegrees = persistentExecutionDegree
                    .readByExecutionYearAndDegreeType(executionYear,
                            new TipoCurso(TipoCurso.LICENCIATURA));
            Iterator iter = executionDegrees.iterator();
            while (iter.hasNext())
            {
                ICursoExecucao executionDegree = (ICursoExecucao) iter.next();
                IDegreeCurricularPlan degreeCurricularPlan = executionDegree
                        .getCurricularPlan();
                IDegreeCurricularPlan oldDegreeCurricularPlan = null;
                ICurso degree = degreeCurricularPlan.getDegree();
                List curricularPlans = degree.getDegreeCurricularPlans();
                Iterator iter3 = curricularPlans.iterator();
                while (iter3.hasNext())
                {
                    IDegreeCurricularPlan degreeCurricularPlanAux = (IDegreeCurricularPlan) iter3
                            .next();
                    if (!degreeCurricularPlan.getIdInternal().equals(
                            degreeCurricularPlanAux.getIdInternal())
                            && !degreeCurricularPlanAux.getName().startsWith(
                                    "PAST")
                            && !degreeCurricularPlanAux.getName()
                                    .equalsIgnoreCase("LEIC 2003"))
                    {
                        oldDegreeCurricularPlan = degreeCurricularPlanAux;
                        break;
                    }

                }
                if (oldDegreeCurricularPlan != null)
                {
                    System.out.println(oldDegreeCurricularPlan.getName());

                    List curricularCourses = degreeCurricularPlan
                            .getCurricularCourses();
                    Iterator iter2 = curricularCourses.iterator();
                    while (iter2.hasNext())
                    {
                        ICurricularCourse curricularCourse = (ICurricularCourse) iter2
                                .next();
                        List curriculums = persistentCurriculum
                                .readCurriculumHistoryByCurricularCourse(curricularCourse);
                        if (curriculums == null || curriculums.size() < 1)
                        {
                            List oldCurricularCourses = oldDegreeCurricularPlan
                                    .getCurricularCourses();
                            Iterator iter4 = oldCurricularCourses.iterator();
                            while (iter4.hasNext())
                            {
                                ICurricularCourse oldCurricularCourse = (ICurricularCourse) iter4
                                        .next();
                                if (curricularCourse.getCode()
                                        .equalsIgnoreCase(
                                                curricularCourse.getCode()))
                                {
                                    List oldCurriculums = persistentCurriculum
                                            .readCurriculumHistoryByCurricularCourse(oldCurricularCourse);
                                    if (oldCurriculums != null
                                            && !oldCurriculums.isEmpty())
                                    {
                                        Iterator iterator = oldCurriculums
                                                .iterator();
                                        while (iterator.hasNext())
                                        {
                                            ICurriculum curriculum = (ICurriculum) iterator
                                                    .next();
                                            curriculum
                                                    .setCurricularCourse(curricularCourse);
                                        }

                                    }
                                    break;
                                }

                            }

                        }

                    }

                }
            }
            ps.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia e)
        {
         
            e.printStackTrace();
        }
    }
}
