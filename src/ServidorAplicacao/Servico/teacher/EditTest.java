/*
 * Created on 1/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class EditTest implements IServico {
	private static EditTest service = new EditTest();

	public static EditTest getService() {
		return service;
	}

	public EditTest() {
	}

	public String getNome() {
		return "EditTest";
	}

	public boolean run(
		Integer executionCourseId,
		Integer testId,
		String title,
		String information)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();

			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, true);

			test.setTitle(title);
			test.setInformation(information);
			test.setLastModifiedDate(null);
			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
