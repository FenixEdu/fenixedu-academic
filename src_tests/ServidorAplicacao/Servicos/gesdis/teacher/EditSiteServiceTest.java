package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISite;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ivo Brandão
 */
public class EditSiteServiceTest extends TestCaseDeleteAndEditServices {

	/**
	 * @param testName
	 */
	public EditSiteServiceTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditSite";
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
		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IExecutionCourse executionCourse = null;
		//InfoSection infoSection = null;
		ISite site = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear = ieyp.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

			executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

			IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
			executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);
			IPersistentSite persistentSite = sp.getIPersistentSite();

			site = persistentSite.readByExecutionCourse(executionCourse);
			
		//	IPersistentSection persistentSection = sp.getIPersistentSection();

		//	ISection section = persistentSection.readBySiteAndSectionAndName(site,null,"Seccao1deTFCI");
			
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}
		
		InfoSite oldInfoSite = Cloner.copyISite2InfoSite(site);
		InfoSite newInfoSite = oldInfoSite;
		newInfoSite.setAlternativeSite("newAlternativeSite");
		newInfoSite.setMail("newMail");
		newInfoSite.setInitialStatement("newInitialStatement");
		newInfoSite.setIntroduction("newIntroduction");
		
		Object[] args = {oldInfoSite, newInfoSite};
		return args;
	}
}