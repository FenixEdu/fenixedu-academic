/*
 * Created on 5/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Question;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class DeleteTestQuestion implements IServico {

	private static DeleteTestQuestion service = new DeleteTestQuestion();

	public static DeleteTestQuestion getService() {

		return service;
	}

	public DeleteTestQuestion() {
	}

	public String getNome() {
		return "DeleteTestQuestion";
	}
	public boolean run(
		Integer executionCourseId,
		Integer testId,
		Integer questionId)
		throws FenixServiceException {
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, true);
			if (test == null)
				throw new InvalidArgumentsServiceException();
			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			IQuestion question = new Question(questionId);
			question =
				(IQuestion) persistentQuestion.readByOId(question, false);
			if (question == null)
				throw new InvalidArgumentsServiceException();

			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			List testQuestionList = persistentTestQuestion.readByTest(test);

			ITestQuestion testQuestion =
				persistentTestQuestion.readByTestAndQuestion(test, question);

			testQuestion =
				(ITestQuestion) persistentTestQuestion.readByOId(
					testQuestion,
					true);
			if (testQuestion == null)
				throw new InvalidArgumentsServiceException();

			Integer questionOrder = testQuestion.getTestQuestionOrder();

			if (testQuestionList != null) {
				Iterator it = testQuestionList.iterator();
				while (it.hasNext()) {
					ITestQuestion iterTestQuestion = (ITestQuestion) it.next();
					Integer iterQuestionOrder =
						iterTestQuestion.getTestQuestionOrder();

					if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
						persistentTestQuestion.simpleLockWrite(
							iterTestQuestion);
						iterTestQuestion.setTestQuestionOrder(
							new Integer(iterQuestionOrder.intValue() - 1));
					}
				}
			}
			persistentTestQuestion.delete(testQuestion);
			test.setNumberOfQuestions(
				new Integer(test.getNumberOfQuestions().intValue() - 1));
			test.setLastModifiedDate(Calendar.getInstance().getTime());
			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}