/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class InsertTest implements IServico {

	private static InsertTest service = new InsertTest();

	public static InsertTest getService() {

		return service;
	}

	public InsertTest() {
	}

	public String getNome() {
		return "InsertTest";
	}

	public Integer run(
		Integer executionCourseId,
		String title,
		String information)
		throws FenixServiceException {
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(executionCourseId);
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test();
			test.setTitle(title);
			test.setInformation(information);
			test.setNumberOfQuestions(new Integer(0));
			test.setCreationDate(null);
			test.setLastModifiedDate(null);
			test.setExecutionCourse(executionCourse);
			test.setVisible(new Boolean(true));
			persistentTest.simpleLockWrite(test);
			return test.getIdInternal();
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
