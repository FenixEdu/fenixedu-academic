/*
 * PlanoCurricularCursoOJBTest.java
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
import org.odmg.Implementation;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.IPlanoCurricularCurso;
import Dominio.PlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;

public class PlanoCurricularCursoOJBTest extends TestCaseOJB {
    public PlanoCurricularCursoOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(PlanoCurricularCursoOJBTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }


	public void testLerPlanoCurricular() {
		System.out.println("testLerPlanoCurricular");        
		// Read existing
		IPlanoCurricularCurso degreeCurricularPlan = null;
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
		assertEquals(degreeCurricularPlan.getCurso(), degree);

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
        
        IPlanoCurricularCurso degreeCurricularPlan = new PlanoCurricularCurso("plano1", degree);
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentDegreeCurricularPlan.escreverPlanoCurricular(degreeCurricularPlan);
            persistentSupport.confirmarTransaccao();
            fail("testEscreverPlanoCurricular: confirmarTransaccao_1");
        } catch(ExcepcaoPersistencia ex2) {
			// All Is Ok
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
        degreeCurricularPlan = new PlanoCurricularCurso("planoDesc", degreeTemp);
        try {
            persistentSupport.iniciarTransaccao();
            persistentDegreeCurricularPlan.escreverPlanoCurricular(degreeCurricularPlan);
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testEscreverPlanoCurricular: confirmarTransaccao_2");
        }
        
        // Check insert
        IPlanoCurricularCurso degreeCurricularTemp2 = null;

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


		IPlanoCurricularCurso degreeCurricularPlan = null;
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
		String oqlQuery = null;;
		List result = null;

		try {
			persistentSupport.iniciarTransaccao();

			try {
	
			// Check existing Execution Degree
			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CursoExecucao.class.getName();
			oqlQuery += " where curricularPlan.name = $1 ";
			oqlQuery += " and curricularPlan.curso.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getCurso().getSigla());
			result = (List) query.execute();
			assertEquals(result.isEmpty(), false);
	
	
			// Check existing CurricularCourse
			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where degreeCurricularPlan.name = $1 ";
			oqlQuery += " and degreeCurricularPlan.curso.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getCurso().getSigla());
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
			persistentDegreeCurricularPlan.apagarPlanoCurricular(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex3) {
			fail("testApagarPlanoCurricular: confirmarTransaccao_1");
		}
		
		try {
			persistentSupport.iniciarTransaccao();

			try {
	
			// Check existing Execution Degree
			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CursoExecucao.class.getName();
			oqlQuery += " where curricularPlan.name = $1 ";
			oqlQuery += " and curricularPlan.curso.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getCurso().getSigla());
			result = (List) query.execute();
			assertEquals(result.isEmpty(), true);
	
	
			// Check existing CurricularCourse
			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where degreeCurricularPlan.name = $1 ";
			oqlQuery += " and degreeCurricularPlan.curso.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getCurso().getSigla());
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


		IPlanoCurricularCurso degreeCurricularPlan = null;
		ICurso degree = null;

		Implementation odmg = null;
		OQLQuery query = null;
		String oqlQuery = null;;
		List result = null;


		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}

		degreeCurricularPlan = new PlanoCurricularCurso("inex", degree);

		try {
			persistentSupport.iniciarTransaccao();

			try {
	
			// Check existing Execution Degree
			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CursoExecucao.class.getName();
			query.create(oqlQuery);
			result = (List) query.execute();
			assertEquals(result.size(), 2);
	
	
			// Check existing CurricularCourse
			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			query.create(oqlQuery);
			result = (List) query.execute();
			assertEquals(result.size(), 10);
	
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
			persistentDegreeCurricularPlan.apagarPlanoCurricular(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testLerPlanoCurricular: confirmarTransaccao_1");
		}
		
		try {
			persistentSupport.iniciarTransaccao();

			try {
	
			// Check existing Execution Degree
			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CursoExecucao.class.getName();
			query.create(oqlQuery);
			result = (List) query.execute();
			assertEquals(result.size(), 2);
	
	
			// Check existing CurricularCourse
			odmg = OJB.getInstance();
			query = odmg.newOQLQuery();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			query.create(oqlQuery);
			result = (List) query.execute();
			assertEquals(result.size(), 10);
	
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
			degreeCurricularPlanList = persistentDegreeCurricularPlan.lerTodosOsPlanosCurriculares();
			assertNotNull(degreeCurricularPlanList);
			assertEquals(degreeCurricularPlanList.isEmpty(), false);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex2) {
			fail("testApagarTodosOsPlanosCurriculares: confirmarTransaccao_1");
		}

        try { 
            persistentSupport.iniciarTransaccao();
            persistentDegreeCurricularPlan.apagarTodosOsPlanosCurriculares();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarTodosOsPlanosCurriculares: confirmarTransaccao_1");
        }
		
		// Check Database Again
		try { 
			persistentSupport.iniciarTransaccao();
			degreeCurricularPlanList = null;
			degreeCurricularPlanList = persistentDegreeCurricularPlan.lerTodosOsPlanosCurriculares();
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
            listap = persistentDegreeCurricularPlan.lerTodosOsPlanosCurriculares();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerTodosOsPlanosCurriculares: confirmarTransaccao_1");
        }
        assertNotNull(listap);
        assertEquals(listap.size(), 2);
    }
}