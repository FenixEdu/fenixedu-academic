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

import DataBeans.InfoMetadata;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.DistributedTest;
import Dominio.Frequenta;
import Dominio.IDisciplinaExecucao;
import Dominio.IDistributedTest;
import Dominio.IFrequenta;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.ITurno;
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
import UtilTests.ParseMetadata;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class InsertDistributedTest implements IServico {
	private static InsertDistributedTest service = new InsertDistributedTest();

	public static InsertDistributedTest getService() {
		return service;
	}

	public InsertDistributedTest() {
	}

	public String getNome() {
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
		String[] selectedShifts)
		throws FenixServiceException {

		try {

			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();
			IDistributedTest distributedTest = new DistributedTest();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, false);
			if (test == null) {
				throw new InvalidArgumentsServiceException();
			}

			distributedTest.setTestInformation(testInformation);
			distributedTest.setBeginDate(beginDate);
			distributedTest.setBeginHour(beginHour);
			distributedTest.setEndDate(endDate);
			distributedTest.setEndHour(endHour);
			distributedTest.setTestType(testType);
			distributedTest.setCorrectionAvailability(correctionAvaiability);
			distributedTest.setStudentFeedback(feedback);
			distributedTest.setTest(test);
			persistentDistributedTest.simpleLockWrite(distributedTest);

			ITurnoPersistente persistentShift =
				persistentSuport.getITurnoPersistente();
			List studentList = new ArrayList();
			for (int i = 0; i < selectedShifts.length; i++) {
				if (selectedShifts[i].equals("Todos os Alunos")) {
					IDisciplinaExecucao executionCourse =
						new DisciplinaExecucao(test.getKeyExecutionCourse());
					executionCourse =
						(IDisciplinaExecucao) persistentSuport
							.getIDisciplinaExecucaoPersistente()
							.readByOId(executionCourse, false);
					List attendList =
						(List) persistentSuport
							.getIFrequentaPersistente()
							.readByExecutionCourse(
							executionCourse);

					Iterator iterStudent = attendList.listIterator();
					while (iterStudent.hasNext()) {
						IFrequenta attend = (Frequenta) iterStudent.next();
						IStudent student = attend.getAluno();
						studentList.add(student);
						//i=selectedShifts.length;
					}
				} else {
					ITurno shift = new Turno(new Integer(selectedShifts[i]));
					shift = (ITurno) persistentShift.readByOId(shift, false);
					studentList.addAll(
						persistentSuport
							.getITurnoAlunoPersistente()
							.readByShift(
							shift));
				}
			}
			IPersistentStudentTestQuestion persistentStudentTestQuestion =
				persistentSuport.getIPersistentStudentTestQuestion();
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();

			List testQuestionList = persistentTestQuestion.readByTest(test);
			Iterator testQuestionIt = testQuestionList.iterator();
			while (testQuestionIt.hasNext()) {
				ITestQuestion testQuestion =
					(ITestQuestion) testQuestionIt.next();
				Iterator studentIt = studentList.iterator();
				while (studentIt.hasNext()) {
					IStudent student = (IStudent) studentIt.next();
					IStudentTestQuestion studentTestQuestion =
						new StudentTestQuestion();
					studentTestQuestion.setStudent(student);
					studentTestQuestion.setDistributedTest(distributedTest);
					studentTestQuestion.setTestQuestionOrder(
						testQuestion.getTestQuestionOrder());
					studentTestQuestion.setTestQuestionValue(
						testQuestion.getTestQuestionValue());
					studentTestQuestion.setResponse(new Integer(0));
					String studentQuestionFileName =
						getStudentQuestionFileName(
							testQuestion.getQuestion().getMetadata());

					IPersistentQuestion persistentQuestion =
						persistentSuport.getIPersistentQuestion();
					IQuestion question =
						(
							IQuestion) persistentQuestion
								.readByFileNameAndMetadataId(
							studentQuestionFileName,
							testQuestion.getQuestion().getMetadata());
					if (question == null) {
						throw new InvalidArgumentsServiceException();
					}
					studentTestQuestion.setQuestion(question);

					ParseQuestion p = new ParseQuestion();
					try {
						studentTestQuestion.setOptionShuffle(
							p.shuffleQuestionOptions(
								studentTestQuestion
									.getQuestion()
									.getXmlFile()));
					} catch (Exception e) {
						throw new FenixServiceException(e);
					}

					persistentStudentTestQuestion.lockWrite(
						studentTestQuestion);
				}
			}
			
			//fazer anoucement para os alunos.........................
			
			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private String getStudentQuestionFileName(IMetadata metadata)
		throws FenixServiceException {
		InfoMetadata infoMetadata = Cloner.copyIMetadata2InfoMetadata(metadata);
		ParseMetadata p = new ParseMetadata();
		try {
			infoMetadata =
				p.parseMetadata(metadata.getMetadataFile(), infoMetadata);
		} catch (Exception e) {
			throw new FenixServiceException(e);
		}

		Random r = new Random();
		int questionIndex = r.nextInt(infoMetadata.getMembers().size());
		String xmlFileName =(String) infoMetadata.getMembers().get(questionIndex);
		if (xmlFileName == null) {
			throw new InvalidArgumentsServiceException();
		}
		return xmlFileName;
	}

}