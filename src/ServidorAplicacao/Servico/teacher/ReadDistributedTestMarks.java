/*
 * Created on Oct 14, 2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTestMarks;
import DataBeans.InfoSiteDistributedTestMarks;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;

import Dominio.DisciplinaExecucao;
import Dominio.DistributedTest;
import Dominio.IDisciplinaExecucao;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 *
 */
public class ReadDistributedTestMarks implements IServico {

	private static ReadDistributedTestMarks service =
		new ReadDistributedTestMarks();

	public static ReadDistributedTestMarks getService() {
		return service;
	}

	public String getNome() {
		return "ReadDistributedTestMarks";
	}

	public SiteView run(Integer executionCourseId, Integer distributedTestId)
		throws FenixServiceException {

		ISuportePersistente persistentSuport;

		InfoSiteDistributedTestMarks infoSiteDistributedTestMarks =
			new InfoSiteDistributedTestMarks();
		List infoDistributedTestMarksList = new ArrayList();

		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(executionCourseId);
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IDistributedTest distributedTest =
				new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();

			int numberOfQuestions =
				distributedTest.getNumberOfQuestions().intValue();
			int[] correctAnswers = new int[(numberOfQuestions)],
				wrongAnswers = new int[(numberOfQuestions)],
				notAnswered = new int[(numberOfQuestions)];

			List studentList =
				(List) persistentSuport
					.getIPersistentStudentTestQuestion()
					.readStudentsByDistributedTest(distributedTest);
			if (studentList == null || studentList.size() == 0)
				throw new FenixServiceException();
			Collections.sort(studentList, new BeanComparator("number"));

			Iterator studentIt = studentList.iterator();
			DecimalFormat df = new DecimalFormat("#0.##");

			while (studentIt.hasNext()) {
				IStudent student = (Student) studentIt.next();
				List studentTestQuestionList =
					(List) persistentSuport
						.getIPersistentStudentTestQuestion()
						.readByStudentAndDistributedTest(
							student,
							distributedTest);
				if (studentTestQuestionList == null
					|| studentTestQuestionList.size() == 0)
					throw new FenixServiceException();
				Collections.sort(
					studentTestQuestionList,
					new BeanComparator("testQuestionOrder"));

				List infoStudentTestQuestionList = new ArrayList();
				Double mark = new Double(0);
				Iterator studentTestQuestionIt =
					studentTestQuestionList.iterator();
				while (studentTestQuestionIt.hasNext()) {
					IStudentTestQuestion studentTestQuestion =
						(IStudentTestQuestion) studentTestQuestionIt.next();
					InfoStudentTestQuestion infoStudentTestQuestion =
						Cloner
							.copyIStudentTestQuestion2InfoStudentTestQuestion(
							studentTestQuestion);
					ParseQuestion parse = new ParseQuestion();
					try {
						infoStudentTestQuestion.setQuestion(
							parse.parseQuestion(
								infoStudentTestQuestion
									.getQuestion()
									.getXmlFile(),
								infoStudentTestQuestion.getQuestion()));
						infoStudentTestQuestion =
							parse.getOptionsShuffle(infoStudentTestQuestion);
					} catch (Exception e) {
						throw new FenixServiceException(e);
					}
					Double thisMark = new Double(0);
					int index =
						studentTestQuestion.getTestQuestionOrder().intValue()
							- 1;
					if (!infoStudentTestQuestion
						.getResponse()
						.equals(new Integer(0))) {
						if (infoStudentTestQuestion
							.getQuestion()
							.getCorrectResponse()
							.contains(infoStudentTestQuestion.getResponse())) {
							thisMark =
								new Double(
									infoStudentTestQuestion
										.getTestQuestionValue()
										.doubleValue());
							correctAnswers[index] = correctAnswers[index] + 1;
						} else {
							thisMark =
								new Double(
									(infoStudentTestQuestion
										.getTestQuestionValue()
										.intValue()
										* (java
											.lang
											.Math
											.pow(
												infoStudentTestQuestion
													.getQuestion()
													.getOptionNumber()
													.intValue()
													- 1,
												-1))));
							wrongAnswers[index] = wrongAnswers[index] + 1;
						}

					} else {
						notAnswered[index] = notAnswered[index] + 1;
					}

					if (thisMark.doubleValue() < 0)
						thisMark = new Double(0);

					mark =
						new Double(mark.doubleValue() + thisMark.doubleValue());
					infoStudentTestQuestion.setMark(
						new Double(df.format(thisMark)));
					infoStudentTestQuestionList.add(infoStudentTestQuestion);
				}
				InfoDistributedTestMarks infoDistributedTestMarks =
					new InfoDistributedTestMarks();
				infoDistributedTestMarks.setInfoStudentTestQuestionList(
					infoStudentTestQuestionList);
				infoDistributedTestMarks.setStudentTestMark(
					new Double(df.format(mark)));
				infoDistributedTestMarksList.add(infoDistributedTestMarks);
			}

			infoSiteDistributedTestMarks.setInfoDistributedTestMarks(
				infoDistributedTestMarksList);
			List correctAnswersList = new ArrayList(),
				wrongAnswersList = new ArrayList(),
				notAnsweredList = new ArrayList();
			df = new DecimalFormat("0%");
			for (int i = 0; i < correctAnswers.length; i++) {
				correctAnswersList.add(df.format(correctAnswers[i]*java.lang.Math.pow(studentList.size(), -1)));
				wrongAnswersList.add(df.format(wrongAnswers[i]*java.lang.Math.pow(studentList.size(), -1)));
				notAnsweredList.add(df.format(notAnswered[i] * java.lang.Math.pow(studentList.size(), -1)));
			}

			infoSiteDistributedTestMarks.setCorrectAnswersPercentage(
				correctAnswersList);
			infoSiteDistributedTestMarks.setWrongAnswersPercentage(
				wrongAnswersList);
			infoSiteDistributedTestMarks.setNotAnsweredPercentage(
				notAnsweredList);

			infoSiteDistributedTestMarks.setExecutionCourse(
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse));
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		SiteView siteView =
			new ExecutionCourseSiteView(
				infoSiteDistributedTestMarks,
				infoSiteDistributedTestMarks);
		return siteView;
	}
}
