/*
 * Created on Oct 15, 2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;

import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
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
		return 1;
	}

	protected Object getObjectToCompare() {

		List resultList = new ArrayList();
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
			sp.confirmarTransaccao();

			InfoStudentTestQuestion infoStudentTestQuestion =
				Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(
					studentTestQuestion);
			List infoStudentTestQuestionList = new ArrayList();
			infoStudentTestQuestionList.add(infoStudentTestQuestion);
			resultList.add(infoStudentTestQuestionList);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}

		return resultList;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}
