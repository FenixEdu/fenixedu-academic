/*
 * EditarTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 19:54
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
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarTurmaServicosTest extends TestCaseDeleteAndEditServices {
	
	private InfoClass infoClass = null;

	public EditarTurmaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditarTurmaServicosTest.class);

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
		return "EditarTurma";
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		this. ligarSuportePersistente(true);

		Object argsEditarTurma[] = new Object[2];
		argsEditarTurma[0] = this.infoClass;
		ITurma turma = Cloner.copyInfoClass2Class(this.infoClass);
		InfoClass newInfoClass = Cloner.copyClass2InfoClass(turma);
		newInfoClass.setAnoCurricular(new Integer(2));
		newInfoClass.setSemestre(new Integer(2));
		newInfoClass.setNome("turmaXPTO");
		argsEditarTurma[1] = newInfoClass;

		return argsEditarTurma;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		this. ligarSuportePersistente(false);

		Object argsEditarTurma[] = new Object[2];
		argsEditarTurma[0] = this.infoClass;
		ITurma turma = Cloner.copyInfoClass2Class(this.infoClass);
		InfoClass newInfoClass = Cloner.copyClass2InfoClass(turma);
		newInfoClass.setAnoCurricular(new Integer(2));
		newInfoClass.setSemestre(new Integer(2));
		newInfoClass.setNome("turmaXPTO");
		argsEditarTurma[1] = newInfoClass;

		return argsEditarTurma;
	}
/*
	// edit existing turma
	public void testEditExistingTurma() {

		this. ligarSuportePersistente(true);

		Object argsEditarTurma[] = new Object[2];
		argsEditarTurma[0] = this.infoClass;
		ITurma turma = Cloner.copyInfoClass2Class(this.infoClass);
		InfoClass newInfoClass = Cloner.copyClass2InfoClass(turma);
		newInfoClass.setAnoCurricular(new Integer(2));
		newInfoClass.setSemestre(new Integer(2));
		newInfoClass.setNome("turmaXPTO");
		argsEditarTurma[1] = newInfoClass;

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsEditarTurma);
			assertEquals("testEditNonExistingTurma", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testEditNonExistingTurma");
		}
	}

	// edit new non-existing turma
	public void testEditarNonExistingTurma() {

		this. ligarSuportePersistente(false);

		Object argsEditarTurma[] = new Object[2];
		argsEditarTurma[0] = this.infoClass;
		ITurma turma = Cloner.copyInfoClass2Class(this.infoClass);
		InfoClass newInfoClass = Cloner.copyClass2InfoClass(turma);
		newInfoClass.setAnoCurricular(new Integer(2));
		newInfoClass.setSemestre(new Integer(2));
		newInfoClass.setNome("turmaXPTO");
		argsEditarTurma[1] = newInfoClass;

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsEditarTurma);
			assertEquals("testEditNonExistingTurma", Boolean.FALSE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testEditNonExistingTurma");
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
				turma = new Turma("asdasdsad", new Integer(1), ice, iep);
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