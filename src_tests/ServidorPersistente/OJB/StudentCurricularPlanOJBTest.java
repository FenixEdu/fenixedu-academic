/*
 * StudentCurricularPlanOJBTest.java
 *
 * Created on 21 of December of 2002, 17:16
 *
 * Tests :
 *  -  1 : Read a Existing Student Curricular Plan (Active) 
 *  -  2 : Read a Non Existing Student Curricular Plan (Active)
 *  -  3 : Write a Existing Student Curricular Plan
 *  -  4 : Write a Non Existing Student Curricular Plan
 *  -  5 : Delete a Existing Student Curricular Plan
 *  -  6 : Delete a Non Existing Student Curricular Plan
 *  -  7 : Delete All
 *  -  8 : Read All Curricular Plans from a Student (Existing)
 *  -  9 : Read All Curricular Plans from a Student (Non Existing)
 * 
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IBranch;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

public class StudentCurricularPlanOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
	IPersistentStudent persistentStudent = null;
	ICursoPersistente persistentDegree = null;
	IPersistentBranch persistentBranch = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;

	public StudentCurricularPlanOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Begin of StudentCurricularPlanOJB Test \n");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of StudentCurricularPlanOJB Test \n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(StudentCurricularPlanOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
		persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
		persistentStudent = persistentSupport.getIPersistentStudent();
		persistentDegree = persistentSupport.getICursoPersistente();
		persistentBranch = persistentSupport.getIPersistentBranch();;
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadExisting() {
		System.out.println("- Test 1 : Read a Existing Student Curricular Plan (Active) ");
		IStudentCurricularPlan studentCurricularPlanTemp = null;

		try {
			persistentSupport.iniciarTransaccao();
			studentCurricularPlanTemp =
				persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing");
		}

		assertNotNull(studentCurricularPlanTemp);
		assertTrue(studentCurricularPlanTemp.getStudent().getNumber().intValue() == 600);
		assertTrue(studentCurricularPlanTemp.getDegreeCurricularPlan().getDegree().getSigla().equals("LEIC"));
		assertTrue(studentCurricularPlanTemp.getCurrentState().getState().intValue() == StudentCurricularPlanState.ACTIVE);
		assertTrue(studentCurricularPlanTemp.getStartDate().toString().equals("2002-12-21"));
	}

	public void testReadNonExisting() {
		System.out.println("- Test 2 : Read a Non Existing Student Curricular Plan (Active) ");
		IStudentCurricularPlan studentCurricularPlanTemp = null;

		try {
			persistentSupport.iniciarTransaccao();
			studentCurricularPlanTemp =
				persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(999999), new TipoCurso(TipoCurso.LICENCIATURA));
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Non Existing");
		}

		assertNull(studentCurricularPlanTemp);
	}

	public void testWriteExisting() {
		System.out.println("- Test 3 : Write a Existing Student Curricular Plan (Active) ");
		IStudentCurricularPlan studentCurricularPlan = null;
		IStudent studentTemp = null;
		ICurso degree = null;
		IDegreeCurricularPlan degreeCurricularPlan;
		IBranch branch = null;

		Calendar data = Calendar.getInstance();
		data.set(2002, Calendar.NOVEMBER, 17);

		try {
			persistentSupport.iniciarTransaccao();
			studentTemp = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(studentTemp);

			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
			
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
			
			branch = persistentBranch.readBranchByNameAndCode("Inteligencia Artificial","IA");
			assertNotNull(branch);
			
			persistentSupport.confirmarTransaccao();

			studentCurricularPlan =
				new StudentCurricularPlan(
					studentTemp,
					degreeCurricularPlan,
					branch,
					data.getTime(),
					new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
			persistentSupport.iniciarTransaccao();
			persistentStudentCurricularPlan.lockWrite(studentCurricularPlan);
			persistentSupport.confirmarTransaccao();
			fail("testWriteExisting: Expected an Exception");
		} catch (ExistingPersistentException ex) {
			assertNotNull("testWriteExisting", ex);
			try {
				//				NOTE: DAVID-RICARDO: Aqui devia estar um cancelarTransaccao mas nao esta porque rebenta (java.util.ConcurrentModificationException)
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				fail("aborting transaction");
				e.printStackTrace();
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("testWriteExisting: Unexpected Exception");
		}
	}

	public void testWriteNonExisting() {
		System.out.println("- Test 4 : Write a Non Existing Student Curricular Plan (Active) ");
		IStudentCurricularPlan studentCurricularPlan = null;
		IStudent studentTemp = null;
		ICurso degree = null;
		IBranch branch = null;
		IDegreeCurricularPlan degreeCurricularPlan;
		
		Calendar data = Calendar.getInstance();
		data.set(2002, Calendar.NOVEMBER, 17);

		try {
			persistentSupport.iniciarTransaccao();
			studentTemp = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(studentTemp);

			degree = persistentDegree.readBySigla("MEEC");
			assertNotNull(degree);
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano2", degree);
			assertNotNull(degreeCurricularPlan);
			branch = persistentBranch.readBranchByNameAndCode("Informatica Industrial","II");
			assertNotNull(branch);
			persistentSupport.confirmarTransaccao();

			studentCurricularPlan =
				new StudentCurricularPlan(
					studentTemp,
					degreeCurricularPlan,
					branch,
					data.getTime(),
					new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
			persistentSupport.iniciarTransaccao();
			persistentStudentCurricularPlan.lockWrite(studentCurricularPlan);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail(" Fail " + ex);
		}
	}

	public void testDeleteExisting() {
		System.out.println("- Test 5 : Delete a Existing Student Curricular Plan");
		IStudentCurricularPlan studentCurricularPlan = null;

		try {
			persistentSupport.iniciarTransaccao();
			studentCurricularPlan =
				persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(studentCurricularPlan);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			persistentStudentCurricularPlan.delete(studentCurricularPlan);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			studentCurricularPlan = null;
			studentCurricularPlan =
				persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNull(studentCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing");
		}
	}

	public void testDeleteNonExisting() {
		System.out.println("- Test 6 : Delete a Non Existing Student Curricular Plan");
		IStudentCurricularPlan studentCurricularPlan = null;
		IStudent studentTemp = null;
		ICurso degree = null;
		IBranch branch = null;
		IDegreeCurricularPlan degreeCurricularPlan;

		Calendar data = Calendar.getInstance();
		data.set(2002, Calendar.NOVEMBER, 17);

		try {
			persistentSupport.iniciarTransaccao();
			studentTemp = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(studentTemp);

			degree = persistentDegree.readBySigla("MEEC");
			assertNotNull(degree);
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano2", degree);
			assertNotNull(degreeCurricularPlan);
			branch = persistentBranch.readBranchByNameAndCode("Informatica Industrial","II");
			assertNotNull(branch);
			persistentSupport.confirmarTransaccao();

			studentCurricularPlan =
				new StudentCurricularPlan(
					studentTemp,
					degreeCurricularPlan,
					branch,
					data.getTime(),
					new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
			persistentSupport.iniciarTransaccao();
			persistentStudentCurricularPlan.delete(studentCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Deleting Non Existing");
		}
	}

	public void testDeleteAll() {
		System.out.println("- Test 7 : Delete all Existing Student Curricular Plan");
		IStudentCurricularPlan studentCurricularPlan = null;

		try {
			persistentSupport.iniciarTransaccao();
			studentCurricularPlan =
				persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(studentCurricularPlan);
			persistentSupport.confirmarTransaccao();

			assertNotNull(studentCurricularPlan);

			persistentSupport.iniciarTransaccao();
			studentCurricularPlan = null;
			persistentStudentCurricularPlan.deleteAll();
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			studentCurricularPlan =
				persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNull(studentCurricularPlan);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing");
		}
	}

	public void testReadAllFromStudent() {
		System.out.println("- Test 8 : Read All Curricular Plans from a Student (Existing)");
		List studentCurricularPlans = null;

		try {
			persistentSupport.iniciarTransaccao();
			studentCurricularPlans = persistentStudentCurricularPlan.readAllFromStudent(600);
			persistentSupport.confirmarTransaccao();

			assertNotNull(studentCurricularPlans);
			assertTrue(studentCurricularPlans.size() == 2);

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing");
		}
	}

	public void testReadAllFromStudentNonExisting() {
		System.out.println("- Test 9 : Read All Curricular Plans from a Student (Non Existing)");
		List curricularTemp = null;

		try {
			persistentSupport.iniciarTransaccao();
			curricularTemp = persistentStudentCurricularPlan.readAllFromStudent(99999);
			persistentSupport.confirmarTransaccao();

			assertNotNull(curricularTemp);
			assertTrue(curricularTemp.size() == 0);

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing");
		}
	}
	
	public void testReadStudentActiveCurricularPlan(){
		System.out.println("- Test 10 : Read Student Active Curricular Plan ");
		IStudentCurricularPlan studentCurricularPlan = null;
		try {
			persistentSupport.iniciarTransaccao();
			studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600),new TipoCurso(TipoCurso.LICENCIATURA));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
			fail("Reading Actual Student Curricular Plan");
		}
		assertNotNull("1 - Student curricular plan must be not null!",studentCurricularPlan);

		try {
			persistentSupport.iniciarTransaccao();

			studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentAndSpecializationCurricularPlan(new Integer(41329),new TipoCurso(TipoCurso.MESTRADO),new Specialization(Specialization.MESTRADO));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
			fail("Reading Actual Student Curricular Plan");
		}
		assertNotNull("2 - Student curricular plan must be not null!",studentCurricularPlan);
	}

}
