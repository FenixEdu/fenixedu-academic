/*
 * CursoExecucaoOJBTest.java
 *
 * Created on 2 de Novembro de 2002, 22:46
 */

package ServidorPersistente.OJB;

import junit.framework.*;
import java.util.List;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import ServidorPersistente.*;
import Dominio.*;
import org.odmg.Implementation;
import org.apache.ojb.odmg.OJB;


/**
 *
 * @author rpfi
 */
public class CursoExecucaoOJBTest extends TestCaseOJB {
    
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
      
      executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
      assertNotNull(executionYear);
      executionDegrees = persistentExecutionDegree.readByExecutionYear(executionYear);
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByCursoAndAnoLectivo:fail read existing cursoExecucao");
    }
    assertEquals("testReadByCursoAndAnoLectivo:read existing cursoExecucao",1 ,executionDegrees.size());
  }

  // write new non-existing cursoExecucao
  public void testCreateNonExistingCursoExecucao() {
    try {
    	persistentSupport.iniciarTransaccao();
    	ICurso degree = persistentDegree.readBySigla("LEEC");
    	assertNotNull(degree);
		IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano2", degree);
		assertNotNull(degreeCurricularPlan);
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
    	persistentSupport.confirmarTransaccao();

    	ICursoExecucao executionDegree = new CursoExecucao(executionYear, degreeCurricularPlan);

        persistentSupport.iniciarTransaccao();
        persistentExecutionDegree.lockWrite(executionDegree);
        persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingCursoExecucao"); 
    }
  }

  // write new existing cursoExecucao
  public void testCreateExistingCursoExecucao() {
	try {
		persistentSupport.iniciarTransaccao();
		ICurso degree = persistentDegree.readBySigla("LEIC");
		assertNotNull(degree);

		IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		persistentSupport.confirmarTransaccao();

		ICursoExecucao executionDegree = new CursoExecucao(executionYear, degreeCurricularPlan);

		persistentSupport.iniciarTransaccao();
		persistentExecutionDegree.lockWrite(executionDegree);
		persistentSupport.confirmarTransaccao();
		fail("testCreateNonExistingCursoExecucao");	
	} catch (ExcepcaoPersistencia ex) {
	   // All is OK
	}
  }

  /** Test of write method, of class ServidorPersistente.OJB.ItemOJB. */
  public void testWriteExistingChangedObject() {
	IPlanoCurricularCurso degreeCurricularPlan1 = null;
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

		persistentSupport.confirmarTransaccao();

		persistentSupport.iniciarTransaccao();
		ICursoExecucao executionDegreeTemp = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan1, executionYear2);		
		persistentSupport.confirmarTransaccao();

		assertEquals(executionDegreeTemp, executionDegree);

	} catch (ExcepcaoPersistencia ex) {
		fail("testWriteExistingChangedObject");
	}		

  }

  /** Test of delete method, of class ServidorPersistente.OJB.CursoExecucaoOJB. */
  public void testDeleteCursoExecucao() {
	IPlanoCurricularCurso degreeCurricularPlan = null;
	IExecutionYear executionYear = null;
	ICursoExecucao executionDegree = null;
  	
    try {
		persistentSupport.iniciarTransaccao();
		ICurso degree = persistentDegree.readBySigla("LEIC");
		assertNotNull(degree);

		degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
		executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);		
    	persistentSupport.confirmarTransaccao();

      persistentSupport.iniciarTransaccao();
	  assertEquals(SuportePersistenteOJB.getInstance().getITurmaPersistente().readByExecutionDegree(executionDegree).isEmpty(), false);

      persistentExecutionDegree.delete(executionDegree);
      persistentSupport.confirmarTransaccao();

      persistentSupport.iniciarTransaccao();
      ICursoExecucao executionDegreeTemp = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNull(executionDegreeTemp);
	  assertEquals(SuportePersistenteOJB.getInstance().getITurmaPersistente().readByExecutionDegree(executionDegree).size(), 0);

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
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select all from " + CursoExecucao.class.getName();
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

}