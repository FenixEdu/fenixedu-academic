package ServidorAplicacao.Servicos.gesdis;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IAnnouncement;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISite;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ivo Brandão
 */
public class ReadLastAnnouncementServiceTest extends TestCaseReadServices {

	/**
	 * @param testName
	 */
	public ReadLastAnnouncementServiceTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadLastAnnouncement";
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadLastAnnouncementServiceTest.class);

		return suite;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente persistentSupport = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		ISite site = null;
		
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentSupport.iniciarTransaccao();

			IPersistentExecutionYear ipey = persistentSupport.getIPersistentExecutionYear();
			executionYear = ipey.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod ipep = persistentSupport.getIPersistentExecutionPeriod();
			executionPeriod = ipep.readByNameAndExecutionYear("2º Semestre", executionYear);

			IDisciplinaExecucaoPersistente idep = persistentSupport.getIDisciplinaExecucaoPersistente();
			executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod(
				"TFCI", executionPeriod);

			IPersistentSite ips = persistentSupport.getIPersistentSite();
			site = ips.readByExecutionCourse(executionCourse);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
		}

		InfoSite infoSite = Cloner.copyISite2InfoSite(site);
		Object[] args = { infoSite };
		return args;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
	protected Object getObjectToCompare() {
		IAnnouncement announcement = null;

		ISuportePersistente persistentSupport = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		ISite site = null;
		
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentSupport.iniciarTransaccao();

			IPersistentExecutionYear ipey = persistentSupport.getIPersistentExecutionYear();
			executionYear = ipey.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod ipep = persistentSupport.getIPersistentExecutionPeriod();
			executionPeriod = ipep.readByNameAndExecutionYear("2º Semestre", executionYear);

			IDisciplinaExecucaoPersistente idep = persistentSupport.getIDisciplinaExecucaoPersistente();
			executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod(
				"TFCI", executionPeriod);

			IPersistentSite ips = persistentSupport.getIPersistentSite();
			site = ips.readByExecutionCourse(executionCourse);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			System.out.println("getObjectToCompare1:failed setting up the test data" + e);
		}

		InfoSite infoSite = Cloner.copyISite2InfoSite(site);
		
		//read existing
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2003);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 22);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();

//		announcement = new Announcement("announcement2deTFCI", date, date, "information2", site);
		InfoAnnouncement infoAnnouncement = null;
		try {		
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			announcement =
				SuportePersistenteOJB
					.getInstance()
					.getIPersistentAnnouncement()
					.readAnnouncementByTitleAndCreationDateAndSite(
						"announcement2deTFCI",
			(Timestamp)date,
						site);
			infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(announcement);
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			System.out.println("getObjectToCompare2:failed setting up the test data" + e);
		}

		return infoAnnouncement;
	}
}