/*
 * Created on 19/Ago/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Advisory;
import Dominio.DistributedTest;
import Dominio.DistributedTestAdvisory;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import Dominio.IAdvisory;
import Dominio.IDistributedTest;
import Dominio.IDistributedTestAdvisory;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IOnlineTest;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.ITestScope;
import Dominio.ITurno;
import Dominio.OnlineTest;
import Dominio.Student;
import Dominio.StudentTestQuestion;
import Dominio.Test;
import Dominio.TestScope;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.CorrectionAvailability;
import Util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class InsertDistributedTest implements IService {

	private String contextPath = new String();

	public InsertDistributedTest() {
	}

	public boolean run(Integer executionCourseId, Integer testId,
			String testInformation, Calendar beginDate, Calendar beginHour,
			Calendar endDate, Calendar endHour, TestType testType,
			CorrectionAvailability correctionAvaiability, Boolean imsFeedback,
			String[] selected, Boolean insertByShifts, String contextPath)
			throws FenixServiceException {
		this.contextPath = contextPath.replace('\\', '/');
		try {

			ISuportePersistente persistentSuport = SuportePersistenteOJB
					.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
					.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
					.readByOID(ExecutionCourse.class, executionCourseId);
			if (executionCourse == null)
				throw new InvalidArgumentsServiceException();

			IPersistentDistributedTest persistentDistributedTest = persistentSuport
					.getIPersistentDistributedTest();
			IDistributedTest distributedTest = new DistributedTest();

			IPersistentTest persistentTest = persistentSuport
					.getIPersistentTest();
			ITest test = (ITest) persistentTest.readByOID(Test.class, testId);
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
			distributedTest.setImsFeedback(imsFeedback);
			distributedTest.setNumberOfQuestions(test.getNumberOfQuestions());

			ITestScope testScope = persistentSuport
					.getIPersistentTestScope().readByDomainObject(
							executionCourse);

			if (testScope == null) {
				testScope = new TestScope(persistentExecutionCourse
						.materialize(executionCourse));
				persistentSuport.getIPersistentTestScope().simpleLockWrite(
						testScope);
			}
			distributedTest.setTestScope(testScope);
			persistentDistributedTest.simpleLockWrite(distributedTest);

			List studentList = null;
			if (insertByShifts.booleanValue())
				studentList = returnStudentsFromShiftsArray(persistentSuport,
						selected);
			else
				studentList = returnStudentsFromStudentsArray(persistentSuport,
						selected, executionCourse);

			IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
					.getIPersistentStudentTestQuestion();
			IPersistentTestQuestion persistentTestQuestion = persistentSuport
					.getIPersistentTestQuestion();

			List testQuestionList = persistentTestQuestion.readByTest(test);
			Iterator testQuestionIt = testQuestionList.iterator();

			while (testQuestionIt.hasNext()) {
				ITestQuestion testQuestion = (ITestQuestion) testQuestionIt
						.next();
				List questionList = new ArrayList();
				questionList.addAll(testQuestion.getQuestion().getMetadata()
						.getVisibleQuestions());
				Iterator studentIt = studentList.iterator();
				while (studentIt.hasNext()) {
					IStudent student = (IStudent) studentIt.next();
					IStudentTestQuestion studentTestQuestion = new StudentTestQuestion();
					persistentStudentTestQuestion
							.lockWrite(studentTestQuestion);
					studentTestQuestion.setStudent(student);
					studentTestQuestion.setDistributedTest(distributedTest);
					studentTestQuestion.setTestQuestionOrder(testQuestion
							.getTestQuestionOrder());
					studentTestQuestion.setTestQuestionValue(testQuestion
							.getTestQuestionValue());
					studentTestQuestion.setCorrectionFormula(testQuestion
							.getCorrectionFormula());
					studentTestQuestion.setTestQuestionMark(new Double(0));
					studentTestQuestion.setOldResponse(new Integer(0));
					studentTestQuestion.setResponse(null);

					if (questionList.size() == 0)
						questionList.addAll(testQuestion.getQuestion()
								.getMetadata().getVisibleQuestions());
					IQuestion question = getStudentQuestion(questionList);
					if (question == null) {
						throw new InvalidArgumentsServiceException();
					}
					studentTestQuestion.setQuestion(question);
					questionList.remove(question);
				}
			}
			//Create Evaluation - OnlineTest and Marks
			if (distributedTest.getTestType().equals(
					new TestType(TestType.EVALUATION))) {
				IOnlineTest onlineTest = new OnlineTest();
				persistentSuport.getIPersistentEvaluation().simpleLockWrite(
						onlineTest);
				onlineTest.setDistributedTest(distributedTest);
				List executionCourseList = new ArrayList();
				executionCourseList.add(executionCourse);
				onlineTest.setAssociatedExecutionCourses(executionCourseList);
			}

			//Create Advisory
			Iterator studentIt = studentList.iterator();
			List group = new ArrayList();
			while (studentIt.hasNext()) {
				IStudent student = (IStudent) studentIt.next();
				group.add(student.getPerson());
			}

			IAdvisory advisory = new Advisory();
			advisory.setCreated(null);
			advisory.setExpires(endDate.getTime());
			advisory.setSender("Docente da disciplina "
					+ executionCourse.getNome());
			advisory.setSubject(distributedTest.getTitle());
			String msgBeginning;

			if (distributedTest.getTestType().equals(
					new TestType(TestType.INQUIRY)))
				msgBeginning = new String(
						"Tem um <a href='"
								+ this.contextPath
								+ "/student/studentTests.do?method=prepareToDoTest&testCode="
								+ distributedTest.getIdInternal()
								+ "'>questionário</a> para responder entre ");
			else
				msgBeginning = new String(
						"Tem uma <a href='"
								+ this.contextPath
								+ "/student/studentTests.do?method=prepareToDoTest&testCode="
								+ distributedTest.getIdInternal()
								+ "'>Ficha de Trabalho</a> a realizar entre ");
			advisory.setMessage(msgBeginning + " as "
					+ getHourFormatted(beginHour) + " de "
					+ getDateFormatted(beginDate) + " e as "
					+ getHourFormatted(endHour) + " de "
					+ getDateFormatted(endDate));
			advisory.setOnlyShowOnce(new Boolean(false));
			persistentSuport.getIPersistentAdvisory().write(advisory, group);

			//Create DistributedTestAdvisory
			IDistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
			persistentSuport.getIPersistentDistributedTestAdvisory()
					.simpleLockWrite(distributedTestAdvisory);
			distributedTestAdvisory.setAdvisory(advisory);
			distributedTestAdvisory.setDistributedTest(distributedTest);

			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} catch (Exception e) {
			throw new FenixServiceException(e);
		}
	}

	private IQuestion getStudentQuestion(List questions)
			throws ExcepcaoPersistencia {
		IQuestion question = null;
		if (questions.size() != 0) {
			Random r = new Random();
			int questionIndex = r.nextInt(questions.size());
			question = (IQuestion) questions.get(questionIndex);
		}
		return question;
	}

	private List returnStudentsFromShiftsArray(
			ISuportePersistente persistentSuport, String[] shifts)
			throws FenixServiceException {
		List studentsList = new ArrayList();
		try {

			ITurnoPersistente persistentShift = persistentSuport
					.getITurnoPersistente();
			for (int i = 0; i < shifts.length; i++) {
				if (shifts[i].equals("Todos os Turnos")) {
					continue;
				} 
					ITurno shift = (ITurno) persistentShift.readByOID(
							Turno.class, new Integer(shifts[i]));
					Iterator studentIt = persistentSuport
							.getITurnoAlunoPersistente().readByShift(shift)
							.iterator();
					while (studentIt.hasNext()) {
						IStudent student = (IStudent) studentIt.next();
						if (!studentsList.contains(student))
							studentsList.add(student);
					}
				
			}
		} catch (Exception e) {
			throw new FenixServiceException(e);
		}
		return studentsList;
	}

	private List returnStudentsFromStudentsArray(
			ISuportePersistente persistentSuport, String[] students,
			IExecutionCourse executionCourse) throws FenixServiceException {
		List studentsList = new ArrayList();
		try {

			for (int i = 0; i < students.length; i++) {
				if (students[i].equals("Todos os Alunos")) {
					List attendList = persistentSuport
							.getIFrequentaPersistente().readByExecutionCourse(
									executionCourse);

					Iterator iterStudent = attendList.listIterator();
					while (iterStudent.hasNext()) {
						IFrequenta attend = (Frequenta) iterStudent.next();
						IStudent student = attend.getAluno();
						studentsList.add(student);
					}
					break;
				} 
					IStudent student = (IStudent) persistentSuport
							.getIPersistentStudent().readByOID(Student.class,
									new Integer(students[i]));
					if (!studentsList.contains(student))
						studentsList.add(student);
				
			}
		} catch (Exception e) {
			throw new FenixServiceException(e);
		}
		return studentsList;
	}

	private String getDateFormatted(Calendar date) {
		String result = new String();
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		return result;
	}

	private String getHourFormatted(Calendar hour) {
		String result = new String();
		result += hour.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (hour.get(Calendar.MINUTE) < 10)
			result += "0";
		result += hour.get(Calendar.MINUTE);
		return result;
	}
}