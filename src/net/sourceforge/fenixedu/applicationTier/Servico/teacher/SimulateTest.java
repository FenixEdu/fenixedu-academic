/*
 * Created on 3/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.IQuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.QuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.ITestScope;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.domain.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.domain.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class SimulateTest implements IService {

    public SimulateTest() {
    }

    private String path = new String();

    public InfoSiteStudentTestFeedback run(Integer executionCourseId, Integer testId,
            Response[] responses, String[] questionCodes, String[] optionShuffle, TestType testType,
            CorrectionAvailability correctionAvailability, Boolean imsfeedback, String testInformation,
            String path) throws FenixServiceException {

        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new InfoSiteStudentTestFeedback();
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();
            ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
            if (test == null)
                throw new FenixServiceException();

            double totalMark = 0;
            int responseNumber = 0;
            int notResponseNumber = 0;
            List errors = new ArrayList();

            ITestScope testScope = persistentSuport.getIPersistentTestScope().readByDomainObject(
                    executionCourse);

            if (testScope == null) {
                testScope = new TestScope(persistentSuport.getIPersistentExecutionCourse().materialize(
                        executionCourse));
                persistentSuport.getIPersistentTestScope().simpleLockWrite(testScope);
            }

            IDistributedTest distributedTest = new DistributedTest();
            distributedTest.setIdInternal(testId);
            distributedTest.setTestScope(testScope);
            distributedTest.setTestType(testType);
            distributedTest.setCorrectionAvailability(correctionAvailability);
            distributedTest.setImsFeedback(imsfeedback);
            distributedTest.setTestInformation(testInformation);
            distributedTest.setTitle(test.getTitle());
            distributedTest.setNumberOfQuestions(test.getNumberOfQuestions());

            List infoStudentTestQuestionList = getInfoStudentTestQuestionList(persistentSuport,
                    questionCodes, optionShuffle, responses, distributedTest, test);
            Iterator it = infoStudentTestQuestionList.iterator();
            if (infoStudentTestQuestionList.size() == 0)
                return null;
            while (it.hasNext()) {

                InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) it.next();

                if (infoStudentTestQuestion.getResponse().isResponsed()) {
                    responseNumber++;
                    try {
                    } catch (Exception e) {
                        throw new FenixServiceException(e);
                    }

                    IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory = QuestionCorrectionStrategyFactory
                            .getInstance();
                    IQuestionCorrectionStrategy questionCorrectionStrategy = questionCorrectionStrategyFactory
                            .getQuestionCorrectionStrategy(infoStudentTestQuestion);

                    String error = questionCorrectionStrategy.validResponse(infoStudentTestQuestion);
                    if (error == null) {
                        if ((!distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
                                && infoStudentTestQuestion.getQuestion()
                                        .getResponseProcessingInstructions().size() != 0) {

                            infoStudentTestQuestion = questionCorrectionStrategy
                                    .getMark(infoStudentTestQuestion);
                        }
                        totalMark += infoStudentTestQuestion.getTestQuestionMark().doubleValue();

                    } else {
                        notResponseNumber++;
                        responseNumber--;
                        errors.add(error);
                        if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID)
                            infoStudentTestQuestion.setResponse(new ResponseLID());
                        else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType()
                                .intValue() == QuestionType.STR)
                            infoStudentTestQuestion.setResponse(new ResponseSTR());
                        else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType()
                                .intValue() == QuestionType.NUM)
                            infoStudentTestQuestion.setResponse(new ResponseNUM());

                    }

                } else
                    notResponseNumber++;

            }

            infoSiteStudentTestFeedback.setResponseNumber(new Integer(responseNumber));
            infoSiteStudentTestFeedback.setNotResponseNumber(new Integer(notResponseNumber));
            infoSiteStudentTestFeedback.setErrors(errors);

            infoSiteStudentTestFeedback.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoSiteStudentTestFeedback;
    }

    private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion, Double questionValue) {
        Double maxValue = new Double(0);

        Iterator it = infoQuestion.getResponseProcessingInstructions().iterator();
        while (it.hasNext()) {
            ResponseProcessing responseProcessing = (ResponseProcessing) it.next();
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            it = infoQuestion.getResponseProcessingInstructions().iterator();
            double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);

            while (it.hasNext()) {
                ResponseProcessing responseProcessing = (ResponseProcessing) it.next();

                responseProcessing.setResponseValue(new Double(responseProcessing.getResponseValue()
                        .doubleValue()
                        * difValue));
            }
        }

        return infoQuestion;
    }

    private List getInfoStudentTestQuestionList(ISuportePersistente sp, String[] questionCodes,
            String[] optionShuffle, Response[] responses, IDistributedTest distributedTest, ITest test)
            throws ExcepcaoPersistencia, InvalidArgumentsServiceException, FenixServiceException {
        List infoStudentTestQuestionList = new ArrayList();

        List testQuestionList = sp.getIPersistentTestQuestion().readByTest(test);
        for (int i = 0; i < testQuestionList.size(); i++) {
            ITestQuestion testQuestionExample = (ITestQuestion) testQuestionList.get(i);

            IStudentTestQuestion studentTestQuestion = new StudentTestQuestion();
            studentTestQuestion.setDistributedTest(distributedTest);
            studentTestQuestion.setTestQuestionOrder(testQuestionExample.getTestQuestionOrder());
            studentTestQuestion.setTestQuestionValue(testQuestionExample.getTestQuestionValue());
            studentTestQuestion.setOldResponse(new Integer(0));
            studentTestQuestion.setCorrectionFormula(testQuestionExample.getCorrectionFormula());
            studentTestQuestion.setTestQuestionMark(new Double(0));
            studentTestQuestion.setResponse(null);

            IQuestion question = (IQuestion) sp.getIPersistentQuestion().readByOID(Question.class,
                    new Integer(questionCodes[i]));
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            studentTestQuestion.setQuestion(question);
            ParseQuestion parse = new ParseQuestion();
            InfoStudentTestQuestion infoStudentTestQuestion;
            try {
                studentTestQuestion.setOptionShuffle(optionShuffle[i]);
                infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest
                        .newInfoFromDomain(studentTestQuestion);
                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion,
                        this.path);
                infoStudentTestQuestion.setQuestion(correctQuestionValues(infoStudentTestQuestion
                        .getQuestion(), new Double(infoStudentTestQuestion.getTestQuestionValue()
                        .doubleValue())));
                infoStudentTestQuestion.setResponse(responses[i]);

            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            infoStudentTestQuestionList.add(infoStudentTestQuestion);

        }

        return infoStudentTestQuestionList;
    }
}