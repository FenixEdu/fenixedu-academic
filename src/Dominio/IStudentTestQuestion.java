/*
 * Created on 27/Ago/2003
 *
 */
package Dominio;

/**
 * @author Susana Fernandes
 */
public interface IStudentTestQuestion extends IDomainObject {
	public abstract IDistributedTest getDistributedTest();
	public abstract Integer getKeyDistributedTest();
	public abstract Integer getKeyQuestion();
	public abstract Integer getKeyStudent();
	public abstract String getOptionShuffle();
	public abstract IQuestion getQuestion();
	public abstract Integer getResponse();
	public abstract IStudent getStudent();
	public abstract Integer getTestQuestionOrder();
	public abstract Integer getTestQuestionValue();
	public abstract Double getTestQuestionMark();
	public abstract void setDistributedTest(IDistributedTest test);
	public abstract void setKeyDistributedTest(Integer integer);
	public abstract void setKeyQuestion(Integer integer);
	public abstract void setKeyStudent(Integer integer);
	public abstract void setOptionShuffle(String string);
	public abstract void setQuestion(IQuestion question);
	public abstract void setResponse(Integer response);
	public abstract void setStudent(IStudent student);
	public abstract void setTestQuestionOrder(Integer integer);
	public abstract void setTestQuestionValue(Integer integer);
	public abstract void setTestQuestionMark(Double double1);
}