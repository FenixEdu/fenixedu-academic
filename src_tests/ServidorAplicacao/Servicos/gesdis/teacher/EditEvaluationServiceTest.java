/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.InfoEvaluation;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EditEvaluationServiceTest extends TestCaseDeleteAndEditServices {

	/**
	 * @param testName
	 */
	public EditEvaluationServiceTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditEvaluation";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		
		ISuportePersistente sp = null;
			IExecutionYear executionYear = null;
			IExecutionPeriod executionPeriod = null;
			IDisciplinaExecucao executionCourse = null;
		IEvaluation evaluation=null;
			try {
				sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();

				IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
				executionYear = ieyp.readExecutionYearByName("2002/2003");

				IPersistentExecutionPeriod iepp =
					sp.getIPersistentExecutionPeriod();

				executionPeriod =
					iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

				IDisciplinaExecucaoPersistente idep =
					sp.getIDisciplinaExecucaoPersistente();
				executionCourse =
					idep.readByExecutionCourseInitialsAndExecutionPeriod(
						"TFCI",
						executionPeriod);
			 evaluation= sp.getIPersistentEvaluation().readByExecutionCourse(executionCourse);
				
				sp.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				System.out.println("failed setting up the test data");
			}

			
		InfoEvaluation infoEvaluationOld = Cloner.copyIEvaluation2InfoEvaluation(evaluation);	
		InfoEvaluation infoEvaluationNew = Cloner.copyIEvaluation2InfoEvaluation(evaluation);
		infoEvaluationNew.setEvaluationElements("xpto");		
		Object[] args = { infoEvaluationOld, infoEvaluationNew};
		return args;
		
	
	}

}
