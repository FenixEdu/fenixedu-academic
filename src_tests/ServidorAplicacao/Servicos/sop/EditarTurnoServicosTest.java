/*
 * EditarTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 21:05
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
import ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class EditarTurnoServicosTest extends TestCaseServicosWithAuthorization {

	private InfoShift infoShift = null;

	public EditarTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditarTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditarTurno";
	}

	// edit existing turno
	public void testEditExistingTurno() {

		this.ligarSuportePersistente(true);

		Object argsEditarTurno[] = new Object[2];
		argsEditarTurno[0] = this.infoShift;
		ITurno turno = Cloner.copyInfoShift2Shift(this.infoShift);
		InfoShift newInfoShift = Cloner.copyShift2InfoShift(turno);
		newInfoShift.setLotacao(new Integer(200));
		newInfoShift.setTipo(new TipoAula(TipoAula.DUVIDAS));
		newInfoShift.setNome("turno3243324324sdv");
		argsEditarTurno[1] = newInfoShift;

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsEditarTurno);
			assertEquals("testEditNonExistingTurno", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testEditNonExistingTurno");
		}
	}

	// edit non-existing turno
	public void testEditarNonExistingTurno() {

		this.ligarSuportePersistente(false);

		Object argsEditarTurno[] = new Object[2];
		argsEditarTurno[0] = this.infoShift;
		ITurno turno = Cloner.copyInfoShift2Shift(this.infoShift);
		InfoShift newInfoShift = Cloner.copyShift2InfoShift(turno);
		newInfoShift.setLotacao(new Integer(200));
		newInfoShift.setTipo(new TipoAula(TipoAula.DUVIDAS));
		newInfoShift.setNome("turno3243324324sdv");
		argsEditarTurno[1] = newInfoShift;

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsEditarTurno);
			assertEquals("testEditNonExistingTurno", Boolean.FALSE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testEditNonExistingTurno");
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