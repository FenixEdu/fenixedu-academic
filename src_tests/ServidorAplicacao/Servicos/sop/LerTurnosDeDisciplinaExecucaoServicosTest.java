/*
 * LerTurnosDeDisciplinaExecucaoServicosTest.java
 * JUnit based test
 *
 * Created on 01 de Dezembro de 2002, 18:31
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurnosDeDisciplinaExecucaoServicosTest extends TestCaseServicosWithAuthorization {

	private InfoExecutionCourse infoExecutionCourse = null;

    public LerTurnosDeDisciplinaExecucaoServicosTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(LerTurnosDeDisciplinaExecucaoServicosTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
  
  
  protected String getNameOfServiceToBeTested() {
	  return "LerTurnosDeDisciplinaExecucao";
  }

  

  public void testReadExistingTurnos() {

	this.ligarSuportePersistente(true);
		
	Object argsLerTurnos[] = {this.infoExecutionCourse} ;

    Object result = null; 
      try {
        result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerTurnos);
        assertEquals("testLerExistingTurnos", 10, ((List) result).size());
      } catch (Exception ex) {
      	fail("testLerExistingTurnos");
      }
  }

  // read new non-existing turnos
  public void testReadNonExistingTurnos() {
	this.ligarSuportePersistente(false);
		
	Object argsLerTurnos[] = {this.infoExecutionCourse} ;

	Object result = null; 
	  try {
		result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerTurnos);
		assertTrue("testLerExistingTurnos", ((List) result).isEmpty());
	  } catch (Exception ex) {
		fail("testLerExistingTurnos");
	  }
  }


  private void ligarSuportePersistente(boolean existing) {

	  ISuportePersistente sp = null;

	  try {
		  sp = SuportePersistenteOJB.getInstance();
		  sp.iniciarTransaccao();


		  IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
		  IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		  assertNotNull(executionYear);
		  
		  IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
		  IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
		  assertNotNull(executionPeriod);
		  
		  
		  IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
		  IDisciplinaExecucao executionCourse = null;
			

		  if(existing) {
			executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
			assertNotNull(executionCourse);
		  } else {
			executionCourse = new DisciplinaExecucao("desc", "desc", "desc", new Double(1), new Double(2), new Double(3), new Double(4), executionPeriod);
		  }
			
		  this.infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);

		  sp.confirmarTransaccao();

	  } catch (ExcepcaoPersistencia excepcao) {
		  try {
			  sp.cancelarTransaccao();
		  } catch (ExcepcaoPersistencia ex) {
			  fail("ligarSuportePersistente: cancelarTransaccao");
		  }
		  fail("ligarSuportePersistente: confirmarTransaccao");
	  }
  }



    
}
