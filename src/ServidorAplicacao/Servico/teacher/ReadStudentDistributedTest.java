/*
 * Created on 8/Set/2003
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.TestType;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTest implements IService {

	private String path = new String();

	public ReadStudentDistributedTest() {
	}

	public List run(Integer executionCourseId, Integer distributedTestId,
			Integer studentId, String path) throws FenixServiceException {
		this.path = path.replace('\\', '/');
		ISuportePersistente persistentSuport;
		List infoStudentTestQuestionList = new ArrayList();
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			IStudent student = (IStudent) persistentSuport
					.getIPersistentStudent()
					.readByOID(Student.class, studentId);
			if (student == null)
				throw new FenixServiceException();
			IDistributedTest distributedTest = (IDistributedTest) persistentSuport
					.getIPersistentDistributedTest().readByOID(
							DistributedTest.class, distributedTestId);
			if (distributedTest == null)
				throw new FenixServiceException();
			List studentTestQuestionList = persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByStudentAndDistributedTest(student, distributedTest);
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it
						.next();
				InfoStudentTestQuestion infoStudentTestQuestion;
				ParseQuestion parse = new ParseQuestion();
				try {
					if (studentTestQuestion.getOptionShuffle() == null) {
						persistentSuport.getIPersistentStudentTestQuestion()
								.simpleLockWrite(studentTestQuestion);
						boolean shuffle = true;
						if (distributedTest.getTestType().equals(
								new TestType(3))) //INQUIRY
							shuffle = false;
						studentTestQuestion.setOptionShuffle(parse
								.shuffleQuestionOptions(studentTestQuestion
										.getQuestion().getXmlFile(), shuffle,
										this.path));
					}

					infoStudentTestQuestion = Cloner
							.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);

					infoStudentTestQuestion = parse.parseStudentTestQuestion(
							infoStudentTestQuestion, this.path);
				} catch (Exception e) {
					throw new FenixServiceException(e);
				}

				infoStudentTestQuestionList.add(infoStudentTestQuestion);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoStudentTestQuestionList;
	}
}