/*
 * Created on 9/Fev/2004
 *
 */
package DataBeans;

import java.util.List;

/**
 *
 * @author Susana Fernandes
 *
 */
public class InfoInquiryStatistics extends DataTranferObject implements ISiteComponent
{
	private InfoStudentTestQuestion infoStudentTestQuestion;
	private List optionStatistics;

	public InfoInquiryStatistics()
	{
	}

	public List getOptionStatistics()
	{
		return optionStatistics;
	}

	public InfoStudentTestQuestion getInfoStudentTestQuestion()
	{
		return infoStudentTestQuestion;
	}

	public void setOptionStatistics(List list)
	{
		optionStatistics = list;
	}

	public void setInfoStudentTestQuestion(InfoStudentTestQuestion question)
	{
		infoStudentTestQuestion = question;
	}

}
