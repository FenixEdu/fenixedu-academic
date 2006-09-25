/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public class Test extends Test_Base {

    public Test() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void insertTestQuestion(final Question question, final Integer testQuestionOrder, final Double testQuestionValue,
            final CorrectionFormula formula) {
        organizeTestQuestionsOrder(testQuestionOrder);
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(question);
        testQuestion.setTestQuestionOrder(testQuestionOrder);
        testQuestion.setTestQuestionValue(testQuestionValue);
        testQuestion.setCorrectionFormula(formula);
        testQuestion.setTest(this);
        this.setLastModifiedDate(Calendar.getInstance().getTime());

    }

    private void organizeTestQuestionsOrder(Integer testQuestionToInsertOrder) {

        List<TestQuestion> testQuestions = getTestQuestions();
        for (final TestQuestion testQuestion : testQuestions) {
            Integer iterQuestionOrder = testQuestion.getTestQuestionOrder();
            if (testQuestionToInsertOrder.compareTo(iterQuestionOrder) <= 0) {
                testQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() + 1));
            }
        }
    }

    public void deleteTestQuestion(final TestQuestion testQuestionToDelete) {
        List<TestQuestion> aux = new ArrayList<TestQuestion>();

        List<TestQuestion> testQuestions = getTestQuestions();
        for (final TestQuestion testQuestion : testQuestions) {
            if (!testQuestionToDelete.equals(testQuestion)) {
                Integer iterQuestionOrder = testQuestion.getTestQuestionOrder();
                if (testQuestionToDelete.getTestQuestionOrder().compareTo(iterQuestionOrder) < 0) {
                    testQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder.intValue() - 1));
                }
            } else {
                aux.add(testQuestion);
            }
        }

        for (final TestQuestion testQuestion : aux) {
            testQuestion.delete();
        }

        setLastModifiedDate(Calendar.getInstance().getTime());
    }

    public TestQuestion getTestQuestion(final Question question) {
        for (final TestQuestion testQuestion : getTestQuestions()) {
            if (testQuestion.getQuestion() == question) {
                return testQuestion;
            }
        }
        return null;
    }

    public void delete() {
        for (; !getTestQuestions().isEmpty(); getTestQuestions().get(0).delete())
            ;
        removeTestScope();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public Question findQuestionByOID(Integer questionId) {
        for (TestQuestion testQuestion : this.getTestQuestions()) {
            if (testQuestion.getQuestion().getIdInternal().equals(questionId)) {
                return testQuestion.getQuestion();
            }
        }
        return null;
    }

    public String getLastModifiedDateFormatted() {
        String result = "";
        Date date = getLastModifiedDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        result += calendar.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += calendar.get(Calendar.MONTH) + 1;
        result += "/";
        result += calendar.get(Calendar.YEAR);
        result += " ";
        result += calendar.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (calendar.get(Calendar.MINUTE) < 10)
            result += "0";
        result += calendar.get(Calendar.MINUTE);

        return result;
    }

    public String getCreationDateFormatted() {
        String result = "";
        Date date = getCreationDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        result += calendar.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += calendar.get(Calendar.MONTH) + 1;
        result += "/";
        result += calendar.get(Calendar.YEAR);
        result += " ";
        result += calendar.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (calendar.get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        result += calendar.get(Calendar.MINUTE);

        return result;
    }
}
