/*
 * Created on 23/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadDegreesServiceTest extends TestCaseReadServices {
	    
	/**
	 * @param testName
	 */
	 public ReadDegreesServiceTest(String testName) {
		super(testName);
			}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	 protected String getNameOfServiceToBeTested() {
		return "ReadDegreesService";
	   }
       
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	  }
    
	public static Test suite() {
		TestSuite suite = new TestSuite(ReadDegreesServiceTest.class);
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

//	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
//		return null;
//	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		return null;
	}

   /* (non-Javadoc)
	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	*/
	protected int getNumberOfItemsToRetrieve() {
		return 4;
	}
        
	/* (non-Javadoc)
		 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
		 */
		 
	protected Object getObjectToCompare() {
		ISuportePersistente persistentSupport = null;
		List persistentDegrees = null;
		
		try {
					
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentSupport.iniciarTransaccao();			
			persistentDegrees = persistentSupport.getICursoPersistente().readAll();
			persistentSupport.confirmarTransaccao();

		}catch (ExcepcaoPersistencia exception) {
					  exception.printStackTrace(System.out);
					  fail("Using services at getItemsToPutInSessionForActionToBeTestedSuccessfuly()!");
					}	
			Iterator iterator = persistentDegrees.iterator();
			List args = new ArrayList(4);
			while (iterator.hasNext())
			args.add( Cloner.copyIDegree2InfoDegree((ICurso) iterator.next()) );
									
			return args;
	}
}