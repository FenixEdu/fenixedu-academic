/*
 * CursoExecucaoOJBTest.java
 *
 * Created on 2 de Novembro de 2002, 22:46
 */

package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 *
 * @author rpfi
 */
public class CursoExecucaoOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentExecutionYear persistentExecutionYear = null;
	ICursoExecucaoPersistente persistentExecutionDegree = null;
	ICursoPersistente persistentDegree = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
	IPersistentTeacher persistentTeacher = null;
	

	public CursoExecucaoOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CursoExecucaoOJBTest.class);

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
		persistentExecutionDegree =
			persistentSupport.getICursoExecucaoPersistente();
		persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
		persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
		persistentDegree = persistentSupport.getICursoPersistente();
		persistentTeacher = persistentSupport.getIPersistentTeacher();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/** Test of readByCursoAndAnoLectivo method, of class ServidorPersistente.OJB.CursoExecucaoOJB. */
	public void testReadAllExecutionDegreesByExecutionYear() {
		List executionDegrees = null;
		IExecutionYear executionYear = null;
		// read existing CursoExecucao
		try {
			persistentSupport.iniciarTransaccao();

			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			executionDegrees =
				persistentExecutionDegree.readByExecutionYear(executionYear);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByCursoAndAnoLectivo:fail read existing cursoExecucao");
		}
		assertEquals(
			"testReadByCursoAndAnoLectivo:read existing cursoExecucao",
			4,
			executionDegrees.size());
	}

	// write new non-existing cursoExecucao
	public void testCreateNonExistingExecutionDegree() {
		try {
			persistentSupport.iniciarTransaccao();
			ICurso degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
			IDegreeCurricularPlan degreeCurricularPlan =
				persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName("2003/2004");
			assertNotNull(executionYear);
			
			ITeacher coordinator = persistentTeacher.readTeacherByNumber(new Integer(6));
			assertNotNull(coordinator);
			persistentSupport.confirmarTransaccao();

			ICursoExecucao executionDegree =
				new CursoExecucao(executionYear, degreeCurricularPlan);
			executionDegree.setCoordinator(coordinator);

			persistentSupport.iniciarTransaccao();
			persistentExecutionDegree.lockWrite(executionDegree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
			fail("testCreateNonExistingCursoExecucao");
		}
	}

	// write new existing cursoExecucao
	public void testCreateExistingCursoExecucao() {
		try {
			persistentSupport.iniciarTransaccao();
			ICurso degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			IDegreeCurricularPlan degreeCurricularPlan =
				persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			persistentSupport.confirmarTransaccao();

			ICursoExecucao executionDegree =
				new CursoExecucao(executionYear, degreeCurricularPlan);

			persistentSupport.iniciarTransaccao();
			persistentExecutionDegree.lockWrite(executionDegree);
			persistentSupport.confirmarTransaccao();
			fail("testCreateNonExistingCursoExecucao");
		} catch (ExistingPersistentException ex) {
			// All is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("testCreateNonExistingCursoExecucao: unexpected exception");
		}
	}

	/** Test of write method, of class ServidorPersistente.OJB.ItemOJB. */
	public void testWriteExistingChangedObject() {
		IDegreeCurricularPlan degreeCurricularPlan1 = null;
		IExecutionYear executionYear1 = null;
		IExecutionYear executionYear2 = null;
		ICursoExecucao executionDegree = null;

		try {
			persistentSupport.iniciarTransaccao();
			ICurso degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan1 = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan1);

			executionYear1 = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear1);

			executionYear2 = persistentExecutionYear.readExecutionYearByName("2003/2004");
			assertNotNull(executionYear2);

			executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan1, executionYear1);
			executionDegree.setExecutionYear(executionYear2);

			ITeacher coordinator = persistentTeacher.readTeacherByNumber(new Integer(6));
			assertNotNull(coordinator);

			executionDegree.setCoordinator(coordinator);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			ICursoExecucao executionDegreeTemp = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan1, executionYear2);
			persistentSupport.confirmarTransaccao();
			assertEquals(executionDegreeTemp, executionDegree);
			assertEquals(coordinator, executionDegreeTemp.getCoordinator());

		} catch (ExcepcaoPersistencia ex) {
			fail("testWriteExistingChangedObject");
		}

	}

	/** Test of delete method, of class ServidorPersistente.OJB.CursoExecucaoOJB. */
	public void testDeleteCursoExecucao() {
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IExecutionYear executionYear = null;
		ICursoExecucao executionDegree = null;

		try {
			persistentSupport.iniciarTransaccao();
			ICurso degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			degreeCurricularPlan =
				persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			executionDegree =
				persistentExecutionDegree
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			persistentExecutionDegree.delete(executionDegree);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			ICursoExecucao executionDegreeTemp =
				persistentExecutionDegree
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNull(executionDegreeTemp);
			assertEquals(
				SuportePersistenteOJB
					.getInstance()
					.getITurmaPersistente()
					.readByExecutionDegree(executionDegree)
					.size(),
				0);

			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteCursoExecucao");
		}
	}

	/** Test of deleteAll method, of class ServidorPersistente.OJB.CursoExecucaoOJB. */
	public void testDeleteAll() {
		try {
			persistentSupport.iniciarTransaccao();
			persistentExecutionDegree.deleteAll();
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			List result = null;
			try {
				Implementation odmg = OJB.getInstance();

				///////////////////////////////////////////////////////////////////
				// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
				///////////////////////////////////////////////////////////////////
				Database db = odmg.newDatabase();

				try {
					db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
				} catch (ODMGException e) {
					e.printStackTrace();
				}
				///////////////////////////////////////////////////////////////////
				// End of Added Code
				///////////////////////////////////////////////////////////////////

				OQLQuery query = odmg.newOQLQuery();
				;
				String oqlQuery =
					"select all from " + CursoExecucao.class.getName();
				query.create(oqlQuery);
				result = (List) query.execute();
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			persistentSupport.confirmarTransaccao();
			assertNotNull(result);
			assertTrue(result.isEmpty());
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteItem");
		}
	}
	
	public void testReadAllMasterDegrees(){
		try {
			persistentSupport.iniciarTransaccao();
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			List result = persistentExecutionDegree.readMasterDegrees(executionYear.getYear());
			persistentSupport.confirmarTransaccao();
			assertNotNull(result);
			assertEquals(result.isEmpty(), false);
			assertEquals(3,result.size());

			
		} catch (ExcepcaoPersistencia ex) {
			fail("ReadMasterDegrees");
		}
	}


	public void testReadByDegreeNameAndExecutionYear(){
		try {
			persistentSupport.iniciarTransaccao();
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			ICursoExecucao result = persistentExecutionDegree.readByDegreeCodeAndExecutionYear("MEEC", executionYear);
			persistentSupport.confirmarTransaccao();
			assertNotNull(result);

			
		} catch (ExcepcaoPersistencia ex) {
			fail("ReadMasterDegrees");
		}
	}

	public void testReadByTeacher(){
		try {
			persistentSupport.iniciarTransaccao();

			ITeacher teacher = persistentTeacher.readTeacherByUsername("nmsn");
			assertNotNull(teacher);

			List result = persistentExecutionDegree.readByTeacher(teacher);

			assertNotNull(result);
			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 3);
			

			teacher = persistentTeacher.readTeacherByUsername("jorge");
			assertNotNull(teacher);

			result = persistentExecutionDegree.readByTeacher(teacher);

			assertNull(result);


			persistentSupport.confirmarTransaccao();			
		} catch (ExcepcaoPersistencia ex) {
			fail("ReadMasterDegrees");
		}
	}


}