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
      _suportePersistente.iniciarTransaccao();
      
      executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
      assertNotNull(executionYear);
      executionDegrees = cursoExecucaoPersistente.readByExecutionYear(executionYear);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByCursoAndAnoLectivo:fail read existing cursoExecucao");
    }
    assertEquals("testReadByCursoAndAnoLectivo:read existing cursoExecucao",1 ,executionDegrees.size());
  }

  // write new non-existing cursoExecucao
  public void testCreateNonExistingCursoExecucao() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ICurso degree = cursoPersistente.readBySigla("LEEC");
    	assertNotNull(degree);
		IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano2", degree);
		assertNotNull(degreeCurricularPlan);
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
    	_suportePersistente.confirmarTransaccao();

    	ICursoExecucao executionDegree = new CursoExecucao(executionYear, degreeCurricularPlan);

        _suportePersistente.iniciarTransaccao();
        cursoExecucaoPersistente.lockWrite(executionDegree);
        _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingCursoExecucao"); 
    }
  }

  // write new existing cursoExecucao
  public void testCreateExistingCursoExecucao() {
	try {
		_suportePersistente.iniciarTransaccao();
		ICurso degree = cursoPersistente.readBySigla("LEIC");
		assertNotNull(degree);

		IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		_suportePersistente.confirmarTransaccao();

		ICursoExecucao executionDegree = new CursoExecucao(executionYear, degreeCurricularPlan);

		_suportePersistente.iniciarTransaccao();
		cursoExecucaoPersistente.lockWrite(executionDegree);
		_suportePersistente.confirmarTransaccao();
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
		_suportePersistente.iniciarTransaccao();
		ICurso degree = cursoPersistente.readBySigla("LEIC");
		assertNotNull(degree);

		degreeCurricularPlan1 = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan1);
		
		executionYear1 = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear1);

		executionYear2 = persistentExecutionYear.readExecutionYearByName("2003/2004");
		assertNotNull(executionYear2);

		executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan1, executionYear1);
		executionDegree.setExecutionYear(executionYear2);

		_suportePersistente.confirmarTransaccao();

		_suportePersistente.iniciarTransaccao();
		ICursoExecucao executionDegreeTemp = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan1, executionYear2);		
		_suportePersistente.confirmarTransaccao();

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
		_suportePersistente.iniciarTransaccao();
		ICurso degree = cursoPersistente.readBySigla("LEIC");
		assertNotNull(degree);

		degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
		executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);		
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
	  assertEquals(SuportePersistenteOJB.getInstance().getITurmaPersistente().readByExecutionDegree(executionDegree).isEmpty(), false);

      cursoExecucaoPersistente.delete(executionDegree);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      ICursoExecucao executionDegreeTemp = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
	  assertNull(executionDegreeTemp);
	  assertEquals(SuportePersistenteOJB.getInstance().getITurmaPersistente().readByExecutionDegree(executionDegree).size(), 0);

      _suportePersistente.confirmarTransaccao();
      
      
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteCursoExecucao");
    }
  }


  /** Test of deleteAll method, of class ServidorPersistente.OJB.CursoExecucaoOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      cursoExecucaoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
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
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteItem");
    }
  }

}