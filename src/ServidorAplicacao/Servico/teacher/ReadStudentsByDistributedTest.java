/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.Student;
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

			List studentList =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readStudentsByDistributedTest(distributedTest);
			Iterator it = studentList.iterator();
			while (it.hasNext()) {
				IStudent student = (Student) it.next();
				infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoStudentList;
	}
}
