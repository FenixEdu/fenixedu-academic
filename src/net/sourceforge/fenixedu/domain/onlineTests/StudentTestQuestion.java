/*
 * Created on 27/Ago/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestion extends StudentTestQuestion_Base {
    private List<SubQuestion> studentSubQuestions;

    public static final Comparator<StudentTestQuestion> COMPARATOR_BY_TEST_QUESTION_ORDER = new BeanComparator(
            "testQuestionOrder");

    public static final Comparator<StudentTestQuestion> COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER)
                .addComparator(new BeanComparator("student.number"));
        ((ComparatorChain) COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER)
                .addComparator(new BeanComparator("testQuestionOrder"));
    }

    public StudentTestQuestion() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public List<SubQuestion> getStudentSubQuestions() {
        return studentSubQuestions;
    }

    public void setStudentSubQuestions(List<SubQuestion> studentSubQuestions) {
        this.studentSubQuestions = studentSubQuestions;
    }

    public void addStudentSubQuestion(SubQuestion subQuestion) {
        if (studentSubQuestions == null) {
            studentSubQuestions = new ArrayList<SubQuestion>();
        }
        studentSubQuestions.add(subQuestion);
    }

    public SubQuestion getSubQuestionByItem() {
        for (SubQuestion subQuestion : getStudentSubQuestions()) {
            if (getItemId() != null && subQuestion.getItemId() != null) {
                if (getItemId().equals(subQuestion.getItemId())) {
                    return subQuestion;
                }
            } else if (getItemId() == null && subQuestion.getItemId() == null) {
                return subQuestion;
            }
        }
        return null;
    }

    public void delete() {
        removeDistributedTest();
        removeQuestion();
        removeStudent();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

	public static Set<StudentTestQuestion> findStudentTestQuestions(final Registration registration, final DistributedTest distributedTest) {
		return findStudentTestQuestions(registration.getStudentTestsQuestionsSet(), distributedTest);
	}

    public static Set<StudentTestQuestion> findStudentTestQuestions(final Question question,
            final DistributedTest distributedTest) {
        return findStudentTestQuestions(question.getStudentTestsQuestionsSet(), distributedTest);
    }

    private static Set<StudentTestQuestion> findStudentTestQuestions(
            final Set<StudentTestQuestion> inputSet, final DistributedTest distributedTest) {
        final Set<StudentTestQuestion> studentTestQuestions = new HashSet<StudentTestQuestion>();
        for (final StudentTestQuestion studentTestQuestion : inputSet) {
            if (studentTestQuestion.getDistributedTest() == distributedTest) {
                studentTestQuestions.add(studentTestQuestion);
            }
        }
        return studentTestQuestions;
    }
    
	public static StudentTestQuestion findStudentTestQuestion(final Question question, final Registration registration, final DistributedTest distributedTest) {
		for (final StudentTestQuestion studentTestQuestion : question.getStudentTestsQuestionsSet()) {
			if (distributedTest == studentTestQuestion.getDistributedTest() && registration == studentTestQuestion.getStudent()) {
				return studentTestQuestion;
			}
		}
		return null;
	}

    public void setSubQuestionByItem(SubQuestion newSubQuestion) {
        int i = 0;
        for (; i < getStudentSubQuestions().size(); i++) {
            SubQuestion subQuestion = getStudentSubQuestions().get(i);
            if (getItemId() != null && subQuestion.getItemId() != null) {
                if (getItemId().equals(subQuestion.getItemId())) {
                    break;
                }
            } else if (getItemId() == null && subQuestion.getItemId() == null) {
                break;
            }
        }
        getStudentSubQuestions().set(i, newSubQuestion);
    }
}
