/*
 * Created on 3/Set/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteStudentTestFeedback;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IQuestion;
import Dominio.IStudentTestQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.ITestScope;
import Dominio.Question;
import Dominio.StudentTestQuestion;
import Dominio.Test;
import Dominio.TestScope;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.strategy.tests.IQuestionCorrectionStrategyFactory;
import ServidorAplicacao.strategy.tests.QuestionCorrectionStrategyFactory;
import ServidorAplicacao.strategy.tests.strategys.IQuestionCorrectionStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.CorrectionAvailability;
import Util.tests.QuestionType;
import Util.tests.Response;
import Util.tests.ResponseLID;
import Util.tests.ResponseNUM;
import Util.tests.ResponseProcessing;
import Util.tests.ResponseSTR;
import Util.tests.TestType;
import UtilTests.ParseQuestion;

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
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
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