/*
 * DegreeCurricularPlanOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class DegreeCurricularPlanOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	ICursoPersistente persistentDegree = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
	
    public DegreeCurricularPlanOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DegreeCurricularPlanOJBTest.class);
        
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
		persistentDegree = persistentSupport.getICursoPersistente();
		persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
		
    }
    
    protected void tearDown() {
        super.tearDown();
    }


	public void testLerPlanoCurricular() {
		System.out.println("testLerPlanoCurricular");        
		// Read existing
		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}
		assertNotNull(degreeCurricularPlan);
        
		assertTrue(degreeCurricularPlan.getName().equals("plano1"));
		assertEquals(degreeCurricularPlan.getDegree(), degree);

		// Read non existing
		degreeCurricularPlan = null;
		try {
			persistentSupport.iniciarTransaccao();
			
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("nao existe", degree);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_2");
		}
		assertNull(degreeCurricularPlan);
	}

    
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testEscreverPlanoCurricular() {
        System.out.println("testEscreverPlanoCurricular");
        
        // Write existing
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testEscreverPlanoCurricular: confirmarTransaccao_1");
		}
        
        IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan("plano1", degree);
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentDegreeCurricularPlan.write(degreeCurricularPlan);
            persistentSupport.confirmarTransaccao();
            fail("testEscreverPlanoCurricular: confirmarTransaccao_1");
        } catch(ExistingPersistentException ex) {
			assertNotNull("Write DegreeCurricularPlan: Exception an exception: ", ex);
		} catch(ExcepcaoPersistencia ex) {
			fail("Write DegreeCurricularPlan: unexpected exception: " + ex);
		}        
        // Write non Existing
		ICurso degreeTemp = null;
		try {
            persistentSupport.iniciarTransaccao();
            degreeTemp = persistentDegree.readBySigla("LEIC");
            persistentSupport.confirmarTransaccao();
			
        } catch(ExcepcaoPersistencia ex2) {
            fail("testEscreverPlanoCurricular: confirmarTransaccao_2");
        }
	
		degreeCurricularPlan = null;
        degreeCurricularPlan = new DegreeCurricularPlan("planoDesc", degreeTemp);
        try {
            persistentSupport.iniciarTransaccao();
            persistentDegreeCurricularPlan.write(degreeCurricularPlan);
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testEscreverPlanoCurricular: confirmarTransaccao_2");
        }
        
        // Check insert
        IDegreeCurricularPlan degreeCurricularTemp2 = null;

        try {
            persistentSupport.iniciarTransaccao();
            degreeCurricularTemp2 = persistentDegreeCurricularPlan.readByNameAndDegree(degreeCurricularPlan.getName(), degreeTemp);
			assertNotNull(degreeCurricularTemp2);
			persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverPlanoCurricular: confirmarTransaccao_3");
        }
        

        assertEquals(degreeCurricularTemp2, degreeCurricularPlan);
    }


	public void testApagarPlanoCurricular() {
		System.out.println("testApagarPlanoCurricular");


		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = null;
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}
		assertNotNull(degreeCurricularPlan);

		Implementation odmg = null;
		OQLQuery query = null;
		String oqlQuery = null;
		List result = null;

		try {
			persistentSupport.iniciarTransaccao();

			try {
	
			// Check existing Execution Degree
			odmg = OJB.getInstance();

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

			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CursoExecucao.class.getName();
			oqlQuery += " where curricularPlan.name = $1 ";
			oqlQuery += " and curricularPlan.degree.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getSigla());
			result = (List) query.execute();
			assertEquals(result.isEmpty(), false);
	
	
			// Check existing CurricularCourse
			odmg = OJB.getInstance();

			///////////////////////////////////////////////////////////////////
			// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
			///////////////////////////////////////////////////////////////////
			db = odmg.newDatabase();

			try {
				db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
			} catch (ODMGException e) {
				e.printStackTrace();
			}
			///////////////////////////////////////////////////////////////////
			// End of Added Code
			///////////////////////////////////////////////////////////////////

			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where degreeCurricularPlan.name = $1 ";
			oqlQuery += " and degreeCurricularPlan.degree.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getSigla());
			result = (List) query.execute();
			assertEquals(result.isEmpty(), false);
	
			} catch (QueryException ex) {
			  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}
		
		// Delete
		
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeCurricularPlan.deleteDegreeCurricularPlan(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex3) {
			fail("testApagarPlanoCurricular: confirmarTransaccao_1");
		}
		
		try {
			persistentSupport.iniciarTransaccao();

			try {
	
			// Check existing Execution Degree
			odmg = OJB.getInstance();

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
			
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CursoExecucao.class.getName();
			oqlQuery += " where curricularPlan.name = $1 ";
			oqlQuery += " and curricularPlan.degree.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getSigla());
			result = (List) query.execute();
			assertEquals(result.isEmpty(), true);
	
	
			// Check existing CurricularCourse
			odmg = OJB.getInstance();

			///////////////////////////////////////////////////////////////////
			// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
			///////////////////////////////////////////////////////////////////
			db = odmg.newDatabase();

			try {
				db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
			} catch (ODMGException e) {
				e.printStackTrace();
			}
			///////////////////////////////////////////////////////////////////
			// End of Added Code
			///////////////////////////////////////////////////////////////////
			
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where degreeCurricularPlan.name = $1 ";
			oqlQuery += " and degreeCurricularPlan.degree.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getSigla());
			result = (List) query.execute();
			assertEquals(result.isEmpty(), true);
	
			} catch (QueryException ex) {
			  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}
		
	}
		
	
	public void testApagarPlanoCurricularInexistente() {
		System.out.println("testApagarPlanoCurricular");


		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = null;

		Implementation odmg = null;
		OQLQuery query = null;
		String oqlQuery = null;
		List result = null;


		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}

		degreeCurricularPlan = new DegreeCurricularPlan("inex", degree);

		try {
			persistentSupport.iniciarTransaccao();

			try {
	
			// Check existing Execution Degree
			odmg = OJB.getInstance();

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
			
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CursoExecucao.class.getName();
			query.create(oqlQuery);
			result = (List) query.execute();
			assertEquals(4, result.size());
	
	
			// Check existing CurricularCourse
			odmg = OJB.getInstance();

			///////////////////////////////////////////////////////////////////
			// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
			///////////////////////////////////////////////////////////////////
			db = odmg.newDatabase();

			try {
				db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
			} catch (ODMGException e) {
				e.printStackTrace();
			}
			///////////////////////////////////////////////////////////////////
			// End of Added Code
			///////////////////////////////////////////////////////////////////
			
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			query.create(oqlQuery);
			result = (List) query.execute();
			assertEquals(13, result.size());
	
			} catch (QueryException ex) {
			  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}

		// Delete

		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeCurricularPlan.deleteDegreeCurricularPlan(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}
		
		try {
			persistentSupport.iniciarTransaccao();

			try {
	
			// Check existing Execution Degree
			odmg = OJB.getInstance();

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


			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CursoExecucao.class.getName();
			query.create(oqlQuery);
			result = (List) query.execute();
			assertEquals(4, result.size());
	
	
			// Check existing CurricularCourse
			odmg = OJB.getInstance();

			///////////////////////////////////////////////////////////////////
			// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
			///////////////////////////////////////////////////////////////////
			db = odmg.newDatabase();

			try {
				db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
			} catch (ODMGException e) {
				e.printStackTrace();
			}
			///////////////////////////////////////////////////////////////////
			// End of Added Code
			///////////////////////////////////////////////////////////////////


			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			query.create(oqlQuery);
			result = (List) query.execute();
			assertEquals(13, result.size());
	
			} catch (QueryException ex) {
			  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}
		
	}
	

    
    
    public void testApagarTodosOsPlanosCurriculares() {
        System.out.println("testApagarTodosOsPlanosCurriculares");

		List degreeCurricularPlanList = null;
	
		// Check Database		
		try { 
			persistentSupport.iniciarTransaccao();
			degreeCurricularPlanList = persistentDegreeCurricularPlan.readAll();
			assertNotNull(degreeCurricularPlanList);
			assertEquals(degreeCurricularPlanList.isEmpty(), false);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testApagarTodosOsPlanosCurriculares: confirmarTransaccao_1");
		}

        try { 
            persistentSupport.iniciarTransaccao();
            persistentDegreeCurricularPlan.deleteAll();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarTodosOsPlanosCurriculares: confirmarTransaccao_1");
        }
		
		// Check Database Again
		try { 
			persistentSupport.iniciarTransaccao();
			degreeCurricularPlanList = null;
			degreeCurricularPlanList = persistentDegreeCurricularPlan.readAll();
			assertNotNull(degreeCurricularPlanList);
			assertEquals(degreeCurricularPlanList.isEmpty(), true);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testApagarTodosOsPlanosCurriculares: confirmarTransaccao_1");
		}
    }
// -------------------------------------------------------------------------------------------------------------------------------------------


    public void testLerTodosOsPlanosCurriculares() {
        System.out.println("testLerTodosOsPlanosCurriculares");
		ArrayList listap = null;

        try {
            persistentSupport.iniciarTransaccao();
            listap = persistentDegreeCurricularPlan.readAll();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerTodosOsPlanosCurriculares: confirmarTransaccao_1");
        }
        assertNotNull(listap);
        assertEquals(4,listap.size());
    }
}