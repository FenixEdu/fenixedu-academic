/*
 * Created on 19/Set/2003
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
public class ReadStudentsWithoutDistributedTestTest extends
		TestCaseReadServices {

	/**
	 * @param testName
	 */
	public ReadStudentsWithoutDistributedTestTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadStudentsWithoutDistributedTest";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(26), new Integer(25) };
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

			IStudent student10 = (IStudent) sp.getIPersistentStudent()
					.readByOID(Student.class, new Integer(10));

			IStudent student11 = (IStudent) sp.getIPersistentStudent()
					.readByOID(Student.class, new Integer(11));

			assertNotNull("student10 null", student10);
			assertNotNull("student11 null", student11);

			sp.confirmarTransaccao();

			InfoStudent infoStudent10 = Cloner
					.copyIStudent2InfoStudent(student10);
			InfoStudent infoStudent11 = Cloner
					.copyIStudent2InfoStudent(student11);

			infoStudentList.add(infoStudent10);
			infoStudentList.add(infoStudent11);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		return infoStudentList;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}