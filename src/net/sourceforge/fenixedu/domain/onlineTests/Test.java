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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Susana Fernandes
 */
public class Test extends Test_Base {

    public Test(String title, String information, TestScope testScope) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setTitle(title);
	setInformation(information);
	DateTime now = new DateTime();
	setCreationDateDateTime(now);
	setLastModifiedDateDateTime(now);
	setTestScope(testScope);

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
	this.setLastModifiedDateDateTime(new DateTime());

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

	setLastModifiedDateDateTime(new DateTime());
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
	DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
	return dateFormat.print(getLastModifiedDateDateTime());
    }

    public String getCreationDateFormatted() {
	DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
	return dateFormat.print(getCreationDateDateTime());
    }
}
