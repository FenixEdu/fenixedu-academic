/*
 * Created on 28/Ago/2003
 *
 */
package DataBeans;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestionMark extends InfoObject
{
	private String studentName;
	private Integer studentNumber;
	private Integer studentIdInternal;
	private Double testQuestionMark;

	public InfoStudentTestQuestionMark()
	{
	}

	public Integer getStudentIdInternal()
	{
		return studentIdInternal;
	}

	public String getStudentName()
	{
		return studentName;
	}

	public Integer getStudentNumber()
	{
		return studentNumber;
	}

	public Double getTestQuestionMark()
	{
		return testQuestionMark;
	}

	public void setStudentIdInternal(Integer integer)
	{
		studentIdInternal = integer;
	}

	public void setStudentName(String string)
	{
		studentName = string;
	}

	public void setStudentNumber(Integer integer)
	{
		studentNumber = integer;
	}

	public void setTestQuestionMark(Double double1)
	{
		testQuestionMark = double1;
	}

	public boolean equals(Object o)
	{
		InfoStudentTestQuestionMark infoStudentTestQuestionMark = (InfoStudentTestQuestionMark) o;
		return (getTestQuestionMark().equals(infoStudentTestQuestionMark.getTestQuestionMark()))
			&& (getStudentIdInternal().equals(infoStudentTestQuestionMark.getStudentIdInternal()))
			&& (getStudentName().equals(infoStudentTestQuestionMark.getStudentName()))
			&& (getStudentNumber().equals(infoStudentTestQuestionMark.getStudentNumber()));
	}
}
