/*
 * AdicionarTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 12:54
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import Dominio.ITurma;
import Dominio.ITurno;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class AdicionarTurnoServicosTest extends TestCaseServicos {
	
	private InfoClass infoClass = null;
	private InfoShift infoShift = null;

	public AdicionarTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(AdicionarTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// write turmaTurno by unauthorized user
	public void testUnauthorizedCreateTurmaTurno() {

		this.ligarSuportePersistente(false);

		Object argsCriarTurmaTurno[] = { this.infoClass, this.infoShift };

		Object result = null;
		try {
			result = _gestor.executar(_userView2, "AdicionarTurno", argsCriarTurmaTurno);
			fail("testUnauthorizedCreateTurmaTurno");
		} catch (Exception ex) {
			assertNull("testUnauthorizedCreateTurmaTurno", result);
		}
	}

	// write existing turmaTurno
	public void testCreateExistingTurmaTurno() {

		this.ligarSuportePersistente(true);

		Object argsCriarTurmaTurno[] = { this.infoClass, this.infoShift };

		Object result = null;
		try {
			result = _gestor.executar(_userView, "AdicionarTurno", argsCriarTurmaTurno);
			fail("testCreateExistingTurmaTurno");
		} catch (Exception ex) {
			assertNull("testCreateExistingTurmaTurno", result);
		}
	}

	// write new non-existing turmaTurno
	public void testCreateNonExistingTurmaTurno() {

		this.ligarSuportePersistente(false);

		Object argsCriarTurmaTurno[] = { this.infoClass, this.infoShift };

		Object result = null;
		try {
			result = _gestor.executar(_userView, "AdicionarTurno", argsCriarTurmaTurno);
			assertEquals("testCreateNonExistingTurmaTurno", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testCreateNonExistingTurmaTurno");
		}
	}

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
			ITurma turma = turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("turma413", ice, iep);
			
//			System.out.println(turma.toString());
			
			IDisciplinaExecucaoPersistente idep = sp.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao ide = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", iep);
			
			ITurnoPersistente itp = sp.getITurnoPersistente();
			ITurno it = null;
			if(existing) {
				it = itp.readByNameAndExecutionCourse("turno453", ide);
			} else {
				it = itp.readByNameAndExecutionCourse("turno1", ide);
			}
			
//			System.out.println(it.toString());
			
			this.infoClass = Cloner.copyClass2InfoClass(turma);
			this.infoShift = Cloner.copyIShift2InfoShift(it);

//			System.out.println(this.infoClass.toString());
//			System.out.println(this.infoShift.toString());

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
