/*
 * Created on Oct 15, 2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTestMarks;
import DataBeans.InfoSiteDistributedTestMarks;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;

import Dominio.DisciplinaExecucao;
import Dominio.IExecutionCourse;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 *
 */
public class ReadDistributedTestMarksTest extends TestCaseReadServices {

	public ReadDistributedTestMarksTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadDistributedTestMarks";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(26), new Integer(1)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		InfoSiteDistributedTestMarks infoSiteDistributedTestMarks =
			new InfoSiteDistributedTestMarks();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IStudentTestQuestion studentTestQuestion =
				new StudentTestQuestion(new Integer(1));

			studentTestQuestion =
				(IStudentTestQuestion) sp
					.getIPersistentStudentTestQuestion()
					.readByOId(
					studentTestQuestion,
					false);
			assertNotNull("studentTestQuestion null", studentTestQuestion);
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IExecutionCourse executionCourse =
				new DisciplinaExecucao(new Integer(26));

			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull("executionCourse null", executionCourse);

			sp.confirmarTransaccao();

			InfoStudentTestQuestion infoStudentTestQuestion =
				Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(
					studentTestQuestion);
			DecimalFormat df = new DecimalFormat("#0.##");
			infoStudentTestQuestion.setTestQuestionMark(
				new Double(0.33));
			List infoStudentTestQuestionList = new ArrayList();
			infoStudentTestQuestionList.add(infoStudentTestQuestion);
			InfoDistributedTestMarks infoDistributedTestMarks =
				new InfoDistributedTestMarks();
			infoDistributedTestMarks.setInfoStudentTestQuestionList(
				infoStudentTestQuestionList);
			infoDistributedTestMarks.setStudentTestMark(
				new Double(df.format(infoStudentTestQuestion.getTestQuestionMark())));
			List infoDistributedTestMarksList = new ArrayList();
			infoDistributedTestMarksList.add(infoDistributedTestMarks);

			infoSiteDistributedTestMarks.setInfoDistributedTestMarks(
				infoDistributedTestMarksList);
			df = new DecimalFormat("%");
			List correctAnswersList = new ArrayList(),
				wrongAnswersList = new ArrayList(),
				notAnsweredList = new ArrayList();
			correctAnswersList.add(df.format(0));
			wrongAnswersList.add(
				df.format(java.lang.Math.pow(1, -1)));
			notAnsweredList.add(df.format(0));

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
			fail("exception: ExcepcaoPersistencia ");
		}
		SiteView siteView =
			new ExecutionCourseSiteView(
				infoSiteDistributedTestMarks,
				infoSiteDistributedTestMarks);
		return siteView;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}
