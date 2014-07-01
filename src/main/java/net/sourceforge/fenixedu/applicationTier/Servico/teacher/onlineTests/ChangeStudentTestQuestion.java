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
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.tests.TestQuestionChangesType;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ChangeStudentTestQuestion {

    protected Boolean run(ExecutionCourse executionCourse, String distributedTestId, String oldQuestionId, String newMetadataId,
            String studentId, TestQuestionChangesType changesType, Boolean delete, TestQuestionStudentsChangesType studentsType,
            String path) throws FenixServiceException {

        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        Question oldQuestion = distributedTest.findQuestionByOID(oldQuestionId);

        if (oldQuestion == null) {
            throw new InvalidArgumentsServiceException();
        }

        Metadata metadata = null;

        List<Question> availableQuestions = new ArrayList<Question>();
        if (newMetadataId != null) {
            metadata = FenixFramework.getDomainObject(newMetadataId);
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
            availableQuestions.addAll(metadata.getVisibleQuestions());
        } else {
            availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
            availableQuestions.remove(oldQuestion);
        }

        final Set<DistributedTest> distributedTestList;
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS) {
            distributedTestList = oldQuestion.findDistributedTests();
        } else {
            distributedTestList = new HashSet<DistributedTest>();

            if (distributedTest == null) {
                throw new InvalidArgumentsServiceException();
            }
            distributedTestList.add(distributedTest);

        }
        boolean canDelete = true;
        for (DistributedTest currentDistributedTest : distributedTestList) {
            Collection<StudentTestQuestion> studentsTestQuestionList = new ArrayList<StudentTestQuestion>();

            if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
                Registration registration = FenixFramework.getDomainObject(studentId);
                if (registration == null) {
                    throw new InvalidArgumentsServiceException();
                }
                studentsTestQuestionList.add(StudentTestQuestion.findStudentTestQuestion(oldQuestion, registration,
                        currentDistributedTest));
            } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
                Registration registration = FenixFramework.getDomainObject(studentId);
                if (registration == null) {
                    throw new InvalidArgumentsServiceException();
                }
                Integer order =
                        StudentTestQuestion.findStudentTestQuestion(oldQuestion, registration, currentDistributedTest)
                                .getTestQuestionOrder();
                studentsTestQuestionList = currentDistributedTest.findStudentTestQuestionsByTestQuestionOrder(order);
            } else {
                studentsTestQuestionList = StudentTestQuestion.findStudentTestQuestions(oldQuestion, currentDistributedTest);
            }

            List<InfoStudent> group = new ArrayList<InfoStudent>();

            for (StudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
                if (!compareDates(studentTestQuestion.getDistributedTest().getEndDate(), studentTestQuestion.getDistributedTest()
                        .getEndHour())) {
                    canDelete = false;
                } else {
                    if (availableQuestions.size() == 0) {
                        availableQuestions.addAll(getNewQuestionList(metadata, oldQuestion));
                    }

                    Question newQuestion = getNewQuestion(availableQuestions);
                    if (newMetadataId == null && (newQuestion == null || newQuestion.equals(oldQuestion))) {
                        return Boolean.FALSE;
                    } else if (newQuestion == null) {
                        throw new InvalidArgumentsServiceException();
                    }

                    studentTestQuestion.setQuestion(newQuestion);
                    studentTestQuestion.setItemId(null);
                    studentTestQuestion.setResponse(null);
                    studentTestQuestion.setOptionShuffle(null);
                    studentTestQuestion.setStudentSubQuestions(null);
                    availableQuestions.remove(newQuestion);
                    double oldMark = studentTestQuestion.getTestQuestionMark().doubleValue();
                    studentTestQuestion.setTestQuestionMark(new Double(0));
                    if (!group.contains(studentTestQuestion.getStudent().getPerson())) {
                        group.add(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));
                    }
                    if (studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                        OnlineTest onlineTest = studentTestQuestion.getDistributedTest().getOnlineTest();
                        Attends attend =
                                studentTestQuestion.getStudent().readAttendByExecutionCourse(
                                        (currentDistributedTest.getTestScope().getExecutionCourse()));
                        Mark mark = onlineTest.getMarkByAttend(attend);
                        if (mark != null) {
                            mark.setMark(getNewStudentMark(studentTestQuestion.getDistributedTest(),
                                    studentTestQuestion.getStudent(), oldMark));
                        }
                    }
                    String event =
                            BundleUtil.getString(Bundle.APPLICATION, "message.changeStudentQuestionLogMessage",
                                    studentTestQuestion.getTestQuestionOrder().toString());

                    new StudentTestLog(studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent(), event);
                }

            }
        }

        if (delete.booleanValue()) {
            metadata = oldQuestion.getMetadata();
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
            removeOldTestQuestion(oldQuestion);
            List<Question> metadataQuestions = metadata.getVisibleQuestions();

            if (metadataQuestions != null && metadataQuestions.size() <= 1) {
                metadata.setVisibility(Boolean.FALSE);
            }

            if (canDelete) {
                oldQuestion.delete();
                if (metadata.getQuestionsSet().size() <= 1) {
                    metadata.delete();
                }
            } else {
                oldQuestion.setVisibility(Boolean.FALSE);
            }

        }
        return Boolean.TRUE;
    }

    private Question getNewQuestion(List<Question> questions) {

        Question question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question;
    }

    private List<Question> getNewQuestionList(Metadata metadata, Question oldQuestion) {
        List<Question> result = new ArrayList<Question>();
        if (metadata != null) {
            result.addAll(metadata.getVisibleQuestions());
        } else {
            result.addAll(oldQuestion.getMetadata().getVisibleQuestions());
            result.remove(oldQuestion);
        }
        return result;
    }

    private boolean compareDates(Calendar date, Calendar hour) {
        Calendar calendar = Calendar.getInstance();
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        if (dateComparator.compare(calendar, date) <= 0) {
            if (dateComparator.compare(calendar, date) == 0) {
                if (hourComparator.compare(calendar, hour) <= 0) {
                    return true;
                }

                return false;
            }
            return true;
        }
        return false;
    }

    private void removeOldTestQuestion(Question oldQuestion) {
        Collection<TestQuestion> testQuestionOldList = oldQuestion.getTestQuestions();
        List<Question> availableQuestions = new ArrayList<Question>();
        availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
        availableQuestions.remove(oldQuestion);

        Question newQuestion = getNewQuestion(availableQuestions);

        if (newQuestion == null || newQuestion.equals(oldQuestion)) {
            for (TestQuestion oldTestQuestion : testQuestionOldList) {
                Test test = oldTestQuestion.getTest();
                test.deleteTestQuestion(oldTestQuestion);
            }
        } else {
            for (TestQuestion oldTestQuestion : testQuestionOldList) {
                oldTestQuestion.setQuestion(newQuestion);
            }
        }
    }

    private String getNewStudentMark(DistributedTest dt, Registration s, double mark2Remove) {
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

    // Service Invokers migrated from Berserk

    private static final ChangeStudentTestQuestion serviceInstance = new ChangeStudentTestQuestion();

    @Atomic
    public static Boolean runChangeStudentTestQuestion(ExecutionCourse executionCourse, String distributedTestId,
            String oldQuestionId, String newMetadataId, String studentId, TestQuestionChangesType changesType, Boolean delete,
            TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse);
        return serviceInstance.run(executionCourse, distributedTestId, oldQuestionId, newMetadataId, studentId, changesType,
                delete, studentsType, path);
    }

}