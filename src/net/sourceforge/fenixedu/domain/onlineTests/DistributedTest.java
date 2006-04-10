/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Student;
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
    
    public List<StudentTestLog> getStudentTestLogs(final Student student){
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

}
