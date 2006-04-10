/*
 * Created on 27/Ago/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Student;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestion extends StudentTestQuestion_Base {

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

	public static Set<StudentTestQuestion> findStudentTestQuestions(final Student student, final DistributedTest distributedTest) {
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

}
