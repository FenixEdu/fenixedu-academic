/*
 * Created on 29/Jan/2004
 *
 */
package ServidorAplicacao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IOnlineTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Mark;
import Dominio.OnlineTest;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TestType;

/**
 *
 * @author Susana Fernandes
 *
 */
public class UpdateOnlineTests
{
	public static void main(String[] args) throws Exception
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		persistentSuport.iniciarTransaccao();
		Integer executionCourseId = new Integer(34882);
		System.out.println("executionCourseId: " + executionCourseId);
		IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
		executionCourse =
			(IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOId(
				executionCourse,
				false);
		if (executionCourse == null)
			throw new InvalidArgumentsServiceException();

		List distributedTestList = persistentSuport.getIPersistentDistributedTest().readAll();

		persistentSuport.confirmarTransaccao();

		Iterator distributedTestIt = distributedTestList.iterator();

		List associatedExecutionCoursesList = new ArrayList();
		associatedExecutionCoursesList.add(executionCourse);
		while (distributedTestIt.hasNext())
		{
			persistentSuport.iniciarTransaccao();
			IDistributedTest distributedTest = (IDistributedTest) distributedTestIt.next();
			if (distributedTest.getTestType().equals(new TestType(1))) //EVALUATION
			{
				System.out.println(
					"DistributedTestId "
						+ distributedTest.getIdInternal()
						+ " "
						+ distributedTest.getTestType().getTypeString());
				IOnlineTest onlineTest = new OnlineTest();
				onlineTest.setDistributedTest(distributedTest);
				onlineTest.setAssociatedExecutionCourses(associatedExecutionCoursesList);
				persistentSuport.getIPersistentEvaluation().simpleLockWrite(onlineTest);

				List studentList =
					persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(
						distributedTest);

				Iterator studentIt = studentList.iterator();
				while (studentIt.hasNext())
				{
					IStudent student = (Student) studentIt.next();

					IFrequenta attend =
						persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
							student,
							executionCourse);
					if (attend != null)
					{

						IMark mark = new Mark();
						mark.setAttend(attend);
						mark.setEvaluation(onlineTest);
						//por a nota!!!
						List studentTestQuestionList =
							persistentSuport
								.getIPersistentStudentTestQuestion()
								.readByStudentAndDistributedTest(
								student,
								distributedTest);

						Iterator studentTestQuestionIt = studentTestQuestionList.iterator();
						Double studentMark = new Double(0);
						while (studentTestQuestionIt.hasNext())
						{
							IStudentTestQuestion studentTestQuestion =
								(IStudentTestQuestion) studentTestQuestionIt.next();
							studentMark =
								new Double(
									studentMark.doubleValue()
										+ studentTestQuestion.getTestQuestionMark().doubleValue());
						}
						if (studentMark.doubleValue() <= 0)
						{
							mark.setMark("RE");
						}
						else
						{
							DecimalFormat df = new DecimalFormat("#0.##");
							mark.setMark(df.format(studentMark));
						}
						persistentSuport.getIPersistentMark().simpleLockWrite(mark);
					}
				}

			}
			else
			{ //INQUIRY

				persistentSuport.getIPersistentDistributedTest().simpleLockWrite(distributedTest);
				distributedTest.setTestType(new TestType(3));
				System.out.println(
					"DistributedTestId "
						+ distributedTest.getIdInternal()
						+ " "
						+ distributedTest.getTestType().getTypeString());
			}
			persistentSuport.confirmarTransaccao();
		}
	}
}
