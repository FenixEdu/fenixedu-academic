/*
 * Created on 29/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.domain.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertTestQuestion implements IService {

    private String path = new String();

    public InsertTestQuestion() {
    }

    public boolean run(Integer executionCourseId, Integer testId, String[] metadataId,
            Integer questionOrder, Double questionValue, CorrectionFormula formula, String path)
            throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            for (int i = 0; i < metadataId.length; i++) {
                IMetadata metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class,
                        new Integer(metadataId[i]));
                if (metadata == null) {
                    throw new InvalidArgumentsServiceException();
                }
                IQuestion question = null;
                if (metadata.getVisibleQuestions() != null && metadata.getVisibleQuestions().size() != 0) {
                    question = (IQuestion) metadata.getVisibleQuestions().get(0);
                } else {
                    throw new InvalidArgumentsServiceException();
                }
                if (question == null) {
                    throw new InvalidArgumentsServiceException();
                }
                IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
                ITest test = (ITest) persistentTest.readByOID(Test.class, testId);
                if (test == null) {
                    throw new InvalidArgumentsServiceException();
                }
                IPersistentTestQuestion persistentTestQuestion = persistentSuport
                        .getIPersistentTestQuestion();
                List testQuestionList = persistentTestQuestion.readByTest(test);
                if (testQuestionList != null) {
                    if (questionOrder == null || questionOrder.equals(new Integer(-1))) {
                        questionOrder = new Integer(testQuestionList.size() + 1);
                    } else
                        questionOrder = new Integer(questionOrder.intValue() + 1);
                    Iterator it = testQuestionList.iterator();
                    while (it.hasNext()) {
                        ITestQuestion iterTestQuestion = (ITestQuestion) it.next();
                        Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
                        if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                            persistentTestQuestion.simpleLockWrite(iterTestQuestion);
                            iterTestQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder
                                    .intValue() + 1));
                        }
                    }
                }
                Double thisQuestionValue = questionValue;
                if (questionValue == null) {
                    ParseQuestion parseQuestion = new ParseQuestion();
                    try {
                        InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);
                        infoQuestion = parseQuestion.parseQuestion(infoQuestion.getXmlFile(),
                                infoQuestion, this.path);
                        thisQuestionValue = infoQuestion.getQuestionValue();
                    } catch (Exception e) {
                        throw new FenixServiceException(e);
                    }
                    if (thisQuestionValue == null)
                        thisQuestionValue = new Double(0);
                }
                ITestQuestion testQuestion = new TestQuestion();
                persistentTestQuestion.simpleLockWrite(testQuestion);
                persistentTest.simpleLockWrite(test);
                test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions().intValue() + 1));
                test.setLastModifiedDate(Calendar.getInstance().getTime());
                testQuestion.setQuestion(question);
                testQuestion.setTest(test);
                testQuestion.setTestQuestionOrder(questionOrder);
                testQuestion.setTestQuestionValue(thisQuestionValue);
                testQuestion.setCorrectionFormula(formula);
            }
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}