/*
 * Created on 29/Jan/2004
 *
 */
package ServidorAplicacao;

import java.util.Iterator;
import java.util.List;

import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.ITest;
import Dominio.ITestScope;
import Dominio.Test;
import Dominio.TestScope;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 *
 * @author Susana Fernandes
 *
 */
public class UpdateTestsFields
{
	public static void main(String[] args) throws Exception
	{

		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		persistentSuport.iniciarTransaccao();
		Integer executionCourseId = new Integer(34882);
		System.out.println("executionCourseId: " + executionCourseId);
		IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
		executionCourse =
			(IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOId(
				executionCourse,
				false);
		if (executionCourse == null)
			throw new InvalidArgumentsServiceException();

		ITestScope testScope =
			persistentSuport.getIPersistentTestScope().readByDomainObject(executionCourse);
		if (testScope == null)
		{
			testScope = new TestScope(executionCourse);
			persistentSuport.getIPersistentTestScope().simpleLockWrite(testScope);
		}
		persistentSuport.confirmarTransaccao();

		persistentSuport.iniciarTransaccao();
		List distributedTestsList = persistentSuport.getIPersistentDistributedTest().readAll();
		Iterator it = distributedTestsList.iterator();
		while (it.hasNext())
		{
			IDistributedTest distributedTest = (DistributedTest) it.next();
			distributedTest.setTestScope(testScope);
			persistentSuport.getIPersistentDistributedTest().simpleLockWrite(distributedTest);
		}
		persistentSuport.confirmarTransaccao();

		persistentSuport.iniciarTransaccao();
		List testsList = persistentSuport.getIPersistentTest().readAll();
		it = testsList.iterator();
		while (it.hasNext())
		{
			ITest test = (Test) it.next();
			test.setTestScope(testScope);
			persistentSuport.getIPersistentTest().simpleLockWrite(test);
		}
		persistentSuport.confirmarTransaccao();
	}
}
