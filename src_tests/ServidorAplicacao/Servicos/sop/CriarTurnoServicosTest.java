/*
 * CriarTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 18:53
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class CriarTurnoServicosTest extends TestCaseCreateServices {
	
	private InfoShift infoShift = null;

	public CriarTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "CriarTurno";
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		this.ligarSuportePersistente(false);

		Object argsCriarTurno[] = { this.infoShift };

		return argsCriarTurno;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		this.ligarSuportePersistente(true);

		Object argsCriarTurno[] = { this.infoShift };

		return argsCriarTurno;
	}
/*
	// write existing turno
	public void testCreateExistingTurno() {

		this.ligarSuportePersistente(true);

		Object argsCriarTurno[] = { this.infoShift };

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsCriarTurno);
			fail("testCreateExistingTurno");
		} catch (Exception ex) {
			assertNull("testCreateExistingTurno", result);
		}
	}

	// write new non-existing turno
	public void testCreateNonExistingTurno() {

		this.ligarSuportePersistente(false);

		Object argsCriarTurno[] = { this.infoShift };

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsCriarTurno);
			assertEquals("testCreateNonExistingTurno", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testCreateNonExistingTurno");
		}
	}
*/
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
				it = new Turno("turnoXPTO", new TipoAula(TipoAula.TEORICA), new Integer(100), ide);
			}

			this.infoShift = Cloner.copyIShift2InfoShift(it);

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