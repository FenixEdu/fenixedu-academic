/*
 * LerAlunosDeTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 22:42
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurno;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAlunosDeTurnoServicosTest extends TestCaseNeedAuthorizationServices {
	
	private InfoShift infoShift = null;

	public LerAlunosDeTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LerAlunosDeTurnoServicosTest.class);

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
		return "LerAlunosDeTurno";
	}

	// read existing alunos
	public void testReadExistingAlunos() {

		this.ligarSuportePersistente(true);

		Object argsLerAlunos[] = new Object[1];
		argsLerAlunos[0] = new ShiftKey(this.infoShift.getNome(), this.infoShift.getInfoDisciplinaExecucao());

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerAlunos);
			assertEquals("testLerExistingAlunos", 1, ((List) result).size());
		} catch (Exception ex) {
			fail("testLerExistingAlunos");
		}
	}

	// read non-existing alunos
	public void testReadNonExistingAlunos() {

		this.ligarSuportePersistente(false);

		Object argsLerAlunos[] = new Object[1];
		argsLerAlunos[0] = new ShiftKey(this.infoShift.getNome(), this.infoShift.getInfoDisciplinaExecucao());

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerAlunos);
			assertTrue("testLerExistingAlunos", ((List) result).isEmpty());
		} catch (Exception ex) {
			fail("testLerExistingAlunos");
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
				it = itp.readByNameAndExecutionCourse("turno4", ide);
			} else {
				it = itp.readByNameAndExecutionCourse("turno1", ide);
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