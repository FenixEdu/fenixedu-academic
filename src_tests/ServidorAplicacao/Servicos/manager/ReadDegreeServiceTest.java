/*
 * Created on 25/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoDegree;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadDegreeServiceTest extends TestCaseReadServices {
	    
	/**
	 * @param testName
	 */
	 public ReadDegreeServiceTest(String testName) {
		super(testName);
			}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	 protected String getNameOfServiceToBeTested() {
		return "ReadDegreeService";
	   }
       
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		Object[] args = { new Integer(5)};
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(9)};
		return args;
	}

   /* (non-Javadoc)
	* @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	*/
	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}
        
	/* (non-Javadoc)
		 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
		 */
		 
	protected Object getObjectToCompare() {			

		ICurso degree = null;
		try {
					
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			ICursoPersistente persistentDegree = sp.getICursoPersistente();
			degree = persistentDegree.readByIdInternal(new Integer(9));
		    sp.confirmarTransaccao();
			
		}catch (ExcepcaoPersistencia exception) {
				exception.printStackTrace(System.out);
				fail("Using services at getObjectToCompare()!");
			  }
			InfoDegree infoDegree = Cloner.copyIDegree2InfoDegree(degree);
		return infoDegree;
	}
	
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServicos#getArgsForAuthorizedUser()
	 */
	protected String[] getArgsForAuthorizedUser() {
		return new String[]{"manager", "pass", getApplication()};
	}
	
	protected boolean needsAuthorization() {
			return true;
		}

}