/*
 * Created on 17/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
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

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadBibliographicReferenceTest extends TestCaseReadServices {

	public ReadBibliographicReferenceTest(String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadBibliographicReference";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IExecutionCourse executionCourse = null;
	
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear = ieyp.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod iepp =
				sp.getIPersistentExecutionPeriod();
			executionPeriod =
				iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

			IPersistentExecutionCourse idep =
				sp.getIPersistentExecutionCourse();
			executionCourse =
				idep.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);

			InfoExecutionCourse infoExecutionCourse =
				(InfoExecutionCourse) Cloner.get(executionCourse);
			Object[] testArgs = { infoExecutionCourse, null};

			sp.confirmarTransaccao();
			return testArgs;
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}

		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 3;
	}
	protected Object getObjectToCompare() {
		return null;
	}
}
