/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class DeleteTest implements IServico {

	private static DeleteTest service = new DeleteTest();

	public static DeleteTest getService() {

		return service;
	}

	public DeleteTest() {
	}

	public String getNome() {
		return "DeleteTest";
	}

	public boolean run(Integer executionCourseId, Integer testId)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, true);
			if (test == null)
				throw new FenixServiceException();

			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			persistentTestQuestion.deleteByTest(test);
			persistentTest.delete(test);

			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
