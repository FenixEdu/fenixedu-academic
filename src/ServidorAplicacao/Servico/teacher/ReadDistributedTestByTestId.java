/*
 * Created on 25/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoDistributedTest;
import DataBeans.util.Cloner;
import Dominio.IDistributedTest;
import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTestByTestId implements IServico {

	private static ReadDistributedTestByTestId service =
		new ReadDistributedTestByTestId();

	public static ReadDistributedTestByTestId getService() {
		return service;
	}

	public String getNome() {
		return "ReadDistributedTestByTestId";
	}

	public List run(Integer executionCourseId, Integer testId)
		throws FenixServiceException {

		ISuportePersistente persistentSuport;
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();

			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, false);
			if (test == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentDistributedTest persistentDistrubutedTest =
				(IPersistentDistributedTest) persistentSuport
					.getIPersistentDistributedTest();
			List distributedTests = persistentDistrubutedTest.readByTest(test);
			List result = new ArrayList();
			Iterator iter = distributedTests.iterator();
			while (iter.hasNext()) {
				IDistributedTest distributedTest =
					(IDistributedTest) iter.next();
				InfoDistributedTest infoDistributedTest =
					Cloner.copyIDistributedTest2InfoDistributedTest(
						distributedTest);
				result.add(infoDistributedTest);
			}
			return result;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
