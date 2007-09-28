package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jvstm.TransactionalCommand;
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
import net.sourceforge.fenixedu.stm.Transaction;
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

        Test test = rootDomainObject.readTestByOID(testId);
        if (test == null)
            throw new InvalidArgumentsServiceException();

        try {
            final DistributedTestCreator distributedTestCreator = new DistributedTestCreator(
                    executionCourse, test, testInformation, beginDate, beginHour,endDate,
                    endHour, testType, correctionAvaiability, imsFeedback);
            distributedTestCreator.start();
            distributedTestCreator.join();

            final String replacedContextPath = contextPath.replace('\\', '/');

            final Integer distributedTestId = distributedTestCreator.distributedTestId;
            if (distributedTestId == null) {
                throw new Error("Creator thread was unable to create a distributed test!");
            }
            Distributor.runThread(infoStudentList, distributedTestId, test.getIdInternal(), replacedContextPath);

        } catch (InterruptedException e) {
            throw new Error(e);
        }

        return ;
    }

    public static class DistributedTestCreator extends Thread implements TransactionalCommand {

        private final Integer executionCourseId;
        private final Integer testId;
        private final String testInformation;
        private final Calendar beginDate;
        private final Calendar beginHour;
        private final Calendar endDate;
        private final Calendar endHour;
        private final TestType testType;
        private final CorrectionAvailability correctionAvaiability;
        private final Boolean imsFeedback;

        private Integer tempDistributedTestId = null;
        public Integer distributedTestId = null;

        public DistributedTestCreator(final ExecutionCourse executionCourse, final Test test,
                final String testInformation, final Calendar beginDate, final Calendar beginHour,
                final Calendar endDate, final Calendar endHour, final TestType testType,
                final CorrectionAvailability correctionAvaiability, final Boolean imsFeedback) {
            this.executionCourseId = executionCourse.getIdInternal();
            this.testId = test.getIdInternal();
            this.testInformation = testInformation;
            this.beginDate = beginDate;
            this.beginHour = beginHour;
            this.endDate = endDate;
            this.endHour = endHour;
            this.testType = testType;
            this.correctionAvaiability = correctionAvaiability;
            this.imsFeedback = imsFeedback;
        }

        public void run() {
            Transaction.withTransaction(this);
            distributedTestId = tempDistributedTestId;
        }

        public void doIt() {
            final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
            final Test test = rootDomainObject.readTestByOID(testId);

            DistributedTest distributedTest = new DistributedTest();
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

            // Create Evaluation - OnlineTest and Marks
            if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) {
                OnlineTest onlineTest = new OnlineTest();
                onlineTest.addAssociatedExecutionCourses(executionCourse);
                onlineTest.setDistributedTest(distributedTest);
            }

            tempDistributedTestId = distributedTest.getIdInternal();
        }

        protected static void runThread(final ExecutionCourse executionCourse, final Test test,
                final String testInformation, final Calendar beginDate, final Calendar beginHour,
                final Calendar endDate, final Calendar endHour, final TestType testType,
                final CorrectionAvailability correctionAvaiability, final Boolean imsFeedback) {
            final DistributedTestCreator distributedTestCreator = new DistributedTestCreator(
                    executionCourse, test, testInformation, beginDate, beginHour,endDate,
                    endHour, testType, correctionAvaiability, imsFeedback);
            distributedTestCreator.start();
        }
    }

    public static class Distributor extends Thread implements TransactionalCommand {

        private final List<InfoStudent> infoStudentList;
        private final Integer distributedTestId;
        private final Integer testId;
        private final String replacedContextPath;

        public Distributor(final List<InfoStudent> infoStudentList, final Integer distributedTestId,
                final Integer testId, final String replacedContextPath) {
            this.infoStudentList = infoStudentList;
            this.distributedTestId = distributedTestId;
            this.testId = testId;
            this.replacedContextPath = replacedContextPath;
        }

        public void run() {
            Transaction.withTransaction(this);
        }

        public void doIt() {
            Test test = rootDomainObject.readTestByOID(testId);

            List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
            Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));
            for (TestQuestion testQuestion : testQuestionList) {
                List<Integer> questionList = new ArrayList<Integer>();
                addAllQuestions(questionList, testQuestion.getQuestion().getMetadata().getVisibleQuestions());

                for (InfoStudent infoStudent : infoStudentList) {
                    try {
                        DistributeForStudentThread.runThread(distributedTestId, replacedContextPath, infoStudent, questionList, testQuestion.getIdInternal());
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        protected static void runThread(final List<InfoStudent> infoStudentList, final Integer distributedTestId,
                final Integer testId, final String replacedContextPath) throws InterruptedException {
            final Distributor distributor = new Distributor(infoStudentList, distributedTestId, testId, replacedContextPath);
            distributor.start();
            distributor.join();
        }
    }

    private static void addAllQuestions(final List<Integer> questionList, final List<Question> visibleQuestions) {
        for (final Question question : visibleQuestions) {
            questionList.add(question.getIdInternal());
        }
    }

    public static class DistributeForStudentThread extends Thread implements TransactionalCommand {

        private final Integer distributedTestId;
        private final String replacedContextPath;
        private final InfoStudent infoStudent;
        private final List<Integer> questionList;
        private final Integer testQuestionId;

        public DistributeForStudentThread(final Integer distributedTestId, final String replacedContextPath,
                final InfoStudent infoStudent, final List<Integer> questionList, final Integer testQuestionId) {
            this.distributedTestId = distributedTestId;
            this.replacedContextPath = replacedContextPath;
            this.infoStudent = infoStudent;
            this.questionList = questionList;
            this.testQuestionId = testQuestionId;
        }

        public void run() {
            Transaction.withTransaction(this);
        }

        public void doIt() {
            final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
            final TestQuestion testQuestion = rootDomainObject.readTestQuestionByOID(testQuestionId);

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
                addAllQuestions(questionList, testQuestion.getQuestion().getMetadata().getVisibleQuestions());
            }
            Question question = null;
            try {
                question = getStudentQuestion(questionList, replacedContextPath);
            } catch (ParseException e) {
                throw new Error(e);
//                throw new InvalidArgumentsServiceException();
            } catch (ParseQuestionException e) {
                throw new Error(e);
//                throw new InvalidArgumentsServiceException();
            }
            if (question == null) {
                throw new Error();
//                throw new InvalidArgumentsServiceException();
            }
            if (question.getSubQuestions().size() >= 1 && question.getSubQuestions().get(0).getItemId() != null) {
                studentTestQuestion.setItemId(question.getSubQuestions().get(0).getItemId());
            }
            studentTestQuestion.setQuestion(question);
            questionList.remove(question);
        }

        private Question getStudentQuestion(List<Integer> questions, String path) throws ParseException, ParseQuestionException {
            Question question = null;
            if (questions.size() != 0) {
                Random r = new Random();
                int questionIndex = r.nextInt(questions.size());
                question = rootDomainObject.readQuestionByOID(questions.get(questionIndex));
                if (question.getSubQuestions() == null || question.getSubQuestions().size() == 0) {
                    question = new ParseSubQuestion().parseSubQuestion(question, path);
                }
            }
            return question;
        }

        protected static void runThread(final Integer distributedTestId, final String replacedContextPath, 
                final InfoStudent infoStudent, final List<Integer> questionList, final Integer testQuestionId) throws InterruptedException {
            final DistributeForStudentThread distributeForStudentThread = new DistributeForStudentThread(distributedTestId,
                    replacedContextPath, infoStudent, questionList, testQuestionId);
            distributeForStudentThread.start();
            //TODO
            distributeForStudentThread.join();
        }
    }

}