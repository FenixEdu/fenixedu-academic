/*
 * Created on 1/Ago/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.utl.ist.berserk.logic.serviceManager.IService;
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
import Dominio.StudentTestQuestion;
import Dominio.Test;
import Dominio.TestScope;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.CorrectionAvailability;
import Util.tests.TestType;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class GenetareStudentTestForSimulation implements IService {
    private String path = new String();

    public GenetareStudentTestForSimulation() {
    }

    public List run(Integer executionCourseId, Integer testId, String path, TestType testType,
            CorrectionAvailability correctionAvailability, Boolean imsfeedback, String testInformation)
            throws FenixServiceException {
        List infoStudentTestQuestionList = new ArrayList();
        try {
            this.path = path.replace('\\', '/');
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();

            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, testId, true);
            if (test == null)
                throw new InvalidArgumentsServiceException();

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
            distributedTest.setTitle(test.getTitle());
            distributedTest.setTestInformation(testInformation);
            distributedTest.setNumberOfQuestions(test.getNumberOfQuestions());

            List testQuestionList = persistentSuport.getIPersistentTestQuestion().readByTest(test);
            for (int i = 0; i < testQuestionList.size(); i++) {
                ITestQuestion testQuestionExample = (ITestQuestion) testQuestionList.get(i);

                List questionList = new ArrayList();
                questionList.addAll(testQuestionExample.getQuestion().getMetadata()
                        .getVisibleQuestions());

                IStudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                studentTestQuestion.setDistributedTest(distributedTest);
                studentTestQuestion.setTestQuestionOrder(testQuestionExample.getTestQuestionOrder());
                studentTestQuestion.setTestQuestionValue(testQuestionExample.getTestQuestionValue());
                studentTestQuestion.setOldResponse(new Integer(0));
                studentTestQuestion.setCorrectionFormula(testQuestionExample.getCorrectionFormula());
                studentTestQuestion.setTestQuestionMark(new Double(0));
                studentTestQuestion.setResponse(null);

                if (questionList.size() == 0)
                    questionList.addAll(testQuestionExample.getQuestion().getMetadata()
                            .getVisibleQuestions());
                IQuestion question = getStudentQuestion(questionList);
                if (question == null) {
                    throw new InvalidArgumentsServiceException();
                }
                studentTestQuestion.setQuestion(question);
                ParseQuestion parse = new ParseQuestion();
                InfoStudentTestQuestion infoStudentTestQuestion;
                try {

                    boolean shuffle = true;
                    if (distributedTest.getTestType().equals(new TestType(3))) //INQUIRY
                        shuffle = false;
                    studentTestQuestion.setOptionShuffle(parse.shuffleQuestionOptions(
                            studentTestQuestion.getQuestion().getXmlFile(), shuffle, this.path));

                    infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest
                            .newInfoFromDomain(studentTestQuestion);
                    infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion,
                            this.path);

                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                infoStudentTestQuestionList.add(infoStudentTestQuestion);
                questionList.remove(question);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoStudentTestQuestionList;
    }

    private IQuestion getStudentQuestion(List questions) throws ExcepcaoPersistencia {
        IQuestion question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = (IQuestion) questions.get(questionIndex);
        }
        return question;
    }

}