/*
 * Created on 19/Ago/2003
 *
 */

package Dominio;

import java.util.Calendar;
import java.util.List;
import Util.CorrectionAvailability;
import Util.TestType;

/**
 * @author Susana Fernandes
 */
public class DistributedTest extends DomainObject implements IDistributedTest
{
	private String title;
	private String testInformation;
	private Calendar beginDate;
	private Calendar endDate;
	private Calendar beginHour;
	private Calendar endHour;
	private TestType testType;
	private CorrectionAvailability correctionAvailability;
	private Boolean studentFeedback;
	private Integer numberOfQuestions;
	private ITestScope testScope;
	private Integer keyTestScope;
	private List studentLogs;
	private List studentQuestions;

	public DistributedTest()
	{
	}

	public DistributedTest(Integer distributedTestId)
	{
		setIdInternal(distributedTestId);
	}

	public Calendar getBeginDate()
	{
		return beginDate;
	}

	public Calendar getBeginHour()
	{
		return beginHour;
	}

	public CorrectionAvailability getCorrectionAvailability()
	{
		return correctionAvailability;
	}

	public Calendar getEndDate()
	{
		return endDate;
	}

	public Calendar getEndHour()
	{
		return endHour;
	}

	public Boolean getStudentFeedback()
	{
		return studentFeedback;
	}

	public TestType getTestType()
	{
		return testType;
	}

	public void setBeginDate(Calendar calendar)
	{
		beginDate = calendar;
	}

	public void setBeginHour(Calendar calendar)
	{
		beginHour = calendar;
	}

	public void setCorrectionAvailability(CorrectionAvailability availability)
	{
		correctionAvailability = availability;
	}

	public void setEndDate(Calendar calendar)
	{
		endDate = calendar;
	}

	public void setEndHour(Calendar calendar)
	{
		endHour = calendar;
	}

	public void setStudentFeedback(Boolean studentFeedback)
	{
		this.studentFeedback = studentFeedback;
	}

	public void setTestType(TestType type)
	{
		testType = type;
	}

	public String getTestInformation()
	{
		return testInformation;
	}

	public void setTestInformation(String string)
	{
		testInformation = string;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String string)
	{
		title = string;
	}

	public Integer getNumberOfQuestions()
	{
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(Integer integer)
	{
		numberOfQuestions = integer;
	}

	public ITestScope getTestScope()
	{
		return testScope;
	}

	public Integer getKeyTestScope()
	{
		return keyTestScope;
	}

	public void setTestScope(ITestScope scope)
	{
		testScope = scope;
	}

	public void setKeyTestScope(Integer integer)
	{
		keyTestScope = integer;
	}

	public List getStudentLogs()
	{
		return studentLogs;
	}

	public void setStudentLogs(List studentLogs)
	{
		this.studentLogs = studentLogs;
	}

	public List getStudentQuestions()
	{
		return studentQuestions;
	}

	public void setStudentQuestions(List studentQuestions)
	{
		this.studentQuestions = studentQuestions;
	}
}
