/*
 * Created on 29/Jul/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public class TestQuestion extends TestQuestion_Base {
    public static final Comparator<TestQuestion> COMPARATOR_BY_TEST_QUESTION_ORDER = new BeanComparator(
            "testQuestionOrder");

    public TestQuestion() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void editTestQuestion(Integer newTestQuestionOrder, final Double newTestQuestionValue,
            final CorrectionFormula newFormula) {
        organizeTestQuestionsOrder(newTestQuestionOrder, this.getTestQuestionOrder());
        this.setTestQuestionOrder(newTestQuestionOrder);
        this.setTestQuestionValue(newTestQuestionValue);
        this.setCorrectionFormula(newFormula);
    }

    private void organizeTestQuestionsOrder(Integer newOrder, Integer oldOrder) {
        List<TestQuestion> testQuestions = getTest().getTestQuestions();
        int diffOrder = newOrder.intValue() - oldOrder.intValue();
        if (diffOrder != 0) {
            if (diffOrder > 0) {
                for (TestQuestion testQuestion : testQuestions) {
                    if (testQuestion.getTestQuestionOrder().compareTo(newOrder) <= 0
                            && testQuestion.getTestQuestionOrder().compareTo(oldOrder) > 0) {
                        testQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder() - 1);
                    }
                }
            } else {
                for (TestQuestion testQuestion : testQuestions) {
                    if (testQuestion.getTestQuestionOrder().compareTo(newOrder) >= 0
                            && testQuestion.getTestQuestionOrder().compareTo(oldOrder) < 0) {
                        testQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder() + 1);
                    }
                }
            }
        }
    }

    public void delete() {
        removeQuestion();
        removeTest();
        removeRootDomainObject();
        deleteDomainObject();
    }

}
