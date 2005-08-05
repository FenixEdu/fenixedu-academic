/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestScope;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
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
            Boolean imsfeedback, String testInformation) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        this.path = path.replace('\\', '/');
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
        if (test == null)
            throw new InvalidArgumentsServiceException();

        ITestScope testScope = persistentSuport.getIPersistentTestScope().readByDomainObject(ExecutionCourse.class.getName(), executionCourseId);

        if (testScope == null) {
            final IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();
            testScope = DomainFactory.makeTestScope(persistentSuport.getIPersistentExecutionCourse().materialize(executionCourse));
        }

        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        infoDistributedTest.setIdInternal(testId);
        infoDistributedTest.setInfoTestScope(InfoTestScope.newInfoFromDomain(testScope));
        infoDistributedTest.setTestType(testType);
        infoDistributedTest.setCorrectionAvailability(correctionAvailability);
        infoDistributedTest.setImsFeedback(imsfeedback);
        infoDistributedTest.setTitle(test.getTitle());
        infoDistributedTest.setTestInformation(testInformation);
        infoDistributedTest.setNumberOfQuestions(test.getNumberOfQuestions());

        List<ITestQuestion> testQuestionList = persistentSuport.getIPersistentTestQuestion().readByTest(testId);
        for (ITestQuestion testQuestionExample : testQuestionList) {

            List<IQuestion> questionList = new ArrayList<IQuestion>();
            questionList.addAll(testQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

            InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
            infoStudentTestQuestion.setDistributedTest(infoDistributedTest);
            infoStudentTestQuestion.setTestQuestionOrder(testQuestionExample.getTestQuestionOrder());
            infoStudentTestQuestion.setTestQuestionValue(testQuestionExample.getTestQuestionValue());
            infoStudentTestQuestion.setOldResponse(new Integer(0));
            infoStudentTestQuestion.setCorrectionFormula(testQuestionExample.getCorrectionFormula());
            infoStudentTestQuestion.setTestQuestionMark(new Double(0));
            infoStudentTestQuestion.setResponse(null);

            if (questionList.size() == 0)
                questionList.addAll(testQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
            IQuestion question = getStudentQuestion(questionList);
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
            ParseQuestion parse = new ParseQuestion();
            try {

                boolean shuffle = true;
                if (infoDistributedTest.getTestType().equals(new TestType(3))) // INQUIRY
                    shuffle = false;
                infoStudentTestQuestion.setOptionShuffle(parse.shuffleQuestionOptions(question.getXmlFile(), shuffle, this.path));

                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, this.path);

            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            infoStudentTestQuestionList.add(infoStudentTestQuestion);
            questionList.remove(question);
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