/*
 *
 * Created on 03/09/2003
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author asnr and scpo
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import Dominio.StudentGroup;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentsWithoutGroupTest extends TestCaseReadServices {

	public ReadStudentsWithoutGroupTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadStudentsWithoutGroupTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadStudentsWithoutGroup";
	}
	
	//WHEN ALL STUDENTS HAVE GROUPS
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] result = { new Integer(3), "user" };
		return result;

	}

	protected int getNumberOfItemsToRetrieve() {
		return 4;
	}

	protected Object getObjectToCompare() {
//		List infoStudentList = new ArrayList();
//		try {
//			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//			sp.iniciarTransaccao();
//			
//			IStudent student = (IStudent) sp.getIPersistentStudent().readByOId(new StudentGroup(new Integer(6)), false);
//			infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));
//
//			student = (IStudent) sp.getIPersistentStudent().readByOId(new StudentGroup(new Integer(7)), false);
//			infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));
//
//			student = (IStudent) sp.getIPersistentStudent().readByOId(new StudentGroup(new Integer(10)), false);
//			infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));
//
//			student = (IStudent) sp.getIPersistentStudent().readByOId(new StudentGroup(new Integer(11)), false);
//			infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));
//
//			sp.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			ex.printStackTrace();
//			
//		}
//		return infoStudentList;
		return null;
	}

}
