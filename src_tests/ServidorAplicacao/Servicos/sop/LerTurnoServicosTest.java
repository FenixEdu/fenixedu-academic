/*
 * LerTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 17:19
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurnoServicosTest extends TestCaseReadServices {
	private InfoExecutionCourse infoExecutionCourse = null;


	public LerTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LerTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "LerTurno";
	}


	protected int getNumberOfItemsToRetrieve(){
		return 0;
	}
	protected Object getObjectToCompare(){
		this.ligarSuportePersistente(true);
		
		InfoShift infoShift = new InfoShift();
		infoShift.setNome("turno1");
		infoShift.setInfoDisciplinaExecucao(this.infoExecutionCourse);
		return infoShift;
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		this.ligarSuportePersistente(true);
		
		Object argsLerTurmas[] = {new ShiftKey("turno1", this.infoExecutionCourse)}; 

		return argsLerTurmas;
	}
	

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		this.ligarSuportePersistente(false);
		
		Object argsLerTurmas[] = {new ShiftKey("turno1", this.infoExecutionCourse)}; 

		return argsLerTurmas;
	}


	private void ligarSuportePersistente(boolean existing) {

		ISuportePersistente sp = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			
			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);


			IPersistentExecutionCourse persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			IExecutionCourse executionCourse = null;

			if(existing) {
				executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
				assertNotNull(executionCourse);

			} else {
				executionCourse = new ExecutionCourse("desc", "desc", new Double(1), new Double(2), new Double(3), new Double(4), executionPeriod);
			}
			
			this.infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse); 

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
