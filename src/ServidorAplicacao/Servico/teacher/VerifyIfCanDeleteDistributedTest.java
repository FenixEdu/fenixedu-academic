/*
 * Created on 23/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TestType;

/**
 * @author Susana Fernandes
 */
public class VerifyIfCanDeleteDistributedTest implements IService
{
	public VerifyIfCanDeleteDistributedTest()
	{
	}

	public boolean run(Integer executionCourseId, Integer distributedTestId)
			throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IDistributedTest distributedTest = (IDistributedTest) persistentSuport
					.getIPersistentDistributedTest().readByOId(new DistributedTest(distributedTestId),
							false);
			if (distributedTest == null)
			{
				throw new InvalidArgumentsServiceException();
			}
			if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION)
			{
				if (persistentSuport.getIPersistentStudentTestQuestion().countResponsedOrNotResponsed(
						null, true, distributedTest) != 0)
					return false;
			}
			return true;
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}