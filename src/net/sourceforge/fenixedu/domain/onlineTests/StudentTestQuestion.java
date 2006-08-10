/*
 * Created on 27/Ago/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestion extends StudentTestQuestion_Base {

    public static final Comparator<StudentTestQuestion> COMPARATOR_BY_TEST_QUESTION_ORDER = new BeanComparator("testQuestionOrder");
	public static final Comparator<StudentTestQuestion> COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER = new ComparatorChain();
	static {
		((ComparatorChain) COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER).addComparator(new BeanComparator("student.number"));
		((ComparatorChain) COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER).addComparator(new BeanComparator("testQuestionOrder"));
	}

    public StudentTestQuestion() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
        removeDistributedTest();
        removeQuestion();
        removeStudent();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

	public static Set<StudentTestQuestion> findStudentTestQuestions(final Registration student, final DistributedTest distributedTest) {
		return findStudentTestQuestions(student.getStudentTestsQuestionsSet(), distributedTest);
	}

	public static Set<StudentTestQuestion> findStudentTestQuestions(final Question question, final DistributedTest distributedTest) {
		return findStudentTestQuestions(question.getStudentTestsQuestionsSet(), distributedTest);
	}

	private static Set<StudentTestQuestion> findStudentTestQuestions(final Set<StudentTestQuestion> inputSet, final DistributedTest distributedTest) {
		final Set<StudentTestQuestion> studentTestQuestions = new HashSet<StudentTestQuestion>();
		for (final StudentTestQuestion studentTestQuestion : inputSet) {
			if (studentTestQuestion.getDistributedTest() == distributedTest) {
				studentTestQuestions.add(studentTestQuestion);
			}
		}
		return studentTestQuestions;
	}

	public static StudentTestQuestion findStudentTestQuestion(final Question question, final Registration student, final DistributedTest distributedTest) {
		for (final StudentTestQuestion studentTestQuestion : question.getStudentTestsQuestionsSet()) {
			if (distributedTest == studentTestQuestion.getDistributedTest() && student == studentTestQuestion.getStudent()) {
				return studentTestQuestion;
			}
		}
		return null;
	}

}
