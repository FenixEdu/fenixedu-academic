/*
 * Created on 28/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.HashMap;

import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IGroupProperties;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CreateGroupPropertiesTest extends TestCaseCreateServices {

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
		return null;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente persistentSupport = null;
		IPersistentExecutionYear persistentExecutionYear = null;
		IPersistentExecutionPeriod persistentExecutionPeriod = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		IDisciplinaExecucao executionCourse = null;

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentExecutionYear =
				persistentSupport.getIPersistentExecutionYear();
			persistentExecutionPeriod =
				persistentSupport.getIPersistentExecutionPeriod();
			persistentExecutionCourse =
				persistentSupport.getIDisciplinaExecucaoPersistente();
			persistentSupport.iniciarTransaccao();

			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCII",
					executionPeriod);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
		}

		IGroupProperties groupProperties =
			new GroupProperties(executionCourse, "newName");
		Object[] args =
			{
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse).getIdInternal(),
				Cloner.copyIGroupProperties2InfoGroupProperties(
					groupProperties)};
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
