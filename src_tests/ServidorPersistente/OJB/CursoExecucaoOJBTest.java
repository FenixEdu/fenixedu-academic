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
  public void testReadByCursoAndAnoLectivo() {
    ICursoExecucao cursoExecucao = null;
    // read existing CursoExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      cursoExecucao = cursoExecucaoPersistente.readByCursoAndAnoLectivo(curso1,"2002/03");
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByCursoAndAnoLectivo:fail read existing cursoExecucao");
    }
    assertEquals("testReadByCursoAndAnoLectivo:read existing cursoExecucao",cursoExecucao,cursoExecucao1);
        
    // read unexisting CursoExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      cursoExecucao = cursoExecucaoPersistente.readByCursoAndAnoLectivo(curso1,"5000");
      assertNull(cursoExecucao);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByCursoAndAnoLectivo:fail read unexisting cursoExecucao");
    }
  }

  // write new non-existing cursoExecucao
  public void testCreateNonExistingCursoExecucao() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ICurso curso = cursoPersistente.readBySigla("LEIC");
    	_suportePersistente.confirmarTransaccao();

    	ICursoExecucao cursoExecucao = new CursoExecucao("2004/05", curso);

      _suportePersistente.iniciarTransaccao();
      cursoExecucaoPersistente.lockWrite(cursoExecucao);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingCursoExecucao"); 
    }
  }

  // write new existing cursoExecucao
  public void testCreateExistingCursoExecucao() {
    ICursoExecucao cursoExecucao = new CursoExecucao("2002/03", curso1);
    try {
      _suportePersistente.iniciarTransaccao();
      cursoExecucaoPersistente.lockWrite(cursoExecucao);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingCursoExecucao");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.ItemOJB. */
  public void testWriteExistingUnchangedObject() {
    // write cursoExecucao already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ICursoExecucao le = cursoExecucaoPersistente.readByCursoAndAnoLectivo(curso1,"2002/03");
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      cursoExecucaoPersistente.lockWrite(le);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.ItemOJB. */
  public void testWriteExistingChangedObject() {
    // write item already mapped into memory
    try {
      _suportePersistente.iniciarTransaccao();
      ICursoExecucao le = cursoExecucaoPersistente.readByCursoAndAnoLectivo(curso1,"2002/03");
      le.setAnoLectivo("10000");
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      le = cursoExecucaoPersistente.readByCursoAndAnoLectivo(curso1,"10000");
      _suportePersistente.confirmarTransaccao();
      assertEquals(le.getAnoLectivo(),"10000");
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.CursoExecucaoOJB. */
  public void testDeleteCursoExecucao() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ICursoExecucao le = cursoExecucaoPersistente.readByCursoAndAnoLectivo(curso1,"2002/03");
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      cursoExecucaoPersistente.delete(le);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      ICursoExecucao cursoExecucao = cursoExecucaoPersistente.readByCursoAndAnoLectivo(cursoExecucao1.getCurso(),cursoExecucao1.getAnoLectivo());
      _suportePersistente.confirmarTransaccao();
      assertEquals(cursoExecucao, null);
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