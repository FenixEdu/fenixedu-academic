/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsByDistributedTest implements IServico {

	private static ReadStudentsByDistributedTest service =
		new ReadStudentsByDistributedTest();

	public static ReadStudentsByDistributedTest getService() {
		return service;
	}

	public String getNome() {
		return "ReadStudentsByDistributedTest";
	}

	public List run(Integer executionCourseId, Integer distributedTestId)
		throws FenixServiceException {

		ISuportePersistente persistentSuport;
		List infoStudentList = new ArrayList();
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();

			IDistributedTest distributedTest =
				new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);

			List studentTestQuestionList =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByDistributedTest(
					distributedTest);

			Iterator it = studentTestQuestionList.iterator();

			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion =
					(IStudentTestQuestion) it.next();
				InfoStudent infoStudent =
					Cloner.copyIStudent2InfoStudent(
						studentTestQuestion.getStudent());
				if (!infoStudentList.contains(infoStudent))
					infoStudentList.add(infoStudent);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoStudentList;
	}
}
