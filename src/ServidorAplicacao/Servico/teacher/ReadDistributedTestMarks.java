/*
 * Created on Oct 14, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteStudentsTestMarks;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadDistributedTestMarks implements IServico
{

	private static ReadDistributedTestMarks service = new ReadDistributedTestMarks();

	public static ReadDistributedTestMarks getService()
	{
		return service;
	}

	public String getNome()
	{
		return "ReadDistributedTestMarks";
	}

	public SiteView run(Integer executionCourseId, Integer distributedTestId)
		throws FenixServiceException
	{

		ISuportePersistente persistentSuport;
		InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = new InfoSiteStudentsTestMarks();
		try
		{
			persistentSuport = SuportePersistenteOJB.getInstance();
			IDistributedTest distributedTest = new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
					distributedTest,
					false);
			if (distributedTest == null)
			{
				throw new InvalidArgumentsServiceException();
			}

			IPersistentStudentTestQuestion persistentStudentTestQuestion =
				persistentSuport.getIPersistentStudentTestQuestion();
			List studentTestQuestionList =
				persistentStudentTestQuestion.readByDistributedTest(distributedTest);
			//Iterator it = studentTestQuestionList.iterator();
			List infoStudentTestQuestionList = null;
			//			Calendar start = Calendar.getInstance();
			infoStudentTestQuestionList =
				(List) CollectionUtils.collect(studentTestQuestionList, new Transformer()
			{
				public Object transform(Object arg0)
				{
					IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) arg0;
					return Cloner.copyIStudentTestQuestion2InfoStudentTestQuestionMark(
						studentTestQuestion);
				}

			});
			//			System.out.println(
			//				"ReadDistributedTestMarks while took ["
			//					+ (Calendar.getInstance().getTimeInMillis() - start.getTimeInMillis())
			//					+ "] ms");
			infoSiteStudentsTestMarks.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
			infoSiteStudentsTestMarks.setExecutionCourse(
				(InfoExecutionCourse) Cloner.get(distributedTest.getTestScope().getDomainObject()));
			infoSiteStudentsTestMarks.setExecutionCourse(
				(InfoExecutionCourse) Cloner.get(distributedTest.getTestScope().getDomainObject()));
			infoSiteStudentsTestMarks.setInfoDistributedTest(
				Cloner.copyIDistributedTest2InfoDistributedTest(distributedTest));

		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		SiteView siteView =
			new ExecutionCourseSiteView(infoSiteStudentsTestMarks, infoSiteStudentsTestMarks);
		return siteView;
	}

}