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
    ICurso curso = null;
    // read existing Curso
    try {
      _suportePersistente.iniciarTransaccao();
      curso = cursoPersistente.readBySigla("LEIC");
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySigla:fail read existing curso");
    }
    assertEquals("testReadBySigla:read existing curso",curso,curso1);
        
    // read unexisting Curso
    try {
      _suportePersistente.iniciarTransaccao();
      curso = cursoPersistente.readBySigla("NÃO EXISTE");
      assertNull(curso);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySigla:fail read unexisting Curso");
    }
  }


  // write new non-existing curso
  public void testCreateNonExistingCurso() {
    ICurso curso = new Curso("Primeira Sigla","Primeiro Nome", new TipoCurso(TipoCurso.DOUTORAMENTO));
    try {
      _suportePersistente.iniciarTransaccao();
      cursoPersistente.lockWrite(curso);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingCurso"); 
    }
  }

  // write new existing Curso
  public void testCreateExistingCurso() {
    ICurso curso = new Curso("LEIC","NÃO VAI INSERIR", new TipoCurso(TipoCurso.LICENCIATURA));
    try {
      _suportePersistente.iniciarTransaccao();
      cursoPersistente.lockWrite(curso);
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
    	ICurso curso = cursoPersistente.readBySigla("LEIC");
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      cursoPersistente.lockWrite(curso);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testWriteExistingChangedObject() {
    // write item already mapped into memory
    try {
      _suportePersistente.iniciarTransaccao();
      cursoPersistente.lockWrite(curso2);
      curso2.setSigla("SIGLA NOVA");
      curso2.setNome("NOME NOVO");
      curso2.setTipoCurso(new TipoCurso(TipoCurso.ESPECIALIZACAO));
      
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      ICurso cursoTemp = cursoPersistente.readBySigla(curso2.getSigla());
      _suportePersistente.confirmarTransaccao();
      assertEquals(cursoTemp.getSigla(),"SIGLA NOVA");
      assertEquals(cursoTemp.getNome(),"NOME NOVO");
      assertTrue(cursoTemp.getTipoCurso().getTipoCurso().equals(new Integer(TipoCurso.ESPECIALIZACAO)));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testDeleteCurso() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ICurso cursoTemp = cursoPersistente.readBySigla("LEIC");
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      cursoPersistente.delete(cursoTemp);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      ICurso cursoTemp2 = cursoPersistente.readBySigla(curso1.getSigla());
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
      List cursos = null;
        /* Testa metodo qdo nao ha salas na BD */
      _suportePersistente.iniciarTransaccao();
      cursoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();
      
      _suportePersistente.iniciarTransaccao();
      cursos = cursoPersistente.readAll();
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadAll: qdo nao ha curso na BD", cursos.size(),0);

      /* Testa metodo qdo nao mais do q uma curso na BD */ 
      _suportePersistente.iniciarTransaccao();
      cursoPersistente.lockWrite(curso1);
      cursoPersistente.lockWrite(curso2); 
      _suportePersistente.confirmarTransaccao();
      _suportePersistente.iniciarTransaccao();
      cursos = cursoPersistente.readAll();
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadAll: qdo ha dois cursos na BD", cursos.size(),2);
      assertTrue("testReadAll: qdo ha dois cursos na BD", cursos.contains(curso1));
      assertTrue("testReadAll: qdo ha dois cursos na BD", cursos.contains(curso2));

    } catch (ExcepcaoPersistencia ex) {
      fail("testReadAll");
    }
  }  
   
}
