package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaDepartamento;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 26/Mar/2003
 */

public class CurricularCourseOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IDisciplinaDepartamentoPersistente persistentDepartmentCourse = null;
	ICursoPersistente persistentDegree = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
	IPersistentCurricularCourse persistentCurricularCourse = null;

	public CurricularCourseOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CurricularCourseOJBTest.class);

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
		persistentDepartmentCourse = persistentSupport.getIDisciplinaDepartamentoPersistente();
		persistentDegree = persistentSupport.getICursoPersistente();
		persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------
	public void testWriteCurricularCourse() {
		IDisciplinaDepartamento departmentCourse = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;

		System.out.println("\n- Test 1.1 : Write Existing CurricularCourse\n");

		try {
			persistentSupport.iniciarTransaccao();
			departmentCourse = persistentDepartmentCourse.lerDisciplinaDepartamentoPorNomeESigla("Engenharia da Programacao", "ep");

			ICurso degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading departmentCourse & degreeCurricularPlan");
		}

		assertNotNull(departmentCourse);
		assertNotNull(degreeCurricularPlan);

		// CurricularCourse ja existente

		ICurricularCourse curricularCourse =
			new CurricularCourse(
				new Double(0.0),
				new Double(0.0),
				new Double(0.0),
				new Double(0.0),
				new Double(0.0),
				"Trabalho Final de Curso I",
				"TFCI",
				departmentCourse,
				degreeCurricularPlan);

		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourse.lockWrite(curricularCourse);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing curricularCourse");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
//				FIXME: DAVID-RICARDO: Aqui devia estar um cancelarTransaccao mas nao esta porque rebenta (java.util.ConcurrentModificationException)
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing CurricularCourse");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing CurricularCourse");
		}

		// CurricularCourse inexistente

		curricularCourse =
			new CurricularCourse(
				new Double(0.0),
				new Double(0.0),
				new Double(0.0),
				new Double(0.0),
				new Double(0.0),
				"Trabalho Final de Curso IX",
				"TFCIX",
				departmentCourse,
				degreeCurricularPlan);

		System.out.println("\n- Test 1.2 : Write Non Existing CurricularCourse\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourse.lockWrite(curricularCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing CurricularCourse");
		}

		ICurricularCourse curricularCourse2 = null;

		try {
			persistentSupport.iniciarTransaccao();
			curricularCourse2 = persistentCurricularCourse.readCurricularCourseByNameAndCode(curricularCourse.getName(), curricularCourse.getCode());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing CurricularCourse Just Writen Before");
		}

		assertNotNull(curricularCourse2);

		assertTrue(curricularCourse2.getDegreeCurricularPlan().equals(curricularCourse.getDegreeCurricularPlan()));

		assertTrue(curricularCourse2.getDepartmentCourse().equals(curricularCourse.getDepartmentCourse()));

	}
	// -------------------------------------------------------------------------------------------------------------------------------------------
	public void testDeleteAllCurricularCourses() {

		System.out.println("\n- Test 2 : Delete All CurricularCourses");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourse.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Delete All CurricularCourses");
		}

		ArrayList result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentCurricularCourse.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All CurricularCourses");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	//// -------------------------------------------------------------------------------------------------------------------------------------------
	public void testReadCurricularCourse() {

		ICurricularCourse curricularCourse = null;

		//		CurricularCourse existente
		System.out.println("\n- Test 3.1 : Read Existing CurricularCourse\n");

		try {
			persistentSupport.iniciarTransaccao();
			curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso I", "TFCI");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing CurricularCourse");
		}
		assertNotNull(curricularCourse);

		// CurricularCourse inexistente

		System.out.println("\n- Test 3.2 : Read Not Existing CurricularCourse\n");
		try {
			persistentSupport.iniciarTransaccao();
			curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Unknown", "unk");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Read Not Existing CurricularCourse");
		}
		assertNull(curricularCourse);
	}
	//// -------------------------------------------------------------------------------------------------------------------------------------------
	public void testDeleteCurricularCourse() {

		ICurricularCourse curricularCourse = null;

		//		CurricularCourse existente
		System.out.println("\n- Test 4.1 : Delete Existing CurricularCourse\n");

		try {
			persistentSupport.iniciarTransaccao();
			curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso I", "TFCI");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Existing CurricularCourse To Delete");
		}
		assertNotNull(curricularCourse);

		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourse.delete(curricularCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing CurricularCourse");
		}

		try {
			persistentSupport.iniciarTransaccao();
			curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso I", "TFCI");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Just Deleted CurricularCourse");
		}
		assertNull(curricularCourse);

		// CurricularCourse inexistente
		System.out.println("\n- Test 4.2 : Delete Non Existing CurricularCourse\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourse.delete(new CurricularCourse());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing CurricularCourse");
		}

	}

	//// -------------------------------------------------------------------------------------------------------------------------------------------
	public void testReadAllCurricularCourses() {
		ArrayList list = null;

		System.out.println("\n- Test 5 : Read All Existing CurricularCourses\n");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentCurricularCourse.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All CurricularCourses");
		}
		assertNotNull(list);
		assertEquals(list.size(), 10);
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------

	public void testReadCurricularCoursesByCurricularYear() {

		// CurricularCourses existentes
		System.out.println("\n- Test 6.1 : Read Existing CurricularCourses\n");

		ArrayList list = null;
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentCurricularCourse.readCurricularCoursesByCurricularYear(new Integer(1));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing CurricularCourse");
		}
		assertNotNull(list);
	}
}