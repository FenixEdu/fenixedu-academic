/*
 * TurnoOJBTest.java
 * JUnit based test
 *
 * Created on 15 de Outubro de 2002, 8:59
 */

package ServidorPersistente.OJB;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Implementation;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoAula;

public class TurnoOJBTest extends TestCaseOJB {
    public TurnoOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(TurnoOJBTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testReadByNome() {
    ITurno turno = null;
    // read existing Turno
    try {
      _suportePersistente.iniciarTransaccao();
      turno = _turnoPersistente.readByNome(_turno1.getNome());
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNome:fail read existing turno");
    }
    assertEquals("testReadByNome:read existing turno",turno,_turno1);
        
    // read unexisting Turno
    try {
      _suportePersistente.iniciarTransaccao();
      turno = _turnoPersistente.readByNome("turnoInexistente");
      assertNull(turno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNome:fail read unexisting turno");
    }
  }

  // write new existing turno
  public void testCreateExistingTurno() {
      
    ITurno turno = new Turno("turno1", new TipoAula(TipoAula.TEORICA), new Integer(100), _disciplinaExecucao1);
    try {
      _suportePersistente.iniciarTransaccao();
      _turnoPersistente.lockWrite(turno);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingTurno");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

  // write new non-existing turno
  public void testCreateNonExistingTur() {
    ITurno turno = null;
    try {
    	_suportePersistente.iniciarTransaccao();
    	IDisciplinaExecucao de =
    	  _disciplinaExecucaoPersistente
    		.readBySiglaAndAnoLectivAndSiglaLicenciatura(
    		_disciplinaExecucao1.getSigla(),
    		_disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
    		_disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getSigla());
    	_suportePersistente.confirmarTransaccao();

    	turno = new Turno("turnoInexistente", new TipoAula(TipoAula.TEORICA),
    	                  new Integer(100), de);

      _suportePersistente.iniciarTransaccao();
      _turnoPersistente.lockWrite(turno);
      _suportePersistente.confirmarTransaccao();
      //      assertTrue(((Item)item).getCodigoInterno() != 0);
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingTurno");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testWriteExistingUnchangedObject() {
    // write turno already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurno turno = _turnoPersistente.readByNome(_turno1.getNome());
    	_suportePersistente.confirmarTransaccao();
    	
      _suportePersistente.iniciarTransaccao();
      _turnoPersistente.lockWrite(turno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testWriteExistingChangedObject() {
      ITurno turno1 = null;
      ITurno turno2 = null;
    // write turno already mapped into memory
    try {
      _suportePersistente.iniciarTransaccao();
      //_turmaPersistente.lockWrite(_turno1);
      turno1 = _turnoPersistente.readByNome(_turno1.getNome());
      turno1.setLotacao(new Integer(50));
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turno2 = _turnoPersistente.readByNome(turno1.getNome());
      _suportePersistente.confirmarTransaccao();
      
      assertEquals(turno2.getLotacao(), new Integer(50));
      //assertTrue(turno.getOrdem() == 5);
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testDeleteTurno() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	ITurno turno = _turnoPersistente.readByNome(_turno1.getNome());
    	_suportePersistente.confirmarTransaccao();    	
    	
      _suportePersistente.iniciarTransaccao();
      _turnoPersistente.delete(turno);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      turno = _turnoPersistente.readByNome(_turno1.getNome());
      _suportePersistente.confirmarTransaccao();

      assertEquals(turno, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurno");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _turnoPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select turno from " + Turno.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteTurno");
    }

  }
    

  /** Test of querie2 method, of class ServidorPersistente.OJB.TurnoOJB. */

   public void testQuerie2() {
    try {
      _suportePersistente.iniciarTransaccao();
      Integer result = _turnoPersistente.querie2("turno453");
      _suportePersistente.confirmarTransaccao();
      assertEquals(new Integer(3), result);
    } catch (ExcepcaoPersistencia ex) {
      fail("testQueire2");
    }

  }  

  /** Test of readByDisciplinaExecucao method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testreadByDisciplinaExecucao() {
    List turnos = null;
    // read existing disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      turnos = _turnoPersistente.readByDisciplinaExecucao(_disciplinaExecucao1.getSigla(),
                                                          _disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
                                                          _disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getSigla());
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadByDisciplinaExecucao: Existing", turnos.size(), 11);
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucao:fail read existing disciplinaExecucao");
    }
      
    // read unexisting disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      turnos = _turnoPersistente.readByDisciplinaExecucao(_disciplinaExecucao2.getSigla(),
                                                          _disciplinaExecucao2.getLicenciaturaExecucao().getAnoLectivo(),
                                                          _disciplinaExecucao2.getLicenciaturaExecucao().getCurso().getSigla());
      assertTrue("testReadByDisciplinaExecucao: Unexisting", turnos.isEmpty());
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucao:fail read unexisting disciplinaExecucao");
    }
  }

/** Test of readByDisciplinaExecucaoAndType method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testreadByDisciplinaExecucaoAndType() {
    List turnos = null;
    // read existing disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      turnos = _turnoPersistente.readByDisciplinaExecucaoAndType(_disciplinaExecucao1.getSigla(),
                                                          _disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
                                                          _disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getSigla(),
                                                          _turno1.getTipo().getTipo());
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadByDisciplinaExecucaoAndType: Existing", turnos.size(), 4);
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucaoAndType:fail read existing disciplinaExecucao");
    }
      
    // read unexisting disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      turnos = _turnoPersistente.readByDisciplinaExecucaoAndType(_disciplinaExecucao2.getSigla(),
                                                          _disciplinaExecucao2.getLicenciaturaExecucao().getAnoLectivo(),
                                                          _disciplinaExecucao2.getLicenciaturaExecucao().getCurso().getSigla(),
                                                          _turno1.getTipo().getTipo());
      assertTrue("testReadByDisciplinaExecucaoAndType: Unexisting", turnos.isEmpty());
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucaoAndType:fail read unexisting disciplinaExecucao");
    }
  }
  
}
