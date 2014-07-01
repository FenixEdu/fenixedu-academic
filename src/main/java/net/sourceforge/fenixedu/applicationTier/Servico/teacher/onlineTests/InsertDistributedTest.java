/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.EvaluationManagementLog;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class InsertDistributedTest {

    protected void run(String executionCourseId, String testId, String testInformation, String evaluationTitle,
            Calendar beginDate, Calendar beginHour, Calendar endDate, Calendar endHour, TestType testType,
            CorrectionAvailability correctionAvaiability, Boolean imsFeedback, List<InfoStudent> infoStudentList)
            throws FenixServiceException {
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Test test = FenixFramework.getDomainObject(testId);
        if (test == null) {
            throw new InvalidArgumentsServiceException();
        }

        try {
            final DistributedTestCreator distributedTestCreator =
                    new DistributedTestCreator(executionCourse, test, testInformation, evaluationTitle, beginDate, beginHour,
                            endDate, endHour, testType, correctionAvaiability, imsFeedback, Authenticate.getUser());
            distributedTestCreator.start();
            distributedTestCreator.join();

            final String distributedTestId = distributedTestCreator.distributedTestId;
            if (distributedTestId == null) {
                throw new Error("Creator thread was unable to create a distributed test!");
            }
            Distributor.runThread(infoStudentList, distributedTestId, test.getExternalId());

        } catch (InterruptedException e) {
            throw new Error(e);
        }

        return;
    }

    public static class DistributedTestCreator extends Thread {

        private final String executionCourseId;

        private final String testId;

        private final String testInformation;

        private final String evaluationTitle;

        private final Calendar beginDate;

        private final Calendar beginHour;

        private final Calendar endDate;

        private final Calendar endHour;

        private final TestType testType;

        private final CorrectionAvailability correctionAvaiability;

        private final Boolean imsFeedback;

        private final User userView;

        private String tempDistributedTestId = null;

        public String distributedTestId = null;

        public DistributedTestCreator(final ExecutionCourse executionCourse, final Test test, final String testInformation,
                final String evaluationTitle, final Calendar beginDate, final Calendar beginHour, final Calendar endDate,
                final Calendar endHour, final TestType testType, final CorrectionAvailability correctionAvaiability,
                final Boolean imsFeedback, User userView) {
            this.executionCourseId = executionCourse.getExternalId();
            this.testId = test.getExternalId();
            this.testInformation = testInformation;
            this.evaluationTitle = evaluationTitle;
            this.beginDate = beginDate;
            this.beginHour = beginHour;
            this.endDate = endDate;
            this.endHour = endHour;
            this.testType = testType;
            this.correctionAvaiability = correctionAvaiability;
            this.imsFeedback = imsFeedback;
            this.userView = userView;
        }

        @Atomic
        @Override
        public void run() {
            try {
                Authenticate.mock(userView);
                doIt();
                distributedTestId = tempDistributedTestId;
            } finally {
                Authenticate.unmock();
            }
        }

        public void doIt() {
            final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
            final Test test = FenixFramework.getDomainObject(testId);

            DistributedTest distributedTest = new DistributedTest();
            distributedTest.setTitle(test.getTitle());
            distributedTest.setTestInformation(testInformation);
            distributedTest.setEvaluationTitle(evaluationTitle);
            distributedTest.setBeginDate(beginDate);
            distributedTest.setBeginHour(beginHour);
            distributedTest.setEndDate(endDate);
            distributedTest.setEndHour(endHour);
            distributedTest.setTestType(testType);
            distributedTest.setCorrectionAvailability(correctionAvaiability);
            distributedTest.setImsFeedback(imsFeedback);
            distributedTest.setNumberOfQuestions(test.getTestQuestionsSet().size());

            TestScope testScope = executionCourse.getTestScope();

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

            tempDistributedTestId = distributedTest.getExternalId();

            EvaluationManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                    "log.executionCourse.evaluation.tests.distribution.created", distributedTest.getTitle(),
                    distributedTest.getEvaluationTitle(), distributedTest.getBeginDateTimeFormatted(), executionCourse.getName(),
                    executionCourse.getDegreePresentationString());
        }

        protected static void runThread(final ExecutionCourse executionCourse, final Test test, final String testInformation,
                final String evaluationTitle, final Calendar beginDate, final Calendar beginHour, final Calendar endDate,
                final Calendar endHour, final TestType testType, final CorrectionAvailability correctionAvaiability,
                final Boolean imsFeedback, final User userView) {
            final DistributedTestCreator distributedTestCreator =
                    new DistributedTestCreator(executionCourse, test, testInformation, evaluationTitle, beginDate, beginHour,
                            endDate, endHour, testType, correctionAvaiability, imsFeedback, userView);
            distributedTestCreator.start();
        }
    }

    private static class QuestionPair {
        private final String testQuestionId;

        private final String questionId;

        public QuestionPair(final TestQuestion testQuestion, final Question question) {
            testQuestionId = testQuestion.getExternalId();
            questionId = question.getExternalId();
        }

        public TestQuestion getTestQuestion() {
            return FenixFramework.getDomainObject(testQuestionId);
        }

        public Question getQuestion() {
            return FenixFramework.getDomainObject(questionId);
        }
    }

    private static class Distribution {

        private final Map<InfoStudent, Collection<QuestionPair>> questionMap =
                new HashMap<InfoStudent, Collection<QuestionPair>>();

        public Distribution(final List<TestQuestion> testQuestionList, final List<InfoStudent> infoStudentList) {
            final int numberOfStudents = infoStudentList.size();

            final Random r = new Random();

            for (final TestQuestion testQuestion : testQuestionList) {
                final List<Question> questions = getQuestions(testQuestion, numberOfStudents);
                if (questions.size() >= numberOfStudents) {
                    for (final InfoStudent infoStudent : infoStudentList) {
                        Collection<QuestionPair> questionsForStudent = questionMap.get(infoStudent);
                        if (questionsForStudent == null) {
                            questionsForStudent = new ArrayList<QuestionPair>();
                            questionMap.put(infoStudent, questionsForStudent);
                        }

                        int questionIndex = r.nextInt(questions.size());
                        final Question question = questions.get(questionIndex);
                        questionsForStudent.add(new QuestionPair(testQuestion, question));
                        questions.remove(questionIndex);
                    }
                }
            }
        }

        private List<Question> getQuestions(final TestQuestion testQuestion, final int numberOfStudents) {
            final List<Question> questions = new ArrayList<Question>();
            final Metadata metadata = testQuestion.getQuestion().getMetadata();
            while (metadata.getQuestionsSet().size() > 0 && questions.size() < numberOfStudents) {
                for (final Question question : metadata.getQuestionsSet()) {
                    if (question.getVisibility()) {
                        questions.add(question);
                    }
                }
            }
            return questions;
        }
    }

    public static class Distributor extends Thread {

        private final List<InfoStudent> infoStudentList;

        private final String distributedTestId;

        private final String testId;

        public Distributor(final List<InfoStudent> infoStudentList, final String distributedTestId, final String testId) {
            this.infoStudentList = infoStudentList;
            this.distributedTestId = distributedTestId;
            this.testId = testId;
        }

        @Atomic
        @Override
        public void run() {
            doIt();
        }

        public void doIt() {
            Test test = FenixFramework.getDomainObject(testId);

            List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
            Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));

            final Distribution distribution = new Distribution(testQuestionList, infoStudentList);
            for (final Entry<InfoStudent, Collection<QuestionPair>> entry : distribution.questionMap.entrySet()) {
                final InfoStudent infoStudent = entry.getKey();
                final Collection<QuestionPair> questions = entry.getValue();
                try {
                    DistributeForStudentThread.runThread(distributedTestId, infoStudent, questions);
                } catch (InterruptedException e) {
                }
            }
        }

        protected static void runThread(final List<InfoStudent> infoStudentList, final String distributedTestId,
                final String testId) throws InterruptedException {
            final Distributor distributor = new Distributor(infoStudentList, distributedTestId, testId);
            distributor.start();
            distributor.join();
        }
    }

    private static void addAllQuestions(final List<String> questionList, final List<Question> visibleQuestions) {
        for (final Question question : visibleQuestions) {
            questionList.add(question.getExternalId());
        }
    }

    public static class DistributeForStudentThread extends Thread {

        private final String distributedTestId;

        private final InfoStudent infoStudent;

        private final Collection<QuestionPair> questionList;

        public DistributeForStudentThread(final String distributedTestId, final InfoStudent infoStudent,
                final Collection<QuestionPair> questionList) {
            this.distributedTestId = distributedTestId;
            this.infoStudent = infoStudent;
            this.questionList = questionList;
        }

        @Atomic
        @Override
        public void run() {
            doIt();
        }

        public void doIt() {
            final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
            final Registration registration = FenixFramework.getDomainObject(infoStudent.getExternalId());

            for (final QuestionPair questionPair : questionList) {
                final TestQuestion testQuestion = questionPair.getTestQuestion();

                final StudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                studentTestQuestion.setStudent(registration);
                studentTestQuestion.setDistributedTest(distributedTest);
                studentTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
                studentTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
                studentTestQuestion.setCorrectionFormula(testQuestion.getCorrectionFormula());
                studentTestQuestion.setTestQuestionMark(Double.valueOf(0));
                studentTestQuestion.setResponse(null);

                Question question = null;
                try {
                    question = getStudentQuestion(questionPair.getQuestion());
                } catch (ParseQuestionException e) {
                    throw new Error(e);
                }
                if (question == null) {
                    throw new Error();
                }
                if (question.getSubQuestions().size() >= 1 && question.getSubQuestions().iterator().next().getItemId() != null) {
                    studentTestQuestion.setItemId(question.getSubQuestions().iterator().next().getItemId());
                }
                studentTestQuestion.setQuestion(question);
                questionList.remove(question);
            }
        }

        private Question getStudentQuestion(final Question question) throws ParseQuestionException {
            return question.getSubQuestions() == null || question.getSubQuestions().size() == 0 ? new ParseSubQuestion()
                    .parseSubQuestion(question) : question;
        }

        protected static void runThread(final String distributedTestId, final InfoStudent infoStudent,
                final Collection<QuestionPair> questionList) throws InterruptedException {
            final DistributeForStudentThread distributeForStudentThread =
                    new DistributeForStudentThread(distributedTestId, infoStudent, questionList);
            distributeForStudentThread.start();
            // TODO
            distributeForStudentThread.join();
        }
    }

    // Service Invokers migrated from Berserk

    private static final InsertDistributedTest serviceInstance = new InsertDistributedTest();

    @Atomic
    public static void runInsertDistributedTest(String executionCourseId, String testId, String testInformation,
            String evaluationTitle, Calendar beginDate, Calendar beginHour, Calendar endDate, Calendar endHour,
            TestType testType, CorrectionAvailability correctionAvaiability, Boolean imsFeedback,
            List<InfoStudent> infoStudentList) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testId, testInformation, evaluationTitle, beginDate, beginHour, endDate, endHour,
                testType, correctionAvaiability, imsFeedback, infoStudentList);
    }

}