/*
 * Created on 31/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlans implements IServico
{

    private static DeleteDegreeCurricularPlans service = new DeleteDegreeCurricularPlans();

    public static DeleteDegreeCurricularPlans getService()
    {
        return service;
    }

    private DeleteDegreeCurricularPlans()
    {
    }

    public final String getNome()
    {
        return "DeleteDegreeCurricularPlans";
    }

    // delete a set of degreeCurricularPlans
    public List run(List degreeCurricularPlansIds) throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
                sp.getIPersistentDegreeCurricularPlan();
            ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentBranch persistentBranch = sp.getIPersistentBranch();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
                sp.getIStudentCurricularPlanPersistente();

            Iterator iter = degreeCurricularPlansIds.iterator();

            List undeletedDegreeCurricularPlansNames = new ArrayList();
            List executionDegrees, curricularCourses, branches, studentCurricularPlans;
            Integer degreeCurricularPlanId;
            IDegreeCurricularPlan degreeCurricularPlan;

            while (iter.hasNext())
            {

                degreeCurricularPlanId = (Integer) iter.next();
                degreeCurricularPlan =
                    (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOID(
                        DegreeCurricularPlan.class, degreeCurricularPlanId);
                if (degreeCurricularPlan != null)
                {
                    executionDegrees =
                        persistentExecutionDegree.readByDegreeCurricularPlan(degreeCurricularPlan);
                    if (!executionDegrees.isEmpty())
                        undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
                    else
                    {
                        curricularCourses =
                            persistentCurricularCourse.readCurricularCoursesByDegreeCurricularPlan(
                                degreeCurricularPlan);
                        if (!curricularCourses.isEmpty())
                            undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
                        else
                        {
                            branches = persistentBranch.readByDegreeCurricularPlan(degreeCurricularPlan);
                            if (!branches.isEmpty())
                                undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
                            else
                            {
                                studentCurricularPlans =
                                    persistentStudentCurricularPlan.readByDegreeCurricularPlan(
                                        degreeCurricularPlan);
                                if (!studentCurricularPlans.isEmpty())
                                    undeletedDegreeCurricularPlansNames.add(
                                        degreeCurricularPlan.getName());
                                else
                                    persistentDegreeCurricularPlan.deleteDegreeCurricularPlan(
                                        degreeCurricularPlan);
                            }
                        }
                    }
                }
            }

            return undeletedDegreeCurricularPlansNames;

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

    }

}
