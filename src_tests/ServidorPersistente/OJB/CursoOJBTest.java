/*
 * CursoOJBTest.java
 * JUnit based test
 *
 * Created on 2 de Novembro de 2002, 19:32
 */

package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Implementation;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.Curso;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoCurso;



/**
 *
 * @author rpfi
 */
public class CursoOJBTest extends TestCaseOJB {
    
    public CursoOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(CursoOJBTest.class);
        
        return suite;
    }    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readBySigla method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testReadBySigla() {
    ICurso degree = null;
    // read existing Curso
    try {
      _suportePersistente.iniciarTransaccao();
      degree = cursoPersistente.readBySigla("LEIC");
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySigla:fail read existing curso");
    }
    assertEquals("testReadBySigla:read existing curso",degree.getSigla(),"LEIC");
        
    // read unexisting Curso
    try {
      _suportePersistente.iniciarTransaccao();
      degree = cursoPersistente.readBySigla("NÃO EXISTE");
      assertNull(degree);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySigla:fail read unexisting Curso");
    }
  }


  // write new non-existing curso
  public void testCreateNonExistingCurso() {
    ICurso degree = new Curso("Primeira Sigla","Primeiro Nome", new TipoCurso(TipoCurso.DOUTORAMENTO));
    try {
      _suportePersistente.iniciarTransaccao();
      cursoPersistente.lockWrite(degree);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingCurso"); 
    }
  }

  // write new existing Curso
  public void testCreateExistingCurso() {
    ICurso degree = new Curso("LEIC","NÃO VAI INSERIR", new TipoCurso(TipoCurso.LICENCIATURA));
    try {
      _suportePersistente.iniciarTransaccao();
      cursoPersistente.lockWrite(degree);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingCurso");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }


  /** Test of write method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testWriteExistingUnchangedObject() {
    // write curso already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ICurso degree = cursoPersistente.readBySigla("LEIC");
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      cursoPersistente.lockWrite(degree);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testWriteExistingChangedObject() {
	ICurso degree = null;
	// read existing Curso
	try {
	  _suportePersistente.iniciarTransaccao();
	  degree = cursoPersistente.readBySigla("LEIC");
	  degree.setSigla("DESC");
	  _suportePersistente.confirmarTransaccao();

	  // Check Insert
	  _suportePersistente.iniciarTransaccao();
	  degree = cursoPersistente.readBySigla("LEIC");
	  assertNull(degree);
	  degree = cursoPersistente.readBySigla("DESC");
	  assertNotNull(degree);
	  assertEquals(degree.getNome(), "Licenciatura de Engenharia Informatica e de Computadores");
	  assertEquals(degree.getSigla(), "DESC");
	  
	  _suportePersistente.confirmarTransaccao();


	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadBySigla:fail read existing curso");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testDeleteCurso() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ICurso cursoTemp = cursoPersistente.readBySigla("LEIC");
        cursoPersistente.delete(cursoTemp);
        _suportePersistente.confirmarTransaccao();

        _suportePersistente.iniciarTransaccao();
        ICurso cursoTemp2 = cursoPersistente.readBySigla("LEIC");
        _suportePersistente.confirmarTransaccao();

      assertEquals(cursoTemp2, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteItem");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      cursoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      List result = null;
      try {
          
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select curso from " + Curso.class.getName();
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

  public void testReadAll() {
    try {
      List degrees = null;
      
      _suportePersistente.iniciarTransaccao();
      degrees = cursoPersistente.readAll();
      _suportePersistente.confirmarTransaccao();
      assertEquals(degrees.size(), 3);

    } catch (ExcepcaoPersistencia ex) {
      fail("testReadAll");
    }
  }  
   
}
