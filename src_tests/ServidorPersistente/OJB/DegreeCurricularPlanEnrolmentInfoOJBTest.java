package ServidorPersistente.OJB;

import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentDegreeCurricularPlanEnrolmentInfo;
import ServidorPersistente.IPersistentExecutionPeriod;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class DegreeCurricularPlanEnrolmentInfoOJBTest extends TestCaseOJB {

	private IPersistentExecutionPeriod executionPeriodDAO;
	SuportePersistenteOJB persistentSupport = null;
	IPersistentDegreeCurricularPlanEnrolmentInfo persistentDegreeEnrolmentInfo = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
	ICursoPersistente persistentDegree = null;

	public DegreeCurricularPlanEnrolmentInfoOJBTest(java.lang.String testName) {
		super(testName);
	}
/*
	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class DegreeCurricularPlanEnrolmentInfoOJB.");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class DegreeCurricularPlanEnrolmentInfoOJB.");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(DegreeCurricularPlanEnrolmentInfoOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error in SetUp.");
		}
		persistentDegreeEnrolmentInfo = persistentSupport.getIPersistentDegreeEnrolmentInfo();
		persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
		persistentDegree = persistentSupport.getICursoPersistente();
	}

	protected void tearDown() {
		super.tearDown();
	}

// -------------------------------------------------------------------------------------------------------------------------

	public void testWriteDegreeEnrolmentInfo() {

		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = null;

		// DegreeCurricularPlanEnrolmentInfo ja existente
		System.out.println("- Test 1.1 : Write Existing DegreeCurricularPlanEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading degree & degreeCurricularPlan");
		}
		assertNotNull(degree);
		assertNotNull(degreeCurricularPlan);

		IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfo = new DegreeCurricularPlanEnrolmentInfo();
		degreeEnrolmentInfo.setDegreeCurricularPlan(degreeCurricularPlan);
		degreeEnrolmentInfo.setDegreeDuration(new Integer(5));
		degreeEnrolmentInfo.setMinimalYearForOptionalCourses(new Integer(3));
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.lockWrite(degreeEnrolmentInfo);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing DegreeCurricularPlanEnrolmentInfo");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing DegreeCurricularPlanEnrolmentInfo");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing DegreeCurricularPlanEnrolmentInfo");
		}

		// DegreeCurricularPlanEnrolmentInfo inexistente
		System.out.println(".- Test 1.2 : Write Non Existing DegreeCurricularPlanEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("MEEC");
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano2", degree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading degree & degreeCurricularPlan");
		}
		assertNotNull(degree);
		assertNotNull(degreeCurricularPlan);

		degreeEnrolmentInfo = new DegreeCurricularPlanEnrolmentInfo();
		degreeEnrolmentInfo.setDegreeCurricularPlan(degreeCurricularPlan);
		degreeEnrolmentInfo.setDegreeDuration(new Integer(5));
		degreeEnrolmentInfo.setMinimalYearForOptionalCourses(new Integer(3));
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.lockWrite(degreeEnrolmentInfo);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing DegreeCurricularPlanEnrolmentInfo");
		}

		IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfo2 = null;
		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfo2 = persistentDegreeEnrolmentInfo.readDegreeEnrolmentInfoByDegreeCurricularPlan(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing DegreeCurricularPlanEnrolmentInfo Just Writen Before");
		}
		assertNotNull(degreeEnrolmentInfo2);
		assertEquals("DegreeEnrolmentInfos not equal!", degreeEnrolmentInfo, degreeEnrolmentInfo2);
	}

// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteAllDegreeEnrolmentInfos() {

		System.out.println("- Test 2 : Delete All DegreeEnrolmentInfos");
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Delete All DegreeEnrolmentInfos");
		}

		List result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentDegreeEnrolmentInfo.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All DegreeEnrolmentInfos");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testreadDegreeEnrolmentInfoByDegreeCurricularPlan() {

		IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfo = null;

		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = null;

		//	DegreeCurricularPlanEnrolmentInfo ja existente
		System.out.println("- Test 3.1 : Read Existing DegreeCurricularPlanEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading degree & degreeCurricularPlan");
		}
		assertNotNull(degree);
		assertNotNull(degreeCurricularPlan);

		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfo = persistentDegreeEnrolmentInfo.readDegreeEnrolmentInfoByDegreeCurricularPlan(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing DegreeCurricularPlanEnrolmentInfo");
		}
		assertNotNull(degreeEnrolmentInfo);
		assertTrue(degreeEnrolmentInfo.getDegreeCurricularPlan().equals(degreeCurricularPlan));
		assertTrue(degreeEnrolmentInfo.getDegreeDuration().intValue() == 5);
		assertTrue(degreeEnrolmentInfo.getMinimalYearForOptionalCourses().intValue() == 3);

		// DegreeCurricularPlanEnrolmentInfo inexistente
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("MEEC");
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano2", degree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading degree & degreeCurricularPlan");
		}
		assertNotNull(degree);
		assertNotNull(degreeCurricularPlan);

		degreeEnrolmentInfo = null;
		System.out.println("- Test 3.2 : Read Non Existing DegreeCurricularPlanEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfo = persistentDegreeEnrolmentInfo.readDegreeEnrolmentInfoByDegreeCurricularPlan(degreeCurricularPlan);			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Non Existing DegreeCurricularPlanEnrolmentInfo");
		}
		assertNull(degreeEnrolmentInfo);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteDegreeEnrolmentInfo() {
		
		IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfo = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = null;

		//	DegreeCurricularPlanEnrolmentInfo ja existente
		System.out.println("- Test 4.1 : Delete Existing DegreeCurricularPlanEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading degree & degreeCurricularPlan");
		}
		assertNotNull(degree);
		assertNotNull(degreeCurricularPlan);

		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfo = persistentDegreeEnrolmentInfo.readDegreeEnrolmentInfoByDegreeCurricularPlan(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Reading Existing DegreeCurricularPlanEnrolmentInfo To Delete");
		}

		assertNotNull(degreeEnrolmentInfo);

		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.delete(degreeEnrolmentInfo);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing DegreeCurricularPlanEnrolmentInfo");
		}

		IDegreeCurricularPlanEnrolmentInfo enr2 = null;
		try {
			persistentSupport.iniciarTransaccao();
			enr2 = persistentDegreeEnrolmentInfo.readDegreeEnrolmentInfoByDegreeCurricularPlan(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Just Deleted DegreeCurricularPlanEnrolmentInfo");
		}
		assertNull(enr2);

		// DegreeCurricularPlanEnrolmentInfo inexistente
		System.out.println("- Test 4.2 : Delete Non Existing DegreeCurricularPlanEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.delete(new DegreeCurricularPlanEnrolmentInfo());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing DegreeCurricularPlanEnrolmentInfo");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadAllDegreeEnrolmentInfos() {

		List list = null;

		System.out.println("- Test 5 : Read All Existing DegreeCurricularPlanEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentDegreeEnrolmentInfo.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All DegreeEnrolmentInfos");
		}
		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	*/
}