/*
 * Created on 28/Ago/2003
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestLog;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestLog;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestLog;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.TestType;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest implements IService {

	private String path = new String();

	public ReadStudentTest() {
	}

	public Object run(String userName, Integer distributedTestId, Boolean log,
			String path) throws FenixServiceException {
		List infoStudentTestQuestionList = new ArrayList();
		this.path = path.replace('\\', '/');
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB
					.getInstance();
			IStudent student = persistentSuport.getIPersistentStudent()
					.readByUsername(userName);
			if (student == null)
				throw new FenixServiceException();
			IDistributedTest distributedTest = (IDistributedTest) persistentSuport
					.getIPersistentDistributedTest().readByOID(
							DistributedTest.class, distributedTestId);
			if (distributedTest == null) {
				throw new InvalidArgumentsServiceException();
			}

			List studentTestQuestionList = persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByStudentAndDistributedTest(student, distributedTest);
			if (studentTestQuestionList.size() == 0)
				throw new InvalidArgumentsServiceException();
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
			if (log.booleanValue()) {
				IPersistentStudentTestLog persistentStudentTestLog = persistentSuport
						.getIPersistentStudentTestLog();

				IStudentTestLog studentTestLog = new StudentTestLog();
				studentTestLog.setDistributedTest(distributedTest);
				studentTestLog.setStudent(student);
				studentTestLog.setDate(Calendar.getInstance().getTime());
				studentTestLog.setEvent(new String("Ler Ficha de Trabalho"));

				persistentStudentTestLog.simpleLockWrite(studentTestLog);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoStudentTestQuestionList;
	}

}