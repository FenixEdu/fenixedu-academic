/*
 * RemoverTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 17:04
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
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class RemoverTurnoServicosTest extends TestCaseDeleteAndEditServices {
	private InfoClass infoClass = null;
	private InfoShift infoShift = null;


	public RemoverTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(RemoverTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "RemoverTurno";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		this.ligarSuportePersistente(true);
		
		Object argsRemoverTurno[] = {this.infoShift, this.infoClass} ;

		return argsRemoverTurno;
	}
	

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		this.ligarSuportePersistente(false);
		
		Object argsRemoverTurno[] = {this.infoShift, this.infoClass} ;

		return argsRemoverTurno;
	}


	private void ligarSuportePersistente(boolean existing) {

		ISuportePersistente sp = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			ICursoPersistente icp = sp.getICursoPersistente();
			ICurso ic = icp.readBySigla("LEIC");

			IPersistentDegreeCurricularPlan ipccp = sp.getIPersistentDegreeCurricularPlan();
			IDegreeCurricularPlan ipcc = ipccp.readByNameAndDegree("plano1", ic);

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

			ICursoExecucaoPersistente icep = sp.getICursoExecucaoPersistente();
			ICursoExecucao ice = icep.readByDegreeCurricularPlanAndExecutionYear(ipcc, iey);

			IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);
			
			ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
			ITurma turma = null;
			
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			IExecutionCourse executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
			assertNotNull(executionCourse);
			
			ITurnoPersistente persistentShift = sp.getITurnoPersistente();
			ITurno shift = null;
			
			if(existing) {
				turma = turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501", ice, iep);
				assertNotNull(turma);
				shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
				assertNotNull(shift);
			} else {
				turma = new Turma("turma1", new Integer(1), ice, iep);
				shift = new Turno("desc", new TipoAula(TipoAula.RESERVA), new Integer(1000), executionCourse);
			}
			
			this.infoClass = Cloner.copyClass2InfoClass(turma);
			this.infoShift = Cloner.copyShift2InfoShift(shift);

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
