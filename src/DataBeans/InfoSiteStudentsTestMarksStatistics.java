/*
 * Created on Oct 24, 2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Susana Fernandes
 *
 */
public class InfoSiteStudentsTestMarksStatistics extends DataTranferObject implements ISiteComponent
{
	private List correctAnswersPercentage;
	private List wrongAnswersPercentage;
	private List notAnsweredPercentage;
	private InfoDistributedTest infoDistributedTest;
	private InfoExecutionCourse executionCourse;

	public InfoSiteStudentsTestMarksStatistics()
	{
	}

	public List getCorrectAnswersPercentage()
	{
		return correctAnswersPercentage;
	}

	public InfoDistributedTest getInfoDistributedTest()
	{
		return infoDistributedTest;
	}

	public List getNotAnsweredPercentage()
	{
		return notAnsweredPercentage;
	}

	public List getWrongAnswersPercentage()
	{
		return wrongAnswersPercentage;
	}

	public void setCorrectAnswersPercentage(List list)
	{
		correctAnswersPercentage = list;
	}

	public void setInfoDistributedTest(InfoDistributedTest infoDistributedTest)
	{
		this.infoDistributedTest = infoDistributedTest;
	}

	public void setNotAnsweredPercentage(List list)
	{
		notAnsweredPercentage = list;
	}

	public void setWrongAnswersPercentage(List list)
	{
		wrongAnswersPercentage = list;
	}

	public InfoExecutionCourse getExecutionCourse()
	{
		return executionCourse;
	}

	public void setExecutionCourse(InfoExecutionCourse course)
	{
		executionCourse = course;
	}

}