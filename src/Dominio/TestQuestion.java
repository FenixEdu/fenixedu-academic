/*
 * Created on 29/Jul/2003
 *
 */
package Dominio;

/**
 * @author Susana Fernandes
 */
public class TestQuestion extends DomainObject implements ITestQuestion {
	private Integer testQuestionOrder;
	private Integer testQuestionValue;
	private IQuestion question;
	private Integer keyQuestion;
	private ITest test;
	private Integer keyTest;

	public TestQuestion() {
	}

	public TestQuestion(Integer testId) {
		setIdInternal(testId);
	}

	public Integer getKeyQuestion() {
		return keyQuestion;
	}

	public Integer getKeyTest() {
		return keyTest;
	}

	public IQuestion getQuestion() {
		return question;
	}

	public ITest getTest() {
		return test;
	}

	public void setKeyQuestion(Integer integer) {
		keyQuestion = integer;
	}

	public void setKeyTest(Integer integer) {
		keyTest = integer;
	}

	public void setQuestion(IQuestion question) {
		this.question = question;
	}

	public void setTest(ITest test) {
		this.test = test;
	}

	public Integer getTestQuestionOrder() {
		return testQuestionOrder;
	}

	public void setTestQuestionOrder(Integer integer) {
		testQuestionOrder = integer;
	}

	public Integer getTestQuestionValue() {
		return testQuestionValue;
	}

	public void setTestQuestionValue(Integer integer) {
		testQuestionValue = integer;
	}

}
