/*
 * Created on 19/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Dominio.Advisory;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import Dominio.IAdvisory;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.ITurno;
import Dominio.Student;
import Dominio.StudentTestQuestion;
import Dominio.Test;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CorrectionAvailability;
import Util.TestType;

/**
 * @author Susana Fernandes
 */
public class InsertDistributedTest implements IServico
{
	private static InsertDistributedTest service = new InsertDistributedTest();
	private String path = new String();

	public static InsertDistributedTest getService()
	{
		return service;
	}

	public InsertDistributedTest()
	{
	}

	public String getNome()
	{
		return "InsertDistributedTest";
	}

	public boolean run(
		Integer executionCourseId,
		Integer testId,
		String testInformation,
		Calendar beginDate,
		Calendar beginHour,
		Calendar endDate,
		Calendar endHour,
		TestType testType,
		CorrectionAvailability correctionAvaiability,
		Boolean feedback,
		String[] selected,
		Boolean insertByShifts,
		String path)
		throws FenixServiceException
	{
		this.path = path.replace('\\', '/');
		try
		{

			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOId(
					executionCourse,
					false);
			if (executionCourse == null)
				throw new InvalidArgumentsServiceException();

			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();
			IDistributedTest distributedTest = new DistributedTest();

			IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, false);
			if (test == null)
				throw new InvalidArgumentsServiceException();

			distributedTest.setTitle(test.getTitle());
			distributedTest.setTestInformation(testInformation);
			distributedTest.setBeginDate(beginDate);
			distributedTest.setBeginHour(beginHour);
			distributedTest.setEndDate(endDate);
			distributedTest.setEndHour(endHour);
			distributedTest.setTestType(testType);
			distributedTest.setCorrectionAvailability(correctionAvaiability);
			distributedTest.setStudentFeedback(feedback);
			distributedTest.setNumberOfQuestions(test.getNumberOfQuestions());
			distributedTest.setExecutionCourse(test.getExecutionCourse());
			distributedTest.setKeyExecutionCourse(test.getKeyExecutionCourse());
			persistentDistributedTest.simpleLockWrite(distributedTest);

			List studentList = null;
			if (insertByShifts.booleanValue())
				studentList = returnStudentsFromShiftsArray(persistentSuport, selected);
			else
				studentList =
					returnStudentsFromStudentsArray(persistentSuport, selected, executionCourseId);

			IPersistentStudentTestQuestion persistentStudentTestQuestion =
				persistentSuport.getIPersistentStudentTestQuestion();
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();

			List testQuestionList = persistentTestQuestion.readByTest(test);
			Iterator testQuestionIt = testQuestionList.iterator();
			while (testQuestionIt.hasNext())
			{
				ITestQuestion testQuestion = (ITestQuestion) testQuestionIt.next();
				Iterator studentIt = studentList.iterator();
				while (studentIt.hasNext())
				{
					IStudent student = (IStudent) studentIt.next();
					IStudentTestQuestion studentTestQuestion = new StudentTestQuestion();
					persistentStudentTestQuestion.lockWrite(studentTestQuestion);
					studentTestQuestion.setStudent(student);
					studentTestQuestion.setDistributedTest(distributedTest);
					studentTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
					studentTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
					studentTestQuestion.setTestQuestionMark(new Double(0));
					studentTestQuestion.setResponse(new Integer(0));

					IQuestion question =
						getStudentQuestion(
							persistentSuport.getIPersistentQuestion(),
							testQuestion.getQuestion().getMetadata());
					if (question == null)
					{
						throw new InvalidArgumentsServiceException();
					}
					studentTestQuestion.setQuestion(question);
				}
			}
			//Create Advisory
			Iterator studentIt = studentList.iterator();
			List group = new ArrayList();
			while (studentIt.hasNext())
			{
				IStudent student = (Student) studentIt.next();
				group.add(student.getPerson());
			}

			IAdvisory advisory = new Advisory();
			advisory.setCreated(null);
			advisory.setExpires(endDate.getTime());
			advisory.setSender("Docente da disciplina " + executionCourse.getNome());
			advisory.setSubject(distributedTest.getTitle());
			advisory.setMessage(
				"Tem uma Ficha de Trabalho a realizar entre "
					+ getDateFormatted(beginDate)
					+ " às "
					+ getHourFormatted(beginHour)
					+ " até "
					+ getDateFormatted(endDate)
					+ " às "
					+ getHourFormatted(endHour));

			advisory.setOnlyShowOnce(new Boolean(false));
			persistentSuport.getIPersistentAdvisory().write(advisory, group);

			return true;
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
	}

	private IQuestion getStudentQuestion(IPersistentQuestion persistentQuestion, IMetadata metadata)
		throws ExcepcaoPersistencia
	{
		List questions = metadata.getVisibleQuestions();
		IQuestion question = null;
		if (questions.size() != 0)
		{
			Random r = new Random();
			int questionIndex = r.nextInt(questions.size());
			question = (IQuestion) questions.get(questionIndex);
		}
		return question;
	}

	private List returnStudentsFromShiftsArray(ISuportePersistente persistentSuport, String[] shifts)
		throws FenixServiceException
	{
		List studentsList = new ArrayList();
		try
		{

			ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();
			for (int i = 0; i < shifts.length; i++)
			{
				if (shifts[i].equals("Todos os Turnos"))
				{
					continue;
				}
				else
				{
					ITurno shift = new Turno(new Integer(shifts[i]));
					shift = (ITurno) persistentShift.readByOId(shift, false);
					Iterator studentIt =
						persistentSuport.getITurnoAlunoPersistente().readByShift(shift).iterator();
					while (studentIt.hasNext())
					{
						IStudent student = (IStudent) studentIt.next();
						if (!studentsList.contains(student))
							studentsList.add(student);
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
		return studentsList;
	}

	private List returnStudentsFromStudentsArray(
		ISuportePersistente persistentSuport,
		String[] students,
		Integer executionCourseId)
		throws FenixServiceException
	{
		List studentsList = new ArrayList();
		try
		{

			for (int i = 0; i < students.length; i++)
			{
				if (students[i].equals("Todos os Alunos"))
				{
					IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
					executionCourse =
						(IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOId(
							executionCourse,
							false);
					List attendList =
						persistentSuport.getIFrequentaPersistente().readByExecutionCourse(
							executionCourse);

					Iterator iterStudent = attendList.listIterator();
					while (iterStudent.hasNext())
					{
						IFrequenta attend = (Frequenta) iterStudent.next();
						IStudent student = attend.getAluno();
						studentsList.add(student);
					}
					break;
				}
				else
				{
					IStudent student = new Student(new Integer(students[i]));
					student =
						(IStudent) persistentSuport.getIPersistentStudent().readByOId(student, false);

					if (!studentsList.contains(student))
						studentsList.add(student);
				}
			}
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
		return studentsList;
	}

	private String getDateFormatted(Calendar date)
	{
		String result = new String();
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		return result;
	}
	private String getHourFormatted(Calendar hour)
	{
		String result = new String();
		result += hour.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (hour.get(Calendar.MINUTE) < 10)
			result += "0";
		result += hour.get(Calendar.MINUTE);
		return result;
	}

}