/*
 * Created on 3/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.IQuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.QuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
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

    private String path = new String();

    public InfoSiteStudentTestFeedback run(Integer executionCourseId, Integer testId, Response[] responses, String[] questionCodes,
            String[] optionShuffle, TestType testType, CorrectionAvailability correctionAvailability, Boolean imsfeedback, String testInformation,
            String path) throws FenixServiceException {

        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new InfoSiteStudentTestFeedback();
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                    executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();
            ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
            if (test == null)
                throw new FenixServiceException();

            double totalMark = 0;
            int responseNumber = 0;
            int notResponseNumber = 0;
            List<String> errors = new ArrayList<String>();

            ITestScope testScope = persistentSuport.getIPersistentTestScope().readByDomainObject(ExecutionCourse.class.getName(), executionCourseId);

            if (testScope == null) {
                testScope = new TestScope(persistentSuport.getIPersistentExecutionCourse().materialize(executionCourse));
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

            List<InfoStudentTestQuestion> infoStudentTestQuestionList = getInfoStudentTestQuestionList(persistentSuport, questionCodes,
                    optionShuffle, responses, distributedTest, testId);
            if (infoStudentTestQuestionList.size() == 0)
                return null;
            for (InfoStudentTestQuestion infoStudentTestQuestion : infoStudentTestQuestionList) {
                if (infoStudentTestQuestion.getResponse().isResponsed()) {
                    responseNumber++;
                    try {
                    } catch (Exception e) {
                        throw new FenixServiceException(e);
                    }

                    IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory = QuestionCorrectionStrategyFactory.getInstance();
                    IQuestionCorrectionStrategy questionCorrectionStrategy = questionCorrectionStrategyFactory
                            .getQuestionCorrectionStrategy(infoStudentTestQuestion);

                    String error = questionCorrectionStrategy.validResponse(infoStudentTestQuestion);
                    if (error == null) {
                        if ((!distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
                                && infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions().size() != 0) {

                            infoStudentTestQuestion = questionCorrectionStrategy.getMark(infoStudentTestQuestion);
                        }
                        totalMark += infoStudentTestQuestion.getTestQuestionMark().doubleValue();

                    } else {
                        notResponseNumber++;
                        responseNumber--;
                        errors.add(error);
                        if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID)
                            infoStudentTestQuestion.setResponse(new ResponseLID());
                        else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR)
                            infoStudentTestQuestion.setResponse(new ResponseSTR());
                        else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM)
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
        for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);
            for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
                responseProcessing.setResponseValue(new Double(responseProcessing.getResponseValue().doubleValue() * difValue));
            }
        }

        return infoQuestion;
    }

    private List<InfoStudentTestQuestion> getInfoStudentTestQuestionList(ISuportePersistente sp, String[] questionCodes, String[] optionShuffle,
            Response[] responses, IDistributedTest distributedTest, Integer testId) throws ExcepcaoPersistencia, InvalidArgumentsServiceException,
            FenixServiceException {
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();

        List<ITestQuestion> testQuestionList = sp.getIPersistentTestQuestion().readByTest(testId);
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

            IQuestion question = (IQuestion) sp.getIPersistentQuestion().readByOID(Question.class, new Integer(questionCodes[i]));
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            studentTestQuestion.setQuestion(question);
            ParseQuestion parse = new ParseQuestion();
            InfoStudentTestQuestion infoStudentTestQuestion;
            try {
                studentTestQuestion.setOptionShuffle(optionShuffle[i]);
                infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest.newInfoFromDomain(studentTestQuestion);
                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, this.path);
                infoStudentTestQuestion.setQuestion(correctQuestionValues(infoStudentTestQuestion.getQuestion(), new Double(infoStudentTestQuestion
                        .getTestQuestionValue().doubleValue())));
                infoStudentTestQuestion.setResponse(responses[i]);

            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            infoStudentTestQuestionList.add(infoStudentTestQuestion);

        }

        return infoStudentTestQuestionList;
    }
}