/*
 * Created on 9/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.CurricularCourse;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class AssociateExecutionCourseToCurricularCourse implements IServico
{

    private static AssociateExecutionCourseToCurricularCourse service =
        new AssociateExecutionCourseToCurricularCourse();

    public static AssociateExecutionCourseToCurricularCourse getService()
    {
        return service;
    }

    private AssociateExecutionCourseToCurricularCourse()
    {
    }

    public final String getNome()
    {
        return "AssociateExecutionCourseToCurricularCourse";
    }

    public void run(Integer executionCourseId, Integer curricularCourseId, Integer executionPeriodId)
        throws FenixServiceException
    {

        try
        {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse persistentExecutionCourse =
                persistentSuport.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse;

            IPersistentCurricularCourse persistentCurricularCourse =
                persistentSuport.getIPersistentCurricularCourse();
            ICurricularCourse curricularCourse =
                (ICurricularCourse) persistentCurricularCourse.readByOId(
                    new CurricularCourse(curricularCourseId),
                    false);

            if (curricularCourse == null)
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);

            List executionCourses = curricularCourse.getAssociatedExecutionCourses();
            if (executionCourses == null) {
                executionCourses = new ArrayList();
            }
            Iterator iter = executionCourses.iterator();

            while (iter.hasNext())
            {
                Integer associatedExecutionPeriodId =
                    ((IExecutionCourse) iter.next()).getExecutionPeriod().getIdInternal();
                if (associatedExecutionPeriodId.equals(executionPeriodId))
                    throw new ExistingServiceException("message.unavailable.execution.period", null);
            }

            executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(
                    new ExecutionCourse(executionCourseId),
                    false);
            if (executionCourse == null)
                throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
            else
            {
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                if (!executionCourses.contains(executionCourse)
                    && !curricularCourses.contains(curricularCourse))
                {
                    persistentCurricularCourse.simpleLockWrite(curricularCourse);
                    executionCourses.add(executionCourse);
                    curricularCourses.add(curricularCourse);
                    curricularCourse.setAssociatedExecutionCourses(executionCourses);
                    executionCourse.setAssociatedCurricularCourses(curricularCourses);
                }
            }
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}