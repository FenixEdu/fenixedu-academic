/*
 * Created on 27/Ago/2003
 *
 */
package Dominio;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestion
	extends DomainObject
	implements IStudentTestQuestion {
	private Integer keyStudent;
	private IStudent student;
	private Integer keyDistributedTest;
	private IDistributedTest distributedTest;
	private IQuestion question;
	private Integer keyQuestion;

	private Integer testQuestionOrder;
	private Integer testQuestionValue;
	private Double testQuestionMark;
	private Integer response;
	private String optionShuffle;

	public StudentTestQuestion() {
	}

	public StudentTestQuestion(Integer studentTestQuestionId) {
		setIdInternal(studentTestQuestionId);
	}

	public IDistributedTest getDistributedTest() {
		return distributedTest;
	}

	public Integer getKeyDistributedTest() {
		return keyDistributedTest;
	}

	public Integer getKeyQuestion() {
		return keyQuestion;
	}

	public Integer getKeyStudent() {
		return keyStudent;
	}

	public String getOptionShuffle() {
		return optionShuffle;
	}

	public IQuestion getQuestion() {
		return question;
	}

	public Integer getResponse() {
		return response;
	}

	public IStudent getStudent() {
		return student;
	}

	public Integer getTestQuestionOrder() {
		return testQuestionOrder;
	}

	public Integer getTestQuestionValue() {
		return testQuestionValue;
	}

	public Double getTestQuestionMark() {
		return testQuestionMark;
	}

	public void setDistributedTest(IDistributedTest test) {
		distributedTest = test;
	}

	public void setKeyDistributedTest(Integer integer) {
		keyDistributedTest = integer;
	}

	public void setKeyQuestion(Integer integer) {
		keyQuestion = integer;
	}

	public void setKeyStudent(Integer integer) {
		keyStudent = integer;
	}

	public void setOptionShuffle(String string) {
		optionShuffle = string;
	}

	public void setQuestion(IQuestion question) {
		this.question = question;
	}

	public void setResponse(Integer response) {
		this.response = response;
	}

	public void setStudent(IStudent student) {
		this.student = student;
	}

	public void setTestQuestionOrder(Integer integer) {
		testQuestionOrder = integer;
	}

	public void setTestQuestionValue(Integer integer) {
		testQuestionValue = integer;
	}

	public void setTestQuestionMark(Double double1) {
		testQuestionMark = double1;
	}

}
