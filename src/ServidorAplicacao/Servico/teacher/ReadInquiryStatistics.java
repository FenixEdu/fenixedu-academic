/*
 * Created on 9/Fev/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoInquiryStatistics;
import DataBeans.InfoSiteInquiryStatistics;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TestType;
import UtilTests.ParseQuestion;

/**
 *
 * @author Susana Fernandes
 *
 */
public class ReadInquiryStatistics implements IServico
{

	private static ReadInquiryStatistics service = new ReadInquiryStatistics();
	private String path = new String();
	public static ReadInquiryStatistics getService()
	{
		return service;
	}

	public String getNome()
	{
		return "ReadInquiryStatistics";
	}

	public SiteView run(Integer executionCourseId, Integer distributedTestId, String path)
		throws FenixServiceException
	{
		this.path = path.replace('\\', '/');
		InfoSiteInquiryStatistics infoSiteInquiryStatistics = new InfoSiteInquiryStatistics();
		List infoInquiryStatisticsList = new ArrayList();

		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);

			IDistributedTest distributedTest = new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
					distributedTest,
					false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();
			infoSiteInquiryStatistics.setExecutionCourse(
				(InfoExecutionCourse) Cloner.get(distributedTest.getTestScope().getDomainObject()));
			IPersistentStudentTestQuestion persistentStudentTestQuestion =
				persistentSuport.getIPersistentStudentTestQuestion();

			List testQuestionList =
				persistentStudentTestQuestion.readStudentTestQuestionsByDistributedTest(distributedTest);
			Iterator it = testQuestionList.iterator();

			while (it.hasNext())
			{
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
				InfoInquiryStatistics infoInquiryStatistics = new InfoInquiryStatistics();

				InfoStudentTestQuestion infoStudentTestQuestion;
				ParseQuestion parse = new ParseQuestion();
				try
				{
					if (studentTestQuestion.getOptionShuffle() == null)
					{
						persistentSuport.getIPersistentStudentTestQuestion().simpleLockWrite(
							studentTestQuestion);
						boolean shuffle = true;
						if (distributedTest.getTestType().equals(new TestType(3))) //INQUIRY
							shuffle = false;
						studentTestQuestion.setOptionShuffle(
							parse.shuffleQuestionOptions(
								studentTestQuestion.getQuestion().getXmlFile(),
								shuffle,
								this.path));
					}

					infoStudentTestQuestion =
						Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);

					infoStudentTestQuestion.setQuestion(
						parse.parseQuestion(
							infoStudentTestQuestion.getQuestion().getXmlFile(),
							infoStudentTestQuestion.getQuestion(),
							this.path));
					infoStudentTestQuestion =
						parse.getOptionsShuffle(infoStudentTestQuestion, this.path);
				}
				catch (Exception e)
				{
					throw new FenixServiceException(e);
				}
				List statistics = new ArrayList();
				int numOfStudentResponses =
					persistentStudentTestQuestion.countResponsedOrNotResponsed(
						studentTestQuestion.getTestQuestionOrder(),
						true,
						distributedTest);
				for (int i = 1;
					i <= infoStudentTestQuestion.getQuestion().getOptionNumber().intValue();
					i++)
				{
					DecimalFormat df = new DecimalFormat("#%");
					int mark =
						persistentStudentTestQuestion.countByQuestionOrderAndOptionAndDistributedTest(
							studentTestQuestion.getTestQuestionOrder(),
							new Integer(i),
							distributedTest);
					String percentage = new String("0%");
					if (mark != 0)
						percentage = df.format(mark * java.lang.Math.pow(numOfStudentResponses, -1));
					statistics.add(new LabelValueBean(new Integer(i).toString(), percentage));
				}
				statistics.add(
					new LabelValueBean(
						new String("Número de alunos que responderam"),
						new Integer(numOfStudentResponses).toString()));
				infoInquiryStatistics.setInfoStudentTestQuestion(infoStudentTestQuestion);
				infoInquiryStatistics.setOptionStatistics(statistics);
				infoInquiryStatisticsList.add(infoInquiryStatistics);
			}

		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		infoSiteInquiryStatistics.setInfoInquiryStatistics(infoInquiryStatisticsList);
		SiteView siteView =
			new ExecutionCourseSiteView(infoSiteInquiryStatistics, infoSiteInquiryStatistics);
		return siteView;
	}

}
