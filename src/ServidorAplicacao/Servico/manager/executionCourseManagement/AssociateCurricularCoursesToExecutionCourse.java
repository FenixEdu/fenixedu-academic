package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.CurricularCourse;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * 
 * @author Fernanda Quitério 29/Dez/2003
 */
public class AssociateCurricularCoursesToExecutionCourse implements IServico
{

	private static AssociateCurricularCoursesToExecutionCourse service =
		new AssociateCurricularCoursesToExecutionCourse();

	public static AssociateCurricularCoursesToExecutionCourse getService()
	{
		return service;
	}

	private AssociateCurricularCoursesToExecutionCourse()
	{
	}

	public final String getNome()
	{
		return "AssociateCurricularCoursesToExecutionCourse";
	}

	public void run(Integer executionCourseId, List curricularCourseIds) throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				persistentSuport.getIPersistentExecutionCourse();
			IPersistentCurricularCourse persistentCurricularCourse =
				persistentSuport.getIPersistentCurricularCourse();

			if (executionCourseId == null)
			{
				throw new FenixServiceException("nullExecutionCourseId");
			}

			if (curricularCourseIds != null)
			{
				IExecutionCourse executionCourse = new ExecutionCourse();
				executionCourse.setIdInternal(executionCourseId);
				executionCourse =
					(IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, true);

				if (executionCourse == null)
				{
					throw new NonExistingServiceException("noExecutionCourse");
				}

				List curricularCourses = executionCourse.getAssociatedCurricularCourses();
				List curricularCoursesToAssociate = new ArrayList();
				Iterator iter = curricularCourseIds.iterator();
				while (iter.hasNext())
				{
					Integer curricularCourseId = (Integer) iter.next();

					ICurricularCourse curricularCourse = new CurricularCourse();
					curricularCourse.setIdInternal(curricularCourseId);
					curricularCourse =
						(ICurricularCourse) persistentCurricularCourse.readByOId(
							curricularCourse,
							false);
					if (curricularCourse == null)
					{
						throw new NonExistingServiceException("noCurricularCourse");
					}
					List executionCourses = curricularCourse.getAssociatedExecutionCourses();
					if(!curricularCourses.contains(curricularCourse) && !executionCourses.contains(executionCourse)) {
						curricularCoursesToAssociate.add(curricularCourse);
						executionCourses.add(executionCourse);
						curricularCourse.setAssociatedExecutionCourses(executionCourses);
					}
				}
				curricularCourses.addAll(curricularCoursesToAssociate);
				executionCourse.setAssociatedCurricularCourses(curricularCourses);
			}
		}
		catch (ExcepcaoPersistencia excepcaoPersistencia)
		{
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}