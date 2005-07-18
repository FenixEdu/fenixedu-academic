/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Susana Fernandes
 */
public class Test extends Test_Base {

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
