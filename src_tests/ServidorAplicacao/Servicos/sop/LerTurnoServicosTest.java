/*
 * LerTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 17:19
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
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

public class LerTurnoServicosTest extends TestCaseServicosWithAuthorization {
	private InfoExecutionCourse infoExecutionCourse = null;


	public LerTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LerTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "LerTurno";
	}


	public void testReadExistingTurno() {
	
		this.ligarSuportePersistente(true);

		Object argsLerTurno[] = new Object[1];
		argsLerTurno[0] = new ShiftKey("turno1", this.infoExecutionCourse);

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerTurno);
			InfoShift infoShift = (InfoShift) result;
			assertEquals(infoShift.getNome(), "turno1");

			
		} catch (Exception ex) {
			fail("testReadExistingTurno" + ex);
		}
	}

	public void testReadUnexistingTurno() {

		this.ligarSuportePersistente(false);

		Object argsLerTurno[] = new Object[1];
		argsLerTurno[0] = new ShiftKey("shift", this.infoExecutionCourse);

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerTurno);
			assertNull("testReadUnexistingTurno", result);
		} catch (Exception ex) {
			fail("testReadExistingTurno");
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
