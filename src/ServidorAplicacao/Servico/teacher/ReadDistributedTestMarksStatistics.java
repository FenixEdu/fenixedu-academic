/*
 * Created on 12/Fev/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteStudentsTestMarksStatistics;
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
 *
 * @author Susana Fernandes
 *
 */
public class ReadDistributedTestMarksStatistics implements IServico
{

	private static ReadDistributedTestMarksStatistics service = new ReadDistributedTestMarksStatistics();

	public static ReadDistributedTestMarksStatistics getService()
	{
		return service;
	}

	public String getNome()
	{
		return "ReadDistributedTestMarksStatistics";
	}

	public SiteView run(Integer executionCourseId, Integer distributedTestId)
		throws FenixServiceException
	{

		ISuportePersistente persistentSuport;
		InfoSiteStudentsTestMarksStatistics infoSiteStudentsTestMarksStatistics =
			new InfoSiteStudentsTestMarksStatistics();
		try
		{
			persistentSuport = SuportePersistenteOJB.getInstance();
			IDistributedTest distributedTest = new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
					distributedTest,
					false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();

			IPersistentStudentTestQuestion persistentStudentTestQuestion =
				persistentSuport.getIPersistentStudentTestQuestion();
			List studentTestQuestionList =
				persistentStudentTestQuestion.readStudentTestQuestionsByDistributedTest(distributedTest);
			Iterator it = studentTestQuestionList.iterator();
			//List infoStudentTestQuestionList = new ArrayList();
			List correctAnswersPercentageList = new ArrayList(),
				wrongAnswersPercentageList = new ArrayList(),
				notAnsweredPercentageList = new ArrayList();

			DecimalFormat df = new DecimalFormat("#%");
			int numOfStudent = persistentStudentTestQuestion.countNumberOfStudents(distributedTest);
			while (it.hasNext())
			{
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
				correctAnswersPercentageList.add(
					df.format(
						persistentStudentTestQuestion.countCorrectOrIncorrectAnswers(
							studentTestQuestion.getTestQuestionOrder(),
							studentTestQuestion.getTestQuestionValue(),
							true,
							distributedTest)
							* java.lang.Math.pow(numOfStudent, -1)));
				wrongAnswersPercentageList.add(
					df.format(
						persistentStudentTestQuestion.countCorrectOrIncorrectAnswers(
							studentTestQuestion.getTestQuestionOrder(),
							studentTestQuestion.getTestQuestionValue(),
							false,
							distributedTest)
							* java.lang.Math.pow(numOfStudent, -1)));
				notAnsweredPercentageList.add(
					df.format(
						persistentStudentTestQuestion.countResponsedOrNotResponsed(
							studentTestQuestion.getTestQuestionOrder(),
							false,
							distributedTest)
							* java.lang.Math.pow(numOfStudent, -1)));
			}
			infoSiteStudentsTestMarksStatistics.setCorrectAnswersPercentage(
				correctAnswersPercentageList);
			infoSiteStudentsTestMarksStatistics.setWrongAnswersPercentage(wrongAnswersPercentageList);
			infoSiteStudentsTestMarksStatistics.setNotAnsweredPercentage(notAnsweredPercentageList);
			infoSiteStudentsTestMarksStatistics.setInfoDistributedTest(
				Cloner.copyIDistributedTest2InfoDistributedTest(distributedTest));
			infoSiteStudentsTestMarksStatistics.setExecutionCourse(
				(InfoExecutionCourse) infoSiteStudentsTestMarksStatistics
					.getInfoDistributedTest()
					.getInfoTestScope()
					.getInfoObject());
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		SiteView siteView =
			new ExecutionCourseSiteView(
				infoSiteStudentsTestMarksStatistics,
				infoSiteStudentsTestMarksStatistics);
		return siteView;
	}
}
