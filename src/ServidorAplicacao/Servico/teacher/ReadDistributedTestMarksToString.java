/*
 * Created on Nov 3, 2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 *
 */
public class ReadDistributedTestMarksToString implements IServico
{
	private static ReadDistributedTestMarksToString service = new ReadDistributedTestMarksToString();

	public static ReadDistributedTestMarksToString getService()
	{
		return service;
	}

	public String getNome()
	{
		return "ReadDistributedTestMarksToString";
	}

	public String run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException
	{

		ISuportePersistente persistentSuport;
		try
		{
			persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				persistentSuport.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
			if (executionCourse == null)
			{
				throw new InvalidArgumentsServiceException();
			}

			IDistributedTest distributedTest = new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
					distributedTest,
					false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();

			int numberOfQuestions = distributedTest.getNumberOfQuestions().intValue();
			String result = new String("Número\tNome\t");
			for (int i = 1; i <= distributedTest.getNumberOfQuestions().intValue(); i++)
			{
				result = result.concat("P" + i + "\t");
			}
			result = result.concat("Nota\n");
			List studentTestQuestionList =
				persistentSuport.getIPersistentStudentTestQuestion().readByDistributedTest(
					distributedTest);
			if (studentTestQuestionList == null || studentTestQuestionList.size() == 0)
				throw new FenixServiceException();
			Iterator it = studentTestQuestionList.iterator();
			int questionIndex = 0;
			Double finalMark = new Double(0);
			DecimalFormat df = new DecimalFormat("#0.##");
			while (it.hasNext())
			{
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
				if (questionIndex == 0)
				{
					result =
						result.concat(
							studentTestQuestion.getStudent().getNumber()
								+ "\t"
								+ studentTestQuestion.getStudent().getPerson().getNome()
								+ "\t");
				}
				result = result.concat(df.format(studentTestQuestion.getTestQuestionMark()) + "\t");
				finalMark =
					new Double(
						finalMark.doubleValue()
							+ studentTestQuestion.getTestQuestionMark().doubleValue());
				questionIndex++;
				if (questionIndex == distributedTest.getNumberOfQuestions().intValue())
				{
					if (finalMark.doubleValue() < 0)
						result = result.concat("0\n");
					else
						result = result.concat(df.format(finalMark.doubleValue()) + "\n");

					finalMark = new Double(0);
					questionIndex = 0;
				}

			}

			return result;

		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}