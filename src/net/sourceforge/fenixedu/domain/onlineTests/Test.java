/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.Calendar;

/**
 * @author Susana Fernandes
 */
public class Test extends Test_Base {

    public void deleteTestQuestion(final ITestQuestion testQuestionToDelete) {
        for (final ITestQuestion testQuestion : getTestQuestions()) {
            if (!testQuestionToDelete.equals(testQuestion)) {
                Integer iterQuestionOrder = testQuestion.getTestQuestionOrder();
                if (testQuestionToDelete.getTestQuestionOrder().compareTo(iterQuestionOrder) < 0) {
                    testQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() - 1));
                }
            } else {
                testQuestion.delete();
            }
        }
        setNumberOfQuestions(new Integer(getNumberOfQuestions().intValue() - 1));
        setLastModifiedDate(Calendar.getInstance().getTime());
    }

}
