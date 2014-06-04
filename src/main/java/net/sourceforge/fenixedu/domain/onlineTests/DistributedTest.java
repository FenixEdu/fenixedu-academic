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
/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.EvaluationManagementLog;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class DistributedTest extends DistributedTest_Base {

    public DistributedTest() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Override
    public java.lang.String getEvaluationTitle() {
        String evaluationTitle = super.getEvaluationTitle();
        if (evaluationTitle == null || evaluationTitle.length() == 0) {
            return getTitle();
        }
        return evaluationTitle;
    }

    public void delete() {
        ExecutionCourse ec = getTestScope().getExecutionCourse();
        EvaluationManagementLog.createLog(ec, Bundle.MESSAGING,
                "log.executionCourse.evaluation.tests.distribution.removed", getEvaluationTitle(), getBeginDateTimeFormatted(),
                ec.getName(), ec.getDegreePresentationString());

        for (; hasAnyDistributedTestQuestions(); getDistributedTestQuestions().iterator().next().delete()) {
            ;
        }
        for (; hasAnyStudentsLogs(); getStudentsLogs().iterator().next().delete()) {
            ;
        }
        if (getTestType().getType().intValue() == TestType.EVALUATION) {
            getOnlineTest().delete();
        }
        deleteQuestions();

        setTestScope(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    private void deleteQuestions() {
        for (StudentTestQuestion studentTestQuestion : getDistributedTestQuestions()) {
            if (!studentTestQuestion.getQuestion().getVisibility()
                    && !isInOtherDistributedTest(studentTestQuestion.getQuestion())) {
                studentTestQuestion.getQuestion().delete();
            }
        }
    }

    private boolean isInOtherDistributedTest(Question question) {
        for (StudentTestQuestion studentTestQuestion : question.getStudentTestsQuestions()) {
            if (!studentTestQuestion.getDistributedTest().equals(this)) {
                return true;
            }
        }
        return false;
    }

    public Calendar getBeginDate() {
        if (getBeginDateDate() != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(getBeginDateDate());
            return calendar;
        }

        return null;
    }

    public void setBeginDate(Calendar beginDate) {
        final Date date = (beginDate != null) ? beginDate.getTime() : null;
        setBeginDateDate(date);
    }

    public Calendar getBeginHour() {
        if (getBeginHourDate() != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(getBeginHourDate());
            return calendar;
        }

        return null;
    }

    public void setBeginHour(Calendar beginHour) {
        final Date date = (beginHour != null) ? beginHour.getTime() : null;
        setBeginHourDate(date);
    }

    public Calendar getEndDate() {
        if (getEndDateDate() != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(getEndDateDate());
            return calendar;
        }

        return null;
    }

    public void setEndDate(Calendar endDate) {
        final Date date = (endDate != null) ? endDate.getTime() : null;
        setEndDateDate(date);
    }

    public Calendar getEndHour() {
        if (getEndHourDate() != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(getEndHourDate());
            return calendar;
        }

        return null;
    }

    public void setEndHour(Calendar endHour) {
        final Date date = (endHour != null) ? endHour.getTime() : null;
        setEndHourDate(date);
    }

    public StudentTestLog getLastSubmissionStudentTestLog(final String registrationId) {
        Registration registration = FenixFramework.getDomainObject(registrationId);
        for (final StudentTestLog studentTestLog : this.getStudentsLogs()) {
            if (studentTestLog.getEvent().startsWith("Submeter Teste;") && registration.equals(studentTestLog.getStudent())) {
                return studentTestLog;
            }
        }
        return null;
    }

    public List<StudentTestLog> getStudentTestLogs(final Registration registration) {
        List<StudentTestLog> result = new ArrayList<StudentTestLog>();
        for (final StudentTestLog studentTestLog : this.getStudentsLogs()) {
            if (studentTestLog.getStudent().equals(registration)) {
                result.add(studentTestLog);
            }
        }
        return result;
    }

    public List<StudentTestQuestion> getStudentTestQuestionsSortedByStudentNumberAndTestQuestionOrder() {
        final List<StudentTestQuestion> studentTestQuestions =
                new ArrayList<StudentTestQuestion>(getDistributedTestQuestionsSet());
        Collections.sort(studentTestQuestions, StudentTestQuestion.COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER);
        return studentTestQuestions;
    }

    public Set<StudentTestQuestion> findStudentTestQuestionsByTestQuestionOrder(final Integer order) {
        final Set<StudentTestQuestion> studentTestQuestions = new HashSet<StudentTestQuestion>();
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (studentTestQuestion.getTestQuestionOrder().equals(order)) {
                studentTestQuestions.add(studentTestQuestion);
            }
        }
        return studentTestQuestions;
    }

    public Set<Registration> findStudents() {
        final SortedSet<Registration> students = new TreeSet<Registration>(Registration.COMPARATOR_BY_NUMBER_AND_ID);
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            students.add(studentTestQuestion.getStudent());
        }
        return students;
    }

    public SortedSet<StudentTestQuestion> findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder() {
        final SortedSet<StudentTestQuestion> studentTestQuestions =
                new TreeSet<StudentTestQuestion>(StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        final Registration registration =
                getDistributedTestQuestionsSet() != null ? getDistributedTestQuestionsSet().iterator().next().getStudent() : null;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (registration == studentTestQuestion.getStudent()) {
                studentTestQuestions.add(studentTestQuestion);
            }
        }
        return studentTestQuestions;
    }

    public SortedSet<StudentTestQuestion> findStudentTestQuestions(final Registration registration) {
        final SortedSet<StudentTestQuestion> studentTestQuestions =
                new TreeSet<StudentTestQuestion>(StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (registration == studentTestQuestion.getStudent()) {
                studentTestQuestions.add(studentTestQuestion);
            }
        }
        return studentTestQuestions;
    }

    public Double calculateMaximumDistributedTestMark() {
        double result = 0;
        for (final StudentTestQuestion studentTestQuestion : findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder()) {
            result += studentTestQuestion.getTestQuestionValue().doubleValue();
        }
        return Double.valueOf(result);
    }

    public Double calculateTestFinalMarkForStudent(final Registration registration) {
        double result = 0;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (registration == studentTestQuestion.getStudent()) {
                result += studentTestQuestion.getTestQuestionMark().doubleValue();
            }
        }
        return Double.valueOf(result);
    }

    public int countLikeResponses(final Integer order, final Response response) {
        int count = 0;
        // for (final StudentTestQuestion studentTestQuestion :
        // getDistributedTestQuestionsSet()) {
        // if (studentTestQuestion.getTestQuestionOrder().equals(order)
        // && studentTestQuestion.getResponse().contains(response)) {
        // count++;
        // }
        // }
        return count;
    }

    public int countResponses(final Integer order, final String response) {
        int count = 0;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (studentTestQuestion.getResponse() != null && studentTestQuestion.getTestQuestionOrder().equals(order)
                    && studentTestQuestion.getResponse().hasResponse(response)) {
                count++;
            }
        }
        return count;
    }

    public Set<Response> findResponses() {
        final Set<Response> responses = new HashSet<Response>();
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (studentTestQuestion.getResponse() != null) {
                responses.add(studentTestQuestion.getResponse());
            }
        }
        return responses;
    }

    public int countResponses(final Integer order, final boolean responded) {
        int count = 0;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (order == null || studentTestQuestion.getTestQuestionOrder().equals(order)) {
                if (responded && studentTestQuestion.getResponse() != null) {
                    count++;
                } else if (!responded && studentTestQuestion.getResponse() == null) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countAnsweres(final Integer order, final double mark, final boolean correct) {
        int count = 0;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (studentTestQuestion.getTestQuestionOrder().equals(order)) {
                if (correct && studentTestQuestion.getTestQuestionMark().doubleValue() >= mark) {
                    count++;
                } else if (!correct && studentTestQuestion.getTestQuestionMark() <= 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countPartiallyCorrectAnswers(final Integer order, final double mark) {
        int count = 0;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (studentTestQuestion.getTestQuestionOrder().equals(order) && studentTestQuestion.getResponse() != null) {
                final double testQuestionMark = studentTestQuestion.getTestQuestionMark().doubleValue();
                if (testQuestionMark < mark && testQuestionMark > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countNumberOfStudents() {
        return getDistributedTestQuestionsSet().size() / getNumberOfQuestions().intValue();
    }

    public Question findQuestionByOID(String questionId) {
        for (StudentTestQuestion studentTestQuestion : this.getDistributedTestQuestions()) {
            if (studentTestQuestion.getQuestion().getExternalId().equals(questionId)) {
                return studentTestQuestion.getQuestion();
            }
        }
        return null;
    }

    public String getBeginDateTimeFormatted() {
        String result = new String();
        Calendar date = getBeginDate();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        result += " ";
        date = getBeginHour();
        result += date.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        result += date.get(Calendar.MINUTE);
        return result;
    }

    public String getEndDateTimeFormatted() {
        String result = new String();
        Calendar date = getEndDate();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        result += " ";
        date = getEndHour();
        result += date.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        result += date.get(Calendar.MINUTE);
        return result;
    }

    public String getBeginDayFormatted() {
        String result = new String();
        if (getBeginDate().get(Calendar.DAY_OF_MONTH) < 10) {
            result += "0";
        }
        return result.concat(Integer.valueOf(getBeginDate().get(Calendar.DAY_OF_MONTH)).toString());

    }

    public String getBeginMonthFormatted() {
        String result = new String();
        if (getBeginDate().get(Calendar.MONTH) + 1 < 10) {
            result += "0";
        }
        return result.concat(Integer.valueOf(getBeginDate().get(Calendar.MONTH) + 1).toString());
    }

    public String getBeginYearFormatted() {
        return new Integer(getBeginDate().get(Calendar.YEAR)).toString();
    }

    public String getBeginHourFormatted() {
        String result = new String();
        if (getBeginHour().get(Calendar.HOUR_OF_DAY) < 10) {
            result += "0";
        }
        return result.concat(Integer.valueOf(getBeginHour().get(Calendar.HOUR_OF_DAY)).toString());
    }

    public String getBeginMinuteFormatted() {
        String result = new String();
        if (getBeginHour().get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        return result.concat(Integer.valueOf(getBeginHour().get(Calendar.MINUTE)).toString());
    }

    public String getEndDayFormatted() {
        String result = new String();
        if (getEndDate().get(Calendar.DAY_OF_MONTH) < 10) {
            result += "0";
        }
        return result.concat(Integer.valueOf(getEndDate().get(Calendar.DAY_OF_MONTH)).toString());
    }

    public String getEndMonthFormatted() {
        String result = new String();
        if (getEndDate().get(Calendar.MONTH) + 1 < 10) {
            result += "0";
        }
        return result.concat(Integer.valueOf(getEndDate().get(Calendar.MONTH) + 1).toString());
    }

    public String getEndYearFormatted() {
        return new Integer(getEndDate().get(Calendar.YEAR)).toString();
    }

    public String getEndHourFormatted() {
        String result = new String();
        if (getEndHour().get(Calendar.HOUR_OF_DAY) < 10) {
            result += "0";
        }
        return result.concat(Integer.valueOf(getEndHour().get(Calendar.HOUR_OF_DAY)).toString());

    }

    public String getEndMinuteFormatted() {
        String result = new String();
        if (getEndHour().get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        return result.concat(Integer.valueOf(getEndHour().get(Calendar.MINUTE)).toString());
    }

    @Deprecated
    public java.util.Date getBeginDateDate() {
        org.joda.time.YearMonthDay ymd = getBeginDateDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setBeginDateDate(java.util.Date date) {
        if (date == null) {
            setBeginDateDateYearMonthDay(null);
        } else {
            setBeginDateDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getBeginHourDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getBeginHourDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setBeginHourDate(java.util.Date date) {
        if (date == null) {
            setBeginHourDateHourMinuteSecond(null);
        } else {
            setBeginHourDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndDateDate() {
        org.joda.time.YearMonthDay ymd = getEndDateDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndDateDate(java.util.Date date) {
        if (date == null) {
            setEndDateDateYearMonthDay(null);
        } else {
            setEndDateDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndHourDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEndHourDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndHourDate(java.util.Date date) {
        if (date == null) {
            setEndHourDateHourMinuteSecond(null);
        } else {
            setEndHourDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog> getStudentsLogs() {
        return getStudentsLogsSet();
    }

    @Deprecated
    public boolean hasAnyStudentsLogs() {
        return !getStudentsLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion> getDistributedTestQuestions() {
        return getDistributedTestQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyDistributedTestQuestions() {
        return !getDistributedTestQuestionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBeginDateDateYearMonthDay() {
        return getBeginDateDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasCorrectionAvailability() {
        return getCorrectionAvailability() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBeginHourDateHourMinuteSecond() {
        return getBeginHourDateHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasOnlineTest() {
        return getOnlineTest() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

    @Deprecated
    public boolean hasTestScope() {
        return getTestScope() != null;
    }

    @Deprecated
    public boolean hasNumberOfQuestions() {
        return getNumberOfQuestions() != null;
    }

    @Deprecated
    public boolean hasEvaluationTitle() {
        return getEvaluationTitle() != null;
    }

    @Deprecated
    public boolean hasImsFeedback() {
        return getImsFeedback() != null;
    }

    @Deprecated
    public boolean hasTestType() {
        return getTestType() != null;
    }

    @Deprecated
    public boolean hasEndDateDateYearMonthDay() {
        return getEndDateDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasEndHourDateHourMinuteSecond() {
        return getEndHourDateHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasTestInformation() {
        return getTestInformation() != null;
    }

}
