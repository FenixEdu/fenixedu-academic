/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class DistributedTest extends DistributedTest_Base {

    public DistributedTest() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public void delete() {
        for (; hasAnyDistributedTestQuestions(); getDistributedTestQuestions().get(0).delete());
        for (; hasAnyDistributedTestAdvisories(); getDistributedTestAdvisories().get(0).delete());
        for (; hasAnyStudentsLogs(); getStudentsLogs().get(0).delete());
        if (getTestType().getType().intValue() == TestType.EVALUATION) {
            getOnlineTest().delete();
        }
        deleteQuestions();
        
        removeTestScope();
        removeRootDomainObject();
        
        deleteDomainObject();
    }
    
    private void deleteQuestions() {
        for (StudentTestQuestion studentTestQuestion : getDistributedTestQuestions()) {
            if(!studentTestQuestion.getQuestion().getVisibility() && !isInOtherDistributedTest(studentTestQuestion.getQuestion())) {
                studentTestQuestion.getQuestion().delete();
            }
        }
    }
    
    private boolean isInOtherDistributedTest(Question question) {
        for (StudentTestQuestion studentTestQuestion : question.getStudentTestsQuestions()) {
            if(!studentTestQuestion.getDistributedTest().equals(this)) {
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
    
    public List<StudentTestLog> getStudentTestLogs(final Registration student){
    	List<StudentTestLog> result = new ArrayList<StudentTestLog>();
    	for (final StudentTestLog studentTestLog : this.getStudentsLogs()) {
			if(studentTestLog.getStudent().equals(student)) {
				result.add(studentTestLog);
			}
		}
    	return result;
    }
    
    public void updateDistributedTestAdvisoryDates(final Date newExpiresDate) {
    	for (final DistributedTestAdvisory distributedTestAdvisory : this.getDistributedTestAdvisories()) {
			distributedTestAdvisory.getAdvisory().setExpires(newExpiresDate);
		}
    }

    public SortedSet<StudentTestQuestion> getStudentTestQuestionsSortedByStudentNumberAndTestQuestionOrder() {
    	final SortedSet<StudentTestQuestion> studentTestQuestions = new TreeSet<StudentTestQuestion>(StudentTestQuestion.COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER);
    	studentTestQuestions.addAll(getDistributedTestQuestionsSet());
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
        final Set<Registration> students = new HashSet<Registration>();
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            students.add(studentTestQuestion.getStudent());
        }
        return students;
    }

    public SortedSet<StudentTestQuestion> findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder() {
        final SortedSet<StudentTestQuestion> studentTestQuestions = new TreeSet<StudentTestQuestion>(StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        final Registration registration = getDistributedTestQuestionsSet() != null ? getDistributedTestQuestionsSet().iterator().next().getStudent() : null;
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

    public Double calculateTestFinalMarkForStudent(final Registration student) {
        double result = 0;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (student == studentTestQuestion.getStudent()) {
                result += studentTestQuestion.getTestQuestionMark().doubleValue();
            }
        }
        return Double.valueOf(result);
    }

    public int countLikeResponses(final Integer order, final String response) {
        int count = 0;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (studentTestQuestion.getTestQuestionOrder().equals(order) && studentTestQuestion.getResponse().contains(response)) {
                count++;
            }
        }
        return count;
    }

    public int countResponses(final Integer order, final String response) {
        int count = 0;
        for (final StudentTestQuestion studentTestQuestion : getDistributedTestQuestionsSet()) {
            if (studentTestQuestion.getTestQuestionOrder().equals(order) && studentTestQuestion.getResponse().equals(response)) {
                count++;
            }
        }
        return count;
    }

    public Set<String> findResponses() {
        final Set<String> responses = new HashSet<String>();
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

    public Question findQuestionByOID(Integer questionId){
        for (StudentTestQuestion studentTestQuestion : this.getDistributedTestQuestions()) {
            if(studentTestQuestion.getQuestion().getIdInternal().equals(questionId)){
                return studentTestQuestion.getQuestion();
            }
        }
        return null;
    }
}
