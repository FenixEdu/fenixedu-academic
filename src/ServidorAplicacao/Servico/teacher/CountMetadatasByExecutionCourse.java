/*
 * Created on 23/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */

public class CountMetadatasByExecutionCourse implements IServico
{

	private static CountMetadatasByExecutionCourse service = new CountMetadatasByExecutionCourse();

	public static CountMetadatasByExecutionCourse getService()
	{
		return service;
	}

	public String getNome()
	{
		return "CountMetadatasByExecutionCourse";
	}
	public Integer run(Integer executionCourseId) throws FenixServiceException
	{

		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				persistentSuport.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
			if (executionCourse == null)
			{
				throw new InvalidArgumentsServiceException();
			}

			return new Integer(
				persistentSuport.getIPersistentMetadata().countByExecutionCourse(executionCourse));

		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}