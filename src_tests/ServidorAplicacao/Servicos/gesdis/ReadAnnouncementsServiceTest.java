/*
 * Created on 17/Mar/2003
 */
package ServidorAplicacao.Servicos.gesdis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataBeans.gesdis.InfoSite;
import DataBeans.util.Cloner;
import Dominio.Announcement;
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
 * @author lmac1
 */

public class ReadAnnouncementsServiceTest extends TestCaseReadServices {
	
	/**
     * @param testName
	 */
     public ReadAnnouncementsServiceTest(String testName) {
		super(testName);
        	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	 protected String getNameOfServiceToBeTested() {
		return "ReadAnnouncements";
       }

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
       }

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		ISite site = null;
		try {
			    sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();

				IPersistentExecutionYear ipey = sp.getIPersistentExecutionYear();
				executionYear = ipey.readExecutionYearByName("2002/2003");

				IPersistentExecutionPeriod ipep = sp.getIPersistentExecutionPeriod();
                executionPeriod = ipep.readByNameAndExecutionYear("2º Semestre", executionYear);

				IDisciplinaExecucaoPersistente idep = sp.getIDisciplinaExecucaoPersistente();
				executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",executionPeriod);

                IPersistentSite ips = sp.getIPersistentSite();
                site = ips.readByExecutionCourse(executionCourse);
                
				sp.confirmarTransaccao();
			}
		catch (ExcepcaoPersistencia e) {
						System.out.println("failed setting up the test data");
					                    }
       
        InfoSite infoSite = Cloner.copyISite2InfoSite(site);
		Object[] args = { infoSite };
		return args;
		}

   /* (non-Javadoc)
	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	*/
	    protected int getNumberOfItemsToRetrieve() {
		return 0;
        }
        
	/* (non-Javadoc)
		 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
		 */
	protected Object getObjectToCompare() {
			ISuportePersistente sp = null;
			IExecutionYear executionYear = null;
			IExecutionPeriod executionPeriod = null;
			IDisciplinaExecucao executionCourse = null;
			ISite site = null;
			try {
				    sp = SuportePersistenteOJB.getInstance();
					sp.iniciarTransaccao();

					IPersistentExecutionYear ipey = sp.getIPersistentExecutionYear();
					executionYear = ipey.readExecutionYearByName("2002/2003");

					IPersistentExecutionPeriod ipep = sp.getIPersistentExecutionPeriod();
                    executionPeriod = ipep.readByNameAndExecutionYear("2º Semestre", executionYear);

					IDisciplinaExecucaoPersistente idep = sp.getIDisciplinaExecucaoPersistente();
					executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);

				    IPersistentSite ips = sp.getIPersistentSite();
					site = ips.readByExecutionCourse(executionCourse);
					
					sp.confirmarTransaccao();
				}
			 catch (ExcepcaoPersistencia e) {
					System.out.println("failed setting up the test data");
				    }

			List expectedList = new ArrayList();
			Calendar calendar = Calendar.getInstance();
			calendar.set(2003, 03, 21);
			Date announcementDate = calendar.getTime();
			IAnnouncement announcement = new Announcement("announcement1", announcementDate, site);
			expectedList.add(announcement);
			return expectedList;

			}
}