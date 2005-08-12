/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public class Test extends Test_Base {

    public void insertTestQuestion(final IQuestion question, final Integer testQuestionOrder, final Double testQuestionValue,
            final CorrectionFormula formula) {
        organizeTestQuestionsOrder(testQuestionOrder);
        ITestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(question);
        testQuestion.setTestQuestionOrder(testQuestionOrder);
        testQuestion.setTestQuestionValue(testQuestionValue);
        testQuestion.setCorrectionFormula(formula);
        testQuestion.setTest(this);
        this.setNumberOfQuestions(new Integer(this.getNumberOfQuestions().intValue() + 1));
        this.setLastModifiedDate(Calendar.getInstance().getTime());

    }

    private void organizeTestQuestionsOrder(Integer testQuestionToInsertOrder) {

        List<ITestQuestion> testQuestions = getTestQuestions();
        for (final ITestQuestion testQuestion : testQuestions) {
            Integer iterQuestionOrder = testQuestion.getTestQuestionOrder();
            if (testQuestionToInsertOrder.compareTo(iterQuestionOrder) <= 0) {
                testQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() + 1));
            }
        }
    }

    public void deleteTestQuestion(final ITestQuestion testQuestionToDelete) {
        List<ITestQuestion> aux = new ArrayList<ITestQuestion>();

        List<ITestQuestion> testQuestions = getTestQuestions();
        for (final ITestQuestion testQuestion : testQuestions) {
            if (!testQuestionToDelete.equals(testQuestion)) {
                Integer iterQuestionOrder = testQuestion.getTestQuestionOrder();
                if (testQuestionToDelete.getTestQuestionOrder().compareTo(iterQuestionOrder) < 0) {
                    testQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() - 1));
                }
            } else {
                aux.add(testQuestion);
            }
        }

        for (final ITestQuestion testQuestion : aux) {
            testQuestion.delete();
        }

        setNumberOfQuestions(new Integer(getNumberOfQuestions().intValue() - 1));
        setLastModifiedDate(Calendar.getInstance().getTime());
    }

}
