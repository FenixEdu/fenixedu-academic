/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestLog;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestLog;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestLog;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest implements IServico {
	private static ReadStudentTest service = new ReadStudentTest();
	private String path = new String();

	public static ReadStudentTest getService() {
		return service;
	}

	public String getNome() {
		return "ReadStudentTest";
	}

	public Object run(
		String userName,
		Integer distributedTestId,
		Boolean log,
		String path)
		throws FenixServiceException {
		List infoStudentTestQuestionList = new ArrayList();
		this.path = path.replace('\\', '/');
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IStudent student =
				persistentSuport.getIPersistentStudent().readByUsername(
					userName);
			if (student == null)
				throw new FenixServiceException();
			IDistributedTest distributedTest =
				new DistributedTest(distributedTestId);
            distributedTest =
            (IDistributedTest) persistentSuport
            .getIPersistentDistributedTest()
            .readByOId(
                    distributedTest,
                    false);
			if (distributedTest == null) {
              		throw new FenixServiceException();
            }
			
			List studentTestQuestionList =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByStudentAndDistributedTest(student, distributedTest);
			if (studentTestQuestionList.size() == 0)
				throw new FenixServiceException();
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion =
					(IStudentTestQuestion) it.next();
				InfoStudentTestQuestion infoStudentTestQuestion =
					Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(
						studentTestQuestion);
				ParseQuestion parse = new ParseQuestion();
				try {
					infoStudentTestQuestion.setQuestion(
						parse.parseQuestion(
							infoStudentTestQuestion.getQuestion().getXmlFile(),
							infoStudentTestQuestion.getQuestion(),
							path));
					infoStudentTestQuestion =
						parse.getOptionsShuffle(infoStudentTestQuestion, path);
				} catch (Exception e) {
					throw new FenixServiceException(e);
				}

				infoStudentTestQuestionList.add(infoStudentTestQuestion);
			}
			if (log.booleanValue()) {
				IPersistentStudentTestLog persistentStudentTestLog =
					persistentSuport.getIPersistentStudentTestLog();

				IStudentTestLog studentTestLog = new StudentTestLog();
				studentTestLog.setDistributedTest(distributedTest);
				studentTestLog.setStudent(student);
				studentTestLog.setDate(null);
				studentTestLog.setEvent(new String("Ler Ficha de Trabalho"));

				persistentStudentTestLog.simpleLockWrite(studentTestLog);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoStudentTestQuestionList;
	}

}
