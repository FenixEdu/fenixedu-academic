/*
 * Created on 27/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */


/**
 * @author lmac2
 */


package ServidorAplicacao.Servicos.gesdis;



import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;




public class ReadItemsServiceTest extends TestCaseReadServices {
	
	/**
	 * @param testName
	 */
	 public ReadItemsServiceTest(String testName) {
		super(testName);
			}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	 protected String getNameOfServiceToBeTested() {
		return "ReadItems";
	   }
       
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	  }
    
	  public static Test suite() {
		TestSuite suite = new TestSuite(ReadItemsServiceTest.class);

		return suite;
	  }

	protected void setUp() {
		
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}



	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente persistentSupport = null;
		IPersistentExecutionYear persistentExecutionYear = null;
		IPersistentExecutionPeriod persistentExecutionPeriod = null;
		IPersistentExecutionCourse persistentExecutionCourse = null;
		IPersistentSite persistentSite = null;
		IPersistentSection persistentSection = null;
		ISection section = null;

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentExecutionYear =
				persistentSupport.getIPersistentExecutionYear();
			persistentExecutionPeriod =
				persistentSupport.getIPersistentExecutionPeriod();
			persistentExecutionCourse =
				persistentSupport.getIPersistentExecutionCourse();
			persistentSite = persistentSupport.getIPersistentSite();
			persistentSection = persistentSupport.getIPersistentSection();
			

			persistentSupport.iniciarTransaccao();

			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");

			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);

			IExecutionCourse executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);

			ISite site = persistentSite.readByExecutionCourse(executionCourse);

			section =
				persistentSection.readBySiteAndSectionAndName(
					site,
					null,
					"Seccao1deTFCI");
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
		}
		Object[] args = { Cloner.copyISection2InfoSection(section)};
		return args;
	}



   /* (non-Javadoc)
	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	*/
		protected int getNumberOfItemsToRetrieve() {
		return 2;
		}
        
	/* (non-Javadoc)
		 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
		 */
	protected Object getObjectToCompare() {
		

		return null;

	}
		}