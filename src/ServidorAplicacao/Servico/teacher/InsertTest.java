/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Date;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITest;
import Dominio.ITestScope;
import Dominio.Test;
import Dominio.TestScope;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class InsertTest implements IServico
{

	private static InsertTest service = new InsertTest();

	public static InsertTest getService()
	{

		return service;
	}

	public InsertTest()
	{
	}

	public String getNome()
	{
		return "InsertTest";
	}

	public Integer run(Integer executionCourseId, String title, String information)
		throws FenixServiceException
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
			
			ITestScope testScope =
				persistentSuport.getIPersistentTestScope().readByDomainObject(
            	executionCourse);
			if (testScope == null)
			{
				testScope = new TestScope(executionCourse);
				persistentSuport.getIPersistentTestScope().simpleLockWrite(testScope);
			}

			IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
			ITest test = new Test();
			test.setTitle(title);
			test.setInformation(information);
			test.setNumberOfQuestions(new Integer(0));
			Date date = Calendar.getInstance().getTime();
			test.setCreationDate(date);
			test.setLastModifiedDate(date);
			test.setTestScope(testScope);
			persistentTest.simpleLockWrite(test);
			return test.getIdInternal();
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}
