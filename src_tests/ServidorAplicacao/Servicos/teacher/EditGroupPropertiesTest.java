package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoGroupProperties;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IGroupProperties;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 * 
 */

public class EditGroupPropertiesTest extends TestCaseDeleteAndEditServices {


	IDisciplinaExecucao executionCourse = null;
	IGroupProperties groupProperties = null;
	
	/**
	 * @param testName
	 */
	public EditGroupPropertiesTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		ISuportePersistente persistentSupport = null;
		IPersistentExecutionYear persistentExecutionYear = null;
		IPersistentExecutionPeriod persistentExecutionPeriod = null;
		IPersistentGroupProperties persistentGroupProperties = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		
		

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
			persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
			persistentSupport.iniciarTransaccao();

			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCII", executionPeriod);
			groupProperties = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndName(executionCourse,"projecto A");
			persistentSupport.confirmarTransaccao();
			
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
		}

		super.setUp();
	}
	

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditGroupProperties";
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		
		InfoGroupProperties infoGroupProperties = Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties);
		infoGroupProperties.setName("projecto B");
		Object[] args = { executionCourse.getIdInternal(), infoGroupProperties};
		return args;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		
		InfoGroupProperties infoGroupProperties = Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties);
		infoGroupProperties.setName("projecto L");
		Object[] args = { executionCourse.getIdInternal(), infoGroupProperties};
		return args;
	}

	/**
	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
	 */
	protected boolean needsAuthorization() {
		return true;
	}
}