/*
 * LerAulasDeDisciplinaExecucaoServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 23:34
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

public class LerAulasDeDisciplinaExecucaoServicosTest extends TestCaseServicosWithAuthorization {
		
	private InfoExecutionCourse infoExecutionCourse = null;

	public LerAulasDeDisciplinaExecucaoServicosTest(
		java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(LerAulasDeDisciplinaExecucaoServicosTest.class);

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
		return "LerAulasDeDisciplinaExecucao";
	}

	// read existing aulas
	public void testReadExistingAulas() {

		ligarSuportePersistente(true);

		Object argsLerAulas[] = { this.infoExecutionCourse };

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerAulas);
			assertEquals("testLerExistingAulas", 6, ((List) result).size());
		} catch (Exception ex) {
			fail("testLerExistingAulas");
		}
	}

	// read new non-existing aulas
	public void testReadNonExistingAulas() {

		ligarSuportePersistente(false);

		Object argsLerAulas[] = { this.infoExecutionCourse };

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerAulas);
			assertTrue("testLerNonExistingAulas", ((List) result).isEmpty());
		} catch (Exception ex) {
			fail("testLerNonExistingAulas");
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
			IDisciplinaExecucao ide = null;
			
			if(existing) {
				ide = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", iep);
			} else {
				ide = new DisciplinaExecucao("NOME", "SIGLA", "Programa", new Double(1.5), new Double(1.5), new Double(1.5), new Double(1.5), iep);
			}
			
			this.infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(ide);

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