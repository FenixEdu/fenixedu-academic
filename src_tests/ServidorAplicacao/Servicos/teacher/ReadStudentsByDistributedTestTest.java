/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsByDistributedTestTest extends TestCaseReadServices {

	/**
	* @param testName
	*/
	public ReadStudentsByDistributedTestTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadStudentsByDistributedTest";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(26), new Integer(26)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 2;
	}

	protected Object getObjectToCompare() {
		List infoStudentList = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IStudent student11 = new Student(new Integer(11));
			IStudent student9 = new Student(new Integer(9));

			student11 =
				(IStudent) sp.getIPersistentStudent().readByOId(
					student11,
					false);
			student9 =
				(IStudent) sp.getIPersistentStudent().readByOId(
					student9,
					false);
			assertNotNull("student11 null", student11);
			assertNotNull("student9 null", student9);

			sp.confirmarTransaccao();

			InfoStudent infoStudent11 =
				Cloner.copyIStudent2InfoStudent(student11);
			InfoStudent infoStudent9 =
				Cloner.copyIStudent2InfoStudent(student9);

			infoStudentList.add(infoStudent11);
			infoStudentList.add(infoStudent9);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		return infoStudentList;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}
