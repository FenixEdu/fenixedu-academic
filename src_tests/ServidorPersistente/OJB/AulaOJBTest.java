/*
 * AulaOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;


/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.*;
import java.util.List;
import org.apache.ojb.odmg.OJB;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import org.odmg.Implementation;
import ServidorPersistente.*;
import Dominio.*;
import Util.*;

public class AulaOJBTest extends TestCaseOJB {
  public AulaOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AulaOJBTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readByDiaSemanaAndInicioAndFimAndSala method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testreadByDiaSemanaAndInicioAndFimAndSala() {
    IAula aula = null;
    // read existing Aula
    try {
    	_suportePersistente.iniciarTransaccao();
    	aula = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
    	_suportePersistente.confirmarTransaccao();
    	assertEquals("testReadByDiaSemanaAndInicioAndFimAndSala:read existing aula", _aula1, aula);
    } catch (ExcepcaoPersistencia ex) {
    	fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read existing aula");
    }
        
    // read unexisting Aula
    try {
      _suportePersistente.iniciarTransaccao();
      aula = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana2, _inicio, _fim, _sala2);
      _suportePersistente.confirmarTransaccao();
      assertNull("testReadByDiaSemanaAndInicioAndFimAndSala:fail read unexisting aula", aula);
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDiaSemanaAndInicioAndFimAndSala:fail read unexisting aula");
    }
  }

  // write new existing aula
  public void testCreateExistingAula() {
    //IAula aula = new Aula(_diaSemana1, _inicio, _fim, _tipoAula, _sala1, _disciplinaExecucao1);
    try {
      _suportePersistente.iniciarTransaccao();
      _aulaPersistente.lockWrite(_aula1);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingAula");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

  // write new non-existing aula
  public void testCreateNonExistingAula() {
    IAula aula = null;
    try {
      _suportePersistente.iniciarTransaccao();
      ISala sala = _salaPersistente.readByNome(_sala2.getNome());
      IDisciplinaExecucao de =
        _disciplinaExecucaoPersistente
      	  .readBySiglaAndAnoLectivAndSiglaLicenciatura(
      	  _disciplinaExecucao1.getSigla(),
      	  _disciplinaExecucao1.getLicenciaturaExecucao().getAnoLectivo(),
      	  _disciplinaExecucao1.getLicenciaturaExecucao().getCurso().getSigla());
      aula = new Aula(_diaSemana2, _inicio, _fim, _tipoAula, sala, de);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      _aulaPersistente.lockWrite(aula);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingAula");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testWriteExistingUnchangedObject() {
    // write item already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	IAula aula = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
    	_suportePersistente.confirmarTransaccao();
    	    	
		_suportePersistente.iniciarTransaccao();
      	_aulaPersistente.lockWrite(aula);
      	_suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testWriteExistingChangedObject() {
    IAula aula1 = null;
    IAula aula2 = null;
      
    // write aula already mapped into memory
    try {
      _suportePersistente.iniciarTransaccao();
      aula1 = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
      aula1.setTipo(new TipoAula(TipoAula.PRATICA));
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      aula2 = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
      _suportePersistente.confirmarTransaccao();

      assertTrue(aula2.getTipo().equals(new TipoAula(TipoAula.PRATICA)));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testDeleteAula() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	IAula aula = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      _aulaPersistente.delete(aula);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      IAula aula2 = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
      _suportePersistente.confirmarTransaccao();

      assertNull("testDeleteAula", aula2);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAula");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      _aulaPersistente.deleteAll();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();

      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select aula from " + Aula.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAula");
    }

  }

  /** Test of readByDisciplinaExecucao method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testreadByDisciplinaExecucao() {
    List aulas = null;
    // read existing disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      aulas = _aulaPersistente.readByDisciplinaExecucao(_disciplinaExecucao1);
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadByDisciplinaExecucao: Existing", 5, aulas.size());
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucao:fail read existing disciplinaExecucao");
    }
      
    // read unexisting disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      aulas = _aulaPersistente.readByDisciplinaExecucao(_disciplinaExecucao2);
      assertTrue("testReadByDisciplinaExecucao: Unexisting", aulas.isEmpty());
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucao:fail read unexisting disciplinaExecucao");
    }
  }

  /** Test of readByDisciplinaExecucaoETipo method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testreadByDisciplinaExecucaoETipo() {
    List aulas = null;
    // read existing disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      aulas = _aulaPersistente.readByDisciplinaExecucaoETipo(_disciplinaExecucao1.getSigla(), _tipoAula);
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadByDisciplinaExecucaoETipo: Existing", 5, aulas.size());
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucaoETipo:fail read existing disciplinaExecucao");
    }

    // read unexisting disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      aulas = _aulaPersistente.readByDisciplinaExecucaoETipo(_disciplinaExecucao2.getSigla(), _tipoAula);
      assertTrue("testReadByDisciplinaExecucaoETipo: Unexisting", aulas.isEmpty());
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByDisciplinaExecucaoETipo:fail read unexisting disciplinaExecucao");
    }
  }

  /** Test of readBySalaEmSemestre method, of class ServidorPersistente.OJB.AulaOJB. */
  public void testreadBySalaEmSemestre() {
    List aulas = null;
    // read existing disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      aulas = _aulaPersistente.readBySalaEmSemestre(_sala1.getNome(), new Integer(1));
      _suportePersistente.confirmarTransaccao();
      assertEquals("testReadBySalaEmSemestre: Existing", 21, aulas.size());
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySalaEmSemestre:fail read existing aulas em sala");
    }

    // read unexisting disciplinaExecucao
    try {
      _suportePersistente.iniciarTransaccao();
      aulas = _aulaPersistente.readBySalaEmSemestre(_sala3.getNome(), new Integer(1));
      assertTrue("testReadBySalaEmSemestre: Unexisting", aulas.isEmpty());
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySalaEmSemestre:fail read unexisting aulas em sala");
    }
  }

}
