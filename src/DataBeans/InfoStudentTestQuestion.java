/*
 * Created on 28/Ago/2003
 *
 */
package DataBeans;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestion extends InfoObject implements Comparable {
	private InfoStudent student;
	private InfoDistributedTest distributedTest;
	private InfoQuestion question;

	private Integer testQuestionOrder;
	private Integer testQuestionValue;
	private Integer response;
	private String optionShuffle;
	private Double testQuestionMark;

	public InfoStudentTestQuestion() {
	}

	public InfoDistributedTest getDistributedTest() {
		return distributedTest;
	}

	public String getOptionShuffle() {
		return optionShuffle;
	}

	public InfoQuestion getQuestion() {
		return question;
	}

	public Integer getResponse() {
		return response;
	}

	public InfoStudent getStudent() {
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

	public void setDistributedTest(InfoDistributedTest test) {
		distributedTest = test;
	}

	public void setOptionShuffle(String string) {
		optionShuffle = string;
	}

	public void setQuestion(InfoQuestion question) {
		this.question = question;
	}

	public void setResponse(Integer response) {
		this.response = response;
	}

	public void setStudent(InfoStudent student) {
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

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoStudentTestQuestion) {
			InfoStudentTestQuestion infoStudentTestQuestion =
				(InfoStudentTestQuestion) obj;
			result =
				getIdInternal().equals(infoStudentTestQuestion.getIdInternal());
			result =
				result
					|| (getStudent().equals(infoStudentTestQuestion.getStudent()))
					&& (getDistributedTest()
						.equals(infoStudentTestQuestion.getDistributedTest()))
					&& (getQuestion()
						.equals(infoStudentTestQuestion.getQuestion()))
					&& (getTestQuestionOrder()
						.equals(infoStudentTestQuestion.getTestQuestionOrder()))
					&& (getTestQuestionValue()
						.equals(infoStudentTestQuestion.getTestQuestionValue()))
					&& (getResponse()
						.equals(infoStudentTestQuestion.getResponse()))
					&& (getOptionShuffle()
						.equals(infoStudentTestQuestion.getOptionShuffle()))
					&& (getTestQuestionMark()
						.equals(infoStudentTestQuestion.getTestQuestionMark()));
		}
		return result;
	}

	public int compareTo(Object o) {
		InfoStudentTestQuestion infoStudentTestQuestion =
			(InfoStudentTestQuestion) o;
		return getTestQuestionOrder().compareTo(
			infoStudentTestQuestion.getTestQuestionOrder());
	}

}
