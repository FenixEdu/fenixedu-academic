/*
 * AdicionarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 19:45
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;

import DataBeans.InfoShiftServiceResult;
import DataBeans.RoomKey;
import DataBeans.ShiftAndLessonKeys;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class AdicionarAulaServicosTest extends TestCaseServicos {
    public AdicionarAulaServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AdicionarAulaServicosTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  // write turnoAula by unauthorized user
  public void testUnauthorizedCreateTurnoAula() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsCriarTurnoAula[] = new Object[1];
    argsCriarTurnoAula[0] = new ShiftAndLessonKeys("turno1", _diaSemana1, _inicio,
                                                 _fim, keySala);
    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "AdicionarAula", argsCriarTurnoAula);
        fail("testUnauthorizedCreateTurnoAula");
      } catch (Exception ex) {
        assertNull("testUnauthorizedCreateTurnoAula", result);
      }
  }

  // write new existing turnoAula
  public void testCreateExistingTurnoAula() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsCriarTurnoAula[] = new Object[1];
    argsCriarTurnoAula[0] = new ShiftAndLessonKeys("turno1", _diaSemana1, _inicio,
                                                 _fim, keySala);
    
    Object result = null; 
      try {
        result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula);
System.out.println("Resultado-1.1 = " + result);
        fail("testCreateExistingTurnoAula:");
      } catch (Exception ex) {
System.out.println("Resultado0-1.2 = " + result);
        assertNull("testCreateExistingTurnoAula", result);
      }
  }


  // write new non-existing turnoAula
  public void testCreateNonExistingTurnoAula_OK() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsCriarTurnoAula[] = new Object[1];
    argsCriarTurnoAula[0] = new ShiftAndLessonKeys("turno2", _diaSemana1, _inicio, _fim, keySala);

    Object result = null; 
      try {
        result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula);
System.out.println("Resultado0.1 = " + result);        
        assertTrue("testCreateNonExistingTurnoAula", ((InfoShiftServiceResult) result).isSUCESS());
      } catch (Exception ex) {
System.out.println("Resultado0.2 = " + result);              	
      	fail("testCreateNonExistingTurnoAula:");
      }
  }
  
  // carga-horaria maxima ja foi atingida...
  public void testCreateNonExistingTurnoAula_HoursLimitReached() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsCriarTurnoAula1[] = new Object[1];
    Object argsCriarTurnoAula2[] = new Object[1];    
    argsCriarTurnoAula1[0] = new ShiftAndLessonKeys("turno3",_diaSemana2, _inicio, _fim, keySala);
    argsCriarTurnoAula2[0] = new ShiftAndLessonKeys("turno3",_diaSemana3, _inicio, _fim2, keySala);
    
    Object result = null; 
      try {
      	_suportePersistente.iniciarTransaccao();
		_turnoPersistente.lockWrite(_turno3);
		_suportePersistente.confirmarTransaccao();
        result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula1);
System.out.println("Resultado 1.1 = " + result);        
		assertTrue(
			"testCreateNonExistingTurnoAula",
			((InfoShiftServiceResult) result).isSUCESS());

        result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula2);
System.out.println("Resultado 1.2 = " + result);        
		assertTrue(
			"testCreateNonExistingTurnoAula",
			((InfoShiftServiceResult) result).getMessageType() ==
				new InfoShiftServiceResult(
					InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED).getMessageType());

      } catch (Exception ex) {
      	fail("testCreateNonExistingTurnoAula_HoursLimitReached: " + ex);
      }
  }

  // carga-horaria maxima excedida...
  public void testCreateNonExistingTurnoAula_HourLimitExceeded() {
  	RoomKey keySala = new RoomKey(_sala1.getNome());
    Object argsCriarTurnoAula[] = new Object[1];
    argsCriarTurnoAula[0] = new ShiftAndLessonKeys("turno3", _diaSemana3, _inicio, _fim2, keySala);
    
    Object result = null; 
      try {
      	_suportePersistente.iniciarTransaccao();
		_turnoPersistente.lockWrite(_turno3);
		_suportePersistente.confirmarTransaccao();
      	
		result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula);
		assertTrue(
			"testCreateNonExistingTurnoAula",
			((InfoShiftServiceResult) result).getMessageType() ==
				new InfoShiftServiceResult(
					InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED).getMessageType());
      } catch (Exception ex) {
      	System.out.println(":( - " + ex);
      	fail("testCreateNonExistingTurnoAula:");
      }
  }

}
