/*
 * EditAnnouncementTest.java
 * 
 */

package ServidorAplicacao.Servicos.gesdis.teacher;

/**
 * @author Ivo Brandão
 */

import java.util.HashMap;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;

public class GetSitesTest extends TestCaseReadServices {

	private ISuportePersistente persistentSupport;

	public GetSitesTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(GetSitesTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "GetSites";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object argsGetSites[] = new Object[0];

		return argsGetSites;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
	protected int getNumberOfItemsToRetrieve() {
		return 10;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
	protected Object getObjectToCompare() {
		List sites = null;
		
		try {
			sites = persistentSupport.getIPersistentSite().readAll();
		} catch (ExcepcaoPersistencia excepcaoPersistencia){
			fail("getObjectToCompare: " + excepcaoPersistencia.getMessage());
		}
		
		return sites;
	}
}