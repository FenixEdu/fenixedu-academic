/*
 * CriarTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 18:43
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CriarTurmaServicosTest extends TestCaseCreateServices {
	
	private InfoClass infoClass = null;

	public CriarTurmaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarTurmaServicosTest.class);

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
		return "CriarTurma";
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		this. ligarSuportePersistente(false);

		Object argsCriarTurma[] = { this.infoClass };

		return argsCriarTurma;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		this. ligarSuportePersistente(true);

		Object argsCriarTurma[] = { this.infoClass };

		return argsCriarTurma;
	}
/*
	// write existing turma
	public void testCreateExistingTurma() {

		this. ligarSuportePersistente(true);

		Object argsCriarTurma[] = { this.infoClass };

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsCriarTurma);
			fail("testCreateExistingTurma");
		} catch (Exception ex) {
			assertNull("testCreateExistingTurma", result);
		}
	}

	// write new non-existing turma
	public void testCreateNonExistingTurma() {

		this. ligarSuportePersistente(false);

		Object argsCriarTurma[] = { this.infoClass };

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsCriarTurma);
			assertEquals("testCreateNonExistingTurma", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testCreateNonExistingTurma");
		}
	}
*/
	private void ligarSuportePersistente(boolean existing) {

		ISuportePersistente sp = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			ICursoPersistente icp = sp.getICursoPersistente();
			ICurso ic = icp.readBySigla("LEIC");

			IPlanoCurricularCursoPersistente ipccp = sp.getIPlanoCurricularCursoPersistente();
			IPlanoCurricularCurso ipcc = ipccp.readByNameAndDegree("plano1", ic);

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

			ICursoExecucaoPersistente icep = sp.getICursoExecucaoPersistente();
			ICursoExecucao ice = icep.readByDegreeCurricularPlanAndExecutionYear(ipcc, iey);

			IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);
			
			ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
			ITurma turma = null;
			if(existing) {
				turma = turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("turma413", ice, iep);
			} else {
				turma = new Turma("turma1", new Integer(1), ice, iep);
			}
			
			this.infoClass = Cloner.copyClass2InfoClass(turma);

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