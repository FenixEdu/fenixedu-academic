/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.HashMap;

import DataBeans.InfoGroupProperties;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IGroupProperties;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class CreateGroupPropertiesTest extends TestCaseCreateServices {

	ISuportePersistente persistentSupport = null;
	IPersistentExecutionYear persistentExecutionYear = null;
	IPersistentExecutionPeriod persistentExecutionPeriod = null;
	IPersistentExecutionCourse persistentExecutionCourse = null;

	IExecutionCourse executionCourse = null;

	/**
	 * @param testName
	 */
	public CreateGroupPropertiesTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "CreateGroupProperties";
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		try {
		
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentSupport.iniciarTransaccao();
			persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
			persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();

			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCII", executionPeriod);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
		}

		IGroupProperties groupProperties = new GroupProperties(executionCourse, "projecto A");
		Object[] args = { executionCourse.getIdInternal(), Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties)};
		return args;

	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		try {
			
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentSupport.iniciarTransaccao();
			persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
			persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();

			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCII", executionPeriod);
			
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
		}

		IGroupProperties groupProperties = new GroupProperties(executionCourse, "newName");
		
		InfoGroupProperties infoGroupProperties = Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties);
		
		Object[] args = { new Integer(25), infoGroupProperties };
		
		return args;

	}

	/**
	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
	 */
	protected boolean needsAuthorization() {
		return true;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
	 */
	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

}
