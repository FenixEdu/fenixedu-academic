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
import java.util.Calendar;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.EvaluationManagementLog;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditDistributedTest {

    protected void run(String executionCourseId, final String distributedTestId, String testInformation, String evaluationTitle,
            Calendar beginDate, Calendar beginHour, Calendar endDate, Calendar endHour, TestType testType,
            CorrectionAvailability correctionAvailability, Boolean imsFeedback) throws FenixServiceException {
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        distributedTest.setTestInformation(testInformation);
        distributedTest.setEvaluationTitle(evaluationTitle);
        boolean change2EvaluationType = false, change2OtherType = false;
        if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))
                && (!testType.equals(new TestType(TestType.EVALUATION)))) {
            change2OtherType = true;
        } else if ((!distributedTest.getTestType().equals(new TestType(TestType.EVALUATION)))
                && testType.equals(new TestType(TestType.EVALUATION))) {
            change2EvaluationType = true;
        }
        distributedTest.setTestType(testType);
        distributedTest.setCorrectionAvailability(correctionAvailability);
        distributedTest.setImsFeedback(imsFeedback);

        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();

        if (dateComparator.compare(distributedTest.getBeginDate(), beginDate) != 0
                || hourComparator.compare(distributedTest.getBeginHour(), beginHour) != 0
                || dateComparator.compare(distributedTest.getEndDate(), endDate) != 0
                || hourComparator.compare(distributedTest.getEndHour(), endHour) != 0) {

            distributedTest.setBeginDate(beginDate);
            distributedTest.setBeginHour(beginHour);
            distributedTest.setEndDate(endDate);
            distributedTest.setEndHour(endHour);
        }

        if (change2OtherType) {
            // Change evaluation test to study/inquiry test
            // delete evaluation and marks
            OnlineTest onlineTest = distributedTest.getOnlineTest();
            onlineTest.delete();
            /*
             * persistentSupport.getIPersistentMark().deleteByEvaluation(onlineTest
             * ); persistentObject.deleteByOID(OnlineTest.class,
             * onlineTest.getExternalId());
             */
        } else if (change2EvaluationType) {
            // Change to evaluation test
            // Create evaluation (onlineTest) and marks
            OnlineTest onlineTest = new OnlineTest();
            onlineTest.setDistributedTest(distributedTest);
            onlineTest.addAssociatedExecutionCourses(executionCourse);
            final Set<Registration> registrations = distributedTest.findStudents();
            for (Registration registration : registrations) {
                Set<StudentTestQuestion> studentTestQuestionList =
                        StudentTestQuestion.findStudentTestQuestions(registration, distributedTest);
                double studentMark = 0;
                for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                    studentMark += studentTestQuestion.getTestQuestionMark().doubleValue();
                }
                Attends attend = registration.readAttendByExecutionCourse(executionCourse);
                if (attend != null) {
                    Mark mark = new Mark();
                    mark.setAttend(attend);
                    mark.setEvaluation(onlineTest);
                    DecimalFormat df = new DecimalFormat("#0.##");
                    df.getDecimalFormatSymbols().setDecimalSeparator('.');
                    mark.setMark(df.format(Math.max(0, studentMark)));
                }
            }
        }

        EvaluationManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.evaluation.tests.distribution.edited", distributedTest.getEvaluationTitle(),
                executionCourse.getName(), executionCourse.getDegreePresentationString());
    }

    // Service Invokers migrated from Berserk

    private static final EditDistributedTest serviceInstance = new EditDistributedTest();

    @Atomic
    public static void runEditDistributedTest(String executionCourseId, String distributedTestId, String testInformation,
            String evaluationTitle, Calendar beginDate, Calendar beginHour, Calendar endDate, Calendar endHour,
            TestType testType, CorrectionAvailability correctionAvailability, Boolean imsFeedback) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, distributedTestId, testInformation, evaluationTitle, beginDate, beginHour,
                endDate, endHour, testType, correctionAvailability, imsFeedback);
    }

}