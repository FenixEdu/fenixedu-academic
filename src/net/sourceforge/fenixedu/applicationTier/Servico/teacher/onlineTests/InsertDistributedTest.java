package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.commons.beanutils.BeanComparator;

import com.sun.faces.el.impl.parser.ParseException;

public class InsertDistributedTest extends Service {

    public void run(Integer executionCourseId, Integer testId, String testInformation, Calendar beginDate, Calendar beginHour, Calendar endDate,
            Calendar endHour, TestType testType, CorrectionAvailability correctionAvaiability, Boolean imsFeedback,
            List<InfoStudent> infoStudentList, String contextPath) throws FenixServiceException, ExcepcaoPersistencia {
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        DistributedTest distributedTest = new DistributedTest();

        Test test = rootDomainObject.readTestByOID(testId);
        if (test == null)
            throw new InvalidArgumentsServiceException();

        distributedTest.setTitle(test.getTitle());
        distributedTest.setTestInformation(testInformation);
        distributedTest.setBeginDate(beginDate);
        distributedTest.setBeginHour(beginHour);
        distributedTest.setEndDate(endDate);
        distributedTest.setEndHour(endHour);
        distributedTest.setTestType(testType);
        distributedTest.setCorrectionAvailability(correctionAvaiability);
        distributedTest.setImsFeedback(imsFeedback);
        distributedTest.setNumberOfQuestions(test.getTestQuestionsCount());

        TestScope testScope = TestScope.readByDomainObject(ExecutionCourse.class, executionCourseId);

        if (testScope == null) {
            testScope = new TestScope(executionCourse);
        }
        distributedTest.setTestScope(testScope);

        final String replacedContextPath = contextPath.replace('\\', '/');

        List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
        Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));
        for (TestQuestion testQuestion : testQuestionList) {
            List<Question> questionList = new ArrayList<Question>();
            questionList.addAll(testQuestion.getQuestion().getMetadata().getVisibleQuestions());

            for (InfoStudent infoStudent : infoStudentList) {
                Registration registration = rootDomainObject.readRegistrationByOID(infoStudent.getIdInternal());
                StudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                studentTestQuestion.setStudent(registration);
                studentTestQuestion.setDistributedTest(distributedTest);
                studentTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
                studentTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
                studentTestQuestion.setCorrectionFormula(testQuestion.getCorrectionFormula());
                studentTestQuestion.setTestQuestionMark(Double.valueOf(0));
                studentTestQuestion.setResponse(null);

                if (questionList.size() == 0) {
                    questionList.addAll(testQuestion.getQuestion().getMetadata().getVisibleQuestions());
                }
                Question question = null;
                try {
                    question = getStudentQuestion(questionList, replacedContextPath);
                } catch (ParseException e) {
                    throw new InvalidArgumentsServiceException();
                } catch (ParseQuestionException e) {
                    throw new InvalidArgumentsServiceException();
                }
                if (question == null) {
                    throw new InvalidArgumentsServiceException();
                }
                if (question.getSubQuestions().size() >= 1 && question.getSubQuestions().get(0).getItemId() != null) {
                    studentTestQuestion.setItemId(question.getSubQuestions().get(0).getItemId());
                }
                studentTestQuestion.setQuestion(question);
                questionList.remove(question);
            }
        }
        // Create Evaluation - OnlineTest and Marks
        if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) {
            OnlineTest onlineTest = new OnlineTest();
            onlineTest.addAssociatedExecutionCourses(executionCourse);
            onlineTest.setDistributedTest(distributedTest);
        }

        return ;
    }

    private Question getStudentQuestion(List<Question> questions, String path) throws ParseException, ParseQuestionException {
        Question question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
            if (question.getSubQuestions() == null || question.getSubQuestions().size() == 0) {
                question = new ParseSubQuestion().parseSubQuestion(question, path);
            }
        }
        return question;
    }

}