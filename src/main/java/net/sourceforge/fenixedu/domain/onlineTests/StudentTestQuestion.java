/*
 * Created on 27/Ago/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestion extends StudentTestQuestion_Base {
    private static InheritableThreadLocal<Map<StudentTestQuestion, List<SubQuestion>>> studentSubQuestions =
            new InheritableThreadLocal<Map<StudentTestQuestion, List<SubQuestion>>>();

    public static final Comparator<StudentTestQuestion> COMPARATOR_BY_TEST_QUESTION_ORDER = new BeanComparator(
            "testQuestionOrder");

    public static final Comparator<StudentTestQuestion> COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER =
            new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER).addComparator(new BeanComparator(
                "student.number"));
        ((ComparatorChain) COMPARATOR_BY_STUDENT_NUMBER_AND_TEST_QUESTION_ORDER).addComparator(new BeanComparator(
                "testQuestionOrder"));
    }

    public StudentTestQuestion() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public List<SubQuestion> getStudentSubQuestions() {
        if (studentSubQuestions.get() == null) {
            studentSubQuestions.set(new HashMap<StudentTestQuestion, List<SubQuestion>>());
        }

        return studentSubQuestions.get().get(this);
    }

    public void setStudentSubQuestions(List<SubQuestion> studentSubQuestions) {
        getStudentSubQuestions();
        this.studentSubQuestions.get().put(this, studentSubQuestions);
    }

    public void addStudentSubQuestion(SubQuestion subQuestion) {
        if (getStudentSubQuestions() == null) {
            setStudentSubQuestions(new ArrayList<SubQuestion>());
        }
        getStudentSubQuestions().add(subQuestion);
    }

    public SubQuestion getSubQuestionByItem() {
        if (getItemId() == null && !getStudentSubQuestions().isEmpty() && getStudentSubQuestions().size() == 1) {
            return getStudentSubQuestions().iterator().next();
        }
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

    public boolean isSubQuestion() {
        if (getItemId() != null && (!getQuestion().getSubQuestions().iterator().next().getItemId().equals(getItemId()))) {
            return true;
        }
        return false;
    }

    public void delete() {
        setDistributedTest(null);
        setQuestion(null);
        setStudent(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public static boolean hasStudentTestQuestions(final Registration registration, final DistributedTest distributedTest) {
        Set<StudentTestQuestion> studentTestQuestions = findStudentTestQuestions(registration, distributedTest);
        return (studentTestQuestions == null || studentTestQuestions.size() == 0) ? false : true;
    }

    public static Set<StudentTestQuestion> findStudentTestQuestions(final Registration registration,
            final DistributedTest distributedTest) {
        return findStudentTestQuestions(registration.getStudentTestsQuestionsSet(), distributedTest);
    }

    public static Set<StudentTestQuestion> findStudentTestQuestions(final Question question, final DistributedTest distributedTest) {
        return findStudentTestQuestions(question.getStudentTestsQuestionsSet(), distributedTest);
    }

    private static Set<StudentTestQuestion> findStudentTestQuestions(final Set<StudentTestQuestion> inputSet,
            final DistributedTest distributedTest) {
        final Set<StudentTestQuestion> studentTestQuestions = new HashSet<StudentTestQuestion>();
        for (final StudentTestQuestion studentTestQuestion : inputSet) {
            if (studentTestQuestion.getDistributedTest() == distributedTest) {
                studentTestQuestions.add(studentTestQuestion);
            }
        }
        return studentTestQuestions;
    }

    public static StudentTestQuestion findStudentTestQuestion(final Question question, final Registration registration,
            final DistributedTest distributedTest) {
        for (final StudentTestQuestion studentTestQuestion : question.getStudentTestsQuestionsSet()) {
            if (distributedTest == studentTestQuestion.getDistributedTest() && registration == studentTestQuestion.getStudent()) {
                return studentTestQuestion;
            }
        }
        return null;
    }

    public void setSubQuestionByItem(SubQuestion newSubQuestion) {
        if (getItemId() == null && !getStudentSubQuestions().isEmpty() && getStudentSubQuestions().size() == 1) {
            getStudentSubQuestions().set(0, newSubQuestion);
        } else {
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
    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasTestQuestionOrder() {
        return getTestQuestionOrder() != null;
    }

    @Deprecated
    public boolean hasItemId() {
        return getItemId() != null;
    }

    @Deprecated
    public boolean hasDistributedTest() {
        return getDistributedTest() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasQuestion() {
        return getQuestion() != null;
    }

    @Deprecated
    public boolean hasTestQuestionMark() {
        return getTestQuestionMark() != null;
    }

    @Deprecated
    public boolean hasResponse() {
        return getResponse() != null;
    }

    @Deprecated
    public boolean hasTestQuestionValue() {
        return getTestQuestionValue() != null;
    }

    @Deprecated
    public boolean hasCorrectionFormula() {
        return getCorrectionFormula() != null;
    }

    @Deprecated
    public boolean hasOptionShuffle() {
        return getOptionShuffle() != null;
    }

}
