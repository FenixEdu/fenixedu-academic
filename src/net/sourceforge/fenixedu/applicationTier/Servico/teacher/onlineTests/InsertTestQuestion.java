/*
 * Created on 29/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertTestQuestion implements IService {

	private String path = new String();

	public void run(Integer executionCourseId, Integer testId, String[] metadataId,
			Integer questionOrder, Double questionValue, CorrectionFormula formula, String path)
			throws FenixServiceException, ExcepcaoPersistencia {
		this.path = path.replace('\\', '/');

		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		for (int i = 0; i < metadataId.length; i++) {
			Metadata metadata = (Metadata) persistentSuport.getIPersistentMetadata().readByOID(
					Metadata.class, new Integer(metadataId[i]));
			if (metadata == null) {
				throw new InvalidArgumentsServiceException();
			}
			Question question = null;
			if (metadata.getVisibleQuestions() != null && metadata.getVisibleQuestions().size() != 0) {
				question = metadata.getVisibleQuestions().get(0);
			} else {
				throw new InvalidArgumentsServiceException();
			}
			if (question == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
			Test test = (Test) persistentTest.readByOID(Test.class, testId);
			if (test == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTestQuestion persistentTestQuestion = persistentSuport
					.getIPersistentTestQuestion();
			List<TestQuestion> testQuestionList = persistentTestQuestion.readByTest(testId);
			if (testQuestionList != null) {
				if (questionOrder == null || questionOrder.equals(new Integer(-1))) {
					questionOrder = new Integer(testQuestionList.size() + 1);
				} else
					questionOrder = new Integer(questionOrder.intValue() + 1);
				for (TestQuestion iterTestQuestion : testQuestionList) {
					Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
					if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
						iterTestQuestion.setTestQuestionOrder(new Integer(
								iterQuestionOrder.intValue() + 1));
					}
				}
			}
			Double thisQuestionValue = questionValue;
			if (questionValue == null) {
				ParseQuestion parseQuestion = new ParseQuestion();
				try {
					InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);
					infoQuestion = parseQuestion.parseQuestion(infoQuestion.getXmlFile(), infoQuestion,
							this.path);
					thisQuestionValue = infoQuestion.getQuestionValue();
				} catch (Exception e) {
					throw new FenixServiceException(e);
				}
				if (thisQuestionValue == null)
					thisQuestionValue = new Double(0);
			}
			TestQuestion testQuestion = DomainFactory.makeTestQuestion();
			test.setLastModifiedDate(Calendar.getInstance().getTime());
			testQuestion.setQuestion(question);
			testQuestion.setTest(test);
			testQuestion.setTestQuestionOrder(questionOrder);
			testQuestion.setTestQuestionValue(thisQuestionValue);
			testQuestion.setCorrectionFormula(formula);
		}
	}
}