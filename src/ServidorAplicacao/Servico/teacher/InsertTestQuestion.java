/*
 * Created on 29/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoMetadata;
import DataBeans.util.Cloner;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Metadata;
import Dominio.Test;
import Dominio.TestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 */
public class InsertTestQuestion implements IServico {
	private static InsertTestQuestion service = new InsertTestQuestion();

	public static InsertTestQuestion getService() {
		return service;
	}

	public InsertTestQuestion() {
	}

	public String getNome() {
		return "InsertTestQuestion";
	}
	public boolean run(
		Integer executionCourseId,
		Integer testId,
		Integer metadataId,
		Integer questionOrder,
		Integer questionValue)
		throws FenixServiceException {
		try {

			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata(metadataId);
			metadata =
				(IMetadata) persistentMetadata.readByOId(metadata, false);
			if (metadata == null) {
				throw new InvalidArgumentsServiceException();
			}
			InfoMetadata infoMetadata =
				Cloner.copyIMetadata2InfoMetadata(metadata);
			ParseMetadata p = new ParseMetadata();
			try {
				infoMetadata =
					p.parseMetadata(metadata.getMetadataFile(), infoMetadata);
			} catch (Exception e) {
				throw new FenixServiceException(e);
			}
			String xmlFileName = (String) infoMetadata.getMembers().get(0);
			if (xmlFileName == null) {
				throw new InvalidArgumentsServiceException();
			}

			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			IQuestion question =
				(IQuestion) persistentQuestion.readByFileNameAndMetadataId(
					xmlFileName,
					metadata);
			if (question == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, false);
			if (test == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			List testQuestionList = persistentTestQuestion.readByTest(test);
			if (testQuestionList != null) {
				if (questionOrder.equals(new Integer(-1))) {
					questionOrder = new Integer(testQuestionList.size() + 1);
				} else
					questionOrder = new Integer(questionOrder.intValue() + 1);
				Iterator it = testQuestionList.iterator();
				while (it.hasNext()) {
					ITestQuestion iterTestQuestion = (ITestQuestion) it.next();
					Integer iterQuestionOrder =
						iterTestQuestion.getTestQuestionOrder();

					if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
						persistentTestQuestion.simpleLockWrite(
							iterTestQuestion);
						iterTestQuestion.setTestQuestionOrder(
							new Integer(iterQuestionOrder.intValue() + 1));
					}
				}
			}
			ITestQuestion testQuestion = new TestQuestion();
			test.setNumberOfQuestions(
				new Integer(test.getNumberOfQuestions().intValue() + 1));
			persistentTest.simpleLockWrite(test);
			testQuestion.setQuestion(question);
			testQuestion.setTest(test);
			testQuestion.setTestQuestionOrder(questionOrder);
			testQuestion.setTestQuestionValue(questionValue);
			persistentTestQuestion.simpleLockWrite(testQuestion);

			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
