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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.IQuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.QuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ChangeStudentTestQuestionValue {
    protected void run(String executionCourseId, String distributedTestId, Double newValue, String questionId, String studentId,
            TestQuestionStudentsChangesType studentsType) throws FenixServiceException {

        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        Question question = distributedTest.findQuestionByOID(questionId);

        List<StudentTestQuestion> studentsTestQuestionList = new ArrayList<StudentTestQuestion>();
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
            final Registration registration = FenixFramework.getDomainObject(studentId);
            studentsTestQuestionList.add(StudentTestQuestion.findStudentTestQuestion(question, registration, distributedTest));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST_VARIATION) {
            studentsTestQuestionList.addAll(StudentTestQuestion.findStudentTestQuestions(question, distributedTest));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
            final Registration registration = FenixFramework.getDomainObject(studentId);
            final StudentTestQuestion studentTestQuestion =
                    StudentTestQuestion.findStudentTestQuestion(question, registration, distributedTest);
            studentsTestQuestionList.addAll(distributedTest.findStudentTestQuestionsByTestQuestionOrder(studentTestQuestion
                    .getTestQuestionOrder()));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS) {
            studentsTestQuestionList.addAll(question.getStudentTestsQuestionsSet());
        }
        for (StudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
            List<InfoStudent> group = new ArrayList<InfoStudent>();

            if (!group.contains(studentTestQuestion.getStudent().getPerson())) {
                group.add(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));
            }

            if (studentTestQuestion.getResponse() != null
                    && studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                studentTestQuestion.setTestQuestionMark(getNewQuestionMark(studentTestQuestion, newValue));

                OnlineTest onlineTest = studentTestQuestion.getDistributedTest().getOnlineTest();
                ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
                Attends attend = studentTestQuestion.getStudent().readAttendByExecutionCourse(executionCourse);
                Mark mark = onlineTest.getMarkByAttend(attend);
                if (mark != null) {
                    mark.setMark(getNewStudentMark(studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent()));
                }
            }
            studentTestQuestion.setTestQuestionValue(newValue);

            String event = BundleUtil.getString(Bundle.APPLICATION, "message.changeStudentValueLogMessage", newValue.toString());
            new StudentTestLog(studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent(), event);
        }
    }

    private String getNewStudentMark(DistributedTest dt, Registration s) {
        double totalMark = 0;
        Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(s, dt);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        DecimalFormat df = new DecimalFormat("#0.##");
        DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalFormatSymbols);
        return (df.format(Math.max(0, totalMark)));
    }

    private Double getNewQuestionMark(StudentTestQuestion studentTestQuestion, Double newValue) throws FenixServiceException {
        Double newMark = new Double(0);
        if (studentTestQuestion.getResponse() != null && !newValue.equals(Double.parseDouble("0"))) {
            if (studentTestQuestion.getTestQuestionValue().equals(Double.parseDouble("0"))) {
                ParseSubQuestion parse = new ParseSubQuestion();
                try {
                    studentTestQuestion = parse.parseStudentTestQuestion(studentTestQuestion);
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory =
                        QuestionCorrectionStrategyFactory.getInstance();
                IQuestionCorrectionStrategy questionCorrectionStrategy =
                        questionCorrectionStrategyFactory.getQuestionCorrectionStrategy(studentTestQuestion);
                studentTestQuestion = questionCorrectionStrategy.getMark(studentTestQuestion);
                return studentTestQuestion.getTestQuestionMark();
            } else if (!studentTestQuestion.getTestQuestionMark().equals(Double.parseDouble("0"))) {
                newMark =
                        (newValue * studentTestQuestion.getTestQuestionMark())
                                * (java.lang.Math.pow(studentTestQuestion.getTestQuestionValue(), -1));
            }

        }
        return newMark;
    }

    // Service Invokers migrated from Berserk

    private static final ChangeStudentTestQuestionValue serviceInstance = new ChangeStudentTestQuestionValue();

    @Atomic
    public static void runChangeStudentTestQuestionValue(String executionCourseId, String distributedTestId, Double newValue,
            String questionId, String studentId, TestQuestionStudentsChangesType studentsType) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, distributedTestId, newValue, questionId, studentId, studentsType);
    }

}