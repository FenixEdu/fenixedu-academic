package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.IQuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.QuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.beanutils.BeanComparator;

public class SimulateTest extends Service {

    private String path = new String();

    public InfoSiteStudentTestFeedback run(Integer executionCourseId, Integer testId, Response[] responses, String[] questionCodes,
            String[] optionShuffle, TestType testType, CorrectionAvailability correctionAvailability, Boolean imsfeedback, String testInformation,
            String path) throws FenixServiceException, ExcepcaoPersistencia {

//        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new InfoSiteStudentTestFeedback();
//        this.path = path.replace('\\', '/');
//        Test test = rootDomainObject.readTestByOID(testId);
//        if (test == null)
//            throw new FenixServiceException();
//
//        double totalMark = 0;
//        int responseNumber = 0;
//        int notResponseNumber = 0;
//        List<String> errors = new ArrayList<String>();
//
//        TestScope testScope = TestScope.readByDomainObject(ExecutionCourse.class, executionCourseId);
//
//        if (testScope == null) {
//            ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
//            if (executionCourse == null)
//                throw new InvalidArgumentsServiceException();
//            testScope = DomainFactory.makeTestScope(executionCourse);
//        }
//
//        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
//        infoDistributedTest.setIdInternal(testId);
//        infoDistributedTest.setInfoTestScope(InfoTestScope.newInfoFromDomain(testScope));
//        infoDistributedTest.setTestType(testType);
//        infoDistributedTest.setCorrectionAvailability(correctionAvailability);
//        infoDistributedTest.setImsFeedback(imsfeedback);
//        infoDistributedTest.setTestInformation(testInformation);
//        infoDistributedTest.setTitle(test.getTitle());
//        infoDistributedTest.setNumberOfQuestions(test.getTestQuestionsCount());
//
//        List<InfoStudentTestQuestion> infoStudentTestQuestionList = getInfoStudentTestQuestionList(questionCodes, optionShuffle,
//                responses, infoDistributedTest, testId);
//        if (infoStudentTestQuestionList.size() == 0)
//            return null;
//        for (InfoStudentTestQuestion infoStudentTestQuestion : infoStudentTestQuestionList) {
//            if (infoStudentTestQuestion.getResponse().isResponsed()) {
//                responseNumber++;
//
//                IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory = QuestionCorrectionStrategyFactory.getInstance();
//                IQuestionCorrectionStrategy questionCorrectionStrategy = questionCorrectionStrategyFactory
//                        .getQuestionCorrectionStrategy(infoStudentTestQuestion);
//
//                String error = questionCorrectionStrategy.validResponse(infoStudentTestQuestion);
//                if (error == null) {
//                    if ((!infoDistributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
//                            && infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions().size() != 0) {
//
//                        infoStudentTestQuestion = questionCorrectionStrategy.getMark(infoStudentTestQuestion);
//                    }
//                    totalMark += infoStudentTestQuestion.getTestQuestionMark().doubleValue();
//                } else {
//                    notResponseNumber++;
//                    responseNumber--;
//                    errors.add(error);
//                    if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID)
//                        infoStudentTestQuestion.setResponse(new ResponseLID());
//                    else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR)
//                        infoStudentTestQuestion.setResponse(new ResponseSTR());
//                    else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM)
//                        infoStudentTestQuestion.setResponse(new ResponseNUM());
//                }
//            } else
//                notResponseNumber++;
//        }
//
//        infoSiteStudentTestFeedback.setResponseNumber(Integer.valueOf(responseNumber));
//        infoSiteStudentTestFeedback.setNotResponseNumber(Integer.valueOf(notResponseNumber));
//        infoSiteStudentTestFeedback.setErrors(errors);
//
//        infoSiteStudentTestFeedback.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
//        return infoSiteStudentTestFeedback;
        return null;
    }

    private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion, Double questionValue) {
        Double maxValue = Double.valueOf(0);
        for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            double difValue = questionValue * Math.pow(maxValue, -1);
            for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
                responseProcessing.setResponseValue(Double.valueOf(responseProcessing.getResponseValue() * difValue));
            }
        }

        return infoQuestion;
    }

    private List<InfoStudentTestQuestion> getInfoStudentTestQuestionList(String[] questionCodes, String[] optionShuffle,
            Response[] responses, InfoDistributedTest infoDistributedTest, Integer testId) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException, FenixServiceException {
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();

        Test test = rootDomainObject.readTestByOID(testId);
        List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
        Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));
        for (int i = 0; i < testQuestionList.size(); i++) {
            TestQuestion testQuestionExample = testQuestionList.get(i);
            InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
          //  infoStudentTestQuestion.setDistributedTest(distributedTest);
            infoStudentTestQuestion.setTestQuestionOrder(testQuestionExample.getTestQuestionOrder());
            infoStudentTestQuestion.setTestQuestionValue(testQuestionExample.getTestQuestionValue());
            infoStudentTestQuestion.setOldResponse(Integer.valueOf(0));
            infoStudentTestQuestion.setCorrectionFormula(testQuestionExample.getCorrectionFormula());
            infoStudentTestQuestion.setTestQuestionMark(Double.valueOf(0));
            infoStudentTestQuestion.setResponse(null);
            Question question = rootDomainObject.readQuestionByOID(Integer.valueOf(questionCodes[i]));
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
//            ParseQuestion parse = new ParseQuestion();
//            try {
//                infoStudentTestQuestion.setOptionShuffle(optionShuffle[i]);
//                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, this.path);
//                infoStudentTestQuestion.setQuestion(correctQuestionValues(infoStudentTestQuestion.getQuestion(), Double.valueOf(infoStudentTestQuestion.getTestQuestionValue())));
//                infoStudentTestQuestion.setResponse(responses[i]);
//            } catch (Exception e) {
//                throw new FenixServiceException(e);
//            }
            infoStudentTestQuestionList.add(infoStudentTestQuestion);
        }
        return infoStudentTestQuestionList;
    }

}
