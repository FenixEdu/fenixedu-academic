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
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.Curso;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;



/**
 *
 * @author rpfi
 */
public class CursoOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	ICursoPersistente persistentDegree = null;
    
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
	try {
		persistentSupport = SuportePersistenteOJB.getInstance();
	} catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		fail("Error");
	}
	persistentDegree = persistentSupport.getICursoPersistente();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readBySigla method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testReadBySigla() {
    ICurso degree = null;
    // read existing Curso
    try {
      persistentSupport.iniciarTransaccao();
      degree = persistentDegree.readBySigla("LEIC");
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySigla:fail read existing curso");
    }
    assertEquals("testReadBySigla:read existing curso",degree.getSigla(),"LEIC");
        
    // read unexisting Curso
    try {
      persistentSupport.iniciarTransaccao();
      degree = persistentDegree.readBySigla("NÃO EXISTE");
      assertNull(degree);
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySigla:fail read unexisting Curso");
    }
  }


  // write new non-existing curso
  public void testCreateNonExistingCurso() {
    ICurso degree = new Curso("Primeira Sigla","Primeiro Nome", new TipoCurso(TipoCurso.MESTRADO));
    try {
      persistentSupport.iniciarTransaccao();
      persistentDegree.lockWrite(degree);
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingCurso"); 
    }
  }

  // write new existing Curso
  public void testCreateExistingCurso() {
    ICurso degree = new Curso("LEIC","NÃO VAI INSERIR", new TipoCurso(TipoCurso.LICENCIATURA));
    try {
      persistentSupport.iniciarTransaccao();
      persistentDegree.lockWrite(degree);
      persistentSupport.confirmarTransaccao();
      fail("testCreateExistingCurso");
    } catch (ExistingPersistentException ex) {
      //all is ok
  	} catch (ExcepcaoPersistencia ex) {
		fail("testCreateExistingCurso: unexpected exception");
  	}
  }


  /** Test of write method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testWriteExistingUnchangedObject() {
    // write curso already mapped into memory
    try {
    	persistentSupport.iniciarTransaccao();
    	ICurso degree = persistentDegree.readBySigla("LEIC");
    	persistentSupport.confirmarTransaccao();

        persistentSupport.iniciarTransaccao();
        persistentDegree.lockWrite(degree);
        persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
		fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testWriteExistingChangedObject() {
	ICurso degree = null;
	// read existing Curso
	try {
	  persistentSupport.iniciarTransaccao();
	  degree = persistentDegree.readBySigla("LEIC");
	  degree.setSigla("DESC");
	  persistentSupport.confirmarTransaccao();

	  // Check Insert
	  persistentSupport.iniciarTransaccao();
	  degree = persistentDegree.readBySigla("LEIC");
	  assertNull(degree);
	  degree = persistentDegree.readBySigla("DESC");
	  assertNotNull(degree);
	  assertEquals(degree.getNome(), "Licenciatura de Engenharia Informatica e de Computadores");
	  assertEquals(degree.getSigla(), "DESC");
	  
	  persistentSupport.confirmarTransaccao();


	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadBySigla:fail read existing curso");
	}
  }

  /** Test of delete method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testDeleteCurso() {
    try {
    	persistentSupport.iniciarTransaccao();
    	ICurso cursoTemp = persistentDegree.readBySigla("LEIC");
        persistentDegree.delete(cursoTemp);
        persistentSupport.confirmarTransaccao();

        persistentSupport.iniciarTransaccao();
        ICurso cursoTemp2 = persistentDegree.readBySigla("LEIC");
        persistentSupport.confirmarTransaccao();

      assertEquals(cursoTemp2, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteItem");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.CursoOJB. */
  public void testDeleteAll() {
    try {
      persistentSupport.iniciarTransaccao();
      persistentDegree.deleteAll();
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

        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select curso from " + Curso.class.getName();
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

  public void testReadAll() {
    try {
      List degrees = null;
      
      persistentSupport.iniciarTransaccao();
      degrees = persistentDegree.readAll();
      persistentSupport.confirmarTransaccao();
      assertEquals(4, degrees.size());

    } catch (ExcepcaoPersistencia ex) {
      fail("testReadAll");
    }
  }  
   
}
