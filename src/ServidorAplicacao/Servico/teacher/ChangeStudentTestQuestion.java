/*
 * Created on Oct 20, 2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.struts.util.LabelValueBean;

import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import Dominio.Advisory;
import Dominio.DistributedTest;
import Dominio.DistributedTestAdvisory;
import Dominio.ExecutionCourse;
import Dominio.IAdvisory;
import Dominio.IDistributedTest;
import Dominio.IDistributedTestAdvisory;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IMetadata;
import Dominio.IOnlineTest;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Metadata;
import Dominio.Question;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TestQuestionChangesType;
import Util.TestQuestionStudentsChangesType;
import Util.TestType;

/**
 * @author Susana Fernandes
 *  
 */
public class ChangeStudentTestQuestion implements IServico
{
	private static ChangeStudentTestQuestion service = new ChangeStudentTestQuestion();
	private String contextPath = new String();

	public static ChangeStudentTestQuestion getService()
	{
		return service;
	}

	public ChangeStudentTestQuestion()
	{
	}

	public String getNome()
	{
		return "ChangeStudentTestQuestion";
	}

	public List run(Integer executionCourseId, Integer distributedTestId, Integer oldQuestionId,
			Integer newMetadataId, Integer studentId, TestQuestionChangesType changesType,
			Boolean delete, TestQuestionStudentsChangesType studentsType, String contextPath)
			throws FenixServiceException
	{
		this.contextPath = contextPath.replace('\\', '/');
		List result = new ArrayList();
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

			IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
			executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse()
					.readByOId(executionCourse, false);
			if (executionCourse == null)
				throw new InvalidArgumentsServiceException();

			IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
			IQuestion oldQuestion = new Question(oldQuestionId);
			oldQuestion = (IQuestion) persistentQuestion.readByOId(oldQuestion, true);
			if (oldQuestion == null)
				throw new InvalidArgumentsServiceException();

			IDistributedTest distributedTest = new DistributedTest(distributedTestId);
			distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest()
					.readByOId(distributedTest, false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();

			List studentsTestQuestionList = new ArrayList();
			IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
					.getIPersistentStudentTestQuestion();
			if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS)
				studentsTestQuestionList = persistentStudentTestQuestion.readByQuestion(oldQuestion);
			else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST)
				studentsTestQuestionList = persistentStudentTestQuestion
						.readByQuestionAndDistributedTest(oldQuestion, distributedTest);

			else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT)
			{
				IStudent student = new Student(studentId);
				student = (IStudent) persistentSuport.getIPersistentStudent().readByOId(student, false);
				if (student == null)
					throw new InvalidArgumentsServiceException();
				studentsTestQuestionList.add(persistentStudentTestQuestion
						.readByQuestionAndStudentAndDistributedTest(oldQuestion, student,
								distributedTest));
			}

			Iterator studentsTestQuestionIt = studentsTestQuestionList.iterator();
			IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
			IMetadata metadata = null;
			if (newMetadataId != null)
			{
				metadata = new Metadata(newMetadataId);
				metadata = (IMetadata) persistentMetadata.readByOId(metadata, false);
				if (metadata == null)
					throw new InvalidArgumentsServiceException();
			}

			boolean canDelete = true;
			List group = new ArrayList();
			while (studentsTestQuestionIt.hasNext())
			{
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) studentsTestQuestionIt
						.next();

				if (!compareDates(studentTestQuestion.getDistributedTest().getEndDate(),
						studentTestQuestion.getDistributedTest().getEndHour()))
				{

					result.add(new LabelValueBean(studentTestQuestion.getDistributedTest().getTitle()
							.concat(" (Ficha Fechada)"), studentTestQuestion.getStudent().getNumber()
							.toString()));
					canDelete = false;
				}
				else
				{
					result.add(new LabelValueBean(studentTestQuestion.getDistributedTest().getTitle(),
							studentTestQuestion.getStudent().getNumber().toString()));
					IQuestion newQuestion = new Question();
					if (newMetadataId == null)
					{
						newQuestion = getNewQuestion(persistentQuestion, oldQuestion.getMetadata(),
								oldQuestion);
						if (newQuestion == null || newQuestion.equals(oldQuestion))
							return null;
					}
					else
					{
						newQuestion = getNewQuestion(persistentQuestion, metadata, null);
						if (newQuestion == null)
							throw new InvalidArgumentsServiceException();
					}
					System.out.println("Troquei exercicio. AlunoNum: "
							+ studentTestQuestion.getStudent().getNumber() + " ExercicioOld:"
							+ oldQuestion.getIdInternal() + " ExercicioNew: "
							+ newQuestion.getIdInternal() + " RespostaOld:"
							+ studentTestQuestion.getResponse() + " Shuffle: "
							+ studentTestQuestion.getOptionShuffle() + " OldMark: "
							+ studentTestQuestion.getTestQuestionMark());
					studentTestQuestion.setQuestion(newQuestion);
					studentTestQuestion.setResponse(new Integer(0));
					double oldMark = studentTestQuestion.getTestQuestionMark().doubleValue();
					studentTestQuestion.setTestQuestionMark(new Double(0));
					persistentStudentTestQuestion.simpleLockWrite(studentTestQuestion);
					if (!group.contains(studentTestQuestion.getStudent().getPerson()))
						group.add(studentTestQuestion.getStudent().getPerson());
					if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION)))
					{
						IOnlineTest onlineTest = (IOnlineTest) persistentSuport
								.getIPersistentOnlineTest().readByDistributedTest(distributedTest);
						IFrequenta attend = persistentSuport.getIFrequentaPersistente()
								.readByAlunoAndDisciplinaExecucao(studentTestQuestion.getStudent(),
										executionCourse);
						IMark mark = persistentSuport.getIPersistentMark().readBy(onlineTest, attend);
						System.out.println("vou mudar a nota final");
						if (mark != null)
						{
							persistentSuport.getIPersistentMark().simpleLockWrite(mark);
							mark.setMark(getNewStudentMark(persistentSuport, distributedTest,
									studentTestQuestion.getStudent(), oldMark));
						}
					}
				}
			}
			// Create Advisory
			IAdvisory advisory = createTestAdvisory(distributedTest);
			persistentSuport.getIPersistentAdvisory().write(advisory, group);
			// Create DistributedTestAdvisory
			IDistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
			persistentSuport.getIPersistentDistributedTestAdvisory().simpleLockWrite(
					distributedTestAdvisory);
			distributedTestAdvisory.setAdvisory(advisory);
			distributedTestAdvisory.setDistributedTest(distributedTest);

			if (delete.booleanValue())
			{
				metadata = new Metadata(oldQuestion.getKeyMetadata());
				metadata = (IMetadata) persistentMetadata.readByOId(metadata, true);
				if (metadata == null)
					throw new InvalidArgumentsServiceException();
				metadata.setNumberOfMembers(new Integer(metadata.getNumberOfMembers().intValue() - 1));
				removeOldTestQuestion(persistentSuport, oldQuestion);
				List metadataQuestions = metadata.getVisibleQuestions();

				if (metadataQuestions != null && metadataQuestions.size() <= 1)
					metadata.setVisibility(new Boolean(false));

				if (canDelete)
				{
					int metadataNumberOfQuestions = persistentMetadata.getNumberOfQuestions(metadata);
					persistentQuestion.delete(oldQuestion);
					if (metadataNumberOfQuestions <= 1)
						persistentMetadata.delete(metadata);
				}
				else
				{
					oldQuestion.setVisibility(new Boolean(false));
				}

			}
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		return result;
	}

	private IQuestion getNewQuestion(IPersistentQuestion persistentQuestion, IMetadata metadata,
			IQuestion oldQuestion) throws ExcepcaoPersistencia
	{

		List questions = metadata.getVisibleQuestions();
		IQuestion question = null;

		if (questions.size() != 0)
		{
			if (questions.size() == 1)
				return (IQuestion) questions.get(0);
			do
			{
				Random r = new Random();
				int questionIndex = r.nextInt(questions.size());
				question = (IQuestion) questions.get(questionIndex);
			}
			while (question.equals(oldQuestion));
		}
		return question;
	}

	private boolean compareDates(Calendar date, Calendar hour)
	{
		Calendar calendar = Calendar.getInstance();
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		if (dateComparator.compare(calendar, date) <= 0)
		{
			if (dateComparator.compare(calendar, date) == 0)
				if (hourComparator.compare(calendar, hour) <= 0)
					return true;
				else
					return false;
			return true;
		}
		return false;
	}

	private void removeOldTestQuestion(ISuportePersistente persistentSuport, IQuestion oldQuestion)
			throws ExcepcaoPersistencia
	{

		IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();
		Iterator it = persistentTestQuestion.readByQuestion(oldQuestion).iterator();
		IQuestion newQuestion = getNewQuestion(persistentSuport.getIPersistentQuestion(), oldQuestion
				.getMetadata(), oldQuestion);

		if (newQuestion == null || newQuestion.equals(oldQuestion))
		{
			while (it.hasNext())
			{
				ITestQuestion oldTestQuestion = (ITestQuestion) it.next();
				persistentTestQuestion.simpleLockWrite(oldTestQuestion);
				ITest test = oldTestQuestion.getTest();

				List testQuestionList = persistentTestQuestion.readByTest(test);
				Iterator testQuestionListIt = testQuestionList.iterator();
				while (testQuestionListIt.hasNext())
				{
					ITestQuestion testQuestion = (ITestQuestion) testQuestionListIt.next();
					if (testQuestion.getTestQuestionOrder().compareTo(
							oldTestQuestion.getTestQuestionOrder()) > 0)
					{
						persistentTestQuestion.simpleLockWrite(testQuestion);
						testQuestion.setTestQuestionOrder(new Integer(testQuestion
								.getTestQuestionOrder().intValue() - 1));
					}
				}
				persistentSuport.getIPersistentTest().simpleLockWrite(test);
				test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions().intValue() - 1));
				persistentTestQuestion.delete(oldTestQuestion);
			}
		}
		else
		{
			while (it.hasNext())
			{
				ITestQuestion oldTestQuestion = (ITestQuestion) it.next();
				persistentTestQuestion.simpleLockWrite(oldTestQuestion);
				ITest test = oldTestQuestion.getTest();

				oldTestQuestion.setKeyQuestion(newQuestion.getIdInternal());
				oldTestQuestion.setQuestion(newQuestion);
			}
		}
	}

	private String getNewStudentMark(ISuportePersistente sp, IDistributedTest dt, IStudent s,
			double mark2Remove) throws ExcepcaoPersistencia
	{
		double totalMark = 0;
		List studentTestQuestionList = (List) sp.getIPersistentStudentTestQuestion()
				.readByStudentAndDistributedTest(s, dt);

		Iterator it = studentTestQuestionList.iterator();
		while (it.hasNext())
		{
			IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
			totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
		}
		return (new java.text.DecimalFormat("#0.##").format(totalMark));
	}

	private IAdvisory createTestAdvisory(IDistributedTest distributedTest)
	{
		IAdvisory advisory = new Advisory();
		advisory.setCreated(null);
		advisory.setExpires(distributedTest.getEndDate().getTime());
		advisory.setSender("Docente da disciplina "
				+ ((IExecutionCourse) distributedTest.getTestScope().getDomainObject()).getNome());

		advisory.setSubject(distributedTest.getTitle() + ": Alteração na ficha");
		String msgBeginning;
		if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
			msgBeginning = new String("Uma pergunta do seu <a href='" + this.contextPath
					+ "/student/studentTests.do?method=prepareToDoTest&testCode="
					+ distributedTest.getIdInternal()
					+ "'>Questionário</a> foi alterada. Deverá realizar o Questionário");
		else
			msgBeginning = new String("Uma pergunta da sua <a href='" + this.contextPath
					+ "/student/studentTests.do?method=prepareToDoTest&testCode="
					+ distributedTest.getIdInternal()
					+ "'>Ficha de Trabalho</a> foi alterada. Deverá realizar a Ficha de Trabalho");

		advisory.setMessage(msgBeginning + " até às " + getHourFormatted(distributedTest.getEndHour())
				+ " de " + getDateFormatted(distributedTest.getEndDate()));
		advisory.setOnlyShowOnce(new Boolean(false));
		return advisory;
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