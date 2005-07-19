/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
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
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class GenetareStudentTestForSimulation implements IService {
    private String path = new String();

    public List run(Integer executionCourseId, Integer testId, String path, TestType testType, CorrectionAvailability correctionAvailability,
            Boolean imsfeedback, String testInformation) throws FenixServiceException {
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        try {
            this.path = path.replace('\\', '/');
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                    executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();

            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, testId, true);
            if (test == null)
                throw new InvalidArgumentsServiceException();

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
            distributedTest.setTitle(test.getTitle());
            distributedTest.setTestInformation(testInformation);
            distributedTest.setNumberOfQuestions(test.getNumberOfQuestions());

            List<ITestQuestion> testQuestionList = persistentSuport.getIPersistentTestQuestion().readByTest(testId);
            for (ITestQuestion testQuestionExample : testQuestionList) {

                List<IQuestion> questionList = new ArrayList<IQuestion>();
                questionList.addAll(testQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

                IStudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                studentTestQuestion.setDistributedTest(distributedTest);
                studentTestQuestion.setTestQuestionOrder(testQuestionExample.getTestQuestionOrder());
                studentTestQuestion.setTestQuestionValue(testQuestionExample.getTestQuestionValue());
                studentTestQuestion.setOldResponse(new Integer(0));
                studentTestQuestion.setCorrectionFormula(testQuestionExample.getCorrectionFormula());
                studentTestQuestion.setTestQuestionMark(new Double(0));
                studentTestQuestion.setResponse(null);

                if (questionList.size() == 0)
                    questionList.addAll(testQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
                IQuestion question = getStudentQuestion(questionList);
                if (question == null) {
                    throw new InvalidArgumentsServiceException();
                }
                studentTestQuestion.setQuestion(question);
                ParseQuestion parse = new ParseQuestion();
                InfoStudentTestQuestion infoStudentTestQuestion;
                try {

                    boolean shuffle = true;
                    if (distributedTest.getTestType().equals(new TestType(3))) // INQUIRY
                        shuffle = false;
                    studentTestQuestion.setOptionShuffle(parse.shuffleQuestionOptions(studentTestQuestion.getQuestion().getXmlFile(), shuffle,
                            this.path));

                    infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest.newInfoFromDomain(studentTestQuestion);
                    infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, this.path);

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

    private IQuestion getStudentQuestion(List<IQuestion> questions) {
        IQuestion question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question;
    }

}