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
public class InfoSiteStudentsTestMarks extends DataTranferObject implements ISiteComponent
{
	private List infoStudentTestQuestionList;
	private InfoDistributedTest infoDistributedTest;
	private InfoExecutionCourse executionCourse;

	public InfoSiteStudentsTestMarks()
	{
	}

	public InfoExecutionCourse getExecutionCourse()
	{
		return executionCourse;
	}

	public InfoDistributedTest getInfoDistributedTest()
	{
		return infoDistributedTest;
	}

	public List getInfoStudentTestQuestionList()
	{
		return infoStudentTestQuestionList;
	}

	public void setExecutionCourse(InfoExecutionCourse course)
	{
		executionCourse = course;
	}

	public void setInfoDistributedTest(InfoDistributedTest test)
	{
		infoDistributedTest = test;
	}

	public void setInfoStudentTestQuestionList(List list)
	{
		infoStudentTestQuestionList = list;
	}

}