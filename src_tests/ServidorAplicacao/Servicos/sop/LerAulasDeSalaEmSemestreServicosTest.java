/*
 * LerAulasDeSalaEmSemestreServicosTest.java
 * JUnit based test
 *
 * Created on 29 de Outubro de 2002, 15:49
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoSala;

public class LerAulasDeSalaEmSemestreServicosTest extends TestCaseServicosWithAuthorization {

	private InfoExecutionPeriod infoExecutionPeriod = null;
	private InfoRoom infoRoom = null;
	
	public LerAulasDeSalaEmSemestreServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(LerAulasDeSalaEmSemestreServicosTest.class);

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
		return "LerAulasDeSalaEmSemestre";
	}

	// read existing aulas
	public void testReadExistingAulas() {

		this.ligarSuportePersistente(true);

		Object argsLerAulas[] = { this.infoExecutionPeriod, this.infoRoom };

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsLerAulas);
			assertEquals("testLerExistingAulas", 21, ((List) result).size());
		} catch (Exception ex) {
			fail("testLerExistingAulas");
		}
	}

	// read non-existing aulas
	public void testReadNonExistingAulas() {

		this.ligarSuportePersistente(false);

		Object argsLerAulas[] = { this.infoExecutionPeriod, this.infoRoom };

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
			
			if(existing) {
				this.infoRoom = new InfoRoom(new String("Ga1"), new String("Pavilhilhão Central"), new Integer(0), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));
			} else {
				this.infoRoom = new InfoRoom(new String("Ga4"), new String("Pavilhilhão Central"), new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));
			}
			
			this.infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(iep);

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