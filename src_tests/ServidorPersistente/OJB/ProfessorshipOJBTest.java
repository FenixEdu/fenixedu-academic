/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProfessorshipOJBTest extends TestCaseOJB {
	private SuportePersistenteOJB persistentSupport = null;
	private IDisciplinaExecucao executionCourse = null;
	private IExecutionPeriod executionPeriod = null;
	private IExecutionYear executionYear = null;
	private ITeacher teacher = null;
	private IPersistentProfessorship persistentProfessorship = null;
	private IPersistentTeacher persistentTeacher = null;
	private IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	private IPersistentExecutionPeriod persistentExecutionPeriod = null;
	private IPersistentExecutionYear persistentExecutionYear = null;
	/**
	 * @param testName
	 */
	public ProfessorshipOJBTest(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ProfessorshipOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error while setting up");
		}
		persistentExecutionCourse =
			persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentExecutionPeriod =
			persistentSupport.getIPersistentExecutionPeriod();
		persistentExecutionYear =
			persistentSupport.getIPersistentExecutionYear();
		persistentTeacher = persistentSupport.getIPersistentTeacher();
		persistentProfessorship =
			persistentSupport.getIPersistentProfessorship();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testDelete() {

		//read existing professorship
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer(2));
			assertNotNull(teacher);
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(executionPeriod);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			assertNotNull(executionCourse);
			IProfessorship professorship =
				persistentProfessorship.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNotNull(professorship);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Test delete professorship: failed in the test setup");
		}
		//delete professorship
		try {
			persistentSupport.iniciarTransaccao();
			IProfessorship professorship =
				persistentProfessorship.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNotNull(professorship);
			persistentProfessorship.delete(professorship);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete professorship: failed deleting");
		}
		//read again
		try {
			persistentSupport.iniciarTransaccao();
			IProfessorship professorship =
				persistentProfessorship.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNull(professorship);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete professorship: failed reading deleted professorship");
		}
	}

}
