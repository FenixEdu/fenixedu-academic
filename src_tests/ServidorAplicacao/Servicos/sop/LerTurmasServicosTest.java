/*
 * LerTurmasServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 20:07
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurmasServicosTest extends TestCaseReadServices {
	private InfoExecutionDegree infoExecutionDegree = null;
	private InfoExecutionPeriod infoExecutionPeriod = null;


	public LerTurmasServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LerTurmasServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "LerTurmas";
	}
	

	protected int getNumberOfItemsToRetrieve(){
		return 4;
	}
	protected Object getObjectToCompare(){
		return null;
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		this.ligarSuportePersistente(true);
		
		Object argsLerTurmas[] = {this.infoExecutionDegree, this.infoExecutionPeriod, new Integer(1)} ;

		return argsLerTurmas;
	}
	

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		this.ligarSuportePersistente(false);
		
		Object argsLerTurmas[] = {this.infoExecutionDegree, this.infoExecutionPeriod, new Integer(1)} ;

		return argsLerTurmas;
	}
	
	
	private void ligarSuportePersistente(boolean existing) {

		ISuportePersistente sp = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();



			ICursoPersistente persistentDegree = sp.getICursoPersistente();
			ICurso degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
		
			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan();
			IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);

			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = null;

			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree = null;
			
			

			if(existing) {
				
				executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
				assertNotNull(executionDegree);
				
				executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
				assertNotNull(executionPeriod); 

			} else {

				executionYear = new ExecutionYear("desc");
				executionDegree = new CursoExecucao(executionYear, degreeCurricularPlan);
				
				executionPeriod = new ExecutionPeriod("desc", executionYear);
			}
			
			this.infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);
			this.infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);

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
