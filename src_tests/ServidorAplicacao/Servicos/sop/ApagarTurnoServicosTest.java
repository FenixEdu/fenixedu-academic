
/*
 * ApagarTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 18:28
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class ApagarTurnoServicosTest extends TestCaseServicosWithAuthorization {
	
	private InfoShift infoShift = null;

	public ApagarTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ApagarTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// unauthorized delete turno
	public void testUnauthorizedDeleteTurno() {

		super.testUnauthorizedExecutionOfService("ApagarTurno");

//		this.ligarSuportePersistente(true);
//
//		Object argsDeleteTurno[] = { new ShiftKey(this.infoShift.getNome(), this.infoShift.getInfoDisciplinaExecucao()) };
//
//		Object result = null;
//		try {
//			result = _gestor.executar(_userView2, "ApagarTurno", argsDeleteTurno);
//			fail("testUnauthorizedDeleteTurno");
//		} catch (Exception ex) {
//			assertNull("testUnauthorizedDeleteTurno", result);
//		}
	}

	// delete existing turno
	public void testDeleteExistingTurno() {

		this.ligarSuportePersistente(true);

		Object argsDeleteTurno[] = { new ShiftKey(this.infoShift.getNome(), this.infoShift.getInfoDisciplinaExecucao()) };

		Object result = null;
		try {
			result = _gestor.executar(_userView, "ApagarTurno", argsDeleteTurno);
			assertEquals("testDeleteExistingTurno", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteExistingTurno");
		}
	}

	// delete non-existing turno
	public void testDeleteNonExistingTurno() {

		this.ligarSuportePersistente(false);

		Object argsDeleteTurno[] = { new ShiftKey(this.infoShift.getNome(), this.infoShift.getInfoDisciplinaExecucao()) };

		Object result = null;
		try {
			result = _gestor.executar(_userView, "ApagarTurno", argsDeleteTurno);
			assertEquals("testDeleteNonExistingTurno", Boolean.FALSE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteNonExistingTurno");
		}
	}

	private void ligarSuportePersistente(boolean existing) {

		ISuportePersistente sp = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);
			
			IDisciplinaExecucaoPersistente idep = sp.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao ide = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", iep);
			
			ITurnoPersistente itp = sp.getITurnoPersistente();
			ITurno it = null;
			if(existing) {
				it = itp.readByNameAndExecutionCourse("turno1", ide);
			} else {
				it = new Turno("qqqqq", new TipoAula(1), new Integer(100), ide);
			}
			
			System.out.println(it.toString());
			
			this.infoShift = Cloner.copyIShift2InfoShift(it);

			System.out.println(this.infoShift.toString());

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
